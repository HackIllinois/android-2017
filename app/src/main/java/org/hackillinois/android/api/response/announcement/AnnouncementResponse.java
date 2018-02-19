
package org.hackillinois.android.api.response.announcement;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

public class AnnouncementResponse {
	@SerializedName("data") private List<Announcement> announcements;
	@SerializedName("meta") private String meta;

	public List<Announcement> getAnnouncements() {
		return announcements;
	}

	public String getMeta() {
		return meta;
	}

	public static class Announcement implements Comparable<Announcement> {
		@SerializedName("created") private DateTime created;
		@SerializedName("description") private String description;
		@SerializedName("id") private long id;
		@SerializedName("title") private String title;

		public DateTime getCreated() {
			return created;
		}

		public String getDescription() {
			return description;
		}

		public long getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public int compareTo(@NonNull Announcement other) {
			int compared = other.getCreated().compareTo(getCreated()); // reverse
			if (compared == 0) {
				return getTitle().compareTo(other.getTitle());
			}
			return compared;
		}
	}
}
