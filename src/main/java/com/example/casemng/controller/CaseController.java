package com.example.casemng.controller;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Customer;
import com.example.casemng.form.CaseEntryForm;
import com.example.casemng.form.CaseForm;
import com.example.casemng.form.OrderForm;
import com.example.casemng.form.SearchForm;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.CustomerService;

@Controller
public class CaseController {

	@Autowired
	CaseService caseService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CustomerController customerCtrl;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	MessageSource messageSource;

	@GetMapping("/case")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {

		return caseService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}

	@GetMapping("/case/{id}")
	public String getEdit(@ModelAttribute("formCase") CaseForm form, @PathVariable int id, Model model) {

		Case cases = caseService.findById(id);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		if (form.getCustomerId() == 0) {
			form = modelMapper.map(cases, CaseForm.class);
		}
		//バリデーションエラー時
		if (form.getOrder() == null) {
			form.setOrder(modelMapper.map(cases.getOrder(), OrderForm.class));
		}

		model.addAttribute("formCase", form);
		return "case/edit";
	}

	@PostMapping("/case/{id}/edit")
	public String postEdit(@ModelAttribute("formCase") @Validated CaseForm form, BindingResult result,
			@PathVariable int id, Model model) {

		if (result.hasErrors()) {
			return getEdit(form, id, model);
		}

		Case conv = modelMapper.map(form, Case.class);

		if (caseService.caseEdit(conv, result).hasErrors()) {
			return getEdit(form, id, model);
		}

		int customerId = form.getCustomerId();
		return "redirect:/customer/" + customerId + "?caseId=" + form.getId();
	}

	@GetMapping("/case/create/{id}")
	public String getCreate(@PathVariable("id") int customerId, Model model) {

		Customer check = customerService.findById(customerId);
		if (check == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		CaseEntryForm form = new CaseEntryForm();
		form.setCustomerId(customerId);
		model.addAttribute("formCaseEntry", form);
		return "case/create";
	}

	@PostMapping("/case/create/{id}")
	public String postCreate(@ModelAttribute("formCaseEntry") @Validated CaseEntryForm form, BindingResult result,
			@PathVariable int id, Model model) {

		if (result.hasErrors()) {
			return "case/create";
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