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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private ArrayList<Object> events;
    private HomeAdapter homeAdapter;
    private Unbinder unbinder;

    @BindView(R.id.homeEventList) RecyclerView homeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        events = new ArrayList<>();
        View view = inflater.inflate(R.layout.layout_home, parent, false);
        unbinder = ButterKnife.bind(this, view);

        homeAdapter = new HomeAdapter(events);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(mLayoutManager);
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeRecyclerView.setAdapter(homeAdapter);
        addData();
        return view;
    }

    private void addData() {
        events.add(new HomeTime(new GregorianCalendar(2017, Calendar.FEBRUARY, 24, 21, 0)));
        events.add(new HomeEvent());
        events.add(new HomeEvent());

        homeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
