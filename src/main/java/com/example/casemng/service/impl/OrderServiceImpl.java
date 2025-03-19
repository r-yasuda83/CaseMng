package com.example.casemng.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.Order;
import com.example.casemng.model.entity.OrderProduct;
import com.example.casemng.repository.CaseMapper;
import com.example.casemng.repository.OrderMapper;
import com.example.casemng.repository.OrderProductMapper;
import com.example.casemng.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderMapper orderMapper;

	public Order findById(int id) {
		Order order = orderMapper.findById(id);
		return order;
	}
	
	public Order findByCaseId(int id) {
		Order order = orderMapper.findByCaseId(id);
		return order;
	}

	@Transactional
	public void orderEdit(Order order) {
		orderMapper.orderEdit(order);
	}

	public List<Order> findAll() {
		List<Order> list = orderMapper.findAll();
		return list;
	}
	
	@Autowired
	OrderProductMapper orderProductMapper;

	@Transactional
	public int create(Order order) {
		orderMapper.create(order);
		return order.getId();
	}
	
	@Autowired
	CaseMapper caseMapper;
	
	@Transactional
	public void logicalDelete(Order order) {
		orderMapper.logicalDelete(order.getId());
		caseMapper.whenDeleteOrder(order.getCaseId());
	}
	
	public List<OrderProduct> generateProductList(){
		int ProductCount = 1;
		List<OrderProduct> orderProductList = new ArrayList<>();
		for (int i = 0; i < ProductCount; i++) {
			orderProductList.add(new OrderProduct());
		}
		return orderProductList;
	}
}