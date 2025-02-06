package com.example.casemng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casemng.entity.Role;
import com.example.casemng.repository.RoleMapper;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleMapper mapper;
	
	public List<Role> findAll(){
		return mapper.findAll();
	}
}
