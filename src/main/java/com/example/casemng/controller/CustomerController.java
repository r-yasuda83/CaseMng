package com.example.casemng.controller;

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
import com.example.casemng.form.FormCustomer;
import com.example.casemng.form.FormSearch;
import com.example.casemng.service.CustomerService;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/customer")
	public String getList(@ModelAttribute("search") FormSearch form,
			@RequestParam(name = "displayedNum", required = false) Integer displayedNum,
			@RequestParam(name = "sortKey", required = false) String sortKey,
			@RequestParam(name = "sortDirection", required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {
		return customerService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}

	@GetMapping("/customer/{id}")
	public String getDetails(@PathVariable("id") int id, @RequestParam(required = false) Integer caseId,
			@RequestParam(name = "quotationId", required = false) Integer quotationId,
			@RequestParam(required = false) Integer inquiryId,
			Model model) {
		Customer customer = customerService.findById(id);

		if (customer == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("customer", customer);
		model.addAttribute("caseId", caseId);
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
		return "redirect:/customer/" + id;
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

		return "redirect:/customer/" + id;
	}

	@PostMapping("/customer/delete/{id}")
	public String postDelete(@PathVariable("id") int id, Model model) {
		customerService.logicalDelete(id);
		return "redirect:/customer";
	}
}