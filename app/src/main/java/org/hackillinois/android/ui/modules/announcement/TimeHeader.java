package org.hackillinois.android.ui.modules.announcement;

import android.view.View;
import android.widget.TextView;

import org.hackillinois.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class TimeHeader extends AbstractHeaderItem<TimeHeader.TimeViewHolder> {
	private final String timeSection;

	public TimeHeader(String timeSection) {
		this.timeSection = timeSection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimeHeader that = (TimeHeader) o;

		return timeSection.equals(that.timeSection);
	}

	@Override
	public int hashCode() {
		return timeSection.hashCode();
	}

	@Override
	public int getLayoutRes() {
		return R.layout.time_header;
	}

	@Override
	public TimeHeader.TimeViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
		return new TimeViewHolder(view, adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, TimeHeader.TimeViewHolder holder, int position, List payloads) {
		holder.time.setText(timeSection);
	}

	public static class TimeViewHolder extends FlexibleViewHolder {
		@BindView(R.id.section_time) TextView time;

		public TimeViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter);
			ButterKnife.bind(this, view);
		}
	}
}
