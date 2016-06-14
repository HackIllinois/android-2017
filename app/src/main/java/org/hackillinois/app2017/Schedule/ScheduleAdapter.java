package org.hackillinois.app2017.Schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Event[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView locationTextView;
        public TextView titleTextView;
        public TextView timeTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.eventName);
            locationTextView = (TextView) v.findViewById(R.id.eventLocation);
            timeTextView = (TextView) v.findViewById(R.id.eventTime);
        }
    }

    public ScheduleAdapter(Event[] data){
        dataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.locationTextView.setText(dataset[position].getLocation());
        holder.titleTextView.setText(dataset[position].getTitle());
        holder.timeTextView.setText(dataset[position].getTime());
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
