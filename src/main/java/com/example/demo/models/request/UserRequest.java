package com.example.demo.models.request;

import com.example.demo.models.IUser;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRequest implements IUser {
	
	@NotBlank
	@Size(min = 4, max = 9)
	private String username;
	
	@NotEmpty
	@Email
	private String email;

	@Transient // not mapped to database
	private boolean admin;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
