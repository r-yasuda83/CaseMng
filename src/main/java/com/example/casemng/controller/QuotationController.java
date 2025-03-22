package com.example.casemng.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.form.RegisterProductForm;
import com.example.casemng.form.quantity.QuantityCaseForm;
import com.example.casemng.form.quantity.QuotationForm;
import com.example.casemng.form.validator.QuotationProductListValidator;
import com.example.casemng.model.entity.Case;
import com.example.casemng.model.entity.Product;
import com.example.casemng.model.entity.Quotation;
import com.example.casemng.model.entity.QuotationProduct;
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

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	QuotationProductService quotationProductService;

	@GetMapping("/quotation/{id}")
	public String getEdit(@PathVariable int id, Model model) {

		Quotation quotation = quotationService.findById(id);
		if (quotation == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		if (!model.containsAttribute("quotationForm")) {
			QuotationForm form = modelMapper.map(quotation, QuotationForm.class);
			model.addAttribute("quotationForm", form);
		}

		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/edit";
	}

	@Autowired
	private QuotationProductListValidator quotationProductListValidator;

	@PostMapping("/quotation/{id}/edit")
	public String postEdit(@ModelAttribute("quotationForm") @Validated QuotationForm form, BindingResult result,
			@PathVariable int id, Model model) {

		quotationProductListValidator.validate(form, result);
		if (result.hasErrors()) {
			return getEdit(id, model);
		}

		Quotation quotation = modelMapper.map(form, Quotation.class);
		quotationService.quotationEdit(quotation);
		quotationProductService.edit(quotation.getQuotationProduct(), quotation.getId());
		return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCaseId()
				+ "&quotationId=" + quotation.getId();
	}

	@Autowired
	CaseService caseService;
	
	@Autowired
	QuotationForm quotationForm;
	
	@GetMapping("/quotation/create/{caseId}")
	public String getCreate(@PathVariable int caseId,
			Model model) {

		Case cases = caseService.findById(caseId);

		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		if (!model.containsAttribute("quotationForm")) {
			QuotationForm form = quotationForm;
			form.setCaseId(caseId);
			QuantityCaseForm caseForm = modelMapper.map(cases, QuantityCaseForm.class);
			form.setCases(caseForm);

			List<QuotationProduct> list = quotationService.generateProductList();
			List<RegisterProductForm> listForm = modelMapper.map(list, new TypeToken<List<QuotationProduct>>() {
			}.getType());
			form.setQuotationProduct(listForm);
			
			model.addAttribute("quotationForm", form);
		}

		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/create";
	}

	@PostMapping("/quotation/create/{caseId}")
	public String postCreate(@ModelAttribute("quotationForm") @Validated QuotationForm form, BindingResult result,
			@PathVariable int caseId, Model model) {

		quotationProductListValidator.validate(form, result);

		if (result.hasErrors()) {
			return getCreate(caseId, model);
		}

		Quotation quotation = modelMapper.map(form, Quotation.class);
		int quotationId = quotationService.create(quotation);
		List<QuotationProduct> list = quotationProductService.setQuotationId(quotation.getQuotationProduct(),
				quotationId);
		quotationProductService.addQuotationProduct(list);

		return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCaseId()
				+ "&quotationId=" + quotationId;

	}

	@PostMapping("/quotation/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {

		Quotation quotation = quotationService.findById(id);
		quotationService.logicalDelete(id);
		return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCases().getId();
	}
}
