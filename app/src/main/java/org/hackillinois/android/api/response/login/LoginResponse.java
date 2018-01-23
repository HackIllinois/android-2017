package org.hackillinois.android.api.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private LoginResponseData loginResponseData;

	public String getMeta() {
		return meta;
	}

	public LoginResponseData getLoginResponseData() {
		return loginResponseData;
	}

	public static class LoginResponseData {
		@SerializedName("auth") private String auth;

		public String getAuth() {
			return auth;
		}
	}
}
