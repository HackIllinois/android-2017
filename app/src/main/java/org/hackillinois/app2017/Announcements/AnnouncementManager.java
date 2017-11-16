package org.hackillinois.app2017.Announcements;

import android.content.Context;

import org.hackillinois.app2017.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by kevin on 2/23/2017.
 */

public class AnnouncementManager {
    private static AnnouncementManager instance;
    private ArrayList<Announcement> announcements;
    private static CallBack callback;

    public static void sync(Context applicationContext) {
        BackgroundAnnouncements.sync(applicationContext);
    }

    public interface CallBack {
        void onEventsSet();
    }

    private AnnouncementManager() {
        announcements = new ArrayList<>();
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

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Collection<Announcement> newAnnouncements) {
        announcements.clear();
        announcements.addAll(newAnnouncements);
        sortAnnouncements();
        if(callback != null) {
            callback.onEventsSet();
        }
    }

    private void sortAnnouncements() {
        Collections.sort(announcements, (lhs, rhs) -> {
			Date rhsDate = Utils.getDateFromAPI(rhs.getCreated());
			if(rhsDate == null) {
				return -1;
			}
			return rhsDate.compareTo(Utils.getDateFromAPI(lhs.getCreated()));
		});
    }
}