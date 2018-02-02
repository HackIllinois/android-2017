package org.hackillinois.android.ui.modules.schedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.android.R;
import org.hackillinois.android.ui.base.BaseFragment;
import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class ScheduleFragment extends BaseFragment {
	@BindView(R.id.tabs) TabLayout tabLayout;
	@BindView(R.id.day_viewpager) ViewPager viewPager;
	private Unbinder unbinder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.layout_schedule, parent, false);
		unbinder = ButterKnife.bind(this, view);

		tabLayout.setupWithViewPager(viewPager);
		viewPager.setAdapter(new ScheduleViewPagerAdapter(getContext(), getChildFragmentManager()));
		int currentDayTab = getCurrentDayTab();
		viewPager.setCurrentItem(currentDayTab);
		Timber.d("Tab seletected: %s", currentDayTab);

		return view;
	}

	public int getCurrentDayTab() {
		DateTime now = DateTime.now();
		Timber.d("Current day is: %s", now.dayOfWeek().getAsText());
		switch (now.getDayOfWeek()) {
			case 5: // friday
				return 0;
			case 6: // saturday
				return 1;
			case 7: // sunday
				return 2;
			default:
				return 0;
		}
	}
}
