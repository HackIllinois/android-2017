package org.hackillinois.android.api.response.location;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {
    @SerializedName("meta") private String meta;
    @SerializedName("data") private Location[] locations;

    public Location[] getLocations() { return locations; }

    public static class Location {
        @SerializedName("id") private long id;
        @SerializedName("name") private String name;
        @SerializedName("longitude") private double longitude;
        @SerializedName("latitude") private double latitude;

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double logitude) {
            this.longitude = logitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
