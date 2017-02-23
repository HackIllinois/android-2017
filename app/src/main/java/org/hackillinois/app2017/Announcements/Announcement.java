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
        if(TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMilliseconds) < 60) { //less than 60 seconds
            return "Less than a minute ago";
        } else if (TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMilliseconds) < 60) {
            return TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMilliseconds) + " minutes ago";
        } else if(TimeUnit.MILLISECONDS.toHours(timeDifferenceInMilliseconds) < 24) {
            return TimeUnit.MILLISECONDS.toHours(timeDifferenceInMilliseconds) + " hours ago";
        } else {
            return TimeUnit.MILLISECONDS.toDays(timeDifferenceInMilliseconds) + " days ago";
        }
    }
}
