package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.casemng.entity.User;

@Mapper
public interface UserMapper {
	
	public User findByUsername(String username);
	public List<User> findAll();
	public User findById(int id);
	public List<User> findWithoutThisId(int id);
	public void create(User user);
	public void edit(User user);
	public void editPassword(User user);
	public void editExceptPassword(User user);
	public void editLoginUser(User user);
	public void logicalDelete(int id);
}
