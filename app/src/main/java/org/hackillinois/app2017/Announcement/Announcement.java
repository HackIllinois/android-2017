package org.hackillinois.app2017.Announcement;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class Announcement {

    private String title;
    private String message;

    public Announcement(String title, String message){
        this.title = title;
        this.message = message;
    }

    public String getTitle(){
        return title;
    }

    public String getMessage(){
        return message;
    }
}
