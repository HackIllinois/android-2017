package org.hackillinois.android.activity.login;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.activity.HackillinoisActivity;
import org.hackillinois.android.api.ApiEndpoints;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitHubLoginActivity extends HackillinoisActivity {
	@BindView(R.id.github_webview) WebView githubWebview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_github_login);
		ButterKnife.bind(this);
		// add webview, loading github auth page
		// intercept api auth code

		githubWebview.getSettings().setJavaScriptEnabled(true); // required for github
		Log.d("GITHUB", "JAVASCRIPT:" + githubWebview.getSettings().getJavaScriptEnabled());
		githubWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("https://hackillinois.org/auth/?code=") || url.contains("?code=")) {
					// todo clean up url checking
					//we got the code
					String code = url.split("\\?code=")[1];
					Toast.makeText(getApplicationContext(), "Got code=" + code, Toast.LENGTH_LONG).show();

					HackIllinoisAPI.api.verifyUser(code)
							.enqueue(new Callback<LoginResponse>() {
								@Override
								public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
									Log.d("LOGIN", "Call=" + call);
									Toast.makeText(getApplicationContext(), "Got " + response, Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(Call<LoginResponse> call, Throwable t) {
									Toast.makeText(getApplicationContext(), "Error " + t, Toast.LENGTH_LONG).show();
								}
							});

					return true;
				}
				return false;
			}
		});
		githubWebview.loadUrl(ApiEndpoints.AUTH);
	}
}
