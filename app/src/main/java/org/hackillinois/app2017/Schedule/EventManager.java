package org.hackillinois.app2017.Schedule;

import java.util.ArrayList;

/**
 * @author dl-eric
 */

public class EventManager {

    private static EventManager instance;
    private ArrayList<Event> events;

    private EventManager() {
        events = new ArrayList<>();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
