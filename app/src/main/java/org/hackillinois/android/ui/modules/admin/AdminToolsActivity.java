package org.hackillinois.android.ui.modules.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.announcement.AnnouncementRequest;
import org.hackillinois.android.api.response.tracking.TrackingRequest;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AdminToolsActivity extends BaseActivity {

    @BindView(R.id.announcement_title_h) EditText announcementTitle;
    @BindView(R.id.announcement_description_h) EditText announcementDescription;
    @BindView(R.id.make_announcement) Button announceButton;
    @BindView(R.id.event_name_h) EditText eventName;
    @BindView(R.id.event_duration_h) EditText eventDuration;
    @BindView(R.id.start_tracking) Button trackingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tools);
        //ButterKnife.bind(this);
    }

    @OnClick(R.id.make_announcement)
    public void makeAnnouncement() {
        String title = announcementTitle.getText().toString();
        String description = announcementDescription.getText().toString();
        AnnouncementRequest request = new AnnouncementRequest(title, description);
        getApi().makeAnnouncement(Settings.get().getAuthString(), request);
    }
    @OnClick(R.id.start_tracking)
    public void startTracking() {
        String name = eventName.getText().toString();
        int minutes = Integer.valueOf(eventDuration.getText().toString());
        TrackingRequest request = new TrackingRequest(name, String.valueOf(minutes * 60));
        getApi().startTracking(Settings.get().getAuthString(), request);
    }
}
