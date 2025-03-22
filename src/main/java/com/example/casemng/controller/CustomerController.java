package com.example.casemng.controller;

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
import com.example.casemng.form.customer.CustomerForm;
import com.example.casemng.model.Pagenation;
import com.example.casemng.model.entity.Customer;
import com.example.casemng.service.CustomerService;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	Pagenation pagenation;

	@GetMapping("/customer")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {
		
		Pageable p = pagenation.getPageable(displayedNum, sortKey, sortDirection, pageable, model);
		
		String searchKey = null;
		if (form.getKeyword() == null) {
			searchKey = "";
		}else {
			searchKey = form.getKeyword();
		}
		Page<Customer> customer = customerService.findByKeyword(p, searchKey);
		model.addAttribute("page", customer);

		return "customer/list";
	}

	@GetMapping("/customer/{id}")
	public String getDetails(@PathVariable int id, @RequestParam(required = false) Integer caseId,
			@RequestParam(required = false) Integer quotationId,
			@RequestParam(required = false) Integer inquiryId,
			Model model) {
		
		Customer customer = customerService.findById(id);

		if (customer == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("customer", customer);
		model.addAttribute("caseId", caseId);
		
		model.addAttribute("shippingStatus", Constant.ShippingStatus.values());
		
		return "customer/details";
	}

	@GetMapping("/customer/{customerId}/edit")
	public String getEdit(@PathVariable int customerId, Model model) {
		
		Customer customer = customerService.findByIdEdit(customerId);
		
		if (customer == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		
		if (!model.containsAttribute("customerForm")) {
			CustomerForm form = modelMapper.map(customer, CustomerForm.class);
			model.addAttribute("customerForm", form);
		}
		
		return "customer/edit";
	}

	@PostMapping("/customer/{id}/edit")
	public String postEdit(@ModelAttribute("customerForm") @Validated CustomerForm form,
			BindingResult result, @PathVariable int id, Model model) {
		
		if (result.hasErrors()) {
			return getEdit(id, model);
		}
		
		Customer customer = modelMapper.map(form, Customer.class);
		customerService.customerEdit(customer);
		return "redirect:/customer/" + id;
	}
	
	@Autowired
	CustomerForm customerForm;

	@GetMapping("/customer/create")
	public String getCreate(Model model) {
		
		if (!model.containsAttribute("customerForm")) {
			model.addAttribute("customerForm", customerForm);
		}
		return "customer/create";
	}

	@PostMapping("/customer/create")
	public String postCreate(@ModelAttribute("customerForm") @Validated CustomerForm form, BindingResult result,
			Model model) {
		
		if (result.hasErrors()) {
			return getCreate(model);
		}

		Customer customer = modelMapper.map(form, Customer.class);
		int id = customerService.create(customer);

		return "redirect:/customer/" + id;
	}

	@PostMapping("/customer/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {
		
		customerService.logicalDelete(id);
		return "redirect:/customer";
	}
}