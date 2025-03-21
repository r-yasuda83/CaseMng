package com.example.casemng.form.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.casemng.form.userform.UserForm;
import com.example.casemng.model.entity.User;
import com.example.casemng.service.UserService;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserForm.class.equals(clazz);
	}
	
	@Autowired
	UserService userService;
	
	public void validate(Object target, Errors errors) {
		UserForm form = (UserForm) target;
		
		List<User> userList = userService.findWithoutThisId(form.getId());
		for (User check : userList) {
			if (check.getUserId().equals(form.getUserId())) {
				errors.rejectValue("userId", "error.userForm",
						"このユーザーIDは既に使われています");
				break;
			}
		}
	}
}
