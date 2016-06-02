package org.hackillinois.branding.Schedule;

import java.util.Date;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class Event {

    private String title;
    private String location;
    private long time;

    public Event(String title, String location, long time){
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public String getTitle(){
        return title;
    }

    public String getLocation(){
        return location;
    }

    public Date getTime(){
        return new Date(time);
    }
}
