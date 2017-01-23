package org.hackillinois.app2017.Announcements;

public class Reminder implements Notification {
    private String message;
    private String time;
    private String location;

    public Reminder(String message, String time, String location) {
        this.message = message;
        this.time = time;
        this.location = location;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}
