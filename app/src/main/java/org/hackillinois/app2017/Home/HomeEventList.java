package org.hackillinois.app2017.Home;

import android.util.Log;

import org.hackillinois.app2017.Events.Event;
import org.hackillinois.app2017.Events.EventManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        clear();
        for(Event e : events) {
            Date start = getDateFromAPI(e.getStartTime()); //format as date
            Date end = getDateFromAPI(e.getEndTime()); //format as date
            Date date = new Date();
            if(date.after(start) && date.before(end)) { //if current time is after start and before end, add it
                Log.d("HomeEventList", "current date " + date.toString() + " is after " + e.getStartTime() + " and after " + e.getEndTime());
                add(e);
            }
        }
    }

    public static Date getDateFromAPI(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clear() {
        super.clear();
        add(header);
    }
}
