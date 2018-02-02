package org.hackillinois.android.ui.modules.event;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventInfoDialog extends Dialog {
	private EventResponse.Event event;
	@BindView(R.id.event_name) TextView eventName;
	@BindView(R.id.event_location) TextView eventLocation;
	@BindView(R.id.event_distance) TextView eventDistance;
	@BindView(R.id.event_description) TextView eventDescription;

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
		eventLocation.setText(String.valueOf(event.getLocations().get(0).getLocationId()));
		eventDistance.setText("Building is far");
		eventDescription.setText(event.getDescription());
	}

	@OnClick(R.id.event_info_close)
	public void dismiss() {
		super.dismiss();
	}

	@OnClick(R.id.event_star)
	public void favoriteEvent() {

	}
}
