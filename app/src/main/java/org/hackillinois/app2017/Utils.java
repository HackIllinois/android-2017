package org.hackillinois.app2017;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.glxn.qrgen.android.QRCode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.hackillinois.app2017.Utils.HackIllinoisStatus.AFTER;
import static org.hackillinois.app2017.Utils.HackIllinoisStatus.BEFORE;
import static org.hackillinois.app2017.Utils.HackIllinoisStatus.DURING;

/**
 * Created by kevin on 2/21/2017.
 */

public class Utils {
    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String HACKILLINOIS_START = "2017-02-24T16:00:00.000Z";
    public static final String HACKILLINOIS_END = "2017-02-26T17:00:00.000Z";

    public static Bitmap getQRCodeBitmap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        return QRCode.from(sharedPreferences.getString("id", "N/A")).withSize(1024, 1024)
                .withColor(ContextCompat.getColor(context, R.color.seafoam_blue), Color.TRANSPARENT).bitmap();
    }

    //TODO save the image instead of creating it every time
    public static void showFullScreenQRCode(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.dialog_qr_popup, null);
        ImageView qrCode = (ImageView) dialogLayout.findViewById(R.id.qr_popup);
        qrCode.setImageBitmap(getQRCodeBitmap(context));
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.qr_popup);
                Bitmap icon = getQRCodeBitmap(context);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);
                image.setImageBitmap(icon);
            }
        });
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

    public static boolean isBeforeHackIllinois(Date date) {
        Date start = getDateFromAPI(HACKILLINOIS_START);
        return date.before(start);
    }

    public static boolean isDuringHackIllinois(Date date) {
        return !isBeforeHackIllinois(date) && !isAfterHackIllinois(date);
    }

    public static boolean isAfterHackIllinois(Date date) {
        Date end = getDateFromAPI(HACKILLINOIS_END);
        return date.after(end);
    }

    public static HackIllinoisStatus getHackIllinoisStatus() {
        Date date = new Date();
        if(isBeforeHackIllinois(date)) {
            return BEFORE;
        } else if(isDuringHackIllinois(date)) {
            return DURING;
        } else {
            return AFTER;
        }
    }

    public enum HackIllinoisStatus {
        BEFORE,
        DURING,
        AFTER
    }
}
