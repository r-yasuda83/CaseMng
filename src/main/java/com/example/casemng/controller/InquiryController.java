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

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Inquiry;
import com.example.casemng.form.CaseForm;
import com.example.casemng.form.InquiryForm;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.InquiryService;

@Controller
public class InquiryController {
	
	@Autowired
	InquiryService inquiryService;
	
	@Autowired
	ModelMapper modelMapper;

	@GetMapping("/inquiry/{id}")
	public String getEdit(@PathVariable int id, Model model) {
		
		Inquiry inquiry = inquiryService.findById(id);
		InquiryForm form = modelMapper.map(inquiry, InquiryForm.class);
		
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
	public String postEdit(@ModelAttribute("formInquiry") @Validated InquiryForm form, BindingResult result,
			@PathVariable int id, Model model) {
		if (result.hasErrors()) {
			return "inquiry/edit";
		}
		
		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		inquiryService.inquiryEdit(inquiry);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId=" + id;
	}
	
	@Autowired
	CaseService caseService;
	
	@GetMapping("/inquiry/create/{caseId}")
	public String getCreate(@PathVariable int caseId, Model model) {
		
		InquiryForm form = new InquiryForm();
		Case cases = caseService.findById(caseId);
		CaseForm caseForm = modelMapper.map(cases, CaseForm.class);
		
		if (caseForm == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		
		form.setCaseId(caseId);
		form.setCases(caseForm);
		model.addAttribute("formInquiry", form);
		
		return "inquiry/create";
	}
	
	@PostMapping("/inquiry/create/{caseId}")
	public String postCreate(@ModelAttribute("formInquiry") @Validated InquiryForm form, BindingResult result,
			@PathVariable int caseId, Model model) {
		if (result.hasErrors()) {
			
			return "inquiry/create";
		}
		Inquiry inquiry = modelMapper.map(form, Inquiry.class);
		int inquiryId = inquiryService.create(inquiry);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&inquiryId=" + inquiryId;
	}
	
	@PostMapping("/inquiry/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {
		
		Inquiry inquiry = inquiryService.findById(id);
		
		inquiryService.logicalDelete(id);
		return "redirect:/customer/" + inquiry.getCases().getCustomerId() + "?caseId=" + inquiry.getCaseId();
	}
}