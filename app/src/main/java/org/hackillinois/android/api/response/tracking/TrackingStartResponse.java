package org.hackillinois.android.api.response.tracking;

import com.google.gson.annotations.SerializedName;

public class TrackingStartResponse {

    @SerializedName("id") private String id;
    @SerializedName("name") private String name;
    @SerializedName("duration") private String duration;
    @SerializedName("created") private String created;
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

    public String getCreated() {
        return created;
    }

    public int getCount() {
        return count;
    }
}
