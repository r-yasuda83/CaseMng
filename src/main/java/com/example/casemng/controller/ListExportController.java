package com.example.casemng.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Customer;
import com.example.casemng.entity.Inquiry;
import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.CustomerService;
import com.example.casemng.service.InquiryService;
import com.example.casemng.service.OrderProductService;
import com.example.casemng.service.QuotationProductService;

@Controller
public class ListExportController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/export/customer")
	public String getCustomer(Model model) {
		List<Customer> list = customerService.findAll();
		model.addAttribute("list", list);
		return "export/customer";
	}
	
	@Autowired
	CaseService caseService;
	
	@GetMapping("/export/cases")
	public String getCases(Model model, @PageableDefault Pageable pageable) {
		Page<Case> list = caseService.findAll(pageable);
		model.addAttribute("list", list);
		return "export/case";
	}
	
	@Autowired
	OrderProductService orderProductService;
	
	@GetMapping("/export/orders")
	public String getOrders(Model model) {
		List<OrderProduct> list = orderProductService.findAllExport();
		model.addAttribute("list", list);
		return "export/order";
	}
	
	@Autowired
	QuotationProductService quotationProductService;
	
	@GetMapping("/export/quotation")
	public String getQuotation(Model model) {
		List<QuotationProduct> list = quotationProductService.findAllExport();
		model.addAttribute("list", list);
		return "export/quotation";
	}
	
	@Autowired
	InquiryService inquiryService;
	
	@GetMapping("/export/inquiry")
	public String getInquiry(Model model) {
		List<Inquiry> list = inquiryService.findAll();
		model.addAttribute("list", list);
		return "export/inquiry";
	}
}