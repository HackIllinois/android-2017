package org.hackillinois.app2017.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2/23/2017.
 */

public class IndoorMapViewer extends AppCompatActivity {
    public static String BUILDING_NAME = "BUILDING_NAME";
    @BindView(R.id.indoor_map_viewer)
    LinearLayout indoorMap;
    @BindView(R.id.toolbar_map)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_indoor_map_viewer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.back));

        final Buildings building = getBuildingEnum();
        setTitle(building.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageView> listOfImages = getBuildingImages(building);
                for (ImageView i : listOfImages) {
                    indoorMap.addView(i);
                }
            }
        }).run();
    }

    public static void showMap(Context context, Buildings building) {
        Intent intent = new Intent(context, IndoorMapViewer.class);
        intent.putExtra(BUILDING_NAME, building.toString());
        context.startActivity(intent);
    }

    private List<ImageView> getBuildingImages(Buildings building) {
        List<Integer> ids = new ArrayList<>();
        switch (building) {
            case SIEBEL:
                ids = Arrays.asList(R.drawable.siebel_careerfair, R.drawable.siebel_basement, R.drawable.siebel_firstfloor, R.drawable.siebel_secondfloor, R.drawable.siebel_thirdfloor, R.drawable.siebel_fourthfloor);
                break;
            case ECEB:
                ids = Arrays.asList(R.drawable.eceb_careerfair, R.drawable.eceb_firstfloor, R.drawable.eceb_secondfloor, R.drawable.eceb_thirdfloor, R.drawable.eceb_fourthfloor);
                break;
            case DCL:
                ids = Arrays.asList(R.drawable.dcl_firstfloor);
                break;
            case UNION:
                ids = Arrays.asList(R.drawable.union_firstfloor);
                break;
        }
        return getDrawables(ids);
    }

    public List<ImageView> getDrawables(List<Integer> ids) {
        List<ImageView> images = new ArrayList<>();
        for (int i : ids) {
            ImageView temp = new ImageView(this);
            temp.setImageBitmap(decodeSampledBitmapFromResource(getResources(), i, 512, 512));
            initializeImageView(temp);
            images.add(temp);
        }
        return images;
    }

    public void initializeImageView(ImageView image) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, -250, 0, 0);
        image.setLayoutParams(layoutParams);
    }

    private Buildings getBuildingEnum() {
        String enumAsString = getIntent().getStringExtra(BUILDING_NAME);
        Buildings buildingName = Buildings.SIEBEL;
        if (enumAsString != null) {
            try {
                buildingName = Buildings.valueOf(enumAsString);
            } catch (IllegalArgumentException e) {
                buildingName = Buildings.SIEBEL;
            }
        }
        return buildingName;
    }

    public enum Buildings {
        SIEBEL,
        ECEB,
        DCL,
        UNION
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
