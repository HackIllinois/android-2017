package org.hackillinois.android.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;
import com.google.gson.Gson;

import org.joda.time.DateTime;


public class Settings {
	private static final String PREFS_NAME = "AppPrefs";
	private static final String AUTH_PREF = "AUTH";
	private static final String LAST_TIME_AUTH_PREF = "LAST_TIME_AUTH";
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

	public void saveLastAuth() {
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
}
