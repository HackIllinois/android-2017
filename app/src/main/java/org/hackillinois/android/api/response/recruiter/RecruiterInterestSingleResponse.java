
package org.hackillinois.android.api.response.recruiter;

import com.google.gson.annotations.SerializedName;

public class RecruiterInterestSingleResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private RecruiterInterest data;

	public RecruiterInterest getInterest() {
		return data;
	}

	public String getMeta() {
		return meta;
	}

}
