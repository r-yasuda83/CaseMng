package com.example.casemng.form.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserEditPasswordForm {

	private int id;
	
	private String lastName;

	private String firstName;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String passwordReenter;
	
	public String getFullName() {
		return lastName + " " + firstName;
	}
	
	@AssertTrue(message = "パスワードとパスワード（確認用）は同一にしてください。")
	public boolean isPasswordValid() {
		if (password == null || password.isEmpty()) {
			return true;
		}
		return password.equals(passwordReenter);
	}
}