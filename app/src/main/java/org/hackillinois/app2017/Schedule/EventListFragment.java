package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListFragment extends Fragment {

    private Event[] events;

    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.event_list_layout, parent, false);
        ButterKnife.bind(this, view);

        Bundle extras = getArguments();
        int whichDay = extras.getInt("day");
        switch (whichDay){
            case 0:
                events = getFridayEvents();
                break;
            case 1:
                events = getSaturdayEvents();
                break;
            case 2:
                events = getSundayEvents();
                break;
            default:
                events = getFridayEvents();
                break;
        }

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

    private Event[] getFridayEvents(){
        ArrayList<Event> toReturn = new ArrayList<>();
        toReturn.add(new Event("Buses Arrive/Check-in", "Siebel Atrium", 12, 30, true));
        toReturn.add(new Event("Career Fair", "Siebel/ECEB", 2, 30, true));
        toReturn.add(new Event("Opening Ceremony", "Union", 7, 0, true));
        toReturn.add(new Event("Dinner", "Siebel/ECEB", 10, 0, true));
        toReturn.add(new Event("Hacking Begins", "Siebel/ECEB", 12, 30, false));
        toReturn.add(new Event("Microsoft Tech Talk", "Grainger Auditorium (ECEB)", 4, 0, false));
        return toReturn.toArray(new Event[toReturn.size()]);
    }

    private Event[] getSaturdayEvents(){
        ArrayList<Event> toReturn = new ArrayList<>();
        toReturn.add(new Event("Midnight Snack", "Siebel/ECEB", 9, 30, true));
        toReturn.add(new Event("Brunch", "Siebel/ECEB", 10, 0, true));
        toReturn.add(new Event("Apple Tech Talk", "Grainger Auditorium (ECEB)", 12, 0, false));
        toReturn.add(new Event("Raspberry Pi Workshop", "ECEB", 12, 15, false));
        toReturn.add(new Event("Nerf Wars", "Kenny Gym", 8, 30, false));
        return toReturn.toArray(new Event[toReturn.size()]);
    }

    private Event[] getSundayEvents(){
        ArrayList<Event> toReturn = new ArrayList<>();
        toReturn.add(new Event("Hacking Ends", "Siebel/ECEB", 8, 45, true));
        toReturn.add(new Event("Lunch", "Siebel/ECEB", 12, 0, false));
        toReturn.add(new Event("Awards Ceremony", "Union", 2, 0, false));
        toReturn.add(new Event("Buses Leave", "Siebel/ECEB", 4, 0, false));
        return toReturn.toArray(new Event[toReturn.size()]);
    }
}
