package org.hackillinois.app2017.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.Events.EventManager;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RequestManager requestManager;

    @BindView(R.id.emailField)
    EditText emailField;
    @BindView(R.id.passwordField)
    EditText passwordField;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.incorrectEmailOrPassword)
    TextView incorrectText;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.loading_text)
    TextView loadingText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestManager = RequestManager.getInstance(this);
        sharedPreferences = this.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("hasAuthed", false)) {
            //TODO: reauth using api
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface brandon_med = Typeface.createFromAsset(getAssets(), "fonts/Brandon_med.otf");
        Typeface gotham_med = Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf");
        emailField.setTypeface(gotham_med);
        passwordField.setTypeface(gotham_med);
        loginButton.setTypeface(gotham_med);
        incorrectText.setTypeface(brandon_med);
        loadingText.setTypeface(brandon_med);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailField.getText().toString().isEmpty()) {
                    emailField.setError("Forget something?");
                } else if (passwordField.getText().toString().isEmpty()) {
                    passwordField.setError("Your password goes here!");
                } else {
                    if(Utils.isNetworkAvailable(getApplicationContext())) {
                        authorize(emailField.getText().toString(), passwordField.getText().toString());
                    } else  {
                        Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                    }
                    //loadEvents();
                    // TODO: delete loadEvents() call
                }
            }
        });
    }

    private void authorize(final String email, final String password) {
        loginButton.setClickable(false);
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        incorrectText.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);

        final JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET,
                APIHelper.USER_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");

                    Log.i("UserData", data.getString("firstName"));
                    editor.putString("firstName", data.getString("firstName"));
                    editor.putString("lastName", data.getString("lastName"));
                    editor.putString("diet", data.getString("diet"));
                    editor.putString("age", data.getString("age"));
                    editor.putString("graduationYear", data.getString("graduationYear"));
                    editor.putString("school", data.getString("school"));
                    editor.putString("major", data.getString("major"));
                    editor.putString("github", data.getString("github"));
                    editor.putString("linkedin", data.getString("linkedin"));
                    editor.putString("resumeId", data.getJSONObject("resume").getString("id"));
                    editor.putString("id", data.getString("id"));
                    editor.putBoolean("hasAuthed", true);

                    editor.apply();

                    loadEvents();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Sorry, please try again.", Toast.LENGTH_SHORT).show();
                        loginButton.setClickable(true);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", sharedPreferences.getString("auth", ""));
                return headers;
            }
        };

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                APIHelper.AUTH_ENDPOINT, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String authKey = response.getJSONObject("data").getString("auth");

                            editor.putString("auth", authKey);
                            editor.apply();

                            requestManager.addToRequestQueue(userRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // TODO Do something here.
                            loginButton.setClickable(true);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingView.setVisibility(View.GONE);
                        incorrectText.setVisibility(View.VISIBLE);
                        loginButton.setClickable(true);
                    }
                }
        );

        requestManager.addToRequestQueue(loginRequest);
    }

    private void moveOn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void loadEvents() {
        EventManager.sync(getApplicationContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                moveOn();
            }
        });
    }
}
