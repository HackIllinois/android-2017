package org.hackillinois.app2017;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.hackillinois.app2017.Announcements.AnnouncementListFragment;
import org.hackillinois.app2017.Profile.LoadingFragment;
import org.hackillinois.app2017.Profile.ProfileFragment;
import org.hackillinois.app2017.Schedule.ScheduleFragment;
import org.hackillinois.app2017.Settings.PrefsActivity;
import org.hackillinois.app2017.Settings.PrefsFragment;
import org.hackillinois.app2017.UI.CenteredToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String sharedPrefsName = "AppPrefs";
    private static final int REQUEST_CODE = 12;

    private FragmentManager fragmentManager;
    private MapFragment mMapFragment;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPerms();
        setSupportActionBar(toolbar);
        setUpBottomNavigationBar();

        fragmentManager = getSupportFragmentManager();
        mMapFragment = new MapFragment();

        // Set default fragment to schedule fragment
        fragmentManager.beginTransaction()
                .replace(R.id.content_holder, new ScheduleFragment()).commit();
        setTitle("Schedule");
    }

    @Override
    public void onBackPressed() {
        // TODO: What should happen when onBackPressed?
    }

    private void setUpBottomNavigationBar() {
        // Set stuff
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation.setColored(true);

        // Create Items
        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home_item, R.drawable.ic_home_white_24dp, R.color.primary);
        AHBottomNavigationItem schedule = new AHBottomNavigationItem(R.string.schedule_item, R.drawable.ic_today_white_24dp, R.color.primary);
        AHBottomNavigationItem maps = new AHBottomNavigationItem(R.string.maps_item, R.drawable.ic_map_black_36dp, R.color.primary);
        AHBottomNavigationItem notifications = new AHBottomNavigationItem(R.string.notifications_item, R.drawable.ic_notifications_white_24dp, R.color.primary);
        AHBottomNavigationItem profile = new AHBottomNavigationItem(R.string.profile_item, R.drawable.ic_person_black_36dp, R.color.primary);

        // Add Items
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(schedule);
        bottomNavigation.addItem(maps);
        bottomNavigation.addItem(notifications);
        bottomNavigation.addItem(profile);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch(position) {
                    case 0:
                        // Home
                        break;
                    case 1:
                        // Schedule
                        swapFragment(new ScheduleFragment());
                        setTitle("Schedule");
                        break;
                    case 2:
                        // Maps
                        swapFragment(mMapFragment);
                        setTitle("Map");
                        break;
                    case 3:
                        // Notifications
                        swapFragment(new AnnouncementListFragment());
                        setTitle("Announcements");
                        break;
                    case 4:
                        // Profile
                        swapFragment(new ProfileFragment());
                        setTitle("Profile");
                        break;
                }
                return true;
            }
        });
    }

//    private void selectDrawerItem(MenuItem item){
//        final int selectedID = item.getItemId();
//        switch (selectedID){
//            case R.id.nav_schedule:
//                swapFragment(new ScheduleFragment());
//                setTitle("Schedule");
//                lastSelected = selectedID;
//                break;
//            case R.id.nav_announcements:
//                swapFragment(new AnnouncementListFragment());
//                setTitle("Announcements");
//                lastSelected = selectedID;
//                break;
//            case R.id.nav_map:
//                swapFragment(mMapFragment);
//                setTitle("Map");
//                lastSelected = selectedID;
//                break;
//            case R.id.nav_profile:
//                swapFragment(new ProfileFragment());
//                setTitle("Profile");
//                lastSelected = selectedID;
//                break;
//            case R.id.nav_hacker_help:
//                swapFragment(new HackerHelpFragment());
//                setTitle("Hacker Help");
//                lastSelected = selectedID;
//                break;
//            case R.id.nav_settings:
//                Intent intent = new Intent(this, PrefsActivity.class);
//                startActivity(intent);
//                break;
//        }
//    }

    private void swapFragment(final Fragment fragment) {
        // Load our LoadingFragment first if we're switching to maps
        if(fragment.equals(mMapFragment)) {
            fragmentManager.beginTransaction().replace(R.id.content_holder, new LoadingFragment()).commit();

            // Wait for the NavDrawer to close before loading our fragments
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in,
                            0,
                            0,
                            android.R.anim.fade_out);
                    transaction.replace(R.id.content_holder, fragment).commit();
                }
            }, 300);
        } else {
            fragmentManager.beginTransaction().replace(R.id.content_holder, fragment).commit();
        }
    }

    private void checkPerms(){
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
        for(String permission:PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // TODO: Display popup to tell user that we need location for maps
                    this.finish();
                }
                return;
            }
        }
    }
}
