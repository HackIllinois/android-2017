package org.hackillinois.android.api.response.user;

import com.google.gson.annotations.SerializedName;

public class ResumeResponse {
	@SerializedName("id") private String id;

	public ResumeResponse(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
