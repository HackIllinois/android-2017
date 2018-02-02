package org.hackillinois.android.ui.modules.schedule;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.hackillinois.android.R;

import timber.log.Timber;

public class ScheduleViewPagerAdapter extends FragmentPagerAdapter {
	private final Context context;
	int[] titles = {R.string.friday, R.string.saturday, R.string.sunday};

	public ScheduleViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return context.getString(titles[position]);
	}

	@Override
	public Fragment getItem(int position) {
		int day = getTabDay(position);
		return ScheduleDayFragment.getDay(day);
	}

	@Override
	public int getCount() {
		return 3;
	}

	private int getTabDay(int position) {
		switch (position) {
			case 0: // friday
				return 5;
			case 1: // saturday
				return 6;
			case 2: // sunday
				return 7;
			default:
				return 5;
		}
	}
}
