package org.hackillinois.app2017.Announcements;

public class Announcement implements Notification {
    private int id;
    private String title;
    private String description;
    private String created;

    public Announcement(String message, String time){
        this.description = message;
        this.created = time;
        id = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title== null || title.isEmpty() ? "HackIllinois" : title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getMessage() {
        return getDescription();
    }

    @Override
    public String getTime() { //"2017-02-13T03:53:49.000Z"
        return created;
    }
}
