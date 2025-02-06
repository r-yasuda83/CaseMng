package com.example.casemng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casemng.entity.ProductCategory;
import com.example.casemng.repository.ProductCategoryMapper;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	
	@Autowired
	ProductCategoryMapper mapper;
	
	public List<ProductCategory> findAll(){
		return mapper.findAll();
	}
}
