package org.hackillinois.branding.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.branding.R;

import java.util.ArrayList;

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

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ScheduleAdapter(events);
        mRecyclerView.setAdapter(mAdapter);

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
        toReturn.add(new Event("Web-dev & API Workshop", "ECEB", 12, 15, false));
        return toReturn.toArray(new Event[toReturn.size()]);
    }

    private Event[] getSundayEvents(){
        ArrayList<Event> toReturn = new ArrayList<>();
        toReturn.add(new Event("Hacking Ends", "Siebel/ECEB", 8, 45, true));
        toReturn.add(new Event("Lunch", "Siebel/ECEB", 4, 0, false));
        return toReturn.toArray(new Event[toReturn.size()]);
    }
}
