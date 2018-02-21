package org.hackillinois.android.ui.modules.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.Toast;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.APIErrorResonse;
import org.hackillinois.android.api.response.announcement.AnnouncementRequest;
import org.hackillinois.android.api.response.announcement.AnnouncementStartResponse;
import org.hackillinois.android.api.response.tracking.TrackingRequest;
import org.hackillinois.android.api.response.tracking.TrackingStartResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class AdminToolsActivity extends BaseActivity {
	@BindView(R.id.announcement_title_h) AppCompatEditText announcementTitle;
	@BindView(R.id.announcement_description_h) AppCompatEditText announcementDescription;
	@BindView(R.id.make_announcement) Button announceButton;
	@BindView(R.id.event_name_h) AppCompatEditText eventName;
	@BindView(R.id.event_duration_h) AppCompatEditText eventDuration;
	@BindView(R.id.start_tracking) Button trackingButton;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_tools);
		ButterKnife.bind(this);
		enableDebug();
	}

	@OnClick(R.id.make_announcement)
	public void makeAnnouncement() {
		String title = announcementTitle.getText().toString();
		String description = announcementDescription.getText().toString();
		AnnouncementRequest request = new AnnouncementRequest(title, description);
		getApi().makeAnnouncement(Settings.get().getAuthString(), request).enqueue(new Callback<AnnouncementStartResponse>() {
			@Override
			public void onResponse(Call<AnnouncementStartResponse> call, Response<AnnouncementStartResponse> response) {
				AnnouncementStartResponse announcementStartResponse = response.body();
				if (response != null && response.isSuccessful()) {
					AnnouncementStartResponse.AnnouncementStartData body = announcementStartResponse.getAnnouncementStartData();
					Toast.makeText(getApplicationContext(), "Successfully created " + body.getTitle() + " at " + body.getCreated(), Toast.LENGTH_SHORT).show();
					announcementTitle.setText(null);
					announcementDescription.setText(null);
				}
				reportIfError(response.errorBody());
			}

			@Override
			public void onFailure(Call<AnnouncementStartResponse> call, Throwable t) {
				Timber.w(t, "Failed to create new announcement");
				Toast.makeText(getApplicationContext(), "Failed with " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnClick(R.id.start_tracking)
	public void startTracking() {
		String name = eventName.getText().toString();
		int minutes = Integer.valueOf(eventDuration.getText().toString());
		TrackingRequest request = new TrackingRequest(name, TimeUnit.MINUTES.toSeconds(minutes));
		getApi().startTracking(Settings.get().getAuthString(), request).enqueue(new Callback<TrackingStartResponse>() {
			@Override
			public void onResponse(Call<TrackingStartResponse> call, Response<TrackingStartResponse> response) {
				TrackingStartResponse trackingStartResponse = response.body();
				if (response != null && response.isSuccessful()) {
					TrackingStartResponse.TrackingStartData body = trackingStartResponse.getAnnouncementStartData();
					Toast.makeText(getApplicationContext(), "Successfully created " + body.getName() + " at " + body.getCreated(), Toast.LENGTH_SHORT).show();
					eventName.setText(null);
					eventDuration.setText(null);
				}

				reportIfError(response.errorBody());
			}

			@Override
			public void onFailure(Call<TrackingStartResponse> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "Failed because " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}


}
