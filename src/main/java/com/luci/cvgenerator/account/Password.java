package com.luci.cvgenerator.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Password {
	@NotNull(message = "Username is required")
	@Size(min = 1, message = "Username is required")
	private String username;

	public Password() {
	}

	public Password(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
