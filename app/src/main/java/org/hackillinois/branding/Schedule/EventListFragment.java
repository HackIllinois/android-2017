package org.hackillinois.branding.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.branding.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class EventListFragment extends Fragment {

    private Event[] events;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.event_list_layout, parent, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        events = new Event[2];
        events[0] = new Event("Title", "Location", 900);
        events[1] = new Event("Title 2", "Location 2", 1000);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ScheduleAdapter(events);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
