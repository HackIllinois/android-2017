package org.hackillinois.app2017.Announcements;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kevin on 2/23/2017.
 */

public class AnnouncementManager {
    private static AnnouncementManager instance;
    private ArrayList<Announcement> events;
    private static CallBack callback;

    public static void sync(Context applicationContext) {
        BackgroundAnnouncements.sync(applicationContext);
    }

    public interface CallBack {
        void onEventsSet();
    }

    private AnnouncementManager() {
        events = new ArrayList<>();
    }

    public static AnnouncementManager getInstance() {
        if (instance == null) {
            instance = new AnnouncementManager();
        }

        return instance;
    }

    public void setCallback(CallBack toCall) {
        callback = toCall;
    }

    public ArrayList<Announcement> getEvents() {
        return events;
    }

    public void setEvents(Collection<Announcement> events) {
        this.events.clear();
        this.events.addAll(events);
        if(callback != null) {
            callback.onEventsSet();
        }
    }
}