package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.Customer;
import com.example.casemng.form.FormCustomer;

public interface CustomerService {

	public List<Customer> findAll();
	public Customer findById(int id);
	public FormCustomer findByIdEdit(int id);
	public void customerEdit(FormCustomer form);
	public int create(FormCustomer form);
	public void logicalDelete(int id);
}
