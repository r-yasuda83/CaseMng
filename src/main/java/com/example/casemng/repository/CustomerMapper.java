package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.Customer;

@Mapper
public interface CustomerMapper {

	public List<Customer> findAll();
	public Customer findById(int id);
	public void customerEdit(Customer customer);
	public void create(Customer customer);
	public void logicalDelete(int id);
}
