package com.example.casemng.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.casemng.form.SearchForm;
import com.example.casemng.form.userform.UserEditPasswordForm;
import com.example.casemng.form.userform.UserForm;
import com.example.casemng.form.userform.UserRegistrationForm;
import com.example.casemng.form.validator.NewUserValidator;
import com.example.casemng.form.validator.UserValidator;
import com.example.casemng.model.Pagenation;
import com.example.casemng.model.entity.CustomUserDetails;
import com.example.casemng.model.entity.Role;
import com.example.casemng.model.entity.User;
import com.example.casemng.service.RoleService;
import com.example.casemng.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	Pagenation pagenation;

	@GetMapping("/admin/user")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {

		Pageable p = pagenation.getPageable(displayedNum, sortKey, sortDirection, pageable, model);

		String searchKey = null;
		if (form.getKeyword() == null) {
			searchKey = "";
		} else {
			searchKey = form.getKeyword();
		}

		Page<User> user = userService.findByKeyword(p, searchKey);
		model.addAttribute("page", user);

		return "user/list";
	}

	@GetMapping("/admin/user/{id}")
	public String getEdit(@PathVariable int id, Model model) {

		User user = userService.findById(id);
		if (user == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		if (!model.containsAttribute("userForm")) {
			UserForm userForm = modelMapper.map(user, UserForm.class);
			model.addAttribute("userForm", userForm);
		}

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/edit";
	}

	@Autowired
	private UserValidator userValidator;

	@PostMapping("/admin/user/{id}")
	public String postEdit(@ModelAttribute("userForm") @Validated UserForm form, BindingResult result,
			@PathVariable int id, Model model) {

		userValidator.validate(form, result);

		User user = modelMapper.map(form, User.class);

		if (result.hasErrors()) {
			return getEdit(id, model);
		}

		userService.edit(user);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/{id}/password")
	public String getEditPassword(@PathVariable int id,	Model model) {

		User user = userService.findByIdPassword(id);
		if (user == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}

		if (!model.containsAttribute("userEditPasswordForm")) {
			UserEditPasswordForm form = modelMapper.map(user, UserEditPasswordForm.class);
			model.addAttribute("userEditPasswordForm", form);
		}

		return "user/editPasswordAdmin";
	}

	@PostMapping("/admin/user/{id}/password")
	public String postEditPassword(@ModelAttribute("userEditPasswordForm") @Validated UserEditPasswordForm form,
			BindingResult result,
			@PathVariable int id, Model model) {

		if (result.hasErrors()) {
			return getEditPassword(id, model);
		}

		User user = modelMapper.map(form, User.class);
		userService.editPassword(user);
		return "redirect:/admin/user";
	}
	
	@Autowired
	UserRegistrationForm userRegistrationForm;

	@GetMapping("/admin/user/create")
	public String getCreate(Model model) {
		
		if (!model.containsAttribute("userRegistrationForm")) {
			model.addAttribute("userRegistrationForm", userRegistrationForm);
		}

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/create";
	}

	@Autowired
	private NewUserValidator newUserValidator;

	@PostMapping("/admin/user/create")
	public String postCreate(@ModelAttribute("userRegistrationForm") @Validated UserRegistrationForm form,
			BindingResult result, Model model) {

		newUserValidator.validate(form, result);

		if (result.hasErrors()) {
			return getCreate(model);
		}

		User user = modelMapper.map(form, User.class);
		userService.create(user);
		return "redirect:/admin/user";
	}

	@PostMapping("/admin/user/{id}/delete")
	public String postDelete(@PathVariable int id, Model model) {

		userService.logicalDelete(id);
		return "redirect:/admin/user";
	}

	@GetMapping("/user/setting")
	public String getSetting(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		
		if (!model.containsAttribute("userForm")) {
			int id = userDetails.getId();
			model.addAttribute("userForm", userService.findById(id));
		}

		return "user/setting";
	}

	@PostMapping("/user/setting")
	public String postSetting(@ModelAttribute("userForm") @Validated UserForm form, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails userDetails,
			Model model) {

		if (result.hasErrors()) {
			return getSetting(userDetails, model);
		}

		User user = modelMapper.map(form, User.class);
		userService.editLoginUser(user);
		return "redirect:/case";
	}

	@GetMapping("/user/setting/password")
	public String getSettingPassword(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		
		if (!model.containsAttribute("userEditPasswordForm")) {
			int id = userDetails.getId();
			User user = userService.findByIdPassword(id);
			UserEditPasswordForm form = modelMapper.map(user, UserEditPasswordForm.class);
			model.addAttribute("userEditPasswordForm", form);
		}
		
		return "user/editPassword";
	}

	@PostMapping("/user/setting/password")
	public String postSettingPassword(@ModelAttribute("userEditPasswordForm") @Validated UserEditPasswordForm form,
			BindingResult result, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		if (result.hasErrors()) {
			return getSettingPassword(userDetails, model);
		}

		User user = modelMapper.map(form, User.class);
		userService.editPassword(user);
		return "redirect:/user/setting";
	}
}