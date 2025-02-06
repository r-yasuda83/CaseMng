package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.ProductCategory;

@Mapper
public interface ProductCategoryMapper {

	public List<ProductCategory> findAll();
}
