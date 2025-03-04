package com.example.casemng.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.example.casemng.entity.User;
import com.example.casemng.form.FormUser;
import com.example.casemng.form.FormUserEditPassword;
import com.example.casemng.form.FormUserRegistration;
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
	
	public FormUserEditPassword findByIdPassword(int id) {
		User user = userMapper.findById(id);
		FormUserEditPassword form = modelMapper.map(user, FormUserEditPassword.class);
		return form;
	}
	
	public Page<User> findByKeyword(Pageable pageable, String searchKey) {
		RowBounds rowBounds = new RowBounds(
				(int) pageable.getOffset(), pageable.getPageSize());
		List<User> users = userMapper.findByKeyword(rowBounds, searchKey, pageable);

		Long total = userMapper.count(searchKey);
		return new PageImpl<>(users, pageable, total);
	}

	@Transactional
	public void edit(FormUser form) {
		User user = modelMapper.map(form, User.class);
		userMapper.edit(user);
	}
	
	@Transactional
	public void editPassword(FormUserEditPassword form) {
		User user = modelMapper.map(form, User.class);
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		userMapper.editPassword(user);
	}

	@Transactional
	public void create(FormUserRegistration form) {
		User user = modelMapper.map(form, User.class);
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		
		userMapper.create(user);
	}

	@Transactional
	public void editLoginUser(FormUserRegistration form) {
		User user = modelMapper.map(form, User.class);
	
			userMapper.editLoginUser(user);
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
	
	public String duplicatesUserIdWithoutId(FormUser form) {
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
	
	public String pagenation(String searchKey, Integer displayedNum, String sortKey, String sortDirection, Pageable pageable, Model model) {
		if (searchKey == null) {
			searchKey = "";
		}
		Sort sort = null;
		if (StringUtils.hasLength(sortDirection)) {
			String sd = sortDirection.equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC.name() : Sort.Direction.DESC.name();
			String si = sortKey;
			model.addAttribute("sortKey", si);

			sort = Sort.by(Sort.Direction.fromString(sd), si);
			model.addAttribute("sortDirection", sd);
		}else {
			sort = Sort.by(Sort.Direction.ASC, "id");
		}
		if(displayedNum == null) {
			displayedNum = 5;
		}
		Pageable p = sort == null ? pageable : PageRequest.of(pageable.getPageNumber(), displayedNum, sort);
		Page<User> user = findByKeyword(p, "%" + searchKey + "%");
		model.addAttribute("page", user);
		
		return "user/list";
	}
}