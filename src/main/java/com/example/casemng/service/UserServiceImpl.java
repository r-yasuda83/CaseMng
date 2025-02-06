package com.example.casemng.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.entity.User;
import com.example.casemng.form.FormUser;
import com.example.casemng.form.FormUserForEdit;
import com.example.casemng.repository.DepartmentMapper;
import com.example.casemng.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public User findByUsername(String username) {
		return userMapper.findByUsername(username);
	}

	public List<User> findAll() {
		return userMapper.findAll();
	}

	public FormUser findById(int id) {
		User user = userMapper.findById(id);
		FormUser form = modelMapper.map(user, FormUser.class);
		return form;
	}

	@Transactional
	public void edit(FormUserForEdit form) {
		User user = modelMapper.map(form, User.class);
		
		if (user.getPassword() != "") {
			String password = encoder.encode(user.getPassword());
			user.setPassword(password);
			userMapper.edit(user);
		} else {
			userMapper.editExceptPassword(user);
		}
	}
	
	@Autowired
	DepartmentMapper departmentMapper;

	@Transactional
	public void create(FormUser form) {
		User user = modelMapper.map(form, User.class);
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		
		userMapper.create(user);
	}

	@Transactional
	public void editLoginUser(FormUserForEdit form) {
		User user = modelMapper.map(form, User.class);
		
		if (user.getPassword() != "") {
			String password = encoder.encode(user.getPassword());
			user.setPassword(password);
			userMapper.editLoginUser(user);
		} else {
			userMapper.editLoginUserExceptPassword(user);
		}
	}
	
	@Transactional
	public void logicalDelete(int id) {
		userMapper.logicalDelete(id);
	}
	
	public String duplicatesUserId(String userId) {
		String errMsg = null;
		List<User> userList = userMapper.findAll();
		for(User user : userList) {
			if(user.getUserId().equals(userId)) {
				errMsg = "このユーザーIDは既に使われています";
				break;
			}
		}
		return errMsg;
	}
	
	public String duplicatesUserIdWithoutId(FormUserForEdit form) {
		String errMsg = null;
		List<User> userList = userMapper.findWithoutThisId(form.getId());
		for(User user : userList) {
			if(user.getUserId().equals(form.getUserId())) {
				errMsg = "このユーザーIDは既に使われています";
				break;
			}
		}
		return errMsg;
	}
}