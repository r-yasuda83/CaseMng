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
import com.example.casemng.entity.Order;
import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;
import com.example.casemng.form.CaseForm;
import com.example.casemng.form.OrderForm;
import com.example.casemng.form.OrderProductForm;
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

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OrderProductService orderProductService;

	@Autowired
	CustomerController customerCtrl;

	@Autowired
	CaseService caseService;

	@GetMapping("/order/{id}")
	public String getEdit(@ModelAttribute("formOrder") OrderForm form, @PathVariable int id, Model model) {

		Order order = orderService.findById(id);

		if (order == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		if (form.getCaseId() == 0) {
			form = modelMapper.map(order, OrderForm.class);
		}

		form.setCases(modelMapper.map(order.getCases(), CaseForm.class));
		if (form.getCases().isShippingStockFlg() == true) {
			model.addAttribute("msg", "既に送付処理済の受注IDです。");
			return "error";
		}

		model.addAttribute("formOrder", form);

		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "orderProduct");
		return "order/edit";
	}

	@PostMapping("/order/{id}/edit")
	public String postEdit(@ModelAttribute("formOrder") @Validated OrderForm form, BindingResult result,
			@PathVariable int id, Model model) {

		Order order = modelMapper.map(form, Order.class);

		result = orderProductService.comparisonStock(order.getOrderProduct(), result);
		result = orderProductService.checkDiscount(order.getOrderProduct(), result);
		result = orderProductService.checkProduct(order.getOrderProduct(), result);
		
		if (result.hasErrors()) {
			return getEdit(form, id, model);
		} else {
			orderService.orderEdit(order);
			orderProductService.edit(order.getOrderProduct(), order.getId());
			return "redirect:/customer/" + order.getCases().getCustomerId() + "?caseId=" + order.getCaseId();
		}
	}

	@GetMapping("/order/create/{caseId}")
	public String getCreate(@ModelAttribute("formOrder") OrderForm form, @PathVariable int caseId,
			Model model) {

		Case cases = caseService.findById(caseId);
		if (cases == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		Order order = orderService.findByCaseId(caseId);
		if (order != null && order.isDeleted() == false) {
			model.addAttribute("msg", "既に受注登録されています。");
			return "error";
		}

		form.setCaseId(caseId);

		CaseForm caseForm = modelMapper.map(cases, CaseForm.class);
		form.setCases(caseForm);

		if (form.getOrderProduct() == null) {
			List<OrderProduct> list = orderService.generateProductList();
			List<OrderProductForm> listForm = modelMapper.map(list, new TypeToken<List<OrderProduct>>() {
			}.getType());
			form.setOrderProduct(listForm);
		}

		model.addAttribute("formOrder", form);
		List<Product> productList = productService.findAllForSelectStock();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "orderProduct");
		return "order/create";
	}

	@PostMapping("/order/create/{caseId}")
	public String postCreate(@ModelAttribute("formOrder") @Validated OrderForm form, BindingResult result,
			@PathVariable int caseId, Model model) {

		Order order = modelMapper.map(form, Order.class);

		result = orderProductService.comparisonStock(order.getOrderProduct(), result);
		result = orderProductService.checkDiscount(order.getOrderProduct(), result);

		if (result.hasErrors()) {
			return getCreate(form, caseId, model);
		} else {
			int orderId = orderService.create(order);
			List<OrderProduct> list = orderProductService.setOrdersId(order.getOrderProduct(), orderId);
			orderProductService.addOrderProduct(list);

			return "redirect:/customer/" + order.getCases().getCustomerId() + "?caseId=" + order.getCaseId();
		}
	}

	@PostMapping("/order/delete/{id}")
	public String postDelete(@PathVariable int id, Model model) {

		Order order = orderService.findById(id);
		if (order.getCases().isShippingStockFlg() == true) {
			model.addAttribute("msg", "既に送付処理済の受注IDです。");
			return "error";
		}

		orderService.logicalDelete(order);
		return "redirect:/customer/" + order.getCases().getCustomerId() + "?caseId=" + order.getCaseId();
	}
}