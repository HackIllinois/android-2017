package org.hackillinois.android.api.response.tracking;

import com.google.gson.annotations.SerializedName;

public class TrackingRequest {
    @SerializedName("name") private String name;
    @SerializedName("duration") private String duration;

    public TrackingRequest(String aName, String aDuration) {
        this.name = aName;
        this.duration = aDuration;
    }
}
