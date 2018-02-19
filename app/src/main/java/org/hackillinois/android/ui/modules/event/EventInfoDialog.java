package org.hackillinois.android.ui.modules.event;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import timber.log.Timber;

public class EventInfoDialog extends Dialog {
	private final FlexibleAdapter<EventButtonItem> adapter = new FlexibleAdapter<>(null);

	private EventResponse.Event event;
	@BindView(R.id.event_star) ImageView eventStar;
	@BindView(R.id.event_name) TextView eventName;
	@BindView(R.id.event_description) TextView eventDescription;
	@BindView(R.id.event_buttons) RecyclerView eventButtons;

	public EventInfoDialog(@NonNull Context context, EventResponse.Event event) {
		super(context);
		setContentView(R.layout.dialog_event_info);
		ButterKnife.bind(this);

		setCanceledOnTouchOutside(false);
		Utils.setFullScreen(getWindow());

		setEvent(event);
	}

	public void setEvent(EventResponse.Event event) {
		this.event = event;

		eventName.setText(event.getName());
		List<EventResponse.Location> locations = event.getLocations();
		eventDescription.setText(event.getDescription());
		eventButtons.setAdapter(adapter);
		eventButtons.setLayoutManager(new LinearLayoutManager(getContext()));
		eventButtons.setHasFixedSize(true);

		List<EventButtonItem> eventButtonItems = Stream.of(locations)
				.map(EventButtonItem::new).toList();

		adapter.updateDataSet(eventButtonItems);

		adapter.addListener(new FlexibleAdapter.OnItemClickListener() {
			@Override
			public boolean onItemClick(int position) {
				EventButtonItem item = adapter.getItem(position);

				LocationResponse.Location loc = Settings.get().getLocationMap()
						.get(item.getLocation().getLocationId());

				Utils.goToMapApp(getContext().getApplicationContext(), loc.getLongitude(), loc.getLatitude());
				return false;
			}
		});

		Utils.updateEventStarred(getContext().getApplicationContext(), eventStar, event);
	}

	@OnClick(R.id.event_info_close)
	public void dismiss() {
		super.dismiss();
	}

	@OnClick(R.id.event_star)
	public void favoriteEvent(View v) {
		Utils.toggleEventStarred(getContext().getApplicationContext(), (ImageView) v, event);
		Timber.w("Event \"%s\" Should be starred", event.getName());
	}
}
