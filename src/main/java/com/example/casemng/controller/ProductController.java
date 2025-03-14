package com.example.casemng.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.casemng.entity.Product;
import com.example.casemng.form.ProductForm;
import com.example.casemng.form.SearchForm;
import com.example.casemng.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;
	
	@Autowired
	ModelMapper modelMapper;

	@GetMapping("/product")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {
		
		return productService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}
	
	
	@GetMapping("/product/{id}")
	public String getEdit(@PathVariable int id, Model model) {
		
		Product product = productService.findById(id);
		ProductForm form = modelMapper.map(product, ProductForm.class);
		
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formProduct", form);
		return "product/edit";
	}

	@PostMapping("/product/{id}/edit")
	public String postEdit(@ModelAttribute("formProduct") @Validated ProductForm form, BindingResult result,
			@PathVariable int id, Model model) {
		
		if (result.hasErrors()) {
			return "product/edit";
		}
		
		Product product = modelMapper.map(form, Product.class);
		productService.edit(product);
		return "redirect:/product";
	}
	
	@GetMapping("/product/create")
	public String getCreate(Model model) {
		
		ProductForm form = new ProductForm();
		model.addAttribute("formProduct", form);
		return "product/create";
	}
	
	@PostMapping("/product/create")
	public String postCreate(@ModelAttribute("formProduct") @Validated ProductForm form, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			return "product/create";
		}
		Product product = modelMapper.map(form, Product.class);
		productService.create(product);
		return "redirect:/product";
	}
}