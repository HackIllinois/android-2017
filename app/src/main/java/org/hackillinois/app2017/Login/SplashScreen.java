package org.hackillinois.app2017.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        setTitle("HackIllinois 2017");

        Button splashButton = (Button) findViewById(R.id.splashButton);
        if (splashButton != null) {
            splashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveOn();
                }
            });
        }
    }

    private void moveOn(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
