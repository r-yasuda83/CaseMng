package com.example.casemng.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.casemng.form.FormOrderProduct;
import com.example.casemng.form.FormOrderProductList;
import com.example.casemng.service.OrderProductService;

@Controller
public class OrderProductController {

	@Autowired
	OrderProductService orderProductService;

	@Autowired
	OrderController orderController;

	@PostMapping("/orderproduct/{ordersId}/add")
	public String postAdd(@ModelAttribute("formOrderProduct") @Validated FormOrderProductList form,
			BindingResult result, @PathVariable("ordersId") int ordersId, Model model) {
		List<FormOrderProduct> validList = orderProductService.organizeList(form.getOrderProductList(), ordersId);
		
		List<String> quantityErrMsgs = orderProductService.comparisonStock(validList);
		if (validList.isEmpty()) {
			model.addAttribute("errMsg", "商品は追加されませんでした");
		} else if (quantityErrMsgs.isEmpty()) {
			form.setOrderProductList(validList);
			orderProductService.addOrderProduct(form.getOrderProductList());
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);
		}

		return orderController.getEdit(ordersId, model);
	}
}