package org.hackillinois.android.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.HackillinoisActivity;
import org.hackillinois.android.activity.HomeActivity;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitHubLoginActivity extends HackillinoisActivity {
	@BindView(R.id.github_webview) WebView githubWebview;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_github_login);
		ButterKnife.bind(this);
		// add webview, loading github auth page
		// intercept api auth code

		githubWebview.getSettings().setJavaScriptEnabled(true); // required for github
		githubWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("https://hackillinois.org/auth/?code=") || url.contains("?code=")) {
					// todo clean up url checking
					//we got the code
					String code = url.split("\\?code=")[1];

					HackIllinoisAPI.api.verifyUser(code)
							.enqueue(new Callback<LoginResponse>() {
								@Override
								public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
									Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
									// todo only save auth key
									Settings.getInstance(getApplicationContext()).saveResponse(response.body());
									startActivity(new Intent(GitHubLoginActivity.this, HomeActivity.class));
								}

								@Override
								public void onFailure(Call<LoginResponse> call, Throwable t) {
									//todo handle error
								}
							});

					return true;
				}
				return false;
			}
		});
		githubWebview.loadUrl(HackIllinoisAPI.AUTH);
	}
}
