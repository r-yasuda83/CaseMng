package com.example.casemng.service;

import java.util.List;

import com.example.casemng.entity.User;
import com.example.casemng.form.FormUser;
import com.example.casemng.form.FormUserEditPassword;
import com.example.casemng.form.FormUserForEdit;

public interface UserService {

	public User findByUsername(String username);
	public List<User> findAll();
	public FormUser findById(int id);
	public FormUserEditPassword findByIdPassword(int id);
	public void create(FormUser form);
	public void edit(FormUser form);
	public void editPassword(FormUserEditPassword form);
	public void editLoginUser(FormUserForEdit form);
	public void logicalDelete(int id);
	public String duplicatesUserId(String userId);
	public String duplicatesUserIdWithoutId(FormUser form);
}