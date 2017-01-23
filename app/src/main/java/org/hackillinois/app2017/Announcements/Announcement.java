package org.hackillinois.app2017.Announcements;

public class Announcement implements Notification {
    private String message;
    private String time;

    public Announcement(String message, String time){
        this.message = message;
        this.time = time;
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public String getTime() {
        return time;
    }
}
