package com.example.casemng.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.form.inquiry.InquiryForm;
import com.example.casemng.form.register.RegisterCaseForm;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.Inquiry;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.InquiryService;

@Controller
public class InquiryController {

	@Autowired
	InquiryService inquiryService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping("/inquiry/{inquiryId}")
	public String getEdit(@PathVariable int inquiryId, Model model) {

		Inquiry inquiry = inquiryService.findById(inquiryId);

		if (inquiry == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		
		if (!model.containsAttribute("inquiryForm")) {
			InquiryForm form = modelMapper.map(inquiry, InquiryForm.class);
			model.addAttribute("inquiryForm", form);
		}
		
		return "inquiry/edit";
	}

	@PostMapping("/inquiry/{inquiryId}/edit")
	public String postEdit(@ModelAttribute("inquiryForm") @Validated InquiryForm form, BindingResult result,
			@PathVariable int inquiryId, Model model) {
		
		if (result.hasErrors()) {
			return getEdit(inquiryId, model);
		}

		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		inquiryService.inquiryEdit(inquiry);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId="
				+ inquiryId;
	}

	@Autowired
	CaseService caseService;
	
	@Autowired
	InquiryForm inquiryForm;

	@GetMapping("/inquiry/create/{caseId}")
	public String getCreate(@PathVariable int caseId, Model model) {

		Case cases = caseService.findById(caseId);

		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		
		RegisterCaseForm caseForm = modelMapper.map(cases, RegisterCaseForm.class);
		
		if (!model.containsAttribute("inquiryForm")) {
			InquiryForm form = inquiryForm;
			form.setCaseId(caseId);
			form.setCases(caseForm);
			model.addAttribute("inquiryForm", form);
		}
		
		return "inquiry/create";
	}

	@PostMapping("/inquiry/create/{caseId}")
	public String postCreate(@ModelAttribute("inquiryForm") @Validated InquiryForm form, BindingResult result,
			@PathVariable int caseId, Model model) {
		
		if (result.hasErrors()) {
			return getCreate(caseId, model);
		}
		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		int inquiryId = inquiryService.create(inquiry);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId="
				+ inquiryId;
	}

	@PostMapping("/inquiry/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {

		Inquiry inquiry = inquiryService.findById(id);

		inquiryService.logicalDelete(id);
		return "redirect:/customer/" + inquiry.getCases().getCustomerId() + "?caseId=" + inquiry.getCaseId();
	}
}