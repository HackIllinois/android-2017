package org.hackillinois.app2017;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by kevin on 11/19/2017.
 */

public class HackillinoisApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Brandon_reg.otf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}

}
