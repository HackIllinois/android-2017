package org.hackillinois.android.ui.modules.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.qrcode.TrackingResponse;
import org.hackillinois.android.api.response.user.UserResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;
import org.hackillinois.android.ui.base.BaseFragment;
import org.hackillinois.android.ui.modules.login.LoginChooserActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProfileFragment extends BaseFragment {
	@BindView(R.id.qr_code) ImageView qrCodeImage;
	@BindView(R.id.user_name) TextView userName;
	@BindView(R.id.dietary_restrictions) TextView userDietaryRestrictions;
	@BindView(R.id.profile_page_container) View view;
	@BindView(R.id.event_checkin) Button checkInButton;

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result == null) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		if (result.getContents() == null) {
			Toast.makeText(getContext(), "Scan failed", Toast.LENGTH_SHORT).show();
			return;
		}

		Uri userBadge = Uri.parse(result.getContents());
		String textId = userBadge.getQueryParameter("id");
		String identifier = userBadge.getQueryParameter("identifier");
		if (textId == null) {
			Toast.makeText(getContext(), "Failed to scan any id", Toast.LENGTH_SHORT).show();
		}

		int id = Integer.valueOf(textId);

		getApi().getTracking(Settings.get().getAuthString(), id)
				.enqueue(new Callback<TrackingResponse>() {
					@Override
					public void onResponse(Call<TrackingResponse> call, Response<TrackingResponse> response) {
						if (response != null && response.isSuccessful()) {
							TrackingResponse track = response.body();
							if (track.getError() == null) {
								Toast.makeText(getContext(), identifier + " successfully checked in!", Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getContext(), "Failed to check in " + identifier, Toast.LENGTH_LONG).show();
								Timber.d("Failed to check in User %s", track.getError().getMessage());
							}
						} else if (response.code() == 400) {
							Toast.makeText(getContext(), "Already checked in " + identifier, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onFailure(Call<TrackingResponse> call, Throwable t) {
						Timber.w(t, "Failed to finish request to check in %s(%d)", identifier, id);
					}
				});
	}

	private void logOut() {
		Timber.i("Logging out current user!");
		Settings.get().clear(getContext());
		Intent i = new Intent(getContext(), LoginChooserActivity.class);
		startActivity(i);
		getActivity().finish();
	}

	private void setupQrCode() {
		String auth = Settings.get().getAuthString();

		getApi().getUserInfo(auth).enqueue(new Callback<UserResponse>() {
			@Override
			public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
				if (response.body() != null) {
					UserResponse.User data = response.body().getUserResponseData().getUser();

					// Lets staff or admin check in people
					List<UserResponse.Roles> roles = response.body().getUserResponseData().getRoles();
					checkInButton.setVisibility(View.INVISIBLE);
					for (UserResponse.Roles role : roles) {
						if (role.getRole().equals("ADMIN") || role.getRole().equals("STAFF") || role.getRole().equals("VOLUNTEER")) {
							checkInButton.setVisibility(View.VISIBLE);
						}
					}

					Bitmap qrCode = Utils.getQRCodeBitmap(getContext(), data.getId(), data.getEmail(), 512);
					qrCodeImage.setImageBitmap(qrCode);
					userName.setText(data.getEmail()); // todo actually get name
					userDietaryRestrictions.setText("Unknown dietary restrictions");
				} else {
					Toast.makeText(getContext(), "Couldn't load user info. Try again!", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Call<UserResponse> call, Throwable t) {
				Toast.makeText(getContext(), "Couldn't load user info. Try again!", Toast.LENGTH_LONG).show();
			}
		});
	}

	@OnClick(R.id.event_checkin)
	public void startScan() {
		IntentIntegrator.forSupportFragment(this).initiateScan();
	}
}
