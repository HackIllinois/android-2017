package org.hackillinois.android.ui.modules.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.user.UserResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProfileFragment extends BaseFragment {
	@BindView(R.id.qr_code) ImageView qrCodeImage;
	@BindView(R.id.user_name) TextView userName;
	@BindView(R.id.dietary_restrictions) TextView userDietaryRestrictions;
	@BindView(R.id.profile_refresh) SwipeRefreshLayout swipeRefresh;
	@BindView(R.id.profile_page_container) View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_profile, container, false);
		ButterKnife.bind(this, view);

		swipeRefresh.setOnRefreshListener(this::setupQrCode);
		swipeRefresh.setColorSchemeResources(R.color.lightPink);

		setupQrCode();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.profile_menu, menu);
		menu.findItem(R.id.profile_logout).setIcon(
				new IconicsDrawable(getContext())
						.icon(GoogleMaterial.Icon.gmd_exit_to_app)
						.colorRes(R.color.darkPurple)
						.actionBar()
		);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.profile_logout:
				new AlertDialog.Builder(getContext())
						.setTitle(R.string.do_log_out)
						.setPositiveButton(R.string.yes, (dialogInterface, i) -> logOut())
						.setNegativeButton(R.string.no, null)
						.create()
						.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void logOut() {
		Timber.i("Logging out current user!");
		Settings.get().clear(getContext());
		Intent i = new Intent(getContext(), LoginChooserActivity.class);
		startActivity(i);
		getActivity().finish();
	}

	public void stopRefreshing() {
		if (swipeRefresh != null) {
			swipeRefresh.setRefreshing(false);
		}
	}

	private void setupQrCode() {
		String auth = Settings.get().getAuthString();

		getApi().getUserInfo(auth).enqueue(new Callback<UserResponse>() {
			@Override
			public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
				if (response.body() != null) {
					UserResponse.User data = response.body().getUserResponseData().getUser();

					Bitmap qrCode = Utils.getQRCodeBitmap(getContext(), data.getId(), data.getEmail(), 1024);
					qrCodeImage.setImageBitmap(qrCode);
					userName.setText(data.getEmail()); // todo actually get name
					userDietaryRestrictions.setText("Unknown dietary restrictions");
				} else {
					Toast.makeText(getContext(), "Couldn't load user info. Try again!", Toast.LENGTH_LONG).show();
				}
				stopRefreshing();
			}

			@Override
			public void onFailure(Call<UserResponse> call, Throwable t) {
				Toast.makeText(getContext(), "Couldn't load user info. Try again!", Toast.LENGTH_LONG).show();
				stopRefreshing();
			}
		});
	}
}
