package com.example.casemng.service;

import java.util.List;

import com.example.casemng.model.entity.OrderProduct;

public interface OrderProductService {
	
	public void edit(List<OrderProduct> list, int orderId);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<OrderProduct> list);
	public List<OrderProduct> findAllExport();
	public OrderProduct findByIdAll(int id);
	public List<OrderProduct> setOrdersId(List<OrderProduct> list, int ordersId);
}
