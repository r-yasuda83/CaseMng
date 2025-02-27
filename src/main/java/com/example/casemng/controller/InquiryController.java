package com.example.casemng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormInquiry;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.InquiryService;

@Controller
public class InquiryController {
	
	@Autowired
	InquiryService inquiryService;

	@GetMapping("/inquiry/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {
		FormInquiry form = inquiryService.findById(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formInquiry", form);
		return "inquiry/edit";
	}
	
	@Autowired
	CustomerController customerCtrl;
	
	@PostMapping("/inquiry/{id}/edit")
	public String postEdit(@ModelAttribute("formInquiry") @Validated FormInquiry form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			return "inquiry/edit";
		}
		inquiryService.inquiryEdit(form);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId=" + id;
	}
	
	@Autowired
	CaseService caseService;
	
	@GetMapping("/inquiry/create/{caseId}")
	public String getCreate(@PathVariable("caseId") int caseId, Model model) {
		FormInquiry form = new FormInquiry();
		FormCase cases = caseService.findById(caseId);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		
		form.setCaseId(caseId);
		form.setCases(cases);
		model.addAttribute("formInquiry", form);
		
		return "inquiry/create";
	}
	
	@PostMapping("/inquiry/create/{caseId}")
	public String postCreate(@ModelAttribute("formInquiry") @Validated FormInquiry form, BindingResult result,
			@PathVariable("caseId") int caseId, Model model) {
		if (result.hasErrors()) {
			
			return "inquiry/create";
		}
		int inquiryId = inquiryService.create(form);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId=" + inquiryId;
	}
	
	@PostMapping("/inquiry/delete/{id}")
	public String postDelete(@PathVariable("id")int id, Model model) {
		FormInquiry form = inquiryService.findById(id);
		inquiryService.logicalDelete(id);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId();
	}
}