package org.hackillinois.android.api.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
	@SerializedName("data") private LoginResponseData data;

	public LoginResponse(LoginResponseData data) {
		this.data = data;
	}

	public LoginResponseData getData() {
		return data;
	}
}
