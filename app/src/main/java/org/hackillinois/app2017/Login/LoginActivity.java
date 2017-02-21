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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Schedule.Event;
import org.hackillinois.app2017.Schedule.EventManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        loadingText.setTypeface(brandon_med);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailField.getText().toString().isEmpty()) {
                    emailField.setError("Forget something?");
                } else if (passwordField.getText().toString().isEmpty()) {
                    passwordField.setError("Your password goes here!");
                } else {
                    authorize(emailField.getText().toString(), passwordField.getText().toString());
                    loadEvents();
                    // TODO: Uncomment authorize, delete loadEvents() call
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
                        // TODO: RIP
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
        final JsonObjectRequest eventsRequest = new JsonObjectRequest(Request.Method.GET,
                APIHelper.eventsEndpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Type listType = new TypeToken<ArrayList<Event>>() {
                }.getType();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Gson gson = gsonBuilder.create();

                JsonParser jsonParser = new JsonParser();
                JsonArray jsonEvents = jsonParser.parse(response.toString()).getAsJsonObject().getAsJsonArray("data");

                EventManager.getInstance().setEvents((ArrayList) gson.fromJson(jsonEvents.toString(), listType));

                moveOn();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        });

        requestManager.addToRequestQueue(eventsRequest);
    }
}
