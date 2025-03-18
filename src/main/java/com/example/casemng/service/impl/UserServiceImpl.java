package com.example.casemng.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.casemng.model.entity.CustomUserDetails;
import com.example.casemng.model.entity.User;
import com.example.casemng.repository.UserMapper;
import com.example.casemng.service.UserService;

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

	public User findById(int id) {
		User user = userMapper.findById(id);
		return user;
	}

	public User findByIdPassword(int id) {
		User user = userMapper.findById(id);
		return user;
	}

	public Page<User> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<User> users = userMapper.findByKeyword(rowBounds, searchKey, pageable);

		Long total = userMapper.count(searchKey);
		return new PageImpl<>(users, pageable, total);
	}

	@Transactional
	public void edit(User user) {
		userMapper.edit(user);
	}

	@Transactional
	public void editPassword(User user) {
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		userMapper.editPassword(user);
	}

	@Transactional
	public void create(User user) {
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);

		userMapper.create(user);
	}

	@Transactional
	public void editLoginUser(User user) {
		userMapper.editLoginUser(user);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User users = userMapper.findByUsername(user.getUserId());
		UserDetails userDetails = new CustomUserDetails(users);
		
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
				userDetails,
				authentication.getCredentials(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	@Transactional
	public void logicalDelete(int id) {
		userMapper.logicalDelete(id);
	}
	
	public List<User> findWithoutThisId(int id){
		return userMapper.findWithoutThisId(id);
	}
}