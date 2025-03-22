package com.example.casemng.form.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserForm {

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

	private int role;

	private boolean deleted;

	public String getFullName() {
		return this.lastName + " " + this.firstName;
	}
}
