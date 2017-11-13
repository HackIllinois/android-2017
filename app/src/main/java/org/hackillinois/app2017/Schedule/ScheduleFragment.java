package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScheduleFragment extends Fragment {

    @BindView(R.id.viewpager) ViewPager viewPager;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.layout_schedule, parent, false);
        unbinder = ButterKnife.bind(this, view);

        ScheduleViewPageAdapter scheduleViewPageAdapter = new ScheduleViewPageAdapter(getFragmentManager());
        viewPager.setAdapter(scheduleViewPageAdapter);
        int currentDayTab = getCurrentDayTab();
        viewPager.setCurrentItem(currentDayTab);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(viewPager != null) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(viewPager != null) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public int getCurrentDayTab() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd");
        int day = Integer.parseInt(dateFormat.format(date));
        switch (day) {
            case 24:
                return 0;
            case 25:
                return 1;
            case 26:
                return 2;
            default:
                return 0;
        }
    }
}
