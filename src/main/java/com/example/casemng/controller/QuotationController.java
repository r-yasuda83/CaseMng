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

import com.example.casemng.entity.Case;
import com.example.casemng.entity.Product;
import com.example.casemng.entity.Quotation;
import com.example.casemng.entity.QuotationProduct;
import com.example.casemng.form.CaseForm;
import com.example.casemng.form.QuotationForm;
import com.example.casemng.form.QuotationProductForm;
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

	@Autowired
	CustomerController customerCtrl;

	@GetMapping("/quotation/{id}")
	public String getEdit(@ModelAttribute("formQuotation") QuotationForm form, @PathVariable int id, Model model) {

		Quotation quotation = quotationService.findById(id);

		if (quotation == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		if (form.getCaseId() == 0) {
			form = modelMapper.map(quotation, QuotationForm.class);
		}

		model.addAttribute("formQuotation", form);
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/edit";
	}

	@PostMapping("/quotation/{id}/edit")
	public String postEdit(@ModelAttribute("formQuotation") @Validated QuotationForm form, BindingResult result,
			@PathVariable int id, Model model) {

		Quotation quotation = modelMapper.map(form, Quotation.class);

		result = quotationProductService.comparisonStock(quotation.getQuotationProduct(), result);
		result = quotationProductService.checkDiscount(quotation.getQuotationProduct(), result);
		result = quotationProductService.checkProduct(quotation.getQuotationProduct(), result);

		if (result.hasErrors()) {
			return getEdit(form, id, model);
		} else {
			quotationService.quotationEdit(quotation);
			quotationProductService.edit(quotation.getQuotationProduct(), quotation.getId());
			return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCaseId()
					+ "&quotationId=" + quotation.getId();
		}
	}

	@Autowired
	CaseService caseService;

	@GetMapping("/quotation/create/{caseId}")
	public String getCreate(@ModelAttribute("formQuotation") QuotationForm form, @PathVariable int caseId,
			Model model) {

		Case cases = caseService.findById(caseId);

		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		form.setCaseId(caseId);

		CaseForm caseForm = modelMapper.map(cases, CaseForm.class);
		form.setCases(caseForm);

		if (form.getQuotationProduct() == null) {
			List<QuotationProduct> list = quotationService.generateProductList();
			List<QuotationProductForm> listForm = modelMapper.map(list, new TypeToken<List<QuotationProduct>>() {
			}.getType());
			form.setQuotationProduct(listForm);
		}

		model.addAttribute("formQuotation", form);
		List<Product> productList = productService.findAllForSelect();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "quotationProduct");
		return "quotation/create";
	}

	@PostMapping("/quotation/create/{caseId}")
	public String postCreate(@ModelAttribute("formQuotation") @Validated QuotationForm form, BindingResult result,
			@PathVariable int caseId, Model model) {


		Quotation quotation = modelMapper.map(form, Quotation.class);

		result = quotationProductService.comparisonStock(quotation.getQuotationProduct(), result);
		result = quotationProductService.checkDiscount(quotation.getQuotationProduct(), result);

		if (result.hasErrors()) {
			return getCreate(form, caseId, model);
		} else {
			int quotationId = quotationService.create(quotation);
			List<QuotationProduct> list = quotationProductService.setQuotationId(quotation.getQuotationProduct(),
					quotationId);
			quotationProductService.addQuotationProduct(list);

			return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCaseId()
					+ "&quotationId=" + quotationId;
		}
	}

	@PostMapping("/quotation/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {

		Quotation quotation = quotationService.findById(id);
		quotationService.logicalDelete(id);
		return "redirect:/customer/" + quotation.getCases().getCustomerId() + "?caseId=" + quotation.getCases().getId();
	}
}
