package org.hackillinois.android.ui.modules.recruiter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.recruiter.RecruiterInterestRequestNew;
import org.hackillinois.android.api.response.recruiter.RecruiterInterestResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecruiterToolsActivity extends BaseActivity {
	@BindView(R.id.user_id) AppCompatEditText userId;
	@BindView(R.id.comment) AppCompatEditText comment;
	@BindView(R.id.favorite_user) SwitchCompat favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruiter_tools);
		ButterKnife.bind(this);
		enableDebug();

		userId.setKeyListener(null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result == null) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		if (result.getContents() == null) {
			Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
			return;
		}

		Uri userBadge = Uri.parse(result.getContents());
		String textId = userBadge.getQueryParameter("id");
		String identifier = userBadge.getQueryParameter("identifier");
		Timber.d("Scanning in user %s as %s", textId, identifier);
		if (textId == null) {
			Toast.makeText(this, "Failed to scan any id", Toast.LENGTH_SHORT).show();
			return;
		}

		userId.setText(textId);
	}

	@OnClick(R.id.submit_application)
	public void submitApplication() {
		if (userId.getText().length() == 0) {
			Toast.makeText(this, "Please Scan an Applicant first.", Toast.LENGTH_SHORT).show();
			return;
		}

		long id = Long.valueOf(userId.getText().toString());
		String userComment = comment.getText().toString();
		boolean isFavorite = favorite.isChecked();
		RecruiterInterestRequestNew request = new RecruiterInterestRequestNew(id, userComment, isFavorite);

		getApi().addInterest(Settings.get().getAuthString(), request).enqueue(new Callback<RecruiterInterestResponse>() {
			@Override
			public void onResponse(Call<RecruiterInterestResponse> call, Response<RecruiterInterestResponse> response) {
				if (response != null && response.isSuccessful()) {
					userId.setText(null);
					comment.setText(null);
					favorite.setChecked(false);
					Toast.makeText(getApplicationContext(), "Successfully created application!", Toast.LENGTH_SHORT).show();
				}

				reportIfError(response.errorBody());
			}

			@Override
			public void onFailure(Call<RecruiterInterestResponse> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "Sorry, something went wrong with that request.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnClick(R.id.start_new_application)
	public void startNewApplication() {
		new IntentIntegrator(this).initiateScan();
	}
}
