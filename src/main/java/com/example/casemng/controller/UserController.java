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
import com.example.casemng.form.FormSearch;
import com.example.casemng.form.FormUser;
import com.example.casemng.form.FormUserEditPassword;
import com.example.casemng.form.FormUserRegistration;
import com.example.casemng.service.RoleService;
import com.example.casemng.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/admin/user")
	public String getList(@ModelAttribute("search") FormSearch form,
			@RequestParam(name = "displayedNum", required = false) Integer displayedNum,
			@RequestParam(name = "sortKey", required = false) String sortKey,
			@RequestParam(name = "sortDirection", required = false) String sortDirection,
			@PageableDefault(size = 5) Pageable pageable, Model model) {
		return userService.pagenation(form.getKeyword(), displayedNum, sortKey, sortDirection, pageable, model);
	}

	@Autowired
	RoleService roleService;

	@GetMapping("/admin/user/{id}")
	public String getEdit(@PathVariable("id") int id, Model model) {

		FormUser form = userService.findById(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("formUser", form);

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/edit";
	}

	@Autowired
	ModelMapper modelMapper;

	@PostMapping("/admin/user/{id}")
	public String postEdit(@ModelAttribute("formUser") @Validated FormUser form,
			BindingResult result,
			@PathVariable int id, Model model) {

		String errMsg = userService.duplicatesUserIdWithoutId(form);

		if (result.hasErrors() || errMsg != null) {
			List<Role> roleList = roleService.findAll();
			model.addAttribute("roleList", roleList);

			model.addAttribute("errMsg", errMsg);
			return "user/edit";
		}

		userService.edit(form);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/{id}/password")
	public String getEditPassword(@PathVariable("id") int id, Model model) {

		FormUserEditPassword form = userService.findByIdPassword(id);
		if (form == null) {
			model.addAttribute("msg", "存在しないIDです。");
			return "error";
		}
		model.addAttribute("form", form);
		return "user/editPasswordAdmin";
	}

	@PostMapping("/admin/user/{id}/password")
	public String postEditPassword(@ModelAttribute("form") @Validated FormUserEditPassword form, BindingResult result,
			@PathVariable("id") int id, Model model) {

		if (result.hasErrors()) {
			return "user/editPasswordAdmin";
		}
		userService.editPassword(form);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/create")
	public String getCreate(@ModelAttribute("formUserRegistration") FormUserRegistration form, Model model) {

		model.addAttribute("FormUserRegistration", form);

		List<Role> roleList = roleService.findAll();
		model.addAttribute("roleList", roleList);
		return "user/create";
	}

	@PostMapping("/admin/user/create")
	public String postCreate(@ModelAttribute("formUserRegistration") @Validated FormUserRegistration form,
			BindingResult result, Model model) {

		String errMsg = userService.duplicatesUserId(form.getUserId());

		if (result.hasErrors() || errMsg != null) {
			List<Role> roleList = roleService.findAll();
			model.addAttribute("roleList", roleList);

			model.addAttribute("errMsg", errMsg);
			return "user/create";
		}

		userService.create(form);
		return "redirect:/admin/user";
	}

	@PostMapping("/admin/user/{id}/delete")
	public String postDelete(@PathVariable("id") int id, Model model) {

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
	public String postSetting(@ModelAttribute("formUser") @Validated FormUser form, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "user/setting";
		}
		userService.editLoginUser(form);
		return "redirect:/case";
	}

	@GetMapping("/user/setting/password")
	public String getSettingPassword(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

		int id = userDetails.getId();
		FormUserEditPassword form = userService.findByIdPassword(id);
		model.addAttribute("form", form);
		return "user/editPassword";
	}

	@PostMapping("/user/setting/password")
	public String postSettingPassword(@ModelAttribute("form") @Validated FormUserEditPassword form,
			BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "user/editPassword";
		}
		userService.editPassword(form);
		return "redirect:/user/setting";
	}
}