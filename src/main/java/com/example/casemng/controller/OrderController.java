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
import com.example.casemng.form.FormOrder;
import com.example.casemng.form.FormOrderProduct;
import com.example.casemng.form.FormOrderProductList;
import com.example.casemng.service.CaseService;
import com.example.casemng.service.OrderProductService;
import com.example.casemng.service.OrderService;
import com.example.casemng.service.ProductService;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@GetMapping("/order/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {
		FormOrder form = orderService.findById(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		} else if (form.getCases().isShippingStockFlg() == true) {
			model.addAttribute("msg", "既に送付された受注IDです。");
			return "error";
		}

		model.addAttribute("formOrder", form);

		FormOrderProductList formOrderProduct = new FormOrderProductList();
		formOrderProduct.setOrderProductList(orderService.generateProductList());
		model.addAttribute("formOrderProductList", formOrderProduct);

		List<Product> productList = productService.findAllForSelectStock();
		model.addAttribute("productList", productList);
		return "order/edit";
	}

	@Autowired
	OrderProductService orderProductService;

	@Autowired
	CustomerController customerCtrl;

	@PostMapping("/order/{id}/edit")
	public String postEdit(@ModelAttribute("formOrderProductList") FormOrderProductList formOrderProductList,
			@ModelAttribute("formOrder") @Validated FormOrder form, BindingResult result,
			@PathVariable("id") int id, Model model) {

		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);

			formOrderProductList.setOrderProductList(orderService.generateProductList());
			model.addAttribute("formOrderProductList", formOrderProductList);
			return "order/edit";
		}
		
		List<String> quantityErrMsgs = orderProductService.comparisonStock(form.getOrderProduct());

		if (quantityErrMsgs.isEmpty()) {
			orderService.orderEdit(form);
			orderProductService.edit(form.getOrderProduct());
			return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), null, null, model);
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);

			formOrderProductList.setOrderProductList(orderService.generateProductList());
			return "order/edit";
		}
	}

	@Autowired
	CaseService caseService;

	@GetMapping("/order/create/{caseId}")
	public String getCreate(@PathVariable("caseId") int caseId, Model model) {
		FormOrder form = new FormOrder();
		FormCase cases = caseService.findById(caseId);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		form.setCaseId(caseId);
		form.setCases(cases);
		form.setOrderProduct(orderService.generateProductList());
		model.addAttribute("formOrder", form);

		List<Product> productList = productService.findAllForSelectStock();
		model.addAttribute("productList", productList);
		return "order/create";
	}

	@PostMapping("/order/create/{caseId}")
	public String postCreate(@ModelAttribute("formOrder") @Validated FormOrder form, BindingResult result,
			@PathVariable("caseId") int caseId, Model model) {
		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			return "order/create";
		}
		List<FormOrderProduct> validList = orderProductService.organizeList(form.getOrderProduct());

		List<String> quantityErrMsgs = orderProductService.comparisonStock(validList);
		if (quantityErrMsgs.isEmpty()) {
			orderService.create(form);
			orderProductService.setOrdersId(validList, form.getId());
			orderProductService.addOrderProduct(validList);
			return customerCtrl.getDetails(form.getCases().getCustomerId(), caseId, null, null, model);
		} else {
			model.addAttribute("errMsg", quantityErrMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			return "order/create";
		}
	}

	@PostMapping("/order/delete/{id}")
	public String postDelete(@PathVariable("id") int id, Model model) {
		FormOrder form = orderService.findById(id);

		orderService.logicalDelete(id);
		return customerCtrl.getDetails(form.getCases().getCustomerId(), form.getCaseId(), null, null, model);
	}
}