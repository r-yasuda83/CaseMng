package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.User;

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
	public String duplicatesUserId(String userId);
	public String duplicatesUserIdWithoutId(User user);
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}