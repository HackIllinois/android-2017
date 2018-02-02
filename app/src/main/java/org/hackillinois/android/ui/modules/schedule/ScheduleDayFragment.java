package org.hackillinois.android.ui.modules.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.item.EventItem;
import org.hackillinois.android.ui.modules.event.EventInfoDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ScheduleDayFragment extends Fragment {
	@BindView(R.id.active_events) RecyclerView activeEvents;
	private Unbinder unbinder;

	private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);

	private static final String SCHEDULE_DAY = "SCHEDULE_DAY";
	private int currentDay = 5;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.layout_schedule_day, container, false);
		unbinder = ButterKnife.bind(this, view);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			new EventInfoDialog(getContext(), item.getEvent()).show();
			return false;
		});

		DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
		activeEvents.addItemDecoration(divider);

		Bundle args = getArguments();
		currentDay = args.getInt(SCHEDULE_DAY, currentDay);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		fetchEvents();
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

	public void fetchEvents() {
		Timber.d("Fetching events for day of week: %s", currentDay);
		HackIllinoisAPI.api.getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null && response.isSuccessful()) {
							List<EventItem> events = Stream.of(response.body().getData())
									.filter(value -> value.getStartTime().getDayOfWeek() == currentDay)
									.map(event -> new EventItem(event, true))
									.collect(Collectors.toList());

							itemAdapter.set(events);
						}
					}

					@Override
					public void onFailure(Call<EventResponse> call, Throwable t) {

					}
				});
	}
}
