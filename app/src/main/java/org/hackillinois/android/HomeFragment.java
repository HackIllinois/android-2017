package org.hackillinois.android;

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

import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.dialogs.EventInfoDialog;
import org.hackillinois.android.items.EventItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    @BindView(R.id.active_events) RecyclerView activeEvents;

    private Unbinder unbinder;
    private final ItemAdapter<EventItem> itemAdapter = new ItemAdapter<>();
    private final FastAdapter<EventItem> fastAdapter = FastAdapter.with(itemAdapter);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        //set our adapters to the RecyclerView
        activeEvents.setAdapter(fastAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        activeEvents.addItemDecoration(divider);

        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
			new EventInfoDialog(getContext(), item.getEvent()).show();
			return false;
		});

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //set the items to your ItemAdapter
        HackIllinoisAPI.api.getEvents()
                .enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        if (response != null && response.isSuccessful()) {
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
}
