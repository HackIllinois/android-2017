package org.hackillinois.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.login.LoginChooserActivity;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends HackillinoisActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		try {
			GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
			Log.d("Splash", "Hit");
			int duration = gif.getDuration();

			// todo check if logged in
			new Handler().postDelayed(() -> {
				Intent i = new Intent(getApplicationContext(), LoginChooserActivity.class);
				startActivity(i);
				finish();
			}, duration);

		} catch (IOException e) {
			Intent i = new Intent(this, LoginChooserActivity.class);
			startActivity(i);
			finish();
		}
	}
}
