package org.hackillinois.android.api.response.recruiter;

import com.google.gson.annotations.SerializedName;

public class RecruiterInterestRequestNew extends RecruiterInterestRequestExisting {
	@SerializedName("attendeeUserId") private long attendeeUserId;

	public RecruiterInterestRequestNew(long attendeeUserId, String comments, boolean favorite) {
		super(comments, favorite);
		this.attendeeUserId = attendeeUserId;
	}
}
