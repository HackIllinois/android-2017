package org.hackillinois.android.api.response.user;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
	@SerializedName("data") private UserInfoData data;

	public UserInfoData getData() {
		return data;
	}
}
