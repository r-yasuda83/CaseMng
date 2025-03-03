package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.Product;
import com.example.casemng.form.FormProduct;

public interface ProductService {

	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public FormProduct findById(int id);
	public void edit(FormProduct form);
	public void create(FormProduct form);
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}
