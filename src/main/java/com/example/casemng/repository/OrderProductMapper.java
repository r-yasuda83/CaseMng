package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.model.entity.OrderProduct;

@Mapper
public interface OrderProductMapper {

	public List<OrderProduct> findByOrdersId(int id);
	public void edit(List<OrderProduct> orderProduct);
	public void logicalDelete(List<OrderProduct> list);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<OrderProduct> list);
	public List<OrderProduct> findAllExport();
	public void create(List<OrderProduct> validList);
	public OrderProduct findByIdAll(int id);
}