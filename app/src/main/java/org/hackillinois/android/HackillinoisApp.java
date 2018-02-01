package org.hackillinois.android;

import android.support.multidex.MultiDexApplication;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;
import timber.log.Timber.DebugTree;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class HackillinoisApp extends MultiDexApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		JodaTimeAndroid.init(this);
		Timber.plant(new DebugTree());
	}
}
