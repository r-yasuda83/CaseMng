package com.example.casemng.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FormUserRegistration {

	private int id;
	
	@NotBlank
	private String userId;

	@NotBlank
	private String lastName;

	@NotBlank
	private String firstName;

	@NotBlank
	@Email
	private String emailAddress;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String passwordReenter;

	private int role;

	private boolean deleted;

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
