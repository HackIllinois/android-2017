package org.hackillinois.app2017.Home;

import android.util.Log;

import org.hackillinois.app2017.Events.Event;
import org.hackillinois.app2017.Events.EventManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2/21/2017.
 */

public class HomeEventList extends ArrayList<Object> {
    private HomeTime header;

    public HomeEventList(HomeTime head) {
        header = head;
        add(header);
    }

    public void syncEvents() {
        List<Event> events = EventManager.getInstance().getEvents();
        if(events == null) {
            return;
        }
        clear();
        for(Event e : events) {
            Date start = e.getStartTime();
            Date end = e.getEndTime();
            Date date = new Date();
            if(TimeUnit.MILLISECONDS.toHours(date.getTime() - start.getTime()) > 6) {
                continue;
            }
            if(date.after(start) && date.before(end)) { //if current time is after start and before end, add it
                Log.d("HomeEventList", "current date " + date.toString() + " is after " + e.getStartTime() + " and before " + e.getEndTime());
                add(e);
                // if you would prefer events to show with most recent at the top
                // add(1,e);
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        add(header);
    }
}
