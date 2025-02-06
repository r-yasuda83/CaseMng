package com.example.casemng.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.form.FormQuotationProduct;
import com.example.casemng.form.FormQuotationProductList;
import com.example.casemng.service.QuotationProductService;

@Controller
public class QuotationProductController {

	@Autowired
	QuotationProductService quotationProductService;

	@Autowired
	QuotationController quotationController;

	@PostMapping("/quotationProduct/{quotationId}/add")
	public String postAdd(@ModelAttribute("formQuotationProduct") @Validated FormQuotationProductList form,
			BindingResult result, @PathVariable("quotationId") int id, Model model) {
		for (FormQuotationProduct item : form.getQuotationProductList()) {
			item.setQuotationId(id);
		}
		List<FormQuotationProduct> validQuoProdList = new ArrayList<>();
		for (FormQuotationProduct fop : form.getQuotationProductList()) {
			if (fop.getQuantity() == 0) {
				continue;
			}
			validQuoProdList.add(fop);
		}
		
		List<String> quantityErrMsgs = quotationProductService.comparisonStock(validQuoProdList);
		if (validQuoProdList.isEmpty()) {
			model.addAttribute("errMsg", "商品は追加されませんでした");
		} else if (quantityErrMsgs.isEmpty()) {
			form.setQuotationProductList(validQuoProdList);
			quotationProductService.addQuotationProduct(form.getQuotationProductList());
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);
		}
		
		return quotationController.getEdit(id, model);
	}
}