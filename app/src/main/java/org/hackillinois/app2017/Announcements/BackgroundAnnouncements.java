package org.hackillinois.app2017.Announcements;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
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

/**
 * Created by kevin on 2/21/2017.
 */

public class BackgroundAnnouncements extends BroadcastReceiver {
    public static final int GET_ANNOUNCEMENTS = 0;
    private static PendingIntent grabAnnouncementsTask = null;
    private static final int minutesToWaitBetweenRefresh = 5;

    @Override
    public void onReceive(final Context context, Intent intent) {
        sync(context);
    }

    public static void sync(Context context) {
        requestAnnouncements(context, getDefaultListener(context));
    }

    private static Response.Listener<JSONObject> getDefaultListener(final Context context) {
        return response -> {
			Log.d("BackgroundAnnouncements", "Checking for new announcements");
			AnnouncementQuery announcementQuery = new Gson().fromJson(response.toString(), AnnouncementQuery.class);
			handleNewAnnouncements(announcementQuery.getData(),context);
		};
    }

    private static void setPendingIntent(Context context) {
        Intent i = new Intent(context, BackgroundAnnouncements.class);
        i.setClass(context,BackgroundAnnouncements.class);
        grabAnnouncementsTask = PendingIntent.getBroadcast(context, GET_ANNOUNCEMENTS, i, 0);
    }

    public static void startBackgroundAnnouncements(Context context) {
        setPendingIntent(context);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                50000,
                TimeUnit.MINUTES.toMillis(minutesToWaitBetweenRefresh),
                grabAnnouncementsTask);//5min interval
    }

    public static void stopBackgroundAnnouncements(Context context) {
        if(grabAnnouncementsTask == null) {
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(grabAnnouncementsTask);
    }

    public static void requestAnnouncements(final Context context, final Response.Listener<JSONObject> responseListener) {
        final JsonObjectRequest userRequest = new JsonObjectRequest(
                Request.Method.GET,
                APIHelper.ANNOUNCEMENTS_ENDPOINT,
                null,
                response -> {
                    AnnouncementQuery announcementQuery = new Gson().fromJson(response.toString(), AnnouncementQuery.class);
                    handleNewAnnouncements(announcementQuery.getData(), context);
                    responseListener.onResponse(response);
                },
                error -> {/*TODO should handle*/}
        );
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.addToRequestQueue(userRequest);
    }

    private static void handleNewAnnouncements(Collection<Announcement> announcements, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        int lastAnnouncement = sharedPreferences.getInt("newest_notification_id",0);
        int newestAnnouncement = lastAnnouncement;
        AnnouncementManager.getInstance().setAnnouncements(announcements);

        //basically, what this should be doing is checking how many announcements
        //in the list are greater than lastAnnouncment and that is the number of
        //new announcments.
        for(Announcement announcement : AnnouncementManager.getInstance().getAnnouncements()) {
            if(announcement.getId() > lastAnnouncement) {
                newestAnnouncement = Math.max(newestAnnouncement, announcement.getId());
                Date now = new Date();
                Date notification = Utils.getDateFromAPI(announcement.getCreated());
                if(notification == null) {
                    notification = new Date();
                }
                if(TimeUnit.MILLISECONDS.toHours(now.getTime() - notification.getTime()) <6 && ! MainActivity.isActivelyVisible()) {
                    buildNotification(announcement,context);
                }
            }
        }
        int newNotificationCount = sharedPreferences.getInt("new_notification_count",0) + newestAnnouncement - lastAnnouncement;
        if(newestAnnouncement - lastAnnouncement > 0) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500); // Vibrate for 500 milliseconds
        }
        Log.d("SharedPreferences","about to store preferences : newest_notification_id=" + sharedPreferences.getInt("newest_notification_id",0));
        Log.d("SharedPreferences","about to store preferences : new_notification_count=" + sharedPreferences.getInt("new_notification_count",0));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("newest_notification_id",newestAnnouncement);
        editor.putInt("new_notification_count",newNotificationCount);
        editor.apply();
        Log.d("SharedPreferences","after storing preferences : newest_notification_id=" + sharedPreferences.getInt("newest_notification_id",0));
        Log.d("SharedPreferences","after storing preferences : new_notification_count=" + sharedPreferences.getInt("new_notification_count",0));
    }

    private static void buildNotification(Announcement announcement,Context context) {
        Log.i("BuildNotification", "building");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "announcements")
						.setSmallIcon(R.drawable.status_bar_icon)
						.setContentTitle(announcement.getTitle())
						.setContentText(announcement.getMessage())
						.setPriority(announcement.getId());
        int mNotificationId = announcement.getId();

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.BOTTOM_BAR_TAB,3);
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
