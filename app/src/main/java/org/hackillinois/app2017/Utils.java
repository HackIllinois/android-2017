package org.hackillinois.app2017;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kevin on 2/21/2017.
 */

public class Utils {
    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    //TODO save the image instead of creating it every time
    public static void showFullScreenQRCode(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAdjustViewBounds(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        Bitmap bitmap = QRCode.from(sharedPreferences.getString("id", "N/A")).withSize(400, 400).bitmap();
        imageView.setImageBitmap(bitmap);

        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(imageView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static Date getDateFromAPI(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.US);
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringDateAsAPI(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT,Locale.US);
        return dateFormat.format(date);
    }
}
