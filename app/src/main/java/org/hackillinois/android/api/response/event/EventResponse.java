
package org.hackillinois.android.api.response.event;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

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
		@SerializedName("endTime") private DateTime endTime;
		@SerializedName("id") private long id;
		@SerializedName("locations") private List<Location> locations;
		@SerializedName("name") private String name;
		@SerializedName("startTime") private DateTime startTime;
		@SerializedName("tag") private String tag;

		public String getDescription() {
			return description;
		}

		public DateTime getEndTime() {
			return endTime;
		}

		public long getId() {
			return id;
		}

		public List<Location> getLocations() {
			return locations;
		}

		public String getName() {
			return name;
		}

		public DateTime getStartTime() {
			return startTime;
		}

		public String getTag() {
			return tag;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Event event = (Event) o;

			return getId() == event.getId();
		}

		@Override
		public int hashCode() {
			return (int) (getId() ^ (getId() >>> 32));
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
