package org.hackillinois.android.api.response.tracking;

import com.google.gson.annotations.SerializedName;

public class TrackingRequest {
	@SerializedName("name") private String name;
	@SerializedName("duration") private long duration;

	public TrackingRequest(String name, long duration) {
		this.name = name;
		this.duration = duration;
	}
}
