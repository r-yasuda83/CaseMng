package com.example.casemng.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.casemng.entity.CustomUserDetails;
import com.example.casemng.entity.Role;
import com.example.casemng.entity.User;
import com.example.casemng.form.SearchForm;
import com.example.casemng.form.UserEditPasswordForm;
import com.example.casemng.form.UserForm;
import com.example.casemng.form.UserRegistrationForm;
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

	@GetMapping("/admin/user")
	public String getList(@ModelAttribute("search") SearchForm form,
			@RequestParam(required = false) Integer displayedNum,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {

		return userService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}

	@GetMapping("/admin/user/{keyId}")
	public String getEdit(@ModelAttribute("formUser") UserForm form, @PathVariable("keyId") int id, Model model) {

		User user = userService.findById(id);
		if (user == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		if (form.getId() == 0) {
			form = modelMapper.map(user, UserForm.class);
		}

		model.addAttribute("formUser", form);

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/edit";
	}

	@PostMapping("/admin/user/{keyId}")
	public String postEdit(@ModelAttribute("formUser") @Validated UserForm form, BindingResult result,
			@PathVariable("keyId") int id, Model model) {

		User user = modelMapper.map(form, User.class);
		String errMsg = userService.duplicatesUserIdWithoutId(user);

		if (result.hasErrors() || errMsg != null) {
			model.addAttribute("errMsg", errMsg);
			return getEdit(form, id, model);
		}

		userService.edit(user);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/{id}/password")
	public String getEditPassword(@PathVariable int id, Model model) {

		User user = userService.findByIdPassword(id);
		UserEditPasswordForm form = modelMapper.map(user, UserEditPasswordForm.class);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("form", form);
		return "user/editPasswordAdmin";
	}

	@PostMapping("/admin/user/{id}/password")
	public String postEditPassword(@ModelAttribute @Validated UserEditPasswordForm form, BindingResult result,
			@PathVariable int id, Model model) {

		if (result.hasErrors()) {
			return "user/editPasswordAdmin";
		}
		User user = modelMapper.map(form, User.class);
		userService.editPassword(user);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/create")
	public String getCreate(@ModelAttribute("formUserRegistration") UserRegistrationForm form, Model model) {

		model.addAttribute("FormUserRegistration", form);

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/create";
	}

	@PostMapping("/admin/user/create")
	public String postCreate(@ModelAttribute("formUserRegistration") @Validated UserRegistrationForm form,
			BindingResult result, Model model) {

		User user = modelMapper.map(form, User.class);
		String errMsg = userService.duplicatesUserId(user.getUserId());

		if (result.hasErrors() || errMsg != null) {
			model.addAttribute("errMsg", errMsg);
			return getCreate(form, model);
		}

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

		int id = userDetails.getId();
		model.addAttribute("formUser", userService.findById(id));

		return "user/setting";
	}

	@PostMapping("/user/setting")
	public String postSetting(@ModelAttribute("formUser") @Validated UserForm form, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "user/setting";
		}

		User user = modelMapper.map(form, User.class);
		userService.editLoginUser(user);
		return "redirect:/case";
	}

	@GetMapping("/user/setting/password")
	public String getSettingPassword(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		int id = userDetails.getId();
		User user = userService.findByIdPassword(id);
		UserEditPasswordForm form = modelMapper.map(user, UserEditPasswordForm.class);
		model.addAttribute("form", form);
		return "user/editPassword";
	}

	@PostMapping("/user/setting/password")
	public String postSettingPassword(@ModelAttribute @Validated UserEditPasswordForm form,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "user/editPassword";
		}

		User user = modelMapper.map(form, User.class);
		userService.editPassword(user);
		return "redirect:/user/setting";
	}
}