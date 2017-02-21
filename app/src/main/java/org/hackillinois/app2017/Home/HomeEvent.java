package org.hackillinois.app2017.Home;

import org.hackillinois.app2017.Schedule.Event;

/**
 * @author dl-eric
 */

public class HomeEvent {
    private String title;
    private String location;
    private String time;

    public HomeEvent(Event e) {
        title = e.getShortName();
        location = e.getLocation();
        time = e.getStartTime();
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
