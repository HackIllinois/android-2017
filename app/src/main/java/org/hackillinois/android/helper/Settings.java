package org.hackillinois.android.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Settings {
	public static final DateTime EVENT_START_TIME = new DateTime("2018-02-23T16:00:00-0600"); //  = 2018-02-23T22:00:00+0000
	public static final DateTime HACKING_START_TIME = new DateTime("2018-02-23T23:00:00-0600"); //  = 2018-02-24T07:00:00+0000
	public static final DateTime HACKING_END_TIME = new DateTime("2018-02-25T11:00:00-0600"); //  = 2018-02-25T17:00:00+0000
	public static final DateTime EVENT_END_TIME = new DateTime("2018-02-25T17:00:00-0600"); //  = 2018-02-25T23:00:00+0000
	private static final String PREFS_NAME = "AppPrefs";
	private static final String AUTH_PREF = "AUTH";
	private static final String LAST_TIME_AUTH_PREF = "LAST_TIME_AUTH";
	private static final String LAST_TIME_FETCH_NOTIFICATIONS = "LAST_TIME_FETCH_NOTIFICATIONS";
	private static final String HACKER_PREF = "IS_HACKER";
	private static final String EVENT_STARRED_PREF = "IS_STARRED";
	private static Settings INSTANCE;

	private final SharedPreferences prefs;

	private Settings(Context context) {
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static void initialize(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new Settings(context);
		}
	}

	public static Settings get() {
		return INSTANCE;
	}

	public boolean getIsEventStarred(long eventId) {
		Set<String> starredEvents = prefs.getStringSet(EVENT_STARRED_PREF, new HashSet<>());
		return starredEvents.contains(String.valueOf(eventId));
	}

	public void saveEventIsStarred(long eventId) {
		Set<String> starredEvents = Collections.unmodifiableSet(prefs.getStringSet(EVENT_STARRED_PREF, new HashSet<>()));
		// note starredEvents should *NOT* be modified

		SharedPreferences.Editor prefsEditor = prefs.edit();
		Set<String> newStarredEvents = new HashSet<>(starredEvents);
		newStarredEvents.add(String.valueOf(eventId));
		prefsEditor.putStringSet(EVENT_STARRED_PREF, newStarredEvents);
		prefsEditor.apply();
	}

	public void saveEventIsNotStarred(long eventId) {
		Set<String> starredEvents = Collections.unmodifiableSet(prefs.getStringSet(EVENT_STARRED_PREF, new HashSet<>()));
		// note starredEvents should *NOT* be modified

		SharedPreferences.Editor prefsEditor = prefs.edit();
		Set<String> newStarredEvents = new HashSet<>(starredEvents);
		newStarredEvents.remove(String.valueOf(eventId));
		prefsEditor.putStringSet(EVENT_STARRED_PREF, newStarredEvents);
		prefsEditor.apply();
	}

	public DateTime getLastNotificationFetch() {
		if(!prefs.contains(LAST_TIME_FETCH_NOTIFICATIONS)) {
			return DateTime.now();
		}

		return DateTime.parse(prefs.getString(LAST_TIME_FETCH_NOTIFICATIONS, null));
	}

	public void saveLastNotificationFetch(DateTime last) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString(LAST_TIME_FETCH_NOTIFICATIONS, last.toString());
		prefsEditor.apply();
	}

	public void saveAuthKey(String auth) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString(AUTH_PREF, auth);
		saveLastAuth();
		prefsEditor.apply();
	}

	public Optional<String> getAuthKey() {
		return Optional.ofNullable(prefs.getString(AUTH_PREF, null));
	}

	public Optional<DateTime> getLastAuth() {
		if (!prefs.contains(LAST_TIME_AUTH_PREF)) {
			return Optional.empty();
		}

		return Optional.of(new DateTime(prefs.getString(LAST_TIME_AUTH_PREF, "")));
	}

	private void saveLastAuth() {
		DateTime now = DateTime.now();
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(LAST_TIME_AUTH_PREF, now.toString()).apply();
		edit.apply();
	}

	public String getAuthString() {
		String authKey = prefs.getString(AUTH_PREF, null);
		if (getIsHacker()) {
			return "Bearer " + authKey;
		} else {
			return "Basic " + authKey;
		}
	}

	public void saveIsHacker(boolean isHacker) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(HACKER_PREF, isHacker);
		prefsEditor.apply();
	}

	public boolean getIsHacker() {
		return prefs.getBoolean(HACKER_PREF, false);
	}

	public void clear() {
		prefs.edit().clear().apply();
	}
}
