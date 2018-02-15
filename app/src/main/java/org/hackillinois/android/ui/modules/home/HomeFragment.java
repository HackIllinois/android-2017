package org.hackillinois.android.ui.modules.home;

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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.event.EventInfoDialog;
import org.hackillinois.android.ui.modules.event.EventItem;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements HomeClock.OnFinishListener {
	@BindView(R.id.second_animation) LottieAnimationView seconds;
	@BindView(R.id.minute_animation) LottieAnimationView minutes;
	@BindView(R.id.hour_animation) LottieAnimationView hours;
	@BindView(R.id.day_animation) LottieAnimationView days;
	@BindView(R.id.active_events) RecyclerView activeEvents;
	@BindView(R.id.home_refresh) SwipeRefreshLayout swipeRefresh;
	@BindView(R.id.countdownTitle) TextView countdownTitle;

	private static final int[] TITLE_IDS = new int[]{
			R.string.hackillinois_starts_in,
			R.string.hacking_starts_in,
			R.string.hacking_ends_in,
			R.string.hackillinois_ends_in
	};
	private Unbinder unbinder;
	private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);
	private HomeClock clock;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchEvents();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_home, container, false);
		unbinder = ButterKnife.bind(this, view);

		clock = new HomeClock(seconds, minutes, hours, days);
		List<DateTime> eventTimers = Arrays.asList(Settings.EVENT_START_TIME, Settings.HACKING_START_TIME, Settings.HACKING_END_TIME, Settings.EVENT_END_TIME);
		clock.setCountDownTo(this, eventTimers);

		swipeRefresh.setOnRefreshListener(this::fetchEvents);
		swipeRefresh.setColorSchemeResources(R.color.lightPink);

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			new EventInfoDialog(v.getContext(), item.getEvent()).show();
			return false;
		});

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		fastAdapter.saveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fastAdapter.withSavedInstanceState(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	public void stopRefreshing() {
		if (swipeRefresh != null) {
			swipeRefresh.setRefreshing(false);
		}
	}

	public void fetchEvents() {
		getApi().getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null && response.isSuccessful()) {
							List<EventItem> events = Stream.of(response.body().getData())
									.filter(event -> event.getStartTime().isBeforeNow())
									.filter(event -> event.getEndTime().isAfterNow())
									.map(EventItem::new)
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

	@Override
	public void onFinish(int timerIndex) {
		if (timerIndex < TITLE_IDS.length && countdownTitle != null) {
			countdownTitle.setText(TITLE_IDS[timerIndex]);
		}
	}
}
