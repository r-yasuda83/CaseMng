package com.example.casemng.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.example.casemng.form.ProductForm;
import com.example.casemng.form.SearchForm;
import com.example.casemng.model.Pagenation;
import com.example.casemng.model.entity.Product;
import com.example.casemng.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	Pagenation pagenation;

	@GetMapping("/product")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {

		Pageable p = pagenation.getPageable(displayedNum, sortKey, sortDirection, pageable, model);

		String searchKey = null;
		if (form.getKeyword() == null) {
			searchKey = "";
		} else {
			searchKey = form.getKeyword();
		}
		
		Page<Product> product = productService.findByKeyword(p, "%" + searchKey + "%");
		model.addAttribute("page", product);
		
		return "product/list";
	}

	@GetMapping("/product/{productId}")
	public String getEdit(@ModelAttribute("productForm") ProductForm form, @PathVariable int productId, Model model) {

		Product product = productService.findById(productId);

		if (product == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		if (form.getId() == 0) {
			form = modelMapper.map(product, ProductForm.class);
		}

		model.addAttribute("productForm", form);
		return "product/edit";
	}

	@PostMapping("/product/{productId}/edit")
	public String postEdit(@ModelAttribute("productForm") @Validated ProductForm form, BindingResult result,
			@PathVariable int productId, Model model) {

		if (result.hasErrors()) {
			return getEdit(form, productId, model);
		}

		Product product = modelMapper.map(form, Product.class);
		productService.edit(product);
		return "redirect:/product";
	}

	@GetMapping("/product/create")
	public String getCreate(@ModelAttribute("productForm") ProductForm form, Model model) {

		model.addAttribute("productForm", form);
		return "product/create";
	}

	@PostMapping("/product/create")
	public String postCreate(@ModelAttribute("productForm") @Validated ProductForm form, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return getCreate(form, model);
		}
		Product product = modelMapper.map(form, Product.class);
		productService.create(product);
		return "redirect:/product";
	}
}