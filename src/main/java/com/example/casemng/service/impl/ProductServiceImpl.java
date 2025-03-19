package com.example.casemng.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.Product;
import com.example.casemng.repository.ProductMapper;
import com.example.casemng.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductMapper mapper;

	public List<Product> findAll() {
		return mapper.findAll();
	}

	public List<Product> findAllForSelect() {
		return mapper.findAllForSelect();
	}

	public List<Product> findAllForSelectStock() {
		return mapper.findAllForSelectStock();
	}

	public Product findById(int id) {
		Product product = mapper.findById(id);
		return product;
	}

	public Page<Product> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<Product> product = mapper.findByKeyword(rowBounds, searchKey, pageable);
		Long total = mapper.count(searchKey);
		return new PageImpl<>(product, pageable, total);
	}

	@Transactional
	public void edit(Product product) {
		mapper.edit(product);
	}

	@Transactional
	public void create(Product product) {
		mapper.create(product);
	}
}