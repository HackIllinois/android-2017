package org.hackillinois.android.ui.modules.event;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventItem extends AbstractItem<EventItem, EventItem.EventViewHolder> {
    private final EventResponse.Event event;
    private boolean isStarrable;

    public EventItem(EventResponse.Event event) {
        this(event, false);
    }

    public EventItem(EventResponse.Event event, boolean isStarrable) {
        this.event = event;
        this.isStarrable = isStarrable;
    }

    @NonNull
    @Override
    public EventViewHolder getViewHolder(View v) {
        return new EventViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.event_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.event_list_item;
    }

    public EventResponse.Event getEvent() {
        return event;
    }

    public boolean isStarrable() {
        return isStarrable;
    }

    public static class EventViewHolder extends FastAdapter.ViewHolder<EventItem> {
        @BindView(R.id.event_star)
        public ImageView eventStar;
        @BindView(R.id.event_name)
        public TextView eventName;
        @BindView(R.id.event_location)
        public TextView eventLocation;

        public EventViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(EventItem item, List<Object> payloads) {
            eventName.setText(item.getEvent().getName());
            List<EventResponse.Location> locations = item.getEvent().getLocations();

            StringBuilder locationText = new StringBuilder();
            HashMap<Long, LocationResponse.Location> locationMap = Settings.get().getLocationMap();

            if (locations.size() > 0) {
                for (int i = 0; i < locations.size() - 1; i++) {
                    locationText.append(locationMap.get(locations.get(i).getLocationId()).getName());
                    locationText.append(", ");
                }

                locationText.append(locationMap.get(locations.get(locations.size() - 1).getLocationId()).getName());

                eventLocation.setText(locationText.toString());
            } else {
                eventLocation.setText(R.string.no_location);
            }

            if (item.isStarrable()) {
                Utils.updateEventStarred(eventStar, item.getEvent());
                eventStar.setVisibility(View.VISIBLE);
            } else {
                eventStar.setVisibility(View.INVISIBLE);
            }
        }


        @Override
        public void unbindView(EventItem item) {
            eventName.setText(null);
            eventLocation.setText(null);
        }
    }
}
