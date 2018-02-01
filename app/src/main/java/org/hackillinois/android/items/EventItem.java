package org.hackillinois.android.items;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventItem extends AbstractItem<EventItem, EventItem.EventViewHolder> {
	private EventResponse.Event event;

	public EventItem(EventResponse.Event event) {
		this.event = event;
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

	public static class EventViewHolder extends FastAdapter.ViewHolder<EventItem> {
		@BindView(R.id.event_name) TextView eventName;
		@BindView(R.id.event_location) TextView eventLocation;

		public EventViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@Override
		public void bindView(EventItem item, List<Object> payloads) {
			eventName.setText(item.getEvent().getName());
			eventLocation.setText("Location id:" + item.getEvent().getLocations().get(0).getLocationId());
		}

		@Override
		public void unbindView(EventItem item) {
			eventName.setText(null);
			eventLocation.setText(null);
		}
	}
}
