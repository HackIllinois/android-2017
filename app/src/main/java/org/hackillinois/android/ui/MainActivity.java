package org.hackillinois.android.ui;

import android.content.Intent;
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
import android.view.MenuItem;

import org.hackillinois.android.R;
import org.hackillinois.android.ui.base.BaseActivity;
import org.hackillinois.android.ui.modules.announcement.AnnouncementFragment;
import org.hackillinois.android.ui.modules.home.HomeFragment;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;
import org.hackillinois.android.ui.modules.profile.ProfileFragment;
import org.hackillinois.android.ui.modules.schedule.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.navigation) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.genericToolbar) Toolbar toolbar;

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private AnnouncementFragment announcementFragment;
    private ProfileFragment profileFragment;
    private ScheduleFragment scheduleFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableDebug();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.open_drawer,
				R.string.close_drawer
		);
		drawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
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
                    // TODO: Open Map app
                    break;
                default:
                    break;
            }

            return false;
        });

        // Kill LoginChooserActivity so user can't press back and get to it.
        sendBroadcast(new Intent(LoginChooserActivity.FINISH_ACTIVITY));

        homeFragment = new HomeFragment();
        announcementFragment = new AnnouncementFragment();
        profileFragment = new ProfileFragment();
        scheduleFragment = new ScheduleFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
        getSupportActionBar().setTitle(getString(R.string.menu_home));
    }

    private void swapFragments(Fragment fragment) {
        // Delay fragment swapping for increased fluidity (we wait for drawer to close)
        new Handler().postDelayed(() ->
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit(),
                200);

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
