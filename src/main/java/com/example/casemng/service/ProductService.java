package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.entity.Product;

public interface ProductService {

	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public Page<Product> findByKeyword(Pageable pageable, String searchKey);
	public Product findById(int id);
	public void edit(Product product);
	public void create(Product product);
}
