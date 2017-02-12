package org.hackillinois.app2017.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @BindView(R.id.emailField)
    EditText emailField;
    @BindView(R.id.passwordField)
    EditText passwordField;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.incorrectEmailOrPassword)
    TextView incorrectText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("hasAuthed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface brandon_med = Typeface.createFromAsset(getAssets(), "fonts/Brandon_med.otf");
        emailField.setTypeface(brandon_med);
        passwordField.setTypeface(brandon_med);
        loginButton.setTypeface(brandon_med);
        incorrectText.setTypeface(brandon_med);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize(emailField.getText().toString(), passwordField.getText().toString());
            }
        });
    }

    private void authorize(final String email, final String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        incorrectText.setVisibility(View.INVISIBLE);

        final RequestManager requestManager = RequestManager.getInstance(this);

        final JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET,
                APIHelper.userEndpoint, null, new Response.Listener<JSONObject>() {
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
                            editor.putString("resumeKey", data.getJSONObject("resume").getString("key"));

                            editor.apply();

                            moveOn();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: RIP
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
                APIHelper.authEndpoint, new JSONObject(params),
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
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        incorrectText.setVisibility(View.VISIBLE);
                    }
                }
        );

        requestManager.addToRequestQueue(loginRequest);

        // moveOn();
    }

    private void moveOn() {
        //editor.putBoolean("hasAuthed", true).apply(); Disabled for now just for testing
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
