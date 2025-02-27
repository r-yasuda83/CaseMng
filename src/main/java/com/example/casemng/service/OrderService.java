package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Order;
import com.example.casemng.form.FormOrder;
import com.example.casemng.form.FormOrderProduct;

public interface OrderService {

	public FormOrder findById(int id);
	public Order findByCaseId(int id);
	public void orderEdit(FormOrder form);
	public List<Order> findAll();
	public int create(FormOrder form);
	public void logicalDelete(FormOrder form);
	public List<FormOrderProduct> generateProductList();
}