package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.model.entity.Order;

@Mapper
public interface OrderMapper {

	public Order findById(int id);
	public Order findByCaseId(int caseId);
	public void orderEdit(Order order);
	public List<Order> findAll();
	public void create(Order order);
	public void logicalDelete(int id);
}
