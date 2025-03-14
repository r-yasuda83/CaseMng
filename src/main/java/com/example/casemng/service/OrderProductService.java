package com.example.casemng.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.example.casemng.entity.OrderProduct;

public interface OrderProductService {
	
	public void edit(List<OrderProduct> list, int orderId);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<OrderProduct> list);
	public List<OrderProduct> findAllExport();
	public OrderProduct findByIdAll(int id);
	public BindingResult comparisonStock(List<OrderProduct> list, BindingResult result);
	public List<OrderProduct> setOrdersId(List<OrderProduct> list, int ordersId);
	public BindingResult checkDiscount(List<OrderProduct> list, BindingResult result);
	public BindingResult checkProduct(List<OrderProduct> list, BindingResult result);
}
