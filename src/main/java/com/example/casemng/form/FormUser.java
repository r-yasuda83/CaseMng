package com.example.casemng.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.example.casemng.entity.Department;

import lombok.Data;

@Data
public class FormUser {

	private int id;

	@NotBlank
	private String userId;

	@NotBlank
	private String password;
	
	@NotBlank
	private String passwordReenter;

	@NotBlank
	private String lastName;

	@NotBlank
	private String firstName;

	@NotBlank
	@Email
	private String emailAddress;

	private int departmentId;

	private int role;

	private boolean deleted;

	private Department department;

	public String getFullName() {
		return this.lastName + " " + this.firstName;
	}
	
	@AssertTrue(message = "パスワードとパスワード（確認用）は同一にしてください。")
	public boolean isPasswordValid() {
		if (password == null || password.isEmpty()) {
			return true;
		}
		return password.equals(passwordReenter);
	} 
}
