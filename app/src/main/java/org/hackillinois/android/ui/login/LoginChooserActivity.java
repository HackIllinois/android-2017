package org.hackillinois.android.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.ui.HackillinoisActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginChooserActivity extends HackillinoisActivity {
	private Settings settings;
	private boolean isHacker = true;
	private TextView lastSelection = null;

	private BroadcastReceiver br;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_chooser);
		ButterKnife.bind(this);
		settings = Settings.getInstance(this);

		// Create receiver so we can finish() this activity elsewhere.
		br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };

        registerReceiver(br, new IntentFilter("finish_activity"));
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
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
			lastSelection.setTextColor(ContextCompat.getColor(this, R.color.darkPurple));
			lastSelection.setBackgroundColor(ContextCompat.getColor(this, R.color.mainBackground));
		}

		TextView selectedType = (TextView) view;
		selectedType.setBackgroundColor(ContextCompat.getColor(this, R.color.darkPurple));
		selectedType.setTextColor(ContextCompat.getColor(this, R.color.mainBackground));
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
