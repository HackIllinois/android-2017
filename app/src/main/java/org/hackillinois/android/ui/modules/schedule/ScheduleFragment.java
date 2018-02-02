package org.hackillinois.android.ui.modules.schedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.item.EventItem;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.event.EventInfoDialog;
import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ScheduleFragment extends BaseFragment {
	@BindView(R.id.active_events) RecyclerView activeEvents;
	@BindView(R.id.tabs) TabLayout tabLayout;
	private Unbinder unbinder;

	private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.layout_schedule, parent, false);
		unbinder = ButterKnife.bind(this, view);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			new EventInfoDialog(getContext(), item.getEvent()).show();
			return false;
		});

		DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
		activeEvents.addItemDecoration(divider);

		Tab currentDayTab = tabLayout.getTabAt(getCurrentDayTab());
		currentDayTab.select();
		Timber.d("Tab seletected: %s", currentDayTab.getText());

		itemAdapter.getItemFilter()
				.withFilterPredicate((item, constraint) ->
						item.getEvent().getStartTime().getDayOfWeek() == getTabDay(tabLayout.getSelectedTabPosition())
				);


		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(Tab tab) {
				Timber.d("Filtering for: %s", tab.getText());
				recheckFilter();
				activeEvents.scrollToPosition(0);
			}

			@Override
			public void onTabUnselected(Tab tab) {

			}

			@Override
			public void onTabReselected(Tab tab) {

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		fetchEvents();
	}

	private int getTabDay(int position) {
		switch (position) {
			case 0: // friday
				return 5;
			case 1: // saturday
				return 6;
			case 2: // sunday
				return 7;
			default:
				return 5;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public int getCurrentDayTab() {
		DateTime now = DateTime.now();
		Timber.d("Current day is: %s", now.dayOfWeek().getAsText());
		switch (now.getDayOfWeek()) {
			case 5: // friday
				return 0;
			case 6: // saturday
				return 1;
			case 7: // sunday
				return 2;
			default:
				return 0;
		}
	}

	public void fetchEvents() {
		HackIllinoisAPI.api.getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null && response.isSuccessful()) {
							Stream.of(response.body().getData())
									.map(event -> new EventItem(event, true))
									.forEach(itemAdapter::add);

							recheckFilter();
						}
					}

					@Override
					public void onFailure(Call<EventResponse> call, Throwable t) {

					}
				});
	}

	public void recheckFilter() {
		itemAdapter.filter("null");
	}
}
