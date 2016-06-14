package org.hackillinois.branding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize(emailField.getText().toString(), passwordField.getText().toString());
            }
        });
    }

    private void authorize(String email, String password){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
