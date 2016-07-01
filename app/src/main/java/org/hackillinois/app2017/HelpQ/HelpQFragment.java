package org.hackillinois.app2017.HelpQ;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class HelpQFragment extends Fragment {

    private FloatingActionButton newTicketButton;
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private ArrayList<Ticket> openTickets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_helpq, parent, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.ticketList);

        adapter = new TicketAdapter(openTickets);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        inflateTickets();

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

    private void inflateTickets(){
        Ticket test = new Ticket("Thomas Yu", "MySQL", "123-456-7890");
        openTickets.add(test);
        adapter.notifyDataSetChanged();
    }
}
