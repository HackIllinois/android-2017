package org.hackillinois.android;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GitHubLoginActivity extends HackillinoisActivity {
	@BindView(R.id.github_webview) WebView githubWebview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_github_login);
		ButterKnife.bind(this);
		// add webview, loading github auth page
		// intercept api auth code

		githubWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("https://hackillinois.org/auth/?code=") || url.contains("?code=")) {
					//we got the code
					Toast.makeText(getApplicationContext(), "Got " + url, Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}
		});
		githubWebview.loadUrl(ApiEndpoints.AUTH);
	}
}
