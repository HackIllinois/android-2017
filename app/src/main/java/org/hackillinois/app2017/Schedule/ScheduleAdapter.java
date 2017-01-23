package org.hackillinois.app2017.Schedule;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Event[] dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.eventName) TextView titleTextView;
        @BindView(R.id.eventLocation) TextView locationTextView;
        @BindView(R.id.eventTime) TextView timeTextView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");

            titleTextView.setTypeface(brandon_med);
            locationTextView.setTypeface(brandon_med);
            timeTextView.setTypeface(brandon_med);
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
