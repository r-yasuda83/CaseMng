package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Product;
import com.example.casemng.form.FormProduct;

public interface ProductService {

	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public FormProduct findById(int id);
	public void edit(FormProduct form);
	public void create(FormProduct form);
}
