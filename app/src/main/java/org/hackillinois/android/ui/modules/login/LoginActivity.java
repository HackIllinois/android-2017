package org.hackillinois.android.ui.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.login.LoginRequest;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.MainActivity;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends BaseActivity {
	@BindView(R.id.login_email) AppCompatEditText emailEditText;
	@BindView(R.id.login_password) AppCompatEditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		enableDebug();
	}

	@OnClick(R.id.sign_in)
	public void signIn() {
		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		LoginRequest request = new LoginRequest(email, password);

		getApi().verifyUser(request).enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				LoginResponse loginResponse = response.body();

				if (loginResponse != null) {
					Timber.i("User %s succesfully logged in", email);
					String authKey = loginResponse.getLoginResponseData().getAuth();
					Settings.get().saveAuthKey(authKey);
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				} else {
					Toast.makeText(LoginActivity.this, R.string.failed_incorrect_login, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				Toast.makeText(LoginActivity.this, getString(R.string.normal_login_failed), Toast.LENGTH_LONG).show();
				Timber.w(t, "Failed to send login.");
			}
		});
	}
}
