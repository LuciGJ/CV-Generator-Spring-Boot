package com.luci.cvgenerator.user;

import com.luci.cvgenerator.validation.StrongPassword;
import com.luci.cvgenerator.validation.ValidEmail;
import com.luci.cvgenerator.validation.ValidEqualFields;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidEqualFields.List({ @ValidEqualFields(first = "password", second = "confirmPassword") })
public class CVUser {

	@NotNull(message = "Username is required")
	@Size(min = 1, max = 30, message = "Please choose an username between 1 and 30 characters")
	private String username;

	@StrongPassword
	@NotNull(message = "Password is required")
	private String password;

	@NotNull(message = "Password confirmation is required")

	private String confirmPassword;

	@ValidEmail
	@NotNull(message = "Email is required")
	@Size(min = 1, message = "Email is required")
	private String email;

	public CVUser() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
