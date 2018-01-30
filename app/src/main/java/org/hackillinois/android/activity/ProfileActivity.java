package org.hackillinois.android.activity;

import android.os.Bundle;

import org.hackillinois.android.R;

import butterknife.ButterKnife;

public class ProfileActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }


}
