package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Order;
import com.example.casemng.form.FormOrder;
import com.example.casemng.form.FormOrderProduct;

public interface OrderService {

	public FormOrder findById(int id);
	public void orderEdit(FormOrder form);
	public List<Order> findAll();
	public void create(FormOrder form);
	public void logicalDelete(int id);
	public List<FormOrderProduct> generateProductList();
}
