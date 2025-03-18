package com.example.casemng.model.entity;

import java.util.List;

import lombok.Data;

@Data
public class Customer {

	private int id;

	private String lastName;

	private String firstName;

	private String zipcode;

	private String address;

	private String phoneNumber;

	private String memo;

	private boolean isDeleted;

	private List<Case> cases;

	public String getFullName() {
		return lastName + " " + firstName;
	}
}