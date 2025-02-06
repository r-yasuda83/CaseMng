package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;

@Mapper
public interface ProductMapper {
	
	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public Product findById(int id);
	public void edit(Product product);
	public void editStock(List<OrderProduct> list);
	public void create(Product product);
}
