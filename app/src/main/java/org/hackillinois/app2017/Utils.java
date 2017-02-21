package org.hackillinois.app2017;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

/**
 * Created by kevin on 2/21/2017.
 */

public class Utils {
    //TODO save the image instead of creating it every time
    public static void showFullScreenQRCode(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAdjustViewBounds(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        Bitmap bitmap = QRCode.from(sharedPreferences.getString("id","N/A")).withSize(400,400).bitmap();
        imageView.setImageBitmap(bitmap);

        Dialog dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView(imageView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
