package org.hackillinois.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;
import org.hackillinois.android.ui.modules.announcement.AnnouncementFragment;
import org.hackillinois.android.ui.modules.home.HomeFragment;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;
import org.hackillinois.android.ui.modules.profile.ProfileFragment;
import org.hackillinois.android.ui.modules.schedule.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
	public static final String SET_TAB = "SET_TAB";
	@BindView(R.id.navigation) NavigationView navigationView;
	@BindView(R.id.drawer) DrawerLayout drawerLayout;
	@BindView(R.id.genericToolbar) Toolbar toolbar;

	private FragmentManager fragmentManager;
	private final HomeFragment homeFragment = new HomeFragment();
	private final AnnouncementFragment announcementFragment = new AnnouncementFragment();
	private final ProfileFragment profileFragment = new ProfileFragment();
	private final ScheduleFragment scheduleFragment = new ScheduleFragment();
	private Fragment current = homeFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		enableDebug();

		setSupportActionBar(toolbar);

		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.open_drawer,
				R.string.close_drawer
		);
		drawerToggle.syncState();

		navigationView.setNavigationItemSelectedListener(item -> switchToMenuItem(item.getItemId()));

		// Kill LoginChooserActivity so user can't press back and get to it.
		sendBroadcast(new Intent(LoginChooserActivity.FINISH_ACTIVITY));

		fragmentManager = getSupportFragmentManager();

		if (savedInstanceState == null) {
			fragmentManager.beginTransaction()
					.add(R.id.content_frame, profileFragment)
					.hide(profileFragment)
					.add(R.id.content_frame, announcementFragment)
					.hide(announcementFragment)
					.add(R.id.content_frame, scheduleFragment)
					.hide(scheduleFragment)
					.add(R.id.content_frame, homeFragment)
					.commit();
		}

		getSupportActionBar().setTitle(getString(R.string.menu_home));
		onNewIntent(getIntent());
	}

	private boolean switchToMenuItem(int itemId) {
		switch (itemId) {
			case R.id.menu_home:
				swapFragments(homeFragment);
				getSupportActionBar().setTitle(getString(R.string.menu_home));
				break;
			case R.id.menu_profile:
				swapFragments(profileFragment);
				getSupportActionBar().setTitle(getString(R.string.menu_profile));
				break;
			case R.id.menu_notifications:
				swapFragments(announcementFragment);
				getSupportActionBar().setTitle(getString(R.string.menu_notifications));
				break;
			case R.id.menu_schedule:
				swapFragments(scheduleFragment);
				getSupportActionBar().setTitle(getString(R.string.menu_schedule));
				break;
			case R.id.menu_map:
				goToMaps();
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent == null) {
			return;
		}

		Bundle extras = intent.getExtras();
		if (extras != null) {
			switchToMenuItem(extras.getInt(SET_TAB, R.id.menu_home));
		}
	}

	private void swapFragments(Fragment newFragment) {
		// Delay newFragment swapping for increased fluidity (we wait for drawer to close)
		new Handler().postDelayed(() -> {
					fragmentManager.beginTransaction()
							.hide(current)
							.show(newFragment)
							.commit();
					current = newFragment;
				},
				200);

		drawerLayout.closeDrawer(GravityCompat.START);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void goToMaps() {
		Settings settings = Settings.get();
		String json = settings.getLocations();

		LocationResponse locations = getApp().getGson().fromJson(json, LocationResponse.class);

		// Just get the first location's location to open up maps app.
		double longitude = locations.getLocations()[0].getLongitude();
		double latitude = locations.getLocations()[0].getLatitude();

		if (longitude == 0) {
			longitude = 40.1138; // Default location for ECEB
		}

		if (latitude == 0) {
			latitude = -88.2249; // Default location for ECEB
		}

		Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=" + 18);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");
		startActivity(mapIntent);
	}
}
