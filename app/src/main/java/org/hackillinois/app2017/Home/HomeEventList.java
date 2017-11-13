package org.hackillinois.app2017.Home;

import android.util.Log;

import com.annimon.stream.Stream;

import org.hackillinois.app2017.Events.Event;
import org.hackillinois.app2017.Events.EventManager;

import java.util.ArrayList;
import java.util.Date;
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
        clear();
		Date now = new Date();
		Stream.of(EventManager.getInstance().getEvents())
				.filter(event -> shouldDisplayEvent(now, event))
				.forEach(this::add);
	}

	private static boolean shouldDisplayEvent(Date date, Event event) {
		Date start = event.getStartTime();
		Date end = event.getEndTime();
		if (date.before(start) || date.after(end)) { // if it hasn't started or already ended
			return false;
		} else if (TimeUnit.MILLISECONDS.toHours(date.getTime() - start.getTime()) > 6) {
			// if it started > 6 hours ago
			return false;
		}

		Log.d("HomeEventList", "current date " + date.toString() + " is after " + event.getStartTime() + " and before " + event.getEndTime());
		return true;
	}

    @Override
    public void clear() {
        super.clear();
        add(header);
    }
}
