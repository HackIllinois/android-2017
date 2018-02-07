package org.hackillinois.android.ui.modules.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.MainActivity;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.ButtonAction;
import timber.log.Timber;

public class LoginChooserActivity extends BaseActivity {
	public static final String FINISH_ACTIVITY = "FINISH_ACTIVITY_LOGIN_CHOOSER";
	private boolean isHacker = true;
	private TextView lastSelection = null;

	private BroadcastReceiver br;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_chooser);
		ButterKnife.bind(this);

		ButtonAction skipLogin = new ButtonAction("Skip Login", () -> {
			startActivity(new Intent(LoginChooserActivity.this, MainActivity.class));
		});

		enableDebug(new ActionsModule(skipLogin));

		// Create receiver so we can finish() this activity elsewhere.
		br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(FINISH_ACTIVITY)) {
                    finish();
                }
            }
        };

        registerReceiver(br, new IntentFilter(FINISH_ACTIVITY));
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
		Timber.d("Logging in user as hacker: %b", isHacker);
		Settings.get().saveIsHacker(isHacker);
		if (isHacker) {
			nextActivity = new Intent(getApplicationContext(), GitHubLoginActivity.class);
		} else {
			nextActivity = new Intent(getApplicationContext(), LoginActivity.class);
		}

		startActivity(nextActivity);
	}
}
