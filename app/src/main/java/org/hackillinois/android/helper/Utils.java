package org.hackillinois.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import net.glxn.qrgen.android.QRCode;

import org.hackillinois.android.R;

import java.util.Arrays;
import java.util.Locale;

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
}
