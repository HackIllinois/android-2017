package org.hackillinois.app2017.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ScheduleViewPageAdapter extends FragmentStatePagerAdapter {
    private static SparseArray<EventListFragment> eventHolder;

    public ScheduleViewPageAdapter(FragmentManager fm) {
        super(fm);
        eventHolder = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        EventListFragment toReturn = eventHolder.get(position);
        if(toReturn != null) {
            return toReturn;
        }

        toReturn = new EventListFragment();
        Bundle extras = new Bundle();
        extras.putInt("day", position);
        toReturn.setArguments(extras);

        eventHolder.put(position,toReturn);
        return toReturn;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
