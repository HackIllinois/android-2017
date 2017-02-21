package org.hackillinois.app2017.Schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.gson.annotations.SerializedName;

public class Event {

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

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLocation(){
        return "TODO"; //TODO
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
}
