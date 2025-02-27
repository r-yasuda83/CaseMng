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

import com.example.casemng.entity.Product;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormQuotation;
import com.example.casemng.form.FormQuotationProduct;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.ProductService;
import com.example.casemng.service.QuotationProductService;
import com.example.casemng.service.QuotationService;

@Controller
public class QuotationController {

	@Autowired
	QuotationService quotationService;

	@Autowired
	ProductService productService;

	@GetMapping("/quotation/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {
		
		FormQuotation form = quotationService.findById(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formQuotation", form);

		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/edit";
	}

	@Autowired
	QuotationProductService quotationProductService;

	@Autowired
	CustomerController customerCtrl;

	@PostMapping("/quotation/{id}/edit")
	public String postEdit(@ModelAttribute("formQuotation") @Validated FormQuotation form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		
		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelect();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "quotationProduct");
			return "quotation/edit";
		}
		
		List<String> quantityErrMsgs = quotationProductService.comparisonStock(form.getQuotationProduct());
		
		if (quantityErrMsgs.isEmpty()) {
			quotationService.quotationEdit(form);
			quotationProductService.edit(form.getQuotationProduct(), form.getId());
			return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&quotationId=" + form.getId();
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "quotationProduct");
			return "quotation/edit";
		}
	}

	@Autowired
	CaseService caseService;

	@GetMapping("/quotation/create/{caseId}")
	public String getCreate(@ModelAttribute("formQuotation") FormQuotation form, @PathVariable("caseId") int caseId, Model model) {
		
		FormCase cases = caseService.findById(caseId);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		form.setCaseId(caseId);
		form.setCases(cases);
		form.setQuotationProduct(quotationService.generateProductList());
		model.addAttribute("formQuotation", form);
		
		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/create";
	}

	@PostMapping("/quotation/create/{caseId}")
	public String postCreate(@ModelAttribute("formQuotation") @Validated FormQuotation form, BindingResult result,
			@PathVariable("caseId") int caseId, Model model) {
		
		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelect();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "quotationProduct");
			return "quotation/create";
		}
		
		List<String> quantityErrMsgs = quotationProductService.comparisonStock(form.getQuotationProduct());
		if (quantityErrMsgs.isEmpty()) {
			int quotationId = quotationService.create(form);
			List<FormQuotationProduct> list = quotationProductService.setQuotationId(form.getQuotationProduct(), quotationId);
			quotationProductService.addQuotationProduct(list);
			
			return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId() + "&quotationId=" + quotationId;
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "quotationProduct");
			return "quotation/create";
		}
	}
	
	@PostMapping("/quotation/delete/{id}")
	public String postDelete(@PathVariable("id")int id, Model model) {
		
		FormQuotation form = quotationService.findById(id);
		quotationService.logicalDelete(id);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCases().getId() ;
	}
}
