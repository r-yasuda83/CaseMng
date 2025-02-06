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
import com.example.casemng.form.FormQuotationProductList;
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

		FormQuotationProductList formQuoProduct = new FormQuotationProductList();
		formQuoProduct.setQuotationProductList(quotationService.generateProductList());
		model.addAttribute("formQuotationProductList", formQuoProduct);

		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		return "quotation/edit";
	}

	@Autowired
	QuotationProductService quotationProductService;

	@Autowired
	CustomerController customerCtrl;

	@PostMapping("/quotation/{id}/edit")
	public String postEdit(
			@ModelAttribute("formQuotationProductList") FormQuotationProductList formQuotationProductList,
			@ModelAttribute("formQuotation") @Validated FormQuotation form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelect();
			model.addAttribute("productList", productList);

			formQuotationProductList.setQuotationProductList(quotationService.generateProductList());
			model.addAttribute("formQuotationProductList", formQuotationProductList);
			return "quotation/edit";
		}
		
		/*
		quotationService.quotationEdit(form);
		quotationProductService.quotationProductEdit(form.getQuotationProduct());

		return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), form.getId(), null,model);
		*/
		
		
		List<String> quantityErrMsgs = quotationProductService.comparisonStock(form.getQuotationProduct());
		if (quantityErrMsgs.isEmpty()) {
			quotationService.quotationEdit(form);
			quotationProductService.quotationProductEdit(form.getQuotationProduct());
			return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), form.getId(), null,model);
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);

			formQuotationProductList.setQuotationProductList(quotationService.generateProductList());
			return "quotation/edit";
		}
	}

	@Autowired
	CaseService caseService;

	@GetMapping("/quotation/create/{caseId}")
	public String getCreate(@PathVariable("caseId") int caseId, Model model) {
		FormQuotation form = new FormQuotation();
		FormCase cases = caseService.findById(caseId);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		form.setCaseId(caseId);
		form.setCases(cases);
		form.setQuotationProduct(quotationService.generateProductList());
		model.addAttribute("formQuotation", form);
		System.out.println(form);
		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		return "quotation/create";
	}

	@PostMapping("/quotation/create/{caseId}")
	public String postCreate(@ModelAttribute("formQuotation") @Validated FormQuotation form, BindingResult result,
			@PathVariable("caseId") int caseId, Model model) {
		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelect();
			model.addAttribute("productList", productList);
			return "quotation/create";
		}
		List<String> quantityErrMsgs = quotationProductService.comparisonStock(form.getQuotationProduct());
		if (quantityErrMsgs.isEmpty()) {
			int quotationId = quotationService.create(form);
			return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), quotationId, null, model);
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			return "quotation/create";
		}
	}
	
	@PostMapping("/quotation/delete/{id}")
	public String postDelete(@PathVariable("id")int id, Model model) {
		FormQuotation form = quotationService.findById(id);
		
		quotationService.logicalDelete(id);
		return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), null, null, model);
	}
}
