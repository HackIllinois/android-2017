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

public class StringHeader extends AbstractHeaderItem<StringHeader.TimeViewHolder> {
	private final String timeSection;

	public StringHeader(String timeSection) {
		this.timeSection = timeSection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StringHeader that = (StringHeader) o;

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
	public StringHeader.TimeViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
		return new TimeViewHolder(view, adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, StringHeader.TimeViewHolder holder, int position, List payloads) {
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
