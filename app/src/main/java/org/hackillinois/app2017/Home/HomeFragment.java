package org.hackillinois.app2017.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Schedule.Event;
import org.hackillinois.app2017.Schedule.EventManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private HomeEventList homeEventList;
    private HomeAdapter homeAdapter;
    private Unbinder unbinder;

    @BindView(R.id.homeEventList) RecyclerView homeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        GregorianCalendar hackIllinoisStartTime = new GregorianCalendar(2017, Calendar.FEBRUARY, 24, 21, 0);
        homeEventList = new HomeEventList(new HomeTime(hackIllinoisStartTime));
        View view = inflater.inflate(R.layout.layout_home, parent, false);
        unbinder = ButterKnife.bind(this, view);

        homeAdapter = new HomeAdapter(homeEventList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(mLayoutManager);
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeRecyclerView.setAdapter(homeAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeEventList.syncEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
