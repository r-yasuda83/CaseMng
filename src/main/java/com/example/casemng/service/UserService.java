package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.entity.User;

public interface UserService {

	public User findByUsername(String username);
	public List<User> findAll();
	public Page<User> findByKeyword(Pageable pageable, String searchKey);
	public User findById(int id);
	public User findByIdPassword(int id);
	public void create(User user);
	public void edit(User user);
	public void editPassword(User user);
	public void editLoginUser(User user);
	public void logicalDelete(int id);
	public List<User> findWithoutThisId(int id);
}