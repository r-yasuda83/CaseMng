package com.example.casemng.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Product;
import com.example.casemng.form.FormProduct;
import com.example.casemng.repository.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductMapper mapper;
	
	@Autowired
	ModelMapper modelMapper;
	
	public List<Product> findAll(){
		return mapper.findAll();
	}
	
	public List<Product> findAllForSelect(){
		return mapper.findAllForSelect();
	}
	
	public List<Product> findAllForSelectStock(){
		return mapper.findAllForSelectStock();
	}
	
	public FormProduct findById(int id) {
		Product product = mapper.findById(id);
		FormProduct form = modelMapper.map(product, FormProduct.class);
		return form;
	}
	
	@Transactional
	public void edit(FormProduct form) {
		Product product = modelMapper.map(form, Product.class);
		mapper.edit(product);
	}
	
	@Transactional
	public void create(FormProduct form) {
		Product product = modelMapper.map(form, Product.class);
		mapper.create(product);
	}
}