package org.hackillinois.android.api.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponseData {
	@SerializedName("auth") private String auth;

	public LoginResponseData(String auth) {
		this.auth = auth;
	}

	public String getAuth() {
		return auth;
	}
}
