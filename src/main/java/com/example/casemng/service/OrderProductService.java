package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.OrderProduct;
import com.example.casemng.form.FormOrderProduct;

public interface OrderProductService {
	
	public void orderProductEdit(List<FormOrderProduct> list);
	public OrderProduct findById(int id);
	public void addOrderProduct(List<FormOrderProduct> list);
	public List<OrderProduct> findAllExport();
	public OrderProduct findByIdAll(int id);
	public List<String> comparisonStock(List<FormOrderProduct> list);
}
