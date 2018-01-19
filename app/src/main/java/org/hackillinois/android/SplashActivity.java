package org.hackillinois.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class SplashActivity extends HackillinoisActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        try {
            GifDrawable gif = new GifDrawable(getResources(), R.drawable.appanimation);
            Log.d("Splash", "Hit");
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
