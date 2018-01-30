package org.hackillinois.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.hackillinois.android.R;

public abstract class DrawerActivity extends HackillinoisActivity {
	protected DrawerLayout drawerLayout;
	protected FrameLayout frameLayout;
    protected NavigationView nv;


    @Override
	public void setContentView(final int layoutResID) {
		drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
		frameLayout = drawerLayout.findViewById(R.id.content_frame);

        nv = drawerLayout.findViewById(R.id.navigation);
        nv.setNavigationItemSelectedListener(item -> {
            Log.d("Nav", item.getItemId() + "");
            Intent i;
            switch (item.getItemId()) {
                case R.id.menu_home: // Home
                    i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.menu_schedule: // Schedule
                    i = new Intent(getApplicationContext(), ScheduleActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.menu_notifications: // Notifications
                    i = new Intent(getApplicationContext(), NotificationsActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.menu_map: // Map
                    // TODO:Open User's Map App
                    break;
                case R.id.menu_profile: // Profile
                    i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                    break;
                default:
                    break;
            }

            return true;
        });

		getLayoutInflater().inflate(layoutResID, frameLayout);
		super.setContentView(drawerLayout);
	}
}
