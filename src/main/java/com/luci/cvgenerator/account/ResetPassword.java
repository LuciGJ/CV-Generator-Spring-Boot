package com.luci.cvgenerator.account;

import com.luci.cvgenerator.validation.StrongPassword;
import com.luci.cvgenerator.validation.ValidEqualFields;

import jakarta.validation.constraints.NotNull;

@ValidEqualFields.List({ @ValidEqualFields(first = "password", second = "confirmPassword") })
public class ResetPassword {

	@StrongPassword
	@NotNull(message = "Password is required")
	private String password;

	@NotNull(message = "Password confirmation is required")

	private String confirmPassword;

	private String token;

	public ResetPassword() {
	}

	public ResetPassword(@NotNull(message = "Password is required") String password,
			@NotNull(message = "Password confirmation is required") String confirmPassword) {
		this.password = password;
		this.confirmPassword = confirmPassword;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
