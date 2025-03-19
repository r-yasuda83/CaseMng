package com.example.casemng.form.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.casemng.form.UserRegistrationForm;
import com.example.casemng.model.entity.User;
import com.example.casemng.service.UserService;

@Component
public class NewUserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserRegistrationForm.class.equals(clazz);
	}
	
	@Autowired
	UserService userService;
	
	public void validate(Object target, Errors errors) {
		UserRegistrationForm form = (UserRegistrationForm) target;
		
		List<User> userList = userService.findAll();
		for (User check : userList) {
			if (check.getUserId().equals(form.getUserId())) {
				errors.rejectValue("userId", "error.userRegistrationForm",
						"このユーザーIDは既に使われています");
				break;
			}
		}
	}
}
