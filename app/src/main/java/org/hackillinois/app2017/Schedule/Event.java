package org.hackillinois.app2017.Schedule;

public class Event {

    private String title;
    private String location;
    private int hour;
    private int minutes;
    private boolean AM;

    public Event(String title, String location, int hour, int minutes, boolean AM){
        this.title = title;
        this.location = location;
        this.hour = hour;
        this.minutes = minutes;
        this.AM = AM;
    }

    public String getTitle(){
        return title;
    }

    public String getLocation(){
        return location;
    }

    public String getTime(){
        String base = "";
        if(minutes < 10){
            base = Integer.toString(hour) + ":0" + Integer.toString(minutes);
        }else{
            base = Integer.toString(hour) + ":" + Integer.toString(minutes);
        }
        return base + (AM ? " AM" : " PM");
    }
}
