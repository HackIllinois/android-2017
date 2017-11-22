package org.hackillinois.app2017;

import android.content.Intent;
import android.os.Bundle;

import org.hackillinois.app2017.Login.LoginActivity;

public class SplashActivity extends HackillinoisActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
