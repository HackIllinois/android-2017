package org.hackillinois.android.ui.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import org.hackillinois.android.R;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.MainActivity;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
	private Settings settings;
	@BindView(R.id.login_email) AppCompatEditText emailEditText;
	@BindView(R.id.login_password) AppCompatEditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ButterKnife.bind(this);
		settings = Settings.getInstance(this);
	}

	@OnClick(R.id.sign_in)
	public void signIn() {
		startActivity(new Intent(LoginActivity.this, MainActivity.class));

		/*
		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		LoginRequest request = new LoginRequest(email, password);

		HackIllinoisAPI.API.verifyUser(request).enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				LoginResponse loginResponse = response.body();

				if(loginResponse != null) {
					String authKey = loginResponse.getLoginResponseData().getAuth();
					settings.saveAuthKey(authKey);
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				} else {
					Toast.makeText(LoginActivity.this, R.string.failed_incorrect_login, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				Toast.makeText(LoginActivity.this, getString(R.string.normal_login_failed), Toast.LENGTH_LONG).show();
			}
		});
		*/
	}
}
