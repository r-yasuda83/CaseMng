package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.model.entity.Role;

@Mapper
public interface RoleMapper {

	public List<Role> findAll();
}
