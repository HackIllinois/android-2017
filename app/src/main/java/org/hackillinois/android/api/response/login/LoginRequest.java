package org.hackillinois.android.api.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
	@SerializedName("email") private String email;
	@SerializedName("passowrd") private String password;

	public LoginRequest(String aEmail, String aPassword) {
		this.email = aEmail;
		this.password = aPassword;
	}
}
