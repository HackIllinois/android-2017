package org.hackillinois.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.hackillinois.android.HomeFragment;
import org.hackillinois.android.NotificationFragment;
import org.hackillinois.android.ProfileFragment;
import org.hackillinois.android.R;
import org.hackillinois.android.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends HackillinoisActivity {
    @BindView(R.id.navigation)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment mHomeFragment;
    private Fragment mNotificationFragment;
    private Fragment mProfileFragment;
    private Fragment mScheduleFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.menu_home:
                    swapFragments(mHomeFragment);
                    break;
                case R.id.menu_profile:
                    swapFragments(mProfileFragment);
                    break;
                case R.id.menu_notifications:
                    swapFragments(mNotificationFragment);
                    break;
                case R.id.menu_schedule:
                    swapFragments(mScheduleFragment);
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
        sendBroadcast(new Intent("finish_activity"));

        mHomeFragment = new HomeFragment();
        mNotificationFragment = new NotificationFragment();
        mProfileFragment = new ProfileFragment();
        mScheduleFragment = new ScheduleFragment();

        Toolbar toolbar = findViewById(R.id.genericToolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, mHomeFragment).commit();
        setTitle("Home");
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
