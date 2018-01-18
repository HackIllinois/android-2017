package org.hackillinois.android;

import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

public abstract class DrawerActivity extends HackillinoisActivity {
    protected DrawerLayout drawerLayout;
    protected FrameLayout frameLayout;

    @Override
    public void setContentView(final int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        frameLayout = drawerLayout.findViewById(R.id.content_frame);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(drawerLayout);
    }

}
