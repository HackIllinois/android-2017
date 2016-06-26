package org.hackillinois.app2017.HelpQ;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class HelpQFragment extends Fragment {

    private FloatingActionButton newTicketButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_helpq, parent, false);

        newTicketButton = (FloatingActionButton) view.findViewById(R.id.helpqFab);
        newTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewTicketActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
