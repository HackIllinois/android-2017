package org.hackillinois.android.item;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementItem extends AbstractItem<AnnouncementItem, AnnouncementItem.AnnouncementViewHolder> {
	private final AnnouncementResponse.Announcement announcement;

	public AnnouncementItem(AnnouncementResponse.Announcement announcement) {
		this.announcement = announcement;
	}

	@NonNull
	@Override
	public AnnouncementViewHolder getViewHolder(View v) {
		return new AnnouncementViewHolder(v);
	}

	@Override
	public int getType() {
		return R.id.announcement_item;
	}

	@Override
	public int getLayoutRes() {
		return R.layout.announcement_list_item;
	}

	public AnnouncementResponse.Announcement getAnnouncement() {
		return announcement;
	}

	public static class AnnouncementViewHolder extends FastAdapter.ViewHolder<AnnouncementItem> {
		private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("h:mm a");
		@BindView(R.id.notificationType) TextView notificationType;
		@BindView(R.id.notificationTime) TextView notificationTime;
		@BindView(R.id.notificationDescription) TextView notificationDescription;

		public AnnouncementViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

		@Override
		public void bindView(AnnouncementItem item, List<Object> payloads) {
			notificationType.setText(R.string.announcement);
			notificationTime.setText(DTF.print(item.getAnnouncement().getCreated()));
			notificationDescription.setText(item.getAnnouncement().getDescription());
		}

		@Override
		public void unbindView(AnnouncementItem item) {
			notificationType.setText(null);
			notificationTime.setText(null);
			notificationDescription.setText(null);
		}
	}
}
