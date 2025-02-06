package com.example.casemng.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

import com.example.casemng.entity.Department;

import lombok.Data;

@Data
public class FormUserForEdit {

	private int id;
	
	@NotBlank
	private String userId;

	private String password;
	
	private String passwordReenter;

	@NotBlank
	private String lastName;

	@NotBlank
	private String firstName;

	@NotBlank
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
