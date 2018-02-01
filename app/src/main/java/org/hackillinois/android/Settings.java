package org.hackillinois.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Settings {
	private static final String PREFS_NAME = "AppPrefs";
	private static final String AUTH_PREF = "AUTH";
	private static final String LAST_AUTH_PREF = "LAST_AUTH_TIME";
	private static final String LAST_ZONE_PREF = "LAST_AUTH_ZONE";
	private static final Gson GSON = new Gson();
	private static final String HACKER_PREF = "HACKER";
	private static Settings INSTANCE;

	private final SharedPreferences prefs;

	private Settings(Context context) {
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static Settings getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new Settings(context);
		}
		return INSTANCE;
	}

	/* todo these are too generic, we really only need it to see
		- type of user logged in
		- previous auth key
		- last time authenticated
	 */
	public <Response> void saveResponse(Response response) {
		if (response == null) {
			return;
		}

		SharedPreferences.Editor prefsEditor = prefs.edit();
		String json = GSON.toJson(response);
		prefsEditor.putString(response.getClass().getName(), json);
		prefsEditor.apply();
	}

	public <Response> Response getResponse(Class<Response> responseClass) {
		if (responseClass == null) {
			return null;
		}

		String json = prefs.getString(responseClass.getName(), "");
		return GSON.fromJson(json, responseClass);
	}

	public void saveAuthKey(String auth) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString(AUTH_PREF, auth);
		saveLastAuth(new Date());
		prefsEditor.apply();
	}

	public Optional<String> getAuthKey() {
		return Optional.ofNullable(prefs.getString(AUTH_PREF, null));
	}

	public Optional<Date> getLastAuth() {
		if (!prefs.contains(LAST_AUTH_PREF) || !prefs.contains(LAST_ZONE_PREF)) {
			return Optional.empty();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(prefs.getLong(LAST_AUTH_PREF, 0));
		calendar.setTimeZone(TimeZone.getTimeZone(prefs.getString(LAST_ZONE_PREF, TimeZone.getDefault().getID())));
		return Optional.of(calendar.getTime());
	}

	public void saveLastAuth(Date date) {
		SharedPreferences.Editor edit = prefs.edit();
		edit.putLong(LAST_AUTH_PREF, date.getTime()).apply();
		edit.putString(LAST_ZONE_PREF, TimeZone.getDefault().getID()).apply();
		edit.apply();
	}

	public String getAuthString() {
		String authKey = prefs.getString(AUTH_PREF, null);
		if(getIsHacker()) {
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
}
