package org.hackillinois.android.api.response.announcement;

import com.google.gson.annotations.SerializedName;

public class AnnouncementStartResponse {

    @SerializedName("id") private String id;
    @SerializedName("title") private String title;
    @SerializedName("description") private String description;
    @SerializedName("created") private String created;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }
}
