package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.entity.Customer;

public interface CustomerService {

	public List<Customer> findAll();
	public Page<Customer> findByKeyword(Pageable pageable, String searchKey);
	public Customer findById(int id);
	public Customer findByIdEdit(int id);
	public void customerEdit(Customer customer);
	public int create(Customer customer);
	public void logicalDelete(int id);
}
