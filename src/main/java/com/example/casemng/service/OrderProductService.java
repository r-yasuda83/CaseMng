package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.form.FormOrderProduct;

public interface OrderProductService {
	
	public void edit(List<FormOrderProduct> list, int orderId);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<FormOrderProduct> list);
	public List<OrderProduct> findAllExport();
	public OrderProduct findByIdAll(int id);
	public List<String> comparisonStock(List<FormOrderProduct> list);
	public List<FormOrderProduct> setOrdersId(List<FormOrderProduct> list, int ordersId);
	public String checkDiscount(List<FormOrderProduct> list);
	public String checkProduct(List<FormOrderProduct> list);
}
