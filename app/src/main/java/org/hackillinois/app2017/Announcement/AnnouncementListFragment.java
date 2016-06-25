package org.hackillinois.app2017.Announcement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class AnnouncementListFragment extends Fragment {

    private ArrayList<Announcement> announcements =  new ArrayList<>();
    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_announcements, parent, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.announcementList);

        adapter = new AnnouncementAdapter(announcements);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        testData();

        return view;
    }

    private void testData(){
        Announcement announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        announcement = new Announcement("Yoooo", "This is a test message bruh");
        announcements.add(announcement);

        adapter.notifyDataSetChanged();
    }
}
