package com.example.casemng.model.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private final User user;

	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(this.user.getRoles().getRoleName());
	}
	
	@Override
	public String getUsername() {
		return this.user.getUserId();
	}
	
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}
	
	@Override
	public boolean isEnabled() {
		boolean bool = true;
		if(this.user.isDeleted()){
			bool = false;
		}
		return bool;
	}
	
	public String getLastname() {
		return this.user.getLastName();
	}
	
	public String getFirstname() {
		return this.user.getFirstName();
	}
	
	public String getFullname() {
		return this.user.getFullName();
	}
	
	public int getId() {
		return this.user.getId();
	}
}
