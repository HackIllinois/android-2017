package org.hackillinois.app2017.Announcements;

import java.util.HashSet;

/**
 * Created by kevin on 2/20/2017.
 */

public class AnnouncementQuery {
    private String meta;
    private HashSet<Announcement> data;

    public HashSet<Announcement> getData() {
        return data;
    }
}
