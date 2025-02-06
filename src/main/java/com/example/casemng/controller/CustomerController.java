package com.example.casemng.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.casemng.entity.Customer;
import com.example.casemng.form.FormCustomer;
import com.example.casemng.service.CustomerService;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/customer")
	public String getList(Model model) {
		List<Customer> list = customerService.findAll();
		model.addAttribute("list", list);
		return "customer/list";
	}

	@GetMapping("/customer/{id}")
	public String getDetails(@PathVariable("id") int id, @RequestParam(required = false) Integer caseId,
			@RequestParam(required = false) Integer quotationId, @RequestParam(required = false) Integer inquiryId,
			Model model) {
		Customer customer = customerService.findById(id);
		if (caseId != null) {
			model.addAttribute("caseId", caseId);
		}
		if (quotationId != null) {
			model.addAttribute("quotationId", quotationId);
		}
		if (inquiryId != null) {
			model.addAttribute("inquiryId", inquiryId);
		}

		if (customer == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("customer", customer);
		return "customer/details";
	}

	@GetMapping("/customer/{id}/edit")
	public String getEdit(@PathVariable("id") int id, Model model) {
		FormCustomer form = customerService.findByIdEdit(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		model.addAttribute("formCustomer", form);
		return "customer/edit";
	}

	@PostMapping("/customer/{id}/edit")
	public String postEdit(@ModelAttribute("formCustomer") @Validated FormCustomer form,
			BindingResult result, @PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			return "customer/edit";
		}
		customerService.customerEdit(form);
		return getDetails(id, null, null, null, model);
	}

	@GetMapping("/customer/create")
	public String getCreate(Model model) {
		FormCustomer form = new FormCustomer();
		model.addAttribute("formCustomer", form);
		return "customer/create";
	}

	@PostMapping("/customer/create")
	public String postCreate(@ModelAttribute("formCustomer") @Validated FormCustomer form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "customer/create";
		}

		int id = customerService.create(form);
		//auto_crementしたidを↓で指定
		return getDetails(id, null, null, null, model);
	}

	@PostMapping("/customer/delete/{id}")
	public String postDelete(@PathVariable("id") int id, Model model) {
		customerService.logicalDelete(id);
		return getList(model);
	}
}