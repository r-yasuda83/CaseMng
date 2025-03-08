package com.example.casemng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.example.casemng.entity.User;
import com.example.casemng.form.FormUser;
import com.example.casemng.form.FormUserEditPassword;
import com.example.casemng.form.FormUserRegistration;

public interface UserService {

	public User findByUsername(String username);
	public List<User> findAll();
	public Page<User> findByKeyword(Pageable pageable, String searchKey);
	public FormUser findById(int id);
	public FormUserEditPassword findByIdPassword(int id);
	public void create(FormUserRegistration form);
	public void edit(FormUser form);
	public void editPassword(FormUserEditPassword form);
	public void editLoginUser(FormUser form);
	public void logicalDelete(int id);
	public String duplicatesUserId(String userId);
	public String duplicatesUserIdWithoutId(FormUser form);
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model);
}