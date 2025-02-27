package com.example.casemng.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FormUserForEdit {

	private int id;
	
	@NotBlank
	private String userId;

	@NotBlank
	private String lastName;

	@NotBlank
	private String firstName;

	@NotBlank
	private String emailAddress;

	private int role;

	private boolean deleted;

	public String getFullName() {
		return this.lastName + " " + this.firstName;
	}
}
