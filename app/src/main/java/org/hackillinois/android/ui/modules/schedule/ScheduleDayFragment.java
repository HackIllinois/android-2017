package org.hackillinois.android.ui.modules.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.event.EventInfoDialog;
import org.hackillinois.android.ui.modules.event.EventItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ScheduleDayFragment extends BaseFragment {
	@BindView(R.id.active_events) RecyclerView activeEvents;
	@BindView(R.id.schedule_refresh) SwipeRefreshLayout swipeRefresh;
	private Unbinder unbinder;

	private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);

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
		swipeRefresh.setColorSchemeResources(R.color.lightPink);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			EventInfoDialog dialog = new EventInfoDialog(getContext(), item.getEvent());
			dialog.setOnDismissListener(di -> Utils.updateEventStarred(getContext(), ButterKnife.findById(v, R.id.event_star), item.getEvent()));
			dialog.show();
			return false;
		});

		fastAdapter.withEventHook(new ClickEventHook<EventItem>() {
			@Nullable
			@Override
			public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
				if (viewHolder instanceof EventItem.EventViewHolder) {
					return ((EventItem.EventViewHolder) viewHolder).eventStar;
				}
				return null;
			}

			@Override
			public void onClick(View v, int position, FastAdapter<EventItem> fastAdapter, EventItem item) {
				if (v.getId() == R.id.event_star) {
					Utils.toggleEventStarred(getContext(), (ImageView) v, item.getEvent());
				}
			}
		});

		DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
		activeEvents.addItemDecoration(divider);

		return view;
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
									.map(event -> new EventItem(event, true))
									.collect(Collectors.toList());

							itemAdapter.set(events);
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
