package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.entity.Product;

@Mapper
public interface ProductMapper {
	
	public List<Product> findAll();
	public List<Product> findAllForSelect();
	public List<Product> findAllForSelectStock();
	public List<Product> findByKeyword(RowBounds rowBounds, String searchKey, Pageable pageable);
	public Product findById(int id);
	public Long count(String searchKey);
	public void edit(Product product);
	public void editStock(List<OrderProduct> list);
	public void create(Product product);
}
