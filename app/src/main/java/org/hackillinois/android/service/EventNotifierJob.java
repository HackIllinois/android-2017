package org.hackillinois.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.ui.MainActivity;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import timber.log.Timber;

public class EventNotifierJob extends Job {
	private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("h:mm a");
	public static final String TAG = "event_notifier";
	public static final String EVENT_NAME = "event_name";
	public static final String EVENT_ID = "event_id";
	public static final String EVENT_START_TIME = "event_start_time";

	@NonNull
	@Override
	protected Result onRunJob(@NonNull Params params) {
		PersistableBundleCompat extras = params.getExtras();
		String eventName = extras.getString(EVENT_NAME, "Unknown");
		int eventId = (int) extras.getLong(EVENT_ID, 0);
		DateTime startTime = DateTime.parse(extras.getString(EVENT_START_TIME, ""));

		Intent notificationIntent = new Intent(getContext(), MainActivity.class);
		notificationIntent.putExtra(MainActivity.SET_TAB, R.id.menu_schedule);
		PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Timber.d("Sending notification for %s", eventName);
		Notification notification = new NotificationCompat.Builder(getContext(), TAG)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(eventName)
				.setContentText(getContext().getString(R.string.event_starts_at, eventName, DTF.print(startTime)))
				.setTimeoutAfter(startTime.getMillis())
				.setWhen(startTime.getMillis())
				.setVibrate(new long[]{1000, 1000})
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.setContentIntent(contentIntent)
				.setAutoCancel(true)
				.build();

		NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null) {
			notificationManager.notify(eventId, notification);
		}
		return Result.SUCCESS;
	}

	public static void cancelReminder(EventResponse.Event event) {
		JobManager.instance().cancelAllForTag(getTag(event));
		Timber.d("Canceled reminder for '%s'", event.getName());
	}

	public static void scheduleReminder(EventResponse.Event event, long millisBefore) {
		DateTime timeToNotify = event.getStartTime().minus(millisBefore);
		if (timeToNotify.isBeforeNow()) {
			Timber.w("Reminder for event '%s' not scheduled because it is over.", event.getName());
			return;
		}

		PersistableBundleCompat extras = new PersistableBundleCompat();
		extras.putString(EVENT_NAME, event.getName());
		extras.putString(EVENT_START_TIME, event.getStartTime().toString());
		extras.putLong(EVENT_ID, event.getId());

		long msUntilNotify = new Duration(DateTime.now(), timeToNotify).getMillis();
		new JobRequest.Builder(getTag(event))
				.setExtras(extras)
				.setExact(msUntilNotify)
				.build()
				.schedule();

		Timber.d("Scheduled reminder for '%s'", event.getName());
	}

	@NonNull
	private static String getTag(EventResponse.Event event) {
		return TAG + "_" + event.getId();
	}
}
