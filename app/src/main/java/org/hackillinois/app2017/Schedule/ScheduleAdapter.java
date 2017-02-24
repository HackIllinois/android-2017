package org.hackillinois.app2017.Schedule;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hackillinois.app2017.Events.Event;
import org.hackillinois.app2017.Events.EventActivity;
import org.hackillinois.app2017.Events.EventLocation;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private ArrayList<Event> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.eventName) TextView titleTextView;
        @BindView(R.id.event_location_container) LinearLayout eventLocationContainer;
        private ArrayList<String> locationLong = new ArrayList<>();
        @BindView(R.id.eventTime) TextView timeTextView;
        // @BindView(R.id.event_button_remind_me) TextView remindMeTextView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");

            titleTextView.setTypeface(brandon_med);
            timeTextView.setTypeface(brandon_med);
            // remindMeTextView.setTypeface(brandon_med);
            for(int i = 0; i < eventLocationContainer.getChildCount(); i++) {
                ((TextView)eventLocationContainer.getChildAt(i)).setTypeface(brandon_med);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), EventActivity.class);
                    i.putExtra("title", titleTextView.getText());
                    i.putStringArrayListExtra("location", locationLong);
                    i.putExtra("starttime", timeTextView.getText());

                    v.getContext().startActivity(i);
                }
            });
        }
    }

    public ScheduleAdapter(ArrayList<Event> data){
        mDataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        for(EventLocation e : mDataset.get(position).getLocation()) {
            TextView locationView = Utils.generateLocationTextView(holder.itemView.getContext(),e.getShortName());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            locationView.setLayoutParams(layoutParams);
            locationView.setTextSize(18);
            holder.eventLocationContainer.addView(locationView);
        }
        holder.titleTextView.setText(mDataset.get(position).getName());
        holder.timeTextView.setText(mDataset.get(position).getStartHour());
        for(EventLocation e : mDataset.get(position).getLocation()) {
            holder.locationLong.add(e.getName());
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
