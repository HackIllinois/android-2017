package org.hackillinois.app2017.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @BindView(R.id.loginToolbar) Toolbar toolbar;
    @BindView(R.id.emailField) EditText emailField;
    @BindView(R.id.passwordField) EditText passwordField;
    @BindView(R.id.loginButton) Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        sharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

        setTitle("Login");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
