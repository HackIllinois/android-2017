package org.hackillinois.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.annimon.stream.Optional;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;
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
	private boolean isAppVisible = true;
	private boolean readyToMoveOn = false;
	private final Runnable prepareForMove = () -> {
		readyToMoveOn = true;
		if (isAppVisible) moveOn();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);

		Settings settings = Settings.get();


		Optional<DateTime> lastAuth = settings.getLastAuth();
		Timber.d("User last auth: %s", lastAuth.map(DateTime::toString).orElse("Never"));

		Utils.fetchLocation(getApi());

		if (settings.getIsHacker() && settings.getAuthKey().isPresent()) { // don't need to refresh these
			Timber.d("User is a logged in hacker");
			activityClass = MainActivity.class;

		} else if (!settings.getIsHacker() && settings.getAuthKey().isPresent()) {
			Timber.d("User is a logged in non hacker");
			// todo actually test auth for other users
			DateTime lastTime = lastAuth.get();
			int daysSinceLastAuth = Days.daysBetween(lastTime, DateTime.now()).getDays();
			if (daysSinceLastAuth >= 7) { // too late, must re login
				Settings.get().clear(this);
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
									Settings.get().clear(getApplicationContext());
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

		try {
			GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
			int duration = gif.getDuration();
			handler.postDelayed(prepareForMove, duration);
			Timber.d("Splash Screen Displayed");
		} catch (IOException e) {
			Timber.d("Failed to display splash Screen");
			moveOn();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isAppVisible = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isAppVisible = true;
		if (readyToMoveOn) {
			moveOn();
		}
	}

	private void moveOn() {
		Timber.d("Moving to %s", activityClass.getSimpleName());
		Intent i = new Intent(this, activityClass);
		startActivity(i);
		finish();
	}

	@OnClick(R.id.splash_anim)
	public void skipSplash() {
		Timber.i("Skipping splash screen");
		moveOn();
	}
}
