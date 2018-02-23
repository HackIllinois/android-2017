package org.hackillinois.android.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.hackillinois.android.App;
import org.hackillinois.android.R;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.service.EventNotifierJob;
import org.hackillinois.android.ui.custom.HackIllinoisRefreshView;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Utils {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = null;
		if (connectivityManager != null) {
			activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		}
		return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
	}

	public static void setFullScreen(Window window) {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(window.getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}

	public static Bitmap getQRCodeBitmap(Context context, int id, String identifier, int size) {
		String qrFormattedString = String.format(Locale.US, "hackillinois://qrcode/user?id=%d&identifier=%s", id, identifier);
		try {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			BitMatrix bitMatrix = barcodeEncoder.encode(qrFormattedString, BarcodeFormat.QR_CODE, size, size);
			return createBitmap(bitMatrix, ContextCompat.getColor(context, R.color.lightPink), Color.TRANSPARENT);
		} catch (WriterException e) {
			Timber.wtf(e, "Failed to generate qrcode bitmap for %s", qrFormattedString);
		}
		return null;
	}

	public static Bitmap createBitmap(BitMatrix matrix, int primary, int background) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = matrix.get(x, y) ? primary : background;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static void attachHackIllinoisRefreshView(RecyclerRefreshLayout swipeRefresh, LayoutInflater inflater) {
		swipeRefresh.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.FLOAT);
		View root = inflater.inflate(R.layout.refresh_view, swipeRefresh);
		HackIllinoisRefreshView refreshView = ButterKnife.findById(root, R.id.refresh_view);
		if (refreshView.getParent() != null)
			((ViewGroup) refreshView.getParent()).removeView(refreshView);

		swipeRefresh.setRefreshView(refreshView, refreshView.getLayoutParams());
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static void toggleEventStarred(Context context, ImageView star, EventResponse.Event event) {
		boolean starred = !Settings.get().getIsEventStarred(event.getId());
		setEventStarred(context, star, event, starred);

		if (starred) {
			TimeUnit timeUnit = TimeUnit.MINUTES;
			long duration = 15;
			EventNotifierJob.scheduleReminder(event, timeUnit.toMillis(duration));
			String infoMessage = context.getString(
					R.string.notified_before_event_starts,
					duration,
					timeUnit.toString().toLowerCase(),
					event.getName()
			);
			Toast.makeText(context, infoMessage, Toast.LENGTH_SHORT).show();
		} else {
			EventNotifierJob.cancelReminder(event);
		}
	}

	public static void updateEventStarred(Context context, ImageView star, EventResponse.Event event) {
		setEventStarred(context, star, event, Settings.get().getIsEventStarred(event.getId()));
	}

	public static void setEventStarred(Context context, ImageView star, EventResponse.Event event, boolean starred) {
		GoogleMaterial.Icon icon;
		if (starred) {
			icon = GoogleMaterial.Icon.gmd_star;
			Settings.get().saveEventIsStarred(event.getId());
		} else {
			icon = GoogleMaterial.Icon.gmd_star_border;
			Settings.get().saveEventIsNotStarred(event.getId());
		}

		Timber.i("Setting event %s id=%d to be starred=%b", event.getName(), event.getId(), starred);

		star.setImageDrawable(
				new IconicsDrawable(context)
						.icon(icon)
						.colorRes(R.color.bluePurple)
						.actionBar()
		);
	}

	public static void goToMapApp(Context context) {
		Settings settings = Settings.get();
		String json = settings.getLocations();

		LocationResponse locations = App.getGson().fromJson(json, LocationResponse.class);

		// Just get the first location's location to open up maps app.
		double longitude = 40.1138; // Default location for ECEB
		double latitude = -88.2249; // Default location for ECEB

		if (locations != null && locations.getLocations() != null && locations.getLocations().length > 0) {
			longitude = locations.getLocations()[0].getLongitude();
			latitude = locations.getLocations()[0].getLatitude();
		}

		launchGoogleMaps(context, longitude, latitude);
	}

	public static void goToMapApp(Context context, double longitude, double latitude) {
		launchGoogleMaps(context, longitude, latitude);
	}

	private static void launchGoogleMaps(Context context, double longitude, double latitude) {
		Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=" + 18);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

		if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
			mapIntent.setPackage("com.google.android.apps.maps");
		}

		context.startActivity(mapIntent);
	}

	public static void fetchLocation(HackIllinoisAPI api) {
		Timber.d("Fetching Locations");
		api.getLocations().enqueue(new Callback<LocationResponse>() {
			@Override
			public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
				if (response != null && response.isSuccessful()) {
					Settings.get().setLocations(App.getGson().toJson(response.body()));
				}
			}

			@Override
			public void onFailure(Call<LocationResponse> call, Throwable t) {
				Timber.d("Failed to fetch location");
			}
		});
	}
}
