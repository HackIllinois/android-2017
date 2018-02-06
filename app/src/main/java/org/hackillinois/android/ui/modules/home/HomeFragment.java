package org.hackillinois.android.ui.modules.home;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.item.EventItem;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.event.EventInfoDialog;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HomeFragment extends BaseFragment {
	@BindView(R.id.hour_animation) LottieAnimationView hourAnimation;
	@BindView(R.id.minute_animation) LottieAnimationView minuteAnimation;
	@BindView(R.id.second_animation) LottieAnimationView secondAnimation;
	@BindView(R.id.active_events) RecyclerView activeEvents;

	private Unbinder unbinder;
	private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_home, container, false);
		unbinder = ButterKnife.bind(this, view);

		float endProgress = 60f / 64f;
		float startProgress = 1f/64f;
		secondAnimation.setMinAndMaxProgress(startProgress, endProgress);
		minuteAnimation.setMinAndMaxProgress(startProgress, endProgress);
		hourAnimation.setMinAndMaxProgress(startProgress, endProgress);

		float secondsRatio = 1f / (2.0f);
		float minuteRatio = secondsRatio / 60;
		float hourRatio = minuteRatio / 60;
		secondAnimation.setSpeed(secondsRatio);
		minuteAnimation.setSpeed(secondsRatio);
		hourAnimation.setSpeed(secondsRatio);

		secondAnimation.addAnimatorListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {

			}

			@Override
			public void onAnimationEnd(Animator animator) {

			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {
				minuteAnimation.resumeAnimation();
				new Handler().postDelayed(() -> minuteAnimation.pauseAnimation(), TimeUnit.SECONDS.toMillis(1));
			}
		});

		minuteAnimation.addAnimatorListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {

			}

			@Override
			public void onAnimationEnd(Animator animator) {

			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {
				hourAnimation.resumeAnimation();
				new Handler().postDelayed(() -> hourAnimation.pauseAnimation(), TimeUnit.SECONDS.toMillis(1));
			}
		});

		LottieComposition.Factory.fromAssetFileName(getContext(), "countdown.json", composition -> {
			Period diff = new Period(DateTime.now(), DateTime.now().plusHours(1).plusMinutes(27).plusSeconds(15));
			float totalTime = 60.0f;
			float percentSecond = 1 - (diff.getSeconds() / totalTime);
			float percentMinute = 1 - (diff.getMinutes() / totalTime);
			float percentHour = 1 - (diff.getHours() / totalTime);
			Timber.d("Time remaining %sh %sm %ss", percentHour, percentMinute, percentSecond);
			secondAnimation.setProgress(percentSecond);
			minuteAnimation.setProgress(percentMinute);
			hourAnimation.setProgress(percentHour);
			Timber.d("Actual remaining %sh %sm %ss", hourAnimation.getProgress(), minuteAnimation.getProgress(), secondAnimation.getProgress());
		});

		//set our adapters to the RecyclerView
		activeEvents.setAdapter(fastAdapter);

		fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			new EventInfoDialog(getContext(), item.getEvent()).show();
			return false;
		});

		itemAdapter.getItemFilter()
				.withFilterPredicate((item, constraint) -> {
					EventResponse.Event event = item.getEvent();
					return event.getStartTime().isBeforeNow() && event.getEndTime().isAfterNow();
				});

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Timber.d("Running background filter");
				recheckFilter();
				handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1));
			}
		}, 100);

		DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
		activeEvents.addItemDecoration(divider);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		fetchEvents();
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

	public void fetchEvents() {
		getApi().getEvents()
				.enqueue(new Callback<EventResponse>() {
					@Override
					public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
						if (response != null && response.isSuccessful()) {
							List<EventItem> events = Stream.of(response.body().getData())
									.map(EventItem::new)
									.collect(Collectors.toList());

							itemAdapter.set(events);
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
