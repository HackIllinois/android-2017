package org.hackillinois.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import org.hackillinois.android.R;
import org.hackillinois.android.Settings;
import org.hackillinois.android.activity.items.EventItem;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.api.response.user.AttendeeResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends DrawerActivity {
	private Settings settings;
	@BindView(R.id.active_events) RecyclerView activeEvents;

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
		ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
		//create the managing FastAdapter, by passing in the itemAdapter
		FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
		activeEvents.addItemDecoration(divider);

		fastAdapter.withOnClickListener(new OnClickListener() {
			@Override
			public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
				EventItem eventItem = (EventItem) item;
				Toast.makeText(getApplicationContext(), "Display info about " + eventItem.getEvent().getName(), Toast.LENGTH_LONG).show();

				return false;
			}
		});

		//set the items to your ItemAdapter
		HackIllinoisAPI.api.getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null) {
							Stream.of(response.body().getData())
									.sortBy(EventResponse.Event::getStartTime)
									.map(EventItem::new)
									.forEach(itemAdapter::add);
						}
					}

					@Override
					public void onFailure(Call<EventResponse> call, Throwable t) {

					}
				});
	}
}
