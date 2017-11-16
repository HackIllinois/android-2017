package org.hackillinois.app2017.Events;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.Utils;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author dl-eric
 */

public class EventManager {
    private static EventManager instance;
    private ArrayList<Event> events;

    private EventManager() {
        events = new ArrayList<>();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        if(events == null) {
            Log.d("EventManager","Events should not be null");
            this.events = new ArrayList<>();
        }
    }

    public static void sync(Context context, final Response.Listener<JSONObject> listener) {
		final JsonObjectRequest eventsRequest = new JsonObjectRequest(
				Request.Method.GET,
				APIHelper.EVENTS_ENDPOINT,
				null,
				response -> {
					Type listType = new TypeToken<ArrayList<Event>>() {}.getType();
					GsonBuilder gsonBuilder = new GsonBuilder();
					gsonBuilder.setDateFormat(Utils.API_DATE_FORMAT);
					Gson gson = gsonBuilder.create();

					JsonParser jsonParser = new JsonParser();
					JsonArray jsonEvents = jsonParser.parse(response.toString()).getAsJsonObject().getAsJsonArray("data");
					Log.d("VolleyResponse", "Got response " + response.toString());
					ArrayList<Event> events = gson.fromJson(jsonEvents.toString(), listType);
					getInstance().setEvents(events);
					for (Event e : getInstance().getEvents()) {
						Log.d("AddedEvent", e.getName());
						e.fixTime();
					}
					if (listener != null) {
						listener.onResponse(response);
					}
				},
				error -> Log.d("VolleyError", "Couldn't add new events") // TODO: Handle error
		);

		RequestManager.getInstance(context).addToRequestQueue(eventsRequest);
    }
}
