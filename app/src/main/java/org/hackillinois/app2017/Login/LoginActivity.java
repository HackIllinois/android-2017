package org.hackillinois.app2017.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

        setTitle("Login");
    }

    private void authorize(String email, String password){
        /*if(email.equals("hackillinois") && password.equals("2017")){
            moveOn();
        }else{
            Toast.makeText(getApplicationContext(), "Sorry, Incorrect Credentials", Toast.LENGTH_SHORT).show();
        }*/
        moveOn();
    }

    private void moveOn(){
        editor.putBoolean("hasAuthed", true).apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
