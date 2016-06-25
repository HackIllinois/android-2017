package org.hackillinois.app2017.Announcement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private ArrayList<Announcement> announcements;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView messageTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.announcementTitle);
            messageTextView = (TextView) v.findViewById(R.id.announcementMessage);
        }
    }

    public AnnouncementAdapter(ArrayList<Announcement> announcements){
        this.announcements = announcements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_announcement_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement curAnnouncement = announcements.get(position);
        holder.titleTextView.setText(curAnnouncement.getTitle());
        holder.messageTextView.setText(curAnnouncement.getMessage());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

}
