package org.hackillinois.app2017.Events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2/21/2017.
 */

public class EventLocation {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
