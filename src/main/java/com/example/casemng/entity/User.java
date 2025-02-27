package com.example.casemng.entity;

import lombok.Data;

@Data
public class User {
	
	private int id;
	
	private String userId;
	
	private String password;
	
	private String lastName;
	
	private String firstName;
	
	private String emailAddress;
	
	private int role;
	
	private boolean deleted;
	
	private Role roles;
	
	public String getFullName() {
		return this.lastName + " " +this.firstName;
	}
}
