package org.hackillinois.branding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.hackillinois.branding.Schedule.FridayFragment;
import org.hackillinois.branding.Schedule.SaturdayFragment;
import org.hackillinois.branding.Schedule.SundayFragment;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FridayFragment();
            case 1:
                return new SaturdayFragment();
            default:
                return new SundayFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
