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

import com.example.casemng.entity.Order;
import com.example.casemng.entity.Product;
import com.example.casemng.form.FormCase;
import com.example.casemng.form.FormOrder;
import com.example.casemng.form.FormOrderProduct;
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
			model.addAttribute("msg", "既に送付処理済の受注IDです。");
			return "error";
		}

		model.addAttribute("formOrder", form);

		List<Product> productList = productService.findAllForSelectStock();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "orderProduct");
		return "order/edit";
	}

	@Autowired
	OrderProductService orderProductService;

	@Autowired
	CustomerController customerCtrl;

	@PostMapping("/order/{id}/edit")
	public String postEdit(@ModelAttribute("formOrder") @Validated FormOrder form, BindingResult result,
			@PathVariable("id") int id, Model model) {

		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "orderProduct");
			return "order/edit";
		}

		List<String> errMsgs = orderProductService.comparisonStock(form.getOrderProduct());
		String discountErr = orderProductService.checkDiscount(form.getOrderProduct());
		if (discountErr.isBlank() == false) {
			errMsgs.add(discountErr);
		}

		if (errMsgs.isEmpty()) {
			orderService.orderEdit(form);
			orderProductService.edit(form.getOrderProduct(), form.getId());
			return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId();
		} else {
			model.addAttribute("errMsg", errMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "orderProduct");
			return "order/edit";
		}
	}

	@Autowired
	CaseService caseService;

	@GetMapping("/order/create/{caseId}")
	public String getCreate(@ModelAttribute("formOrder") FormOrder form, @PathVariable("caseId") int caseId,
			Model model) {

		FormCase cases = caseService.findById(caseId);
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
		form.setCases(cases);
		form.setOrderProduct(orderService.generateProductList());
		model.addAttribute("formOrder", form);

		List<Product> productList = productService.findAllForSelectStock();
		model.addAttribute("productList", productList);
		model.addAttribute("distinction", "orderProduct");
		return "order/create";
	}

	@PostMapping("/order/create/{caseId}")
	public String postCreate(@ModelAttribute("formOrder") @Validated FormOrder form, BindingResult result,
			@PathVariable("caseId") int caseId, Model model) {

		if (result.hasErrors()) {
			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "orderProduct");
			return "order/create";
		}

		List<String> errMsgs = orderProductService.comparisonStock(form.getOrderProduct());
		String discountErr = orderProductService.checkDiscount(form.getOrderProduct());
		if (discountErr.isBlank() == false) {
			errMsgs.add(discountErr);
		}
		
		if (errMsgs.isEmpty()) {
			int orderId = orderService.create(form);
			List<FormOrderProduct> list = orderProductService.setOrdersId(form.getOrderProduct(), orderId);
			orderProductService.addOrderProduct(list);

			return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId();
		} else {
			model.addAttribute("errMsg", errMsgs);

			List<Product> productList = productService.findAllForSelectStock();
			model.addAttribute("productList", productList);
			model.addAttribute("distinction", "orderProduct");
			return "order/create";
		}
	}

	@PostMapping("/order/delete/{id}")
	public String postDelete(@PathVariable("id") int id, Model model) {

		FormOrder form = orderService.findById(id);
		FormCase cases = caseService.findById(form.getCaseId());
		int shippingStatus = cases.getShippingStatus();
		if (shippingStatus == 3) {
			model.addAttribute("msg", "発送済みとなっているので削除できません。");
			return "error";
		}

		orderService.logicalDelete(form);
		return "redirect:/customer/" + form.getCases().getCustomerId() + "?caseId=" + form.getCaseId();
	}
}