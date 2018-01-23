package org.hackillinois.android.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.HackillinoisActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginChooserActivity extends HackillinoisActivity {
	private Settings settings;
	private boolean isHacker = true;
	private TextView lastSelection = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_chooser);
		ButterKnife.bind(this);
		settings = Settings.getInstance(this);
	}

	@OnClick(R.id.login_hacker)
	public void userIsHacker() {
		isHacker = true;
	}

	@OnClick(value = {R.id.login_mentor, R.id.login_staff, R.id.login_volunteer})
	public void userIsNotHacker() {
		isHacker = false;
	}

	@OnClick(value = {R.id.login_hacker, R.id.login_mentor, R.id.login_staff, R.id.login_volunteer})
	public void highlightSelection(View view) {
		if (lastSelection != null) {
			lastSelection.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBackground));
		}

		TextView selectedType = (TextView) view;
		selectedType.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
		lastSelection = selectedType;
	}

	@OnClick(R.id.login_next)
	public void login() {
		Intent nextActivity = null;
		settings.saveIsHacker(isHacker);
		if (isHacker) {
			nextActivity = new Intent(getApplicationContext(), GitHubLoginActivity.class);
		} else {
			nextActivity = new Intent(getApplicationContext(), LoginActivity.class);
		}
		startActivity(nextActivity);
	}
}
