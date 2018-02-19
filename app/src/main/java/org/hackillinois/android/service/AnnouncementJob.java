package org.hackillinois.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.annimon.stream.Stream;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.hackillinois.android.R;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.MainActivity;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import timber.log.Timber;

public class AnnouncementJob extends Job {
	public static final String TAG = "announcement_fetcher";
	private static HackIllinoisAPI api;

	@NonNull
	@Override
	protected Result onRunJob(@NonNull Params params) {
		DateTime last = Settings.get().getLastNotificationFetch();
		Response<AnnouncementResponse> networkResponse = null;
		try {
			Timber.d("Fetching announcements in background");
			networkResponse = api.getAnnouncements(null, last, null).execute();
			Settings.get().saveLastNotificationFetch(DateTime.now());
		} catch (IOException e) {
			Timber.w(e, "Failed to fetch announcements");
		}

		if (networkResponse != null && networkResponse.isSuccessful()) {
			AnnouncementResponse response = networkResponse.body();
			Stream.of(response.getAnnouncements())
					.forEach(this::makeNotification);
			return Result.SUCCESS;
		}
		return Result.FAILURE;
	}

	private void makeNotification(AnnouncementResponse.Announcement announcement) {
		Timber.d("Sending notification for %s", announcement.getTitle());

		Intent notificationIntent = new Intent(getContext(), MainActivity.class);
		notificationIntent.putExtra(MainActivity.SET_TAB, R.id.menu_notifications);
		PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(getContext(), TAG)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(announcement.getTitle())
				.setContentText(announcement.getDescription())
				.setWhen(announcement.getCreated().getMillis())
				.setVibrate(new long[]{1000, 1000})
				.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
				.setContentIntent(contentIntent)
				.setAutoCancel(true)
				.build();

		int id = (int) announcement.getId();
		NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null) {
			notificationManager.notify(id, notification);
		}
	}

	public static void scheduleAnnouncementFetcher(HackIllinoisAPI api, long frequency) {
		AnnouncementJob.api = api;

		new JobRequest.Builder(TAG)
				.setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
				.setPeriodic(frequency, TimeUnit.MINUTES.toMillis(5))
				.setRequirementsEnforced(true)
				.setUpdateCurrent(true)
				.build()
				.schedule();

		Timber.d("Scheduled announcement fetcher every %d minutes", TimeUnit.MILLISECONDS.toMinutes(frequency));
	}
}
