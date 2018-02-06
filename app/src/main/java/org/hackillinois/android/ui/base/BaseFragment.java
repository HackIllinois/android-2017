package org.hackillinois.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.hackillinois.android.App;
import org.hackillinois.android.api.HackIllinoisAPI;

public class BaseFragment extends Fragment {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public HackIllinoisAPI getApi() {
		return ((App) getActivity().getApplication()).getApi();
	}
}
