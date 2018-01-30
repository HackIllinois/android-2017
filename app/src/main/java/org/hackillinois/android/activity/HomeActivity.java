package org.hackillinois.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.api.response.user.AttendeeResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends DrawerActivity {
	private Settings settings;
	@BindView(R.id.active_events) RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		settings = Settings.getInstance(this);

		// Kill LoginChooserActivity so user can't press back and get to it.
        sendBroadcast(new Intent("finish_activity"));

		// todo why does this activity show the action bar
		LoginResponse loginResponse = settings.getResponse(LoginResponse.class);

		HackIllinoisAPI.api.getAttendeeInfo(settings.getAuthString())
				.enqueue(new Callback<AttendeeResponse>() {
					@Override
					public void onResponse(Call<AttendeeResponse> call, Response<AttendeeResponse> response) {

					}

					@Override
					public void onFailure(Call<AttendeeResponse> call, Throwable t) {

					}
				});

		//create the ItemAdapter holding your Items
		ItemAdapter itemAdapter = new ItemAdapter();
		//create the managing FastAdapter, by passing in the itemAdapter
		FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

		//set our adapters to the RecyclerView
		recyclerView.setAdapter(fastAdapter);

		//set the items to your ItemAdapter
		//itemAdapter.add(ITEMS);
	}
}
