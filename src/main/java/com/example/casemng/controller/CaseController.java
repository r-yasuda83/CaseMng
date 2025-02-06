package com.example.casemng.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Customer;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormCaseEntry;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.CustomerService;


@Controller
public class CaseController {

	@Autowired
	CaseService caseService;

	@GetMapping("/case")
	public String getList(Model model) {
		List<Case> caseList = caseService.findAll();
		model.addAttribute("list", caseList);
		return "case/list";
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
		return customerCtrl.getDetails(customerId, form.getId(), null, null, model);
	}
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/case/create/{id}")
	public String getCreate(@PathVariable("id")int customerId, Model model) {
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
		
		return customerCtrl.getDetails(form.getCustomerId(), caseId, null, null, model);
	}
	
	@PostMapping("/case/delete/{id}")
	public String postDelete(@PathVariable("id")int id, Model model) {
		int customerId = caseService.findById(id).getCustomerId();
		caseService.logicalDelete(id);
		return customerCtrl.getDetails(customerId, null, null, null, model);
	}
}