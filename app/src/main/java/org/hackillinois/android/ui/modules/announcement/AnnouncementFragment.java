package org.hackillinois.android.ui.modules.announcement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.custom.EmptyRecyclerView;
import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AnnouncementFragment extends BaseFragment {
	@BindView(R.id.announcements) EmptyRecyclerView announcements;
	@BindView(R.id.announcement_refresh) RecyclerRefreshLayout swipeRefresh;
	@BindView(R.id.empty_view) View emptyView;

	private Unbinder unbinder;

	private final FlexibleAdapter<AnnouncementItem> adapter = new FlexibleAdapter<>(null);

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fetchAnnouncements();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_announcement, container, false);
		unbinder = ButterKnife.bind(this, view);

		swipeRefresh.setOnRefreshListener(() -> {
			Settings.get().saveLastNotificationFetch(DateTime.now());
			fetchAnnouncements();
		});
		Utils.attachHackIllinoisRefreshView(swipeRefresh, inflater);

		//set our adapters to the RecyclerView
		adapter.setStickyHeaders(true)
				.setDisplayHeadersAtStartUp(true);
		announcements.setAdapter(adapter);
		announcements.setEmptyView(emptyView);

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
		if (savedInstanceState != null) {
			adapter.onRestoreInstanceState(savedInstanceState);
		}
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

	public void fetchAnnouncements() {
		Timber.d("Fetching new Announcements");
		getApi().getAnnouncements()
				.enqueue(new Callback<AnnouncementResponse>() {
					@Override
					public void onResponse(Call<AnnouncementResponse> call, Response<AnnouncementResponse> response) {
						if (response != null && response.isSuccessful()) {
							//todo check sorting
							List<AnnouncementItem> announcements = Stream.of(response.body().getAnnouncements())
									.map(AnnouncementItem::new)
									.toList();

							adapter.updateDataSet(announcements);
						}
						stopRefreshing();
					}

					@Override
					public void onFailure(Call<AnnouncementResponse> call, Throwable t) {
						stopRefreshing();
					}
				});
	}
}
