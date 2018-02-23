package org.hackillinois.android.ui.modules.recruiter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.recruiter.RecruiterInterest;
import org.hackillinois.android.api.response.recruiter.RecruiterInterestMultipleResponse;
import org.hackillinois.android.api.response.recruiter.RecruiterInterestRequestNew;
import org.hackillinois.android.api.response.recruiter.RecruiterInterestSingleResponse;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.ui.base.BaseActivity;

import java.util.List;

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

	private boolean existingApplicant = false;
	private long applicationId;
	private final Callback<RecruiterInterestSingleResponse> handleInterest = new Callback<RecruiterInterestSingleResponse>() {
		@Override
		public void onResponse(Call<RecruiterInterestSingleResponse> call, Response<RecruiterInterestSingleResponse> response) {
			if (response != null && response.isSuccessful()) {
				String updateText;
				if (existingApplicant) {
					updateText = "Successfully updated application!";
				} else {
					updateText = "Successfully created application!";
				}
				Toast.makeText(getApplicationContext(), updateText, Toast.LENGTH_SHORT).show();
			}

			clearApplication();
			reportIfError(response.errorBody());
		}

		@Override
		public void onFailure(Call<RecruiterInterestSingleResponse> call, Throwable t) {
			Toast.makeText(getApplicationContext(), "Sorry, something went wrong with that request.", Toast.LENGTH_SHORT).show();
			Timber.w(t, "Failed to handle recruiter interest response");
			clearApplication();
		}
	};

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
		findCurrentApplication(Integer.valueOf(textId));
	}

	private void findCurrentApplication(int id) {
		getApi().getInterests(Settings.get().getAuthString()).enqueue(new Callback<RecruiterInterestMultipleResponse>() {
			@Override
			public void onResponse(Call<RecruiterInterestMultipleResponse> call, Response<RecruiterInterestMultipleResponse> response) {
				if (response != null && response.isSuccessful()) {
					RecruiterInterestMultipleResponse interest = response.body();
					List<RecruiterInterest> matchingApplications = Stream.of(interest.getInterests())
							.filter(app -> app.getAttendeeId() == id)
							.toList();

					if (matchingApplications.size() == 1) {
						RecruiterInterest recruiterInterest = matchingApplications.get(0);
						Toast.makeText(getApplicationContext(), "Resuming existing application!", Toast.LENGTH_SHORT).show();
						comment.setText(recruiterInterest.getComments());
						favorite.setChecked(recruiterInterest.getFavorite());
						applicationId = recruiterInterest.getAppId();
						existingApplicant = true;
					}
				}

				reportIfError(response.errorBody());
			}

			@Override
			public void onFailure(Call<RecruiterInterestMultipleResponse> call, Throwable t) {
				Timber.w(t, "Failed to fetch all recruiter interests");
			}
		});
	}

	@OnClick(R.id.submit_application)
	public void submitApplication() {
		if (userId.getText().length() == 0) {
			Toast.makeText(this, "Please scan an applicant first.", Toast.LENGTH_SHORT).show();
			return;
		}

		long id = Long.valueOf(userId.getText().toString());
		String userComment = comment.getText().toString();
		boolean isFavorite = favorite.isChecked();
		RecruiterInterestRequestNew request = new RecruiterInterestRequestNew(id, userComment, isFavorite);

		if (existingApplicant) {
			getApi().putInterest(Settings.get().getAuthString(), applicationId, request).enqueue(handleInterest);
		} else {
			getApi().addInterest(Settings.get().getAuthString(), request).enqueue(handleInterest);
		}
	}

	@OnClick(R.id.cancel_application)
	public void clearApplication() {
		userId.setText(null);
		comment.setText(null);
		favorite.setChecked(false);
		existingApplicant = false;
		applicationId = -1;
	}

	@OnClick(R.id.start_new_application)
	public void startNewApplication() {
		new IntentIntegrator(this).initiateScan();
	}
}
