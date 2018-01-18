package org.hackillinois.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class SplashActivity extends HackillinoisActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);

            int duration = gif.getDuration();

            new Handler().postDelayed(() -> {
                Intent i = new Intent(getApplicationContext(), LoginChooserActivity.class);
                startActivity(i);
                finish();
            }, duration);

        } catch (IOException e) {
            Intent i = new Intent(this, LoginChooserActivity.class);
            startActivity(i);
            finish();
        }
    }
}
