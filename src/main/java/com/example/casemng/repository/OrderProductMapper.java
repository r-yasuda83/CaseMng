package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.form.FormOrderProduct;

@Mapper
public interface OrderProductMapper {

	public List<OrderProduct> findByOrdersId(int id);
	public void edit(List<OrderProduct> orderProduct);
	public void logicalDelete(List<OrderProduct> list);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<FormOrderProduct> list);
	public List<OrderProduct> findAllExport();
	public void create(List<OrderProduct> validList);
	public OrderProduct findByIdAll(int id);
}