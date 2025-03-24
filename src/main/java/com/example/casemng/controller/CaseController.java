package com.example.casemng.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.casemng.constant.Constant;
import com.example.casemng.form.SearchForm;
import com.example.casemng.form.cases.CaseEntryForm;
import com.example.casemng.form.cases.CaseForm;
import com.example.casemng.form.cases.CaseOrderForm;
import com.example.casemng.form.register.RegisterCaseForm;
import com.example.casemng.model.OutOfStock;
import com.example.casemng.model.Pagenation;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;
import com.example.casemng.model.entity.Customer;
import com.example.casemng.model.entity.Order;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.CustomerService;

@Controller
public class CaseController {

	@Autowired
	CaseService caseService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CustomerService customerService;

	@Autowired
	Pagenation pagenation;

	@GetMapping("/case")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {

		Pageable p = pagenation.getPageable(displayedNum, sortKey, sortDirection, pageable, model);

		String searchKey = null;
		if (form.getKeyword() == null) {
			searchKey = "";
		} else {
			searchKey = form.getKeyword();
		}

		Page<CaseForList> cases = caseService.findByKeyword(p, searchKey);
		model.addAttribute("page", cases);

		return "case/list";
	}

	@GetMapping("/case/{id}")
	public String getEdit(@PathVariable int id, Model model) {

		Case cases = caseService.findById(id);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		if (!model.containsAttribute("caseForm")) {
			RegisterCaseForm caseform = modelMapper.map(cases, RegisterCaseForm.class);
			CaseForm form = new CaseForm();
			form.setCases(caseform);
			form.setOrder(modelMapper.map(cases.getOrder(), CaseOrderForm.class));
			model.addAttribute("caseForm", form);
		}

		model.addAttribute("shippingStatus", Constant.ShippingStatus.values());
		return "case/edit";
	}

	@PostMapping("/case/{id}/edit")
	public String postEdit(@ModelAttribute("caseForm") @Validated CaseForm form, BindingResult result,
			@PathVariable int id, Model model) {

		Case cases = modelMapper.map(form.getCases(), Case.class);
		Order order = modelMapper.map(form.getOrder(), Order.class);
		cases.setOrder(order);
		
		List<String> errMsg = new ArrayList<String>();
		int shipped = Constant.ShippingStatus.Shipped.getValue();
		if (cases.getShippingStatus() == shipped && cases.isShippingStockFlg() == false) {
			List<OutOfStock> outOfStockList = caseService.checkStock(cases.getId());
			for (OutOfStock os : outOfStockList) {
				errMsg.add(os.getProduct().getProductName() + "の発注数が在庫数を超えています。在庫数：" + os.getProduct().getStock() + "　注文数：" + os.getRegistedQuantity());
			}
		}

		if (!errMsg.isEmpty()) {
			model.addAttribute("errMsg", errMsg);
			return getEdit(id, model);
		}

		if (result.hasErrors()) {
			return getEdit(id, model);
		}

		caseService.caseEdit(cases);
		
		int customerId = form.getCases().getCustomerId();
		return "redirect:/customer/" + customerId + "?caseId=" + form.getCases().getId();
	}

	@GetMapping("/case/create/{id}")
	public String getCreate(@ModelAttribute("caseEntryForm") CaseEntryForm form, @PathVariable("id") int customerId,
			Model model) {

		Customer check = customerService.findById(customerId);
		if (check == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		form.setCustomerId(customerId);
		model.addAttribute("caseEntryForm", form);
		return "case/create";
	}

	@PostMapping("/case/create/{id}")
	public String postCreate(@ModelAttribute("caseEntryForm") @Validated CaseEntryForm form, BindingResult result,
			@PathVariable int id, Model model) {

		if (result.hasErrors()) {
			return getCreate(form, id, model);
		}
		Date today = new Date();
		form.setCaseDate(today);

		Case cases = modelMapper.map(form, Case.class);
		int caseId = caseService.create(cases);

		return "redirect:/customer/" + form.getCustomerId() + "?caseId=" + caseId;
	}

	@PostMapping("/case/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {

		int customerId = caseService.findById(id).getCustomerId();
		caseService.logicalDelete(id);

		return "redirect:/customer/" + customerId;
	}
}