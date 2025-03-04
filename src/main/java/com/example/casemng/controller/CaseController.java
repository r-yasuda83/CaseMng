package com.example.casemng.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.casemng.entity.Customer;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;
import com.example.casemng.form.FormSearch;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.CustomerService;

@Controller
public class CaseController {

	@Autowired
	CaseService caseService;

	@GetMapping("/case")
	public String getList(@ModelAttribute("search") FormSearch form,
			@RequestParam(name = "displayedNum", required = false) Integer displayedNum,
			@RequestParam(name = "sortKey", required = false) String sortKey,
			@RequestParam(name = "sortDirection", required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {
		
		return caseService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}

	@GetMapping("/case/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {
		FormCase form = caseService.findById(id);

		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formCase", form);
		return "case/edit";
	}

	@Autowired
	CustomerController customerCtrl;

	@PostMapping("/case/{id}/edit")
	public String postEdit(@ModelAttribute("formCase") @Validated FormCase form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			return "case/edit";
		}
		caseService.caseEdit(form);

		int customerId = form.getCustomerId();
		return "redirect:/customer/" + customerId + "?caseId=" + form.getId();
	}

	@Autowired
	CustomerService customerService;

	@GetMapping("/case/create/{id}")
	public String getCreate(@PathVariable("id") int customerId, Model model) {
		Customer check = customerService.findById(customerId);
		if (check == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		FormCaseEntry form = new FormCaseEntry();
		form.setCustomerId(customerId);
		model.addAttribute("formCaseEntry", form);
		return "case/create";
	}

	@PostMapping("/case/create/{id}")
	public String postCreate(@ModelAttribute("formCaseEntry") @Validated FormCaseEntry form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			return "case/create";
		}
		Date today = new Date();
		form.setCaseDate(today);
		int caseId = caseService.create(form);

		return "redirect:/customer/" + form.getCustomerId() + "?caseId=" + caseId;
	}

	@PostMapping("/case/delete/{id}")
	public String postDelete(@PathVariable("id") int id, Model model) {
		int customerId = caseService.findById(id).getCustomerId();
		caseService.logicalDelete(id);
		
		return "redirect:/customer/" + customerId;
	}
}