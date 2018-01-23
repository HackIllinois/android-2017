package org.hackillinois.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Settings {
	private static final String PREFS_NAME = "AppPrefs";
	private static final Gson GSON = new Gson();
	private static Settings INSTANCE;

	private final SharedPreferences preferences;

	private Settings(Context context) {
		preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

		SharedPreferences.Editor prefsEditor = preferences.edit();
		String json = GSON.toJson(response);
		prefsEditor.putString(response.getClass().getName(), json);
		prefsEditor.apply();
	}

	public <Response> Response getResponse(Class<Response> responseClass) {
		if (responseClass == null) {
			return null;
		}

		String json = preferences.getString(responseClass.getName(), "");
		return GSON.fromJson(json, responseClass);
	}
}
