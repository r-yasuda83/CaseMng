package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.Customer;

public interface CustomerService {

	public List<Customer> findAll();
	public Page<Customer> findByKeyword(Pageable pageable, String searchKey);
	public Customer findById(int id);
	public Customer findByIdEdit(int id);
	public void customerEdit(Customer customer);
	public int create(Customer customer);
	public void logicalDelete(int id);
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}
