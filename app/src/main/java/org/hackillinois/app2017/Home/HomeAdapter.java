package org.hackillinois.app2017.Home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
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
            Typeface gotham_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Gotham-Medium.otf");
            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");

            title.setTypeface(gotham_med);
            hour.setTypeface(gotham_med);
            minute.setTypeface(gotham_med);
            second.setTypeface(gotham_med);
            hour_text.setTypeface(gotham_book);
            minute_text.setTypeface(gotham_book);
            second_text.setTypeface(gotham_book);
            target_time.setTypeface(brandon_med);
            happening_now.setTypeface(gotham_med);
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_event_title) TextView title;
        @BindView(R.id.event_location_container) LinearLayout locationContainer;
        private ArrayList<String> locationLong = new ArrayList<>();
        @BindView(R.id.home_event_time) TextView time;
        @BindView(R.id.home_event_qr_button) TextView qrButton;

        public EventViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            Typeface brandon_med = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_med.otf");
            Typeface brandon_reg = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Brandon_reg.otf");

            title.setTypeface(brandon_reg);
            time.setTypeface(brandon_reg);
            qrButton.setTypeface(brandon_med);

            for(int i = 0; i < locationContainer.getChildCount(); i++) {
                ((TextView)locationContainer.getChildAt(i)).setTypeface(brandon_med);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), EventActivity.class);
                    i.putExtra("title", title.getText());
                    i.putStringArrayListExtra("location", locationLong);
                    i.putExtra("starttime", time.getText());

                    v.getContext().startActivity(i);
                }
            });

            qrButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showFullScreenQRCode(v.getContext());
                }
            });
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
        } else { //(viewType == 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_event_layout, parent, false);
            return new EventViewHolder(view);
        }
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
                final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                final HomeTime targetTime = (HomeTime) homeEvents.get(position);
                timeViewHolder.title.setText(targetTime.getTitle());

                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        timeViewHolder.target_time.setText(targetTime.getTime());
                        timeViewHolder.hour.setText(String.format(Locale.US, "%01d", targetTime.getHours()));
                        timeViewHolder.minute.setText(String.format(Locale.US, "%02d", targetTime.getMinutes()));
                        timeViewHolder.second.setText(String.format(Locale.US, "%02d", targetTime.getSeconds()));
                        handler.postDelayed(this, 1000);
                    }
                };
                runnable.run();
                break;
            default:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                Event homeEvent = (Event) homeEvents.get(position);
                eventViewHolder.title.setText(homeEvent.getName());
                if(eventViewHolder.locationContainer.getChildCount() == 0) {
                    for(EventLocation e : homeEvent.getLocation()) {
                        TextView textView = Utils.generateLocationTextView(holder.itemView.getContext(),e.getShortName());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.weight = 1;
                        textView.setLayoutParams(layoutParams);
                        textView.setTextSize(18);
                        eventViewHolder.locationContainer.addView(textView);
                    }
                }
                eventViewHolder.time.setText(homeEvent.getStartTime());
                if(!homeEvent.needsQRCode()) {
                    eventViewHolder.qrButton.setVisibility(View.GONE);
                }
                for(EventLocation e : homeEvent.getLocation()) {
                    eventViewHolder.locationLong.add(e.getName());
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return homeEvents.size();
    }
}
