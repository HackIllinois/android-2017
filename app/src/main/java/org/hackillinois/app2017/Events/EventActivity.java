package org.hackillinois.app2017.Events;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hackillinois.app2017.HackillinoisActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.UI.CenteredToolbar;
import org.hackillinois.app2017.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends HackillinoisActivity {

    @BindView(R.id.toolbar_events) CenteredToolbar toolbar;
    @BindView(R.id.event_title) TextView title;
    @BindView(R.id.event_start_time) TextView startTime;
    @BindView(R.id.event_location_container) LinearLayout locationContainer;
    @BindView(R.id.event_description) TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);

        setUpActionBar(toolbar);

        Typeface brandon_med = Typeface.createFromAsset(getAssets(), "fonts/Brandon_med.otf");

        Bundle bundle = getIntent().getExtras();

        List<String> locations = bundle.getStringArrayList("location");
        if( locations == null) {
            locations = new ArrayList<>();
        }
        for (String location : locations) {
            TextView locationTextView = Utils.generateLocationTextView(getApplicationContext(), location);
            locationTextView.setTypeface(brandon_med);
            locationTextView.setTextSize(18);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart((int) Utils.convertDpToPixel(20, getApplicationContext()));
            locationTextView.setLayoutParams(layoutParams);
            locationTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.faded_blue));
            locationContainer.addView(locationTextView);
        }
        title.setText(bundle.getString("title"));
        startTime.setText(bundle.getString("starttime"));
        description.setText(bundle.getString("description","No description"));
    }

    private void setUpActionBar(CenteredToolbar toolbar) {
        setSupportActionBar(toolbar);
        setTitle("Event Details");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
