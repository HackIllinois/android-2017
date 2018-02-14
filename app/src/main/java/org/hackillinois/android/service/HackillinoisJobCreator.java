package org.hackillinois.android.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class HackillinoisJobCreator implements JobCreator {

	@Nullable
	@Override
	public Job create(@NonNull String tag) {
		if (tag.contains(EventNotifierJob.TAG)) {
			return new EventNotifierJob();
		}

		return null;
	}
}
