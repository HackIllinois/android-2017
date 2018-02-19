package org.hackillinois.android.ui.modules.event;

import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class EventButtonItem extends AbstractFlexibleItem<EventButtonItem.EventButtonViewHolder> {
	private static Gson gson = new Gson();
	private final EventResponse.Location location;

	public EventButtonItem(EventResponse.Location location) {
		this.location = location;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int getLayoutRes() {
		return R.layout.dialog_event_info_button;
	}

	@Override
	public EventButtonViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
		return new EventButtonItem.EventButtonViewHolder(view, adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, EventButtonViewHolder holder, int position, List<Object> payloads) {
		LocationResponse locations = gson.fromJson(Settings.get().getLocations(),
				LocationResponse.class);

		LocationResponse.Location[] locs = locations.getLocations();

		for (LocationResponse.Location loc : locs) {
			if (loc.getId() == location.getLocationId()) {
				holder.buttonName.setText(loc.getName());
				break;
			}
		}
	}

	public EventResponse.Location getLocation() {
		return location;
	}

	public static class EventButtonViewHolder extends FlexibleViewHolder {
		@BindView(R.id.event_dialog_button) Button buttonName;

		public EventButtonViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter);
			ButterKnife.bind(this, view);
		}

	}
}
