package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ScheduleFragment extends Fragment {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_schedule, parent, false);
        ButterKnife.bind(this, view);

        ScheduleViewPageAdapter scheduleViewPageAdapter = new ScheduleViewPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(scheduleViewPageAdapter);

        final TabLayout.Tab friday = tabLayout.newTab();
        final TabLayout.Tab saturday = tabLayout.newTab();
        final TabLayout.Tab sunday = tabLayout.newTab();

        friday.setText("Friday");
        saturday.setText("Saturday");
        sunday.setText("Sunday");

        tabLayout.addTab(friday, 0);
        tabLayout.addTab(saturday, 1);
        tabLayout.addTab(sunday, 2);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this.getContext(), R.drawable.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this.getContext(), R.color.accent));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
