package org.hackillinois.branding.Schedule;

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
        switch (position){
            case 0:
                return new EventListFragment();
            case 1:
                return new EventListFragment();
            default:
                return new EventListFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
