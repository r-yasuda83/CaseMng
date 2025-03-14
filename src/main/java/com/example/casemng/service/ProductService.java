package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.Product;

public interface ProductService {

	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public Product findById(int id);
	public void edit(Product product);
	public void create(Product product);
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}
