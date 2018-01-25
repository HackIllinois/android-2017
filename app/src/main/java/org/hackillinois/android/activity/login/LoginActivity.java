package org.hackillinois.android.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.HackillinoisActivity;
import org.hackillinois.android.activity.HomeActivity;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginRequest;
import org.hackillinois.android.api.response.login.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends HackillinoisActivity {
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
		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		LoginRequest request = new LoginRequest(email, password);

		HackIllinoisAPI.api.verifyUser(request).enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				LoginResponse loginResponse = response.body();

				if(loginResponse != null) {
					String authKey = loginResponse.getLoginResponseData().getAuth();
					settings.saveAuthKey(authKey);
					startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
	}
}
