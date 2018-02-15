package org.hackillinois.android.ui.modules.event;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventButtonItem extends AbstractItem<EventButtonItem, EventButtonItem.EventButtonViewHolder> {
    private static Gson gson = new Gson();
    private final EventResponse.Location location;

    public EventButtonItem(EventResponse.Location location) {
        this.location = location;
    }

    @Override
    public int getType() {
        return R.id.event_dialog_button;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.dialog_event_info_button;
    }

    @NonNull
    @Override
    public EventButtonItem.EventButtonViewHolder getViewHolder(View v) {
        return new EventButtonItem.EventButtonViewHolder(v);
    }

    public EventResponse.Location getLocation() {
        return location;
    }

    public static class EventButtonViewHolder extends FastAdapter.ViewHolder<EventButtonItem> {
        @BindView(R.id.event_dialog_button) Button buttonName;

        public EventButtonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }



        @Override
        public void bindView(EventButtonItem item, List<Object> payloads) {
            LocationResponse locations = gson.fromJson(Settings.get().getLocations(),
                    LocationResponse.class);

            LocationResponse.Location[] locs = locations.getLocations();

            for (LocationResponse.Location loc : locs) {
                if (loc.getId() == item.getLocation().getLocationId()) {
                    buttonName.setText(loc.getName());
                    break;
                }
            }
        }

        @Override
        public void unbindView(EventButtonItem item) {
            buttonName.setText(null);
        }
    }
}
