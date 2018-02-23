package org.hackillinois.android.api.response.recruiter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecruiterInterestMultipleResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private List<RecruiterInterest> data;

	public List<RecruiterInterest> getInterests() {
		return data;
	}

	public String getMeta() {
		return meta;
	}

}

