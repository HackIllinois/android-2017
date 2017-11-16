package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import org.hackillinois.app2017.Events.Event;
import org.hackillinois.app2017.Events.EventManager;
import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventListFragment extends Fragment {

    private List<Event> events;
    private static final Predicate<Event> IS_FRIDAY = event -> event.getStartDay() == 5;
    private static final Predicate<Event> IS_SATURDAY = event -> event.getStartDay() == 6;
    private static final Predicate<Event> IS_SUNDAY = event -> event.getStartDay() == 0;

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = EventManager.getInstance().getEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list_layout, parent, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle extras = getArguments();
        Predicate<Event> eventFilter = getFilterFromArguments(extras);
        events = getEvents(eventFilter);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new ScheduleAdapter(events);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), 1);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private Predicate<Event> getFilterFromArguments(Bundle extras) {
        int whichDay = extras.getInt("day");
        Log.i("Which Day", whichDay + "");
        switch (whichDay) {
            case 0:
                return IS_FRIDAY;
            case 1:
                return IS_SATURDAY;
            case 2:
                return IS_SUNDAY;
            default:
                return IS_FRIDAY;
        }
    }

    private List<Event> getEvents(Predicate<Event> eventFilter) {
        return Stream.of(events)
                .filter(eventFilter)
                .collect(Collectors.toList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
