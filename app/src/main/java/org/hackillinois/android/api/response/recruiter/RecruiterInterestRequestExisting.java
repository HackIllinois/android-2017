package org.hackillinois.android.api.response.recruiter;

import com.google.gson.annotations.SerializedName;

public class RecruiterInterestRequestExisting {
	@SerializedName("comments") private String comments;
	@SerializedName("favorite") private boolean favorite;

	public RecruiterInterestRequestExisting(String comments, boolean favorite) {
		this.comments = comments;
		this.favorite = favorite;
	}
}
