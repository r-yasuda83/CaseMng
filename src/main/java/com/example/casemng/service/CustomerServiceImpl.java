package com.example.casemng.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.Customer;
import com.example.casemng.form.FormCustomer;
import com.example.casemng.repository.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerMapper customerMapper;
	
	public List<Customer> findAll(){
		return customerMapper.findAll();
	}

	public Customer findById(int id) {
		return customerMapper.findById(id);
	}
	
	@Autowired
	ModelMapper modelMapper;
	
	public FormCustomer findByIdEdit(int id) {
		Customer customer = customerMapper.findById(id);
		
		return modelMapper.map(customer, FormCustomer.class);
	}
	
	@Transactional
	public void customerEdit(FormCustomer formCustomer) {
		Customer customer = modelMapper.map(formCustomer, Customer.class);
		customerMapper.customerEdit(customer);
	}
	
	@Transactional
	public int create(FormCustomer form) {
		Customer customer = modelMapper.map(form, Customer.class);
		customerMapper.create(customer);
		return customer.getId();
	}
	
	public void logicalDelete(int id) {
		customerMapper.logicalDelete(id);
	}
}
