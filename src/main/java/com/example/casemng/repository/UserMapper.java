package com.example.casemng.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.example.casemng.model.entity.User;

@Mapper
public interface UserMapper {
	
	public User findByUsername(String username);
	public User findById(int id);
	public List<User> findAll();
	public List<User> findWithoutThisId(int id);
	public List<User> findByKeyword(RowBounds rowBounds, String searchKey, Pageable pageable);
	public void create(User user);
	public void edit(User user);
	public void editPassword(User user);
	public void editExceptPassword(User user);
	public void editLoginUser(User user);
	public void logicalDelete(int id);
	public Long count(String searchKey);
}