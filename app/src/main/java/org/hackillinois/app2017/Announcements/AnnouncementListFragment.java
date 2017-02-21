package org.hackillinois.app2017.Announcements;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.R;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AnnouncementListFragment extends Fragment {

    private ArrayList<Notification> announcements;
    private AnnouncementAdapter adapter;

    @BindView(R.id.announcementList) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        announcements =  new ArrayList<>();
        View view = inflater.inflate(R.layout.layout_announcements, parent, false);
        ButterKnife.bind(this, view);

        adapter = new AnnouncementAdapter(announcements);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        AnnouncementGrabberBroadcastReceiver.requestAnnouncements(getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AnnouncementQuery announcementQuery = new Gson().fromJson(response.toString(), AnnouncementQuery.class);
                announcements.clear();
                announcements.addAll(announcementQuery.getData());
            }
        });

        return view;
    }

    private void testData(){
        Announcement announcement = new Announcement("To the owner of the white van that is parked outside of Siebel, please stop selling soylent to people. It is not encouraged behavior.", "a few seconds ago");
        announcements.add(announcement);

        // Reminders are not used for HackIllinois 2017
//        Reminder reminder = new Reminder("Lunch will be in 10 minutes.", "a few seconds ago", "ECEB");
//        announcements.add(reminder);

        announcement = new Announcement("Hella narwhal Cosby sweater McSweeney's salvia kitsch before they sold out High Life.", "an hour ago");
        announcements.add(announcement);

        announcement = new Announcement("Tousled food truck polaroid, salvia bespoke small batch Pinterest Marfa.", "an hour ago");
        announcements.add(announcement);

        announcement = new Announcement("This is a test message bruh", "yesterday");
        announcements.add(announcement);

        adapter.notifyDataSetChanged();
    }
}
