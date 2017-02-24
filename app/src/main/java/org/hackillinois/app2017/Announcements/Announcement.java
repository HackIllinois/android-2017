package org.hackillinois.app2017.Announcements;

import org.hackillinois.app2017.Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public String getCreated() {
        return created;
    }

    @Override
    public String getTime() { //"2017-02-13T03:53:49.000Z"
        Date date = Utils.getDateFromAPI(created);
        if(date == null) {
            return created;
        } else {
            return prettyPrintDate(date);
        }
    }

    private String prettyPrintDate(Date date) {
        Date now = new Date();
        long timeDifferenceInMilliseconds = now.getTime() - date.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMilliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMilliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMilliseconds);
        long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceInMilliseconds);
        boolean isSingular = false;
        StringBuilder stringBuilder = new StringBuilder();
        if( seconds < 60) { //less than 60 seconds
            stringBuilder.append("Less than a minute ago");
            return stringBuilder.toString();
        } else if ( minutes < 60) {
            isSingular = minutes == 1;
            stringBuilder.append(minutes).append(" minute");
        } else if( hours < 24) {
            isSingular = hours == 1;
            stringBuilder.append(hours).append(" hour");
        } else {
            isSingular = days == 1;
            stringBuilder.append(days).append(" day");
        }

        if(isSingular) {
            stringBuilder.append(" ago");
        } else {
            stringBuilder.append("s ago");
        }

        return stringBuilder.toString();
    }
}
