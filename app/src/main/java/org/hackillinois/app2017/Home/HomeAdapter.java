package org.hackillinois.app2017.Home;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> homeEvents = new ArrayList<>();

    class TimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_time_text) TextView title;
        @BindView(R.id.home_time_hour) TextView hour;
        @BindView(R.id.home_time_minute) TextView minute;
        @BindView(R.id.home_time_second) TextView second;
        @BindView(R.id.home_text_hour) TextView hour_text;
        @BindView(R.id.home_text_minute) TextView minute_text;
        @BindView(R.id.home_text_second) TextView second_text;
        @BindView(R.id.home_target_time) TextView target_time;
        @BindView(R.id.home_happening_now) TextView happening_now;

        public TimeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            Typeface gotham_book = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Gotham-Book.otf");
            Typeface brandon_light = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_light.otf");
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");

            title.setTypeface(gotham_book);
            hour.setTypeface(brandon_light);
            minute.setTypeface(brandon_light);
            second.setTypeface(brandon_light);
            hour_text.setTypeface(brandon_med);
            minute_text.setTypeface(brandon_med);
            second_text.setTypeface(brandon_med);
            target_time.setTypeface(brandon_med);
            happening_now.setTypeface(brandon_reg);
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public HomeAdapter(ArrayList<Object> events) {
        homeEvents = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_time_layout, parent, false);
            return new TimeViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_event_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (homeEvents.get(position) instanceof HomeTime) {
            return 0;
        }

        return 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                        HomeTime targetTime = (HomeTime) homeEvents.get(position);
                        timeViewHolder.target_time.setText(targetTime.getTime());
                        timeViewHolder.hour.setText(String.format(Locale.US, "%03d", targetTime.getHours()));
                        timeViewHolder.minute.setText(String.format(Locale.US, "%02d", targetTime.getMinutes()));
                        timeViewHolder.second.setText(String.format(Locale.US, "%02d", targetTime.getSeconds()));
                        handler.postDelayed(this, 1000);
                    }
                };

                runnable.run();
        }
    }

    @Override
    public int getItemCount() {
        return homeEvents.size();
    }
}
