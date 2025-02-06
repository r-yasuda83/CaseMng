package com.example.casemng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casemng.entity.Department;
import com.example.casemng.repository.DepartmentMapper;

@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	DepartmentMapper mapper;
	
	public List<Department> findAll(){
		return mapper.findAll();
	}
}
