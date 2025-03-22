package com.example.casemng.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.example.casemng.model.Pagenation;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.CaseForList;
import com.example.casemng.model.entity.Customer;
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
			CaseForm form = modelMapper.map(cases, CaseForm.class);
			form.setOrder(modelMapper.map(cases.getOrder(), CaseOrderForm.class));
			model.addAttribute("caseForm", form);
		}

		model.addAttribute("shippingStatus", Constant.ShippingStatus.values());
		return "case/edit";
	}

	@PostMapping("/case/{id}/edit")
	public String postEdit(@ModelAttribute("caseForm") @Validated CaseForm form, BindingResult result,
			@PathVariable int id, Model model) {

		Case cases = modelMapper.map(form, Case.class);
		List<Map<String, String>> notEnoughList = caseService.checkStock(cases);
		List<String> errMsg = new ArrayList<String>();

		for (Map<String, String> map : notEnoughList) {
			errMsg.add(map.get("name") + "の発注数が在庫数を超えています。在庫数：" + map.get("stock") + "　注文数：" + map.get("quantity"));
		}

		if (!errMsg.isEmpty()) {
			model.addAttribute("errMsg", errMsg);
			return getEdit(id, model);
		}

		if (result.hasErrors()) {
			return getEdit(id, model);
		}

		caseService.caseEdit(cases);

		int customerId = form.getCustomerId();
		return "redirect:/customer/" + customerId + "?caseId=" + form.getId();
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