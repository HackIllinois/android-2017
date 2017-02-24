package org.hackillinois.app2017;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.hackillinois.app2017.Utils.HackIllinoisStatus.AFTER;
import static org.hackillinois.app2017.Utils.HackIllinoisStatus.BEFORE;
import static org.hackillinois.app2017.Utils.HackIllinoisStatus.DURING;

/**
 * Created by kevin on 2/21/2017.
 */

public class Utils {
    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String HACKILLINOIS_START = "2017-02-24T22:00:00.000Z";
    public static final String HACKILLINOIS_END = "2017-02-26T21:00:00.000Z";

    public static Bitmap getQRCodeBitmap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
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

    public static TextView generateLocationTextView(final Context context, String name) {
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START | View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(context, R.color.seafoam_blue));
        return textView;
    }

    public static LinearLayout generateLocationLinearLayout(final Context context, TextView name) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,2,0,2);
        linearLayout.setLayoutParams(layoutParams);

        ImageView gpsIcon = new ImageView(context);
        gpsIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.gps));
        LinearLayout.LayoutParams gpsMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gpsMargins.gravity = Gravity.CENTER_VERTICAL;
        gpsMargins.setMargins(0,0,15,0);
        gpsIcon.setLayoutParams(gpsMargins);
        linearLayout.addView(gpsIcon);

        linearLayout.addView(name);

        ImageView rightArrow = new ImageView(context);
        rightArrow.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.event_location_arrow));
        LinearLayout.LayoutParams rightArrowMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightArrowMargins.setMargins(15,5,0,0);
        rightArrowMargins.gravity = Gravity.CENTER_VERTICAL;
        rightArrow.setLayoutParams(rightArrowMargins);
        linearLayout.addView(rightArrow);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(MainActivity.BOTTOM_BAR_TAB, 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
        return linearLayout;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Date getDateFromAPI(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringDateAsAPI(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.US);
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
        if (isBeforeHackIllinois(date)) {
            return BEFORE;
        } else if (isDuringHackIllinois(date)) {
            return DURING;
        } else {
            return AFTER;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public enum HackIllinoisStatus {
        BEFORE,
        DURING,
        AFTER
    }
}
