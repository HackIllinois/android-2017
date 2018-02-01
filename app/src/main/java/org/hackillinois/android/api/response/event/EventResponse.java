
package org.hackillinois.android.api.response.event;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EventResponse {
    @SerializedName("data") private List<Event> mData;
    @SerializedName("meta") private String meta;

    public List<Event> getData() {
        return mData;
    }

    public String getMeta() {
        return meta;
    }

	public static class Event {
		@SerializedName("description") private String description;
		@SerializedName("endTime") private String endTime;
		@SerializedName("id") private Long id;
		@SerializedName("locations") private List<Location> locations;
		@SerializedName("name") private String name;
		@SerializedName("startTime") private String startTime;
		@SerializedName("tag") private String tag;

		public String getDescription() {
			return description;
		}

		public String getEndTime() {
			return endTime;
		}

		public Long getId() {
			return id;
		}

		public List<Location> getLocations() {
			return locations;
		}

		public String getName() {
			return name;
		}

		public String getStartTime() {
			return startTime;
		}

		public String getTag() {
			return tag;
		}
	}

	public static class Location {
		@SerializedName("eventId") private Long eventId;
		@SerializedName("id") private Long id;
		@SerializedName("locationId") private Long locationId;

		public Long getEventId() {
			return eventId;
		}

		public Long getId() {
			return id;
		}

		public Long getLocationId() {
			return locationId;
		}
	}

}
