package org.hackillinois.app2017.Events;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Event {

    @SerializedName("locations")
    private List<EventLocation> locations;

    @SerializedName("description")
    private String description;

    @SerializedName("startTime")
    private Date startTime;

    @SerializedName("endTime")
    private Date endTime;

    @SerializedName("name")
    private String name;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName(value="qrCode", alternate={"tracking"})
    private int qrCode;

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLocation(){ //TODO CHECK THIS
        StringBuilder sb = new StringBuilder();
        String seperator = "";
        for(EventLocation el : locations) {
            sb.append(seperator).append(el.getName());
            seperator = ", ";
        }
        return sb.toString();
    }

    public String getShortLocations() {
        StringBuilder sb = new StringBuilder();
        String seperator = "";
        for(EventLocation el : locations) {
            sb.append(seperator).append(el.getShortName());
            seperator = ", ";
        }
        return sb.toString();
    }

    public String getStartTime() {
        return startTime.toString();
    }

    public int getStartDay() {
        return startTime.getDay();
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public boolean needsQRCode() {
        return qrCode > 0;
    }
}
