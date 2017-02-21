package org.hackillinois.app2017.Announcements;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.json.JSONObject;

import java.util.Collection;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by kevin on 2/21/2017.
 */

public class AnnouncementGrabberBroadcastReceiver extends BroadcastReceiver {
    public static final int GET_ANNOUNCEMENTS = 0;
    @Override
    public void onReceive(final Context context, Intent intent) {
        requestAnnouncements(context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TIMER", "got timed response");
                AnnouncementQuery announcementQuery = new Gson().fromJson(response.toString(), AnnouncementQuery.class);
                handleNewAnnouncements(announcementQuery.getData(),context);
            }
        });
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent i = new Intent(context, AnnouncementGrabberBroadcastReceiver.class);
        i.setClass(context,AnnouncementGrabberBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, GET_ANNOUNCEMENTS, i, 0);
    }

    public static void scheduleAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5000,
                300000, pendingIntent);//5min interval
    }

    public static void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

    public static void requestAnnouncements(Context context, Response.Listener<JSONObject> response) {
        String request = APIHelper.announcementsEndpoint + "?after=2017-02-22T00:00:00.000Z&before=2017-02-27T00:00:00.000Z";
        final JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET,
                request, null, response, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.addToRequestQueue(userRequest);
    }

    private static void handleNewAnnouncements(Collection<Announcement> announcements, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        int lastAnnouncement = sharedPreferences.getInt("newest_notification_id",-1);
        int newestAnnouncement = lastAnnouncement;
        for(Announcement announcement : announcements) {
            if(announcement.getId() > lastAnnouncement) {
                newestAnnouncement = Math.max(newestAnnouncement, announcement.getId());
                buildNotification(announcement,context);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("newest_notification_id",newestAnnouncement);
        editor.apply();
    }

    private static void buildNotification(Announcement announcement,Context context) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(announcement.getTitle())
                        .setContentText(announcement.getMessage());
        int mNotificationId = announcement.getId();
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
