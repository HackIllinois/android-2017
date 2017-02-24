package org.hackillinois.app2017.Home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

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
        homeEventList = new HomeEventList(getTimeToCountdown());
        View view = inflater.inflate(R.layout.layout_home, parent, false);
        unbinder = ButterKnife.bind(this, view);

        homeAdapter = new HomeAdapter(homeEventList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(mLayoutManager);
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeRecyclerView.setAdapter(homeAdapter);
        return view;
    }

    private HomeTime getTimeToCountdown() {
        Utils.HackIllinoisStatus status = Utils.getHackIllinoisStatus();
        Date toCountDownTo;
        String title;
        switch (status) {
            case BEFORE:
                toCountDownTo = Utils.getDateFromAPI(Utils.HACKILLINOIS_START);
                title = "Hacking Starts in...";
                break;
            case DURING:
                toCountDownTo = Utils.getDateFromAPI(Utils.HACKILLINOIS_END);
                title = "Submit Project in...";
                break;
            case AFTER:
                toCountDownTo = Utils.getDateFromAPI(Utils.HACKILLINOIS_END);
                title = "Hacking Ended...";
                break;
            default:
                toCountDownTo = Utils.getDateFromAPI(Utils.HACKILLINOIS_START);
                title = "Hacking Starts In...";
                break;
        }
        Calendar hackIllinoisStartTime = GregorianCalendar.getInstance();
        hackIllinoisStartTime.setTime(toCountDownTo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshEventList();
            }
        }, TimeUnit.SECONDS.toMillis(30));
        return new HomeTime(hackIllinoisStartTime,title);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refreshEventList() {
        homeEventList.syncEvents();
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
