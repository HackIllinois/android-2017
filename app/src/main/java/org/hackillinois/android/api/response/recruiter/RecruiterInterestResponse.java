
package org.hackillinois.android.api.response.recruiter;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class RecruiterInterestResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private RecruiterInterest data;

	public RecruiterInterest getData() {
		return data;
	}

	public String getMeta() {
		return meta;
	}

	public static class RecruiterInterest {
		@SerializedName("recruiterId") private long recruiterId;
		@SerializedName("attendeeId") private long attendeeId;
		@SerializedName("comments") private String comments;
		@SerializedName("favorite") private boolean favorite;
		@SerializedName("created") private DateTime created;
		@SerializedName("updated") private DateTime updated;
		@SerializedName("appId") private long appId;

		public long getAppId() {
			return appId;
		}

		public long getAttendeeId() {
			return attendeeId;
		}

		public String getComments() {
			return comments;
		}

		public boolean getFavorite() {
			return favorite;
		}

		public Long getRecruiterId() {
			return recruiterId;
		}

		public DateTime getCreated() {
			return created;
		}

		public DateTime getUpdated() {
			return updated;
		}
	}

}
