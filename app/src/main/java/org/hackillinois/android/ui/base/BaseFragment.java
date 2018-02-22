package org.hackillinois.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.hackillinois.android.App;
import org.hackillinois.android.api.HackIllinoisAPI;

public class BaseFragment extends Fragment {
	private HackIllinoisAPI api;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = ((App) getActivity().getApplication()).getApi();
	}

	public HackIllinoisAPI getApi() {
		return api;
	}
}
