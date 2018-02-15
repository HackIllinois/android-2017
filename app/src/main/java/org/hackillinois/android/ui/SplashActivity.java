package org.hackillinois.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.annimon.stream.Optional;

import org.hackillinois.android.BuildConfig;
import org.hackillinois.android.R;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {
	@BindView(R.id.splash_anim) GifImageView splash;
	private final Handler handler = new Handler();
	private Class<?> activityClass = LoginChooserActivity.class;
	private final Runnable moveOn = this::moveOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);

		Settings settings = Settings.get();

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		Optional<DateTime> lastAuth = settings.getLastAuth();
		Timber.d("User last auth: %s", lastAuth.map(DateTime::toString).orElse("Never"));

		if (settings.getIsHacker() && settings.getAuthKey().isPresent()) { // don't need to refresh these
			Timber.d("User is a logged in hacker");
			activityClass = MainActivity.class;

		} else if (!settings.getIsHacker() && settings.getAuthKey().isPresent()) {
			Timber.d("User is a logged in non hacker");
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
								if (loginResponse != null && response.isSuccessful()) { // success
									activityClass = MainActivity.class;
									Timber.i("Successfully obtained new authorization token");
									String authKey = loginResponse.getLoginResponseData().getAuth();
									settings.saveAuthKey(authKey);
								} else { // failed, must re login
									Timber.i("Failed to obtain new authorization token");
									activityClass = LoginChooserActivity.class;
								}
							}

							@Override
							public void onFailure(Call<LoginResponse> call, Throwable t) {
								Timber.w(t, "Failed to send reauthentication to server.");
							}
						});
			} else {
				activityClass = MainActivity.class;
			}
		}

		fetchLocation();

		try {
			GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
			int duration = gif.getDuration();
			handler.postDelayed(moveOn, duration);
			Timber.d("Splash Screen Displayed");
		} catch (IOException e) {
			Timber.d("Failed to display splash Screen");
			moveOn();
		}
	}

	private void fetchLocation() {
		getApp().getApi().getLocations().enqueue(new Callback<LocationResponse>() {
			@Override
			public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
				if (response != null && response.isSuccessful()) {
					Settings.get().setLocations(getApp().getGson().toJson(response.body()));
				}
			}

			@Override
			public void onFailure(Call<LocationResponse> call, Throwable t) {
				Timber.d("Failed to fetch location");
			}
		});
	}

	private void moveOn() {
		Timber.d("Moving to %s", activityClass.getSimpleName());
		Intent i = new Intent(this, activityClass);
		startActivity(i);
		finish();
	}

	@OnClick(R.id.splash_anim)
	public void skipSplash() {
		if (BuildConfig.DEBUG) {
			Timber.i("Skipping splash screen");
			handler.removeCallbacks(moveOn);
			moveOn();
		}
	}
}
