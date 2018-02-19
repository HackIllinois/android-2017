package org.hackillinois.android.ui.modules.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.custom.EmptyRecyclerView;
import org.hackillinois.android.ui.modules.event.EventItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ScheduleDayFragment extends BaseFragment {
	@BindView(R.id.active_events) EmptyRecyclerView activeEvents;
	@BindView(R.id.schedule_refresh) RecyclerRefreshLayout swipeRefresh;
	@BindView(R.id.empty_view) View emptyView;
	private Unbinder unbinder;

	private final FlexibleAdapter<EventItem> adapter = new FlexibleAdapter<>(null);

	private static final String SCHEDULE_DAY = "SCHEDULE_DAY";
	private int currentDay = 5;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		currentDay = args.getInt(SCHEDULE_DAY, currentDay);

		fetchEvents();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.layout_schedule_day, container, false);
		unbinder = ButterKnife.bind(this, view);

		swipeRefresh.setOnRefreshListener(this::fetchEvents);
		Utils.attachHackIllinoisRefreshView(swipeRefresh, inflater);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(adapter);
		activeEvents.setEmptyView(emptyView);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		adapter.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			adapter.onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public static ScheduleDayFragment getDay(int day) {
		ScheduleDayFragment dayFragment = new ScheduleDayFragment();
		Bundle args = new Bundle();
		args.putInt(SCHEDULE_DAY, day);
		dayFragment.setArguments(args);
		return dayFragment;
	}

	public void stopRefreshing() {
		if (swipeRefresh != null) {
			swipeRefresh.setRefreshing(false);
		}
	}

	public void fetchEvents() {
		Timber.d("Fetching events for day of week: %s", currentDay);
		getApi().getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null && response.isSuccessful()) {

							List<EventItem> events = Stream.of(response.body().getData())
									.filter(event -> event.getStartTime().getDayOfWeek() == currentDay)
									.sortBy(EventResponse.Event::getStartTime)
									.sorted()
									.map(event -> new EventItem(event, true, true))
									.collect(Collectors.toList());

							adapter.updateDataSet(events);
						}
						stopRefreshing();
					}

					@Override
					public void onFailure(Call<EventResponse> call, Throwable t) {
						stopRefreshing();
					}
				});
	}
}
