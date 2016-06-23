package org.hackillinois.app2017.Announcement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class AnnouncementFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_announcements, parent, false);
        return view;
    }
}
