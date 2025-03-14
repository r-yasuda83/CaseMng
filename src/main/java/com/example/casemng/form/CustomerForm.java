package com.example.casemng.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerForm {

	private int id;
	@NotBlank
	private String lastName;
	@NotBlank
	private String firstName;
	@Pattern(regexp = "^\\d+$", message = "半角数字のみで入力してください")
	@Size(min=7, max=7, message = "7桁で入力してください")
	private String zipcode;
	@NotBlank
	private String address;
	@NotBlank
	@Size(min=10, max=11, message = "10 から 11 の間の桁で入力してください")
	@Pattern(regexp = "^\\d+$", message = "半角数字のみで入力してください")
	private String phoneNumber;

	private String memo;
	
	private boolean isDeleted;
	
	public String getFullName() {
		return lastName + " " + firstName;
	}
}
