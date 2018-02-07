package org.hackillinois.android.ui.modules.event;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;

import java.util.List;

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
		@BindView(R.id.event_star) public ImageView eventStar;
		@BindView(R.id.event_name) public TextView eventName;
		@BindView(R.id.event_location) public TextView eventLocation;

		public EventViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@Override
		public void bindView(EventItem item, List<Object> payloads) {
			eventName.setText(item.getEvent().getName());
			List<EventResponse.Location> locations = item.getEvent().getLocations();
			String location = locations.size() > 0 ? String.valueOf(locations.get(0).getLocationId()) : "unknown";
			eventLocation.setText("Location id:" + location);
			if (item.isStarrable()) {
				eventStar.setVisibility(View.VISIBLE);
			} else {
				eventStar.setVisibility(View.GONE);
			}
		}

		@Override
		public void unbindView(EventItem item) {
			eventName.setText(null);
			eventLocation.setText(null);
		}
	}
}
