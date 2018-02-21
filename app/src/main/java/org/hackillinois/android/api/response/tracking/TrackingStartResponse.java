package org.hackillinois.android.api.response.tracking;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class TrackingStartResponse {
	@SerializedName("data") private TrackingStartData announcementStartData;

	@SerializedName("meta") private String meta;

	public TrackingStartData getAnnouncementStartData() {
		return announcementStartData;
	}

	public static class TrackingStartData {
		@SerializedName("id") private String id;
		@SerializedName("name") private String name;
		@SerializedName("duration") private String duration;
		@SerializedName("created") private DateTime created;
		@SerializedName("count") private int count;

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getDuration() {
			return duration;
		}

		public DateTime getCreated() {
			return created;
		}

		public int getCount() {
			return count;
		}
	}
}
