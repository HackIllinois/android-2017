package org.hackillinois.android.api.response.announcement;


import com.google.gson.annotations.SerializedName;

public class AnnouncementRequest {

    @SerializedName("title") private String title;
    @SerializedName("description") private String description;

    public AnnouncementRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
