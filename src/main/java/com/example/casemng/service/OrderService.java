package com.example.casemng.service;

import java.util.List;

import com.example.casemng.model.entity.Order;
import com.example.casemng.model.entity.OrderProduct;

public interface OrderService {

	public Order findById(int id);
	public Order findByCaseId(int id);
	public void orderEdit(Order order);
	public List<Order> findAll();
	public int create(Order order);
	public void logicalDelete(Order order);
	public List<OrderProduct> generateProductList();
}