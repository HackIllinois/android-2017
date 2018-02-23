package org.hackillinois.android.ui.modules.event;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.modules.announcement.StringHeader;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import timber.log.Timber;

public class EventItem extends AbstractFlexibleItem<EventItem.EventViewHolder>
		implements ISectionable<EventItem.EventViewHolder, StringHeader> {
	private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("h:00 a");


	private final EventResponse.Event event;
	private final StringHeader stringHeader;
	private boolean isStarrable;

	public EventItem(EventResponse.Event event) {
		this(event, false, false);
	}

	public EventItem(EventResponse.Event event, boolean isStarrable, boolean isGroupable) {
		this.event = event;
		this.isStarrable = isStarrable;
		if (isGroupable) {
			stringHeader = new StringHeader(DTF.print(event.getStartTime()));
		} else {
			stringHeader = null;
		}
	}

	@Override
	public int getLayoutRes() {
		return R.layout.event_list_item;
	}

	@Override
	public EventViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
		return new EventViewHolder(view, adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, EventViewHolder holder, int position, List<Object> payloads) {
		holder.eventName.setText(event.getName());
		List<EventResponse.Location> locations = event.getLocations();

		StringBuilder locationText = new StringBuilder();
		HashMap<Long, LocationResponse.Location> locationMap = Settings.get().getLocationMap();

		if (locations.size() > 0) {
			for (int i = 0; i < locations.size() - 1; i++) {
				locationText.append(locationMap.get(locations.get(i).getLocationId()).getName());
				locationText.append(System.lineSeparator());
			}

			locationText.append(locationMap.get(locations.get(locations.size() - 1).getLocationId()).getName());

			holder.eventLocation.setText(locationText.toString());
		} else {
			holder.eventLocation.setText(R.string.no_location);
		}

		if (isStarrable()) {
			Utils.updateEventStarred(holder.eventStar.getContext(), holder.eventStar, event);
			holder.eventStar.setVisibility(View.VISIBLE);
		} else {
			holder.eventStar.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EventItem eventItem = (EventItem) o;

		if (isStarrable() != eventItem.isStarrable()) return false;
		return getEvent().equals(eventItem.getEvent());
	}

	@Override
	public int hashCode() {
		int result = getEvent().hashCode();
		result = 31 * result + (isStarrable() ? 1 : 0);
		return result;
	}

	public EventResponse.Event getEvent() {
		return event;
	}

	public boolean isStarrable() {
		return isStarrable;
	}

	@Override
	public StringHeader getHeader() {
		return stringHeader;
	}

	@Override
	public void setHeader(StringHeader header) {

	}

	public static class EventViewHolder extends FlexibleViewHolder {
		@BindView(R.id.event_star) public ImageView eventStar;
		@BindView(R.id.event_name) public TextView eventName;
		@BindView(R.id.event_location) public TextView eventLocation;


		public EventViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter);
			ButterKnife.bind(this, view);

			eventStar.setOnClickListener(v -> {
				EventItem item = (EventItem) adapter.getItem(getAdapterPosition());
				Utils.toggleEventStarred(eventStar.getContext(), eventStar, item.getEvent());
			});

			view.setOnClickListener(v -> {
				EventItem item = (EventItem) adapter.getItem(getAdapterPosition());
				Timber.d("Clicking event %s", item.getEvent().getName());
				Context context = view.getContext();
				EventInfoDialog dialog = new EventInfoDialog(context, item.getEvent());
				dialog.setOnDismissListener(di -> Utils.updateEventStarred(context, eventStar, item.getEvent()));
				dialog.show();
			});
		}
	}
}
