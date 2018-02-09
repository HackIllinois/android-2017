package org.hackillinois.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.glxn.qrgen.android.QRCode;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;

import java.util.Arrays;
import java.util.Locale;

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
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
	}

	public static Bitmap getQRCodeBitmap(Context context, int id, String identifier) {
		String qrFormattedString = String.format(Locale.US, "hackillinois://qrcode/user?id=%d&identifier=%s", id, identifier);
		return QRCode.from(qrFormattedString)
				.withSize(1024, 1024)
				.withColor(ContextCompat.getColor(context, R.color.darkPurple), Color.TRANSPARENT)
				.bitmap();
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static void toggleEventStarred(ImageView star, EventResponse.Event event) {
		setEventStarred(star, event, !Settings.get().getIsEventStarred(event.getId()));
	}

	public static void updateEventStarred(ImageView star, EventResponse.Event event) {
		setEventStarred(star, event, Settings.get().getIsEventStarred(event.getId()));
	}

	public static void setEventStarred(ImageView star, EventResponse.Event event, boolean starred) {
		GoogleMaterial.Icon icon;
		if (starred) {
			icon = GoogleMaterial.Icon.gmd_star;
		} else {
			icon = GoogleMaterial.Icon.gmd_star_border;
		}

		if (starred) {
			Settings.get().saveEventIsStarred(event.getId());
		} else {
			Settings.get().saveEventIsNotStarred(event.getId());
		}

		Timber.i("Setting event %s id=%d to be starred=%b", event.getName(), event.getId(), starred);

		star.setImageDrawable(
				new IconicsDrawable(star.getContext())
						.icon(icon)
						.colorRes(R.color.bluePurple)
						.actionBar()
		);
	}
}
