package org.hackillinois.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginChooserActivity extends HackillinoisActivity {
	private boolean isHacker = true;
	private TextView lastSelection = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_chooser);
		ButterKnife.bind(this);
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
		if (isHacker) {
			nextActivity = new Intent(getApplicationContext(), GitHubLoginActivity.class);
		} else {
			nextActivity = new Intent(getApplicationContext(), LoginActivity.class);
		}
		startActivity(nextActivity);
	}
}
