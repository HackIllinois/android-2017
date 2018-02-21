package org.hackillinois.android.api.response.announcement;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class AnnouncementStartResponse {
	@SerializedName("data") private AnnouncementStartData announcementStartData;

	@SerializedName("meta") private String meta;

	public AnnouncementStartData getAnnouncementStartData() {
		return announcementStartData;
	}

	public static class AnnouncementStartData {
		@SerializedName("id") private String id;
		@SerializedName("title") private String title;
		@SerializedName("description") private String description;
		@SerializedName("created") private DateTime created;

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public DateTime getCreated() {
			return created;
		}
	}
}
