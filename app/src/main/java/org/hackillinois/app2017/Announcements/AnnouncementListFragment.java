package org.hackillinois.app2017.Announcements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hackillinois.app2017.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnnouncementListFragment extends Fragment implements AnnouncementManager.CallBack {

    private AnnouncementAdapter adapter;
    private Unbinder unbinder;

    @BindView(R.id.announcementList) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_announcements, parent, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new AnnouncementAdapter(AnnouncementManager.getInstance().getAnnouncements());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        AnnouncementManager.getInstance().setCallback(this);
        onEventsSet();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onEventsSet() {
        adapter.notifyDataSetChanged();
    }
}
