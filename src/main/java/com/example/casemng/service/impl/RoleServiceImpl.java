package com.example.casemng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casemng.entity.Role;
import com.example.casemng.repository.RoleMapper;
import com.example.casemng.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleMapper mapper;
	
	public List<Role> findAll(){
		return mapper.findAll();
	}
}
