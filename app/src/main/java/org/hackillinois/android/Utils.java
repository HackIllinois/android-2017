package org.hackillinois.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.view.WindowManager;

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
}
