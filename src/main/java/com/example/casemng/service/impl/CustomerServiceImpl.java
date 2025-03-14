package com.example.casemng.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.example.casemng.entity.Customer;
import com.example.casemng.repository.CustomerMapper;
import com.example.casemng.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerMapper customerMapper;
	
	public List<Customer> findAll(){
		return customerMapper.findAll();
	}
	
	public Page<Customer> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<Customer> customer = customerMapper.findByKeyword(rowBounds, searchKey, pageable);

		Long total = customerMapper.count(searchKey);
		return new PageImpl<>(customer, pageable, total);
	}

	public Customer findById(int id) {
		return customerMapper.findById(id);
	}
	
	@Autowired
	ModelMapper modelMapper;
	
	public Customer findByIdEdit(int id) {
		Customer customer = customerMapper.findById(id);
		return customer;
	}
	
	@Transactional
	public void customerEdit(Customer customer) {
		customerMapper.customerEdit(customer);
	}
	
	@Transactional
	public int create(Customer customer) {
		customerMapper.create(customer);
		return customer.getId();
	}
	
	public void logicalDelete(int id) {
		customerMapper.logicalDelete(id);
	}
	
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model) {
		if (searchKey == null) {
			searchKey = "";
		}
		Sort sort = null;
		if (StringUtils.hasLength(sortDirection)) {
			String sd = sortDirection.equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC.name() : Sort.Direction.DESC.name();
			String si = sortKey;
			model.addAttribute("sortKey", si);

			sort = Sort.by(Sort.Direction.fromString(sd), si);
			model.addAttribute("sortDirection", sd);
		}else {
			sort = Sort.by(Sort.Direction.ASC, "id");
		}
		if(displayedNum == null) {
			displayedNum = 5;
		}
		Pageable p = sort == null ? pageable : PageRequest.of(pageable.getPageNumber(), displayedNum, sort);
		Page<Customer> customer = findByKeyword(p, "%" + searchKey + "%");
		model.addAttribute("page", customer);
		
		return "customer/list";
	}
}
