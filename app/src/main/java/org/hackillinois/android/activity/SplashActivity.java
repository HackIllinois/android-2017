package org.hackillinois.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.annimon.stream.Optional;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.login.LoginChooserActivity;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends HackillinoisActivity {
	private Class<?> activityClass = LoginChooserActivity.class;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Settings settings = Settings.getInstance(this);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		Optional<Date> lastAuth = settings.getLastAuth();
		if(settings.getIsHacker() && settings.getAuthKey().isPresent()) { // don't need to refresh these
			activityClass = HomeActivity.class;
		} else if (!settings.getIsHacker() && settings.getAuthKey().isPresent()) {
			// todo actually test auth for other users
			Date last = lastAuth.get();
			Date now = new Date();
			long daysDiff = TimeUnit.MILLISECONDS.toDays(now.getTime() - last.getTime());
			if (daysDiff >= 7) { // too late, must re login
				activityClass = LoginChooserActivity.class;
			} else if (7 > daysDiff && daysDiff >= 4) { // re authenticate
				HackIllinoisAPI.api.refreshUser(settings.getAuthString())
						.enqueue(new Callback<LoginResponse>() {
							@Override
							public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
								LoginResponse loginResponse = response.body();
								if (loginResponse != null) { // success
									activityClass = HomeActivity.class;
									String authKey = loginResponse.getLoginResponseData().getAuth();
									settings.saveAuthKey(authKey);
								} else { // failed, must re login
									Toast.makeText(getApplicationContext(), R.string.failure, Toast.LENGTH_LONG).show();
									activityClass = LoginChooserActivity.class;
								}
							}

							@Override
							public void onFailure(Call<LoginResponse> call, Throwable t) {

							}
						});
			} else {
				activityClass = HomeActivity.class;
			}
		}

		try {
			GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
			Log.d("Splash", "Hit");
			int duration = gif.getDuration();

			// todo check if logged in
			new Handler().postDelayed(() -> {
				Intent i = new Intent(getApplicationContext(), activityClass);
				startActivity(i);
				finish();
			}, duration);

		} catch (IOException e) {
			Intent i = new Intent(this, activityClass);
			startActivity(i);
			finish();
		}
	}
}
