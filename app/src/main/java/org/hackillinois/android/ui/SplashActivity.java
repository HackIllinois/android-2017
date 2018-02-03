package org.hackillinois.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.annimon.stream.Optional;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {
	private Class<?> activityClass = LoginChooserActivity.class;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Settings settings = Settings.getInstance(this);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		Optional<DateTime> lastAuth = settings.getLastAuth();
		if(settings.getIsHacker() && settings.getAuthKey().isPresent()) { // don't need to refresh these
			Timber.d("User is a logged in hacker");
			activityClass = MainActivity.class;

			Timber.d("User auth code is: %s", settings.getAuthKey().get());
		} else if (!settings.getIsHacker() && settings.getAuthKey().isPresent()) {
			// todo actually test auth for other users
			DateTime lastTime = lastAuth.get();
			int daysSinceLastAuth = Days.daysBetween(lastTime, DateTime.now()).getDays();
			if (daysSinceLastAuth >= 7) { // too late, must re login
				activityClass = LoginChooserActivity.class;
			} else if (7 > daysSinceLastAuth && daysSinceLastAuth >= 4) { // re authenticate
				Timber.d("Trying to reauthenticate user");
				getApi().refreshUser(settings.getAuthString())
						.enqueue(new Callback<LoginResponse>() {
							@Override
							public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
								LoginResponse loginResponse = response.body();
								if (loginResponse != null) { // success
									activityClass = MainActivity.class;
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
				activityClass = MainActivity.class;
			}
		}

		try {
			GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
			int duration = gif.getDuration();

			new Handler().postDelayed(() -> {
				moveOn(getApplicationContext(), activityClass);
			}, duration);
			Timber.d("Splash Screen Displayed");
		} catch (IOException e) {
			Timber.d("Failed to display splash Screen");
			moveOn(this, activityClass);
		}
	}

	private void moveOn(Context applicationContext, Class<?> activityClass) {
		Timber.d("Moving to %s", activityClass.getName());
		Intent i = new Intent(applicationContext, activityClass);
		startActivity(i);
		finish();
	}
}
