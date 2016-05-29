package org.hackillinois.branding.Schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.branding.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class FridayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.friday_schedule, parent, false);
        return view;
    }
}
