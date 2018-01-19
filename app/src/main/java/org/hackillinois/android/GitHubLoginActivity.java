package org.hackillinois.android;

import android.net.Uri;
import android.os.Bundle;

public class GitHubLoginActivity extends HackillinoisActivity {
	private static final Uri AUTH_URI = Uri.parse(ApiEndpoints.AUTH);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_github_login);
		// add webview, loading github auth page
		// intercept api auth code
	}
}
