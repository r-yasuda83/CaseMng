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
import com.example.casemng.form.FormProduct;
import com.example.casemng.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/product")
	public String getList(Model model) {
		List<Product> productList = productService.findAll();
		model.addAttribute("list", productList);
		return "product/list";
	}
	
	@GetMapping("/product/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {
		FormProduct form = productService.findById(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formProduct", form);
		return "product/edit";
	}

	@PostMapping("/product/{id}/edit")
	public String postEdit(@ModelAttribute("formProduct") @Validated FormProduct form, BindingResult result,
			@PathVariable("id") int id, Model model) {
		if (result.hasErrors()) {
			return "product/edit";
		}
		productService.edit(form);
		return "redirect:/product";
	}
	
	@GetMapping("/product/create")
	public String getCreate(Model model) {
		FormProduct form = new FormProduct();
		model.addAttribute("formProduct", form);
		return "product/create";
	}
	
	@PostMapping("/product/create")
	public String postCreate(@ModelAttribute("formProduct") @Validated FormProduct form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "product/create";
		}
		productService.create(form);
		return "redirect:/product";
	}
}