package org.hackillinois.app2017.Announcements;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
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
import org.hackillinois.app2017.Utils;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by kevin on 2/21/2017.
 */

public class BackgroundAnnouncements extends BroadcastReceiver {
    public static final int GET_ANNOUNCEMENTS = 0;
    private static PendingIntent grabAnnouncementsTask = null;
    @Override
    public void onReceive(final Context context, Intent intent) {
        requestAnnouncements(context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("BackgroundAnnouncements", "Checking for new announcements");
                AnnouncementQuery announcementQuery = new Gson().fromJson(response.toString(), AnnouncementQuery.class);
                handleNewAnnouncements(announcementQuery.getData(),context);
            }
        });
    }

    private static void setPendingIntent(Context context) {
        Intent i = new Intent(context, BackgroundAnnouncements.class);
        i.setClass(context,BackgroundAnnouncements.class);
        grabAnnouncementsTask = PendingIntent.getBroadcast(context, GET_ANNOUNCEMENTS, i, 0);
    }

    public static void startBackgroundAnnouncements(Context context) {
        setPendingIntent(context);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5000,
                300000, grabAnnouncementsTask);//5min interval
    }

    public static void stopBackgroundAnnouncements(Context context) {
        if(grabAnnouncementsTask == null) {
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(grabAnnouncementsTask);
    }

    public static void requestAnnouncements(Context context, Response.Listener<JSONObject> response) {
        final JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET,
                APIHelper.announcementsEndpoint, null, response, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO should handle
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
        editor.putInt("newest_notification_id",0);
        editor.apply();
    }

    private static void buildNotification(Announcement announcement,Context context) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(announcement.getTitle())
                        .setContentText(announcement.getMessage())
                        .setPriority(announcement.getId());
        int mNotificationId = announcement.getId();

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.INITIAL_TAB_INTENT,3);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat.from(context).notify(mNotificationId,mBuilder.build());
    }
}
