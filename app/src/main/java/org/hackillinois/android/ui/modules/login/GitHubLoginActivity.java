package org.hackillinois.android.ui.modules.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.MainActivity;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class GitHubLoginActivity extends BaseActivity {
	@BindView(R.id.github_webview) WebView githubWebview;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_github_login);
		ButterKnife.bind(this);

		githubWebview.clearCache(true);
		clearCookies();
		githubWebview.getSettings().setJavaScriptEnabled(true); // required for github
		githubWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("https://hackillinois.org/auth/?code=") || url.contains("?code=")) {
					// todo clean up url checking
					String code = url.split("\\?code=")[1];
					authenticateUser(code);
					return true;
				}
				return false;
			}
		});
		githubWebview.loadUrl(HackIllinoisAPI.AUTH);
	}

	private void clearCookies() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	private void authenticateUser(String code) {
		getApi().verifyUser(code)
				.enqueue(new Callback<LoginResponse>() {
					@Override
					public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
						LoginResponse loginResponse = response.body();
						if (loginResponse != null) {
							String authKey = loginResponse.getLoginResponseData().getAuth();
							Settings.get().saveAuthKey(authKey);
							Timber.i("Successfully authenticated user");
							startActivity(new Intent(GitHubLoginActivity.this, MainActivity.class));
							finish();
						} else {
							Timber.w("User authentication failed");
							Toast.makeText(getApplicationContext(), R.string.failure, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onFailure(Call<LoginResponse> call, Throwable t) {
						//todo handle error
						Timber.w(t, "Failed to send user authentication request");
					}
				});
	}
}
