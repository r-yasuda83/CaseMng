package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.example.casemng.entity.Customer;

@Mapper
public interface CustomerMapper {

	public List<Customer> findAll();
	public List<Customer> findByKeyword(RowBounds rowBounds, String searchKey, Pageable pageable);
	public Customer findById(int id);
	public Long count();
	public void customerEdit(Customer customer);
	public void create(Customer customer);
	public void logicalDelete(int id);
}
