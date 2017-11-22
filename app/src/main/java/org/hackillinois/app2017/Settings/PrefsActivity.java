package org.hackillinois.app2017.Settings;

import android.os.Bundle;
import android.view.MenuItem;

import org.hackillinois.app2017.HackillinoisActivity;
import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */

public class PrefsActivity extends HackillinoisActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_content, new PrefsFragment())
                .commit();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
