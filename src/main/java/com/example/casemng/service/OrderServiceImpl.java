package com.example.casemng.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Order;
import com.example.casemng.form.FormOrder;
import com.example.casemng.form.FormOrderProduct;
import com.example.casemng.repository.CaseMapper;
import com.example.casemng.repository.OrderMapper;
import com.example.casemng.repository.OrderProductMapper;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ModelMapper modelMapper;

	public FormOrder findById(int id) {
		Order order = orderMapper.findById(id);
		FormOrder form = modelMapper.map(order, FormOrder.class);
		return form;
	}
	
	public Order findByCaseId(int id) {
		Order order = orderMapper.findByCaseId(id);
		return order;
	}

	@Transactional
	public void orderEdit(FormOrder form) {
		Order order = modelMapper.map(form, Order.class);
		orderMapper.orderEdit(order);
	}

	public List<Order> findAll() {
		List<Order> list = orderMapper.findAll();
		return list;
	}
	
	@Autowired
	OrderProductMapper orderProductMapper;

	@Transactional
	public int create(FormOrder form) {
		Order order = modelMapper.map(form, Order.class);
		orderMapper.create(order);
		return order.getId();
	}
	
	@Autowired
	CaseMapper caseMapper;
	
	public void logicalDelete(FormOrder form) {
		orderMapper.logicalDelete(form.getId());
		caseMapper.whenDeleteOrder(form.getCaseId());
	}
	
	public List<FormOrderProduct> generateProductList(){
		int ProductCount = 1;
		List<FormOrderProduct> orderProductList = new ArrayList<>();
		for (int i = 0; i < ProductCount; i++) {
			orderProductList.add(new FormOrderProduct());
		}
		return orderProductList;
	}
}