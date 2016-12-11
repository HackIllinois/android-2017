package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ScheduleViewPageAdapter extends FragmentStatePagerAdapter {

    public ScheduleViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EventListFragment toReturn = new EventListFragment();
        Bundle extras = new Bundle();
        extras.putInt("day", position);
        toReturn.setArguments(extras);
        return toReturn;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
