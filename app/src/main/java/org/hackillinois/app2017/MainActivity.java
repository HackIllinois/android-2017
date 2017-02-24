package org.hackillinois.app2017;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.hackillinois.app2017.Announcements.AnnouncementListFragment;
import org.hackillinois.app2017.Announcements.AnnouncementManager;
import org.hackillinois.app2017.Announcements.BackgroundAnnouncements;
import org.hackillinois.app2017.Events.EventManager;
import org.hackillinois.app2017.Home.HomeFragment;
import org.hackillinois.app2017.Map.MapFragment;
import org.hackillinois.app2017.Profile.ProfileFragment;
import org.hackillinois.app2017.Schedule.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_NAME = "AppPrefs";
    private static final int REQUEST_CODE = 12;
    public static final String BOTTOM_BAR_TAB = "BOTTOM_BAR_TAB";
    public static final String UPDATE_NOTIFICATION = "UPDATE_NOTIFICATION";

    private FragmentManager fragmentManager;
    private MapFragment mMapFragment;
    private HomeFragment mHomeFragment;
    private ProfileFragment mProfileFragment;
    private AnnouncementListFragment mAnnouncementListFragment;
    private static boolean activelyVisible;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    @BindView(R.id.map_bar) LinearLayout mapBar;

    @BindView(R.id.map_DCL) public TextView mapDCLText;
    @BindView(R.id.map_Siebel) public TextView mapSiebelText;
    @BindView(R.id.map_ECEB) public TextView mapECEBText;
    @BindView(R.id.map_Union) public TextView mapUnionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMapFragment = new MapFragment();
        mHomeFragment = new HomeFragment();
        mProfileFragment = new ProfileFragment();
        mAnnouncementListFragment = new AnnouncementListFragment();

        checkPerms();
        setSupportActionBar(toolbar);
        setUpBottomNavigationBar();
        setUpTabBar();

        changeFonts();

        fragmentManager = getSupportFragmentManager();

        // Set default fragment to Home fragment
        fragmentManager.beginTransaction()
                .replace(R.id.content_holder, mHomeFragment).commit();
        setTitle("Home");
        BackgroundAnnouncements.startBackgroundAnnouncements(getApplicationContext());

        getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("new_notification_count")) {
                    updateNotification();
                }
            }
        });
        AnnouncementManager.sync(getApplicationContext());
        EventManager.sync(getApplicationContext(),null); //refreshEventList events
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if(intent != null) {
            int currentTabView = intent.getIntExtra(BOTTOM_BAR_TAB,-1);
            if(currentTabView != -1) {
                bottomNavigation.setCurrentItem(currentTabView);
                bottomNavigation.restoreBottomNavigation();
            }

            updateNotification();
        }
    }

    private void updateNotification() {
        NotificationManagerCompat.from(getApplicationContext()).cancelAll();
        int newNotifications = getNewNotificationCount();
        if(newNotifications == 0) {
            bottomNavigation.setNotification("",3);
        } else {
            bottomNavigation.setNotification("HEY",3);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        // TODO: What should happen when onBackPressed?
    }

    public void setMapDCLOnClickListener(View.OnClickListener o) {
        mapDCLText.setOnClickListener(o);
    }

    public void setMapSiebelOnClickListener(View.OnClickListener o) {
        mapSiebelText.setOnClickListener(o);
    }

    public void setMapECEBOnClickListener(View.OnClickListener o) {
        mapECEBText.setOnClickListener(o);
    }

    public void setMapUnionOnClickListener(View.OnClickListener o) {
        mapUnionText.setOnClickListener(o);
    }

    private void setUpTabBar() {
        final TabLayout.Tab friday = tabLayout.newTab();
        final TabLayout.Tab saturday = tabLayout.newTab();
        final TabLayout.Tab sunday = tabLayout.newTab();

        friday.setText("Friday");
        saturday.setText("Saturday");
        sunday.setText("Sunday");

        tabLayout.addTab(friday, 0);
        tabLayout.addTab(saturday, 1);
        tabLayout.addTab(sunday, 2);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.drawable.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.seafoam_blue));
    }

    private void setUpBottomNavigationBar() {
        // Create Items
        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home_item, R.drawable.home_off, R.color.primary);
        AHBottomNavigationItem schedule = new AHBottomNavigationItem(R.string.schedule_item, R.drawable.schedule_off, R.color.primary);
        AHBottomNavigationItem maps = new AHBottomNavigationItem(R.string.maps_item, R.drawable.maps_off, R.color.primary);
        AHBottomNavigationItem notifications = new AHBottomNavigationItem(R.string.notifications_item, R.drawable.notifications_off, R.color.primary);
        AHBottomNavigationItem profile = new AHBottomNavigationItem(R.string.profile_item, R.drawable.profile_off, R.color.primary);

        // Add Items
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(schedule);
        bottomNavigation.addItem(maps);
        bottomNavigation.addItem(notifications);
        bottomNavigation.addItem(profile);

        // Set custom settings
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.setDefaultBackgroundResource(R.color.dark_slate_blue);
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.light_periwinkle));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.faded_blue));
        bottomNavigation.setCurrentItem(0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch(position) {
                    case 0:
                        // Home
                        swapFragment(mHomeFragment);
                        tabLayout.setVisibility(View.GONE);
                        mapBar.setVisibility(View.GONE);
                        setTitle("HOME");
                        break;
                    case 1:
                        // Schedule
                        swapFragment(new ScheduleFragment());
                        tabLayout.setVisibility(View.VISIBLE);
                        mapBar.setVisibility(View.GONE);
                        setTitle("SCHEDULE");
                        break;
                    case 2:
                        // Maps
                        swapFragment(mMapFragment);
                        tabLayout.setVisibility(View.GONE);
                        mapBar.setVisibility(View.VISIBLE);
                        setTitle("MAPS");
                        break;
                    case 3:
                        // Notifications
                        swapFragment(mAnnouncementListFragment);
                        resetNotifications();
                        tabLayout.setVisibility(View.GONE);
                        mapBar.setVisibility(View.GONE);
                        setTitle("NOTIFICATIONS");
                        break;
                    case 4:
                        // Profile
                        swapFragment(mProfileFragment);
                        tabLayout.setVisibility(View.GONE);
                        mapBar.setVisibility(View.GONE);
                        setTitle("PROFILE");
                        break;
                }

                return true;
            }
        });
    }

    private void swapFragment(final Fragment fragment) {
        // Load our LoadingFragment first if we're switching to maps
        if(fragment.equals(mMapFragment)) {
            fragmentManager.beginTransaction().replace(R.id.content_holder, new LoadingFragment()).commit();

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

    private void checkPerms() {
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET};
        for(String permission:PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        activityPaused();
    }

    @Override
    public void onResume() {
        super.onResume();
        activityResumed();
    }

    public static void activityPaused() {
        activelyVisible = false;
    }

    public static void activityResumed() {
        activelyVisible = true;
    }

    public static boolean isActivelyVisible() {
        return activelyVisible;
    }

    private void changeFonts() {
        Typeface brandon_med = Typeface.createFromAsset(getAssets(), "fonts/Brandon_med.otf");

        mapDCLText.setTypeface(brandon_med);
        mapSiebelText.setTypeface(brandon_med);
        mapECEBText.setTypeface(brandon_med);
        mapUnionText.setTypeface(brandon_med);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        BackgroundAnnouncements.stopBackgroundAnnouncements(getApplicationContext());
    }

    private void resetNotifications() {
        SharedPreferences shared = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("new_notification_count",0);
        editor.apply();
        updateNotification();
    }

    public int getNewNotificationCount() {
        int num = 0;
        SharedPreferences shared = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        return shared.getInt("new_notification_count",0);
    }
}
