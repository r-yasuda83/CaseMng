package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.Department;

@Mapper
public interface DepartmentMapper {

	public List<Department> findAll();
}
