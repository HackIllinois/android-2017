package org.hackillinois.android.ui.modules.announcement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.hackillinois.android.item.AnnouncementItem;
import org.hackillinois.android.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementFragment extends BaseFragment {
	@BindView(R.id.announcements) RecyclerView announcements;

	private Unbinder unbinder;
	private final ItemAdapter<AnnouncementItem> itemAdapter = new ItemAdapter<>();
	private final FastAdapter<AnnouncementItem> fastAdapter = FastAdapter.with(itemAdapter);

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_announcement, container, false);
		unbinder = ButterKnife.bind(this, view);

		fetchAnnouncements();

		//set our adapters to the RecyclerView
		announcements.setAdapter(fastAdapter);

		DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
		announcements.addItemDecoration(divider);

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

	public void fetchAnnouncements() {
		getApi().getAnnouncements()
				.enqueue(new Callback<AnnouncementResponse>() {
					@Override
					public void onResponse(Call<AnnouncementResponse> call, Response<AnnouncementResponse> response) {
						if (response != null && response.isSuccessful()) {
							//todo check sorting
							List<AnnouncementItem> announcements = Stream.of(response.body().getAnnouncements())
									.map(AnnouncementItem::new)
									.collect(Collectors.toList());

							itemAdapter.set(announcements);
						}
					}

					@Override
					public void onFailure(Call<AnnouncementResponse> call, Throwable t) {

					}
				});
	}
}
