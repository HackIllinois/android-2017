package org.hackillinois.android;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

// TODO add this to manifest
public class HackillinoisApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		JodaTimeAndroid.init(this);
	}
}
