
package org.hackillinois.android.api.response.announcement;

import com.google.gson.annotations.SerializedName;

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

	public static class Announcement {
		@SerializedName("created") private String created;
		@SerializedName("description") private String description;
		@SerializedName("id") private long id;
		@SerializedName("title") private String title;

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

}
