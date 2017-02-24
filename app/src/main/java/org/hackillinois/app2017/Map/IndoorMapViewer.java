package org.hackillinois.app2017.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kevin on 2/23/2017.
 */

public class IndoorMapViewer extends Activity {
    public static String BUILDING_NAME = "BUILDING_NAME";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);
        ButterKnife.bind(this);

        Buildings building = getBuildingEnum();
        List<ImageView> listOfImages = getBuildingImages(building);
    }

    public Intent showMap(Activity current, Buildings building) {
        Intent intent = new Intent(current.getApplicationContext(), current.getClass());
        intent.putExtra(BUILDING_NAME,building.toString());
        return intent;
    }

    private List<ImageView> getBuildingImages(Buildings building) {
        List<Integer> ids = new ArrayList<>();
        switch (building) {
            case SIEBEL:
                ids = Arrays.asList();
                break;
            case ECEB:
                ids = Arrays.asList();
                break;
            case DCL:
                ids = Arrays.asList();
                break;
            case UNION:
                ids = Arrays.asList();
                break;
        }
        return getDrawables(ids);
    }

    public List<ImageView> getDrawables(List<Integer> ids) {
        List<ImageView> images = new ArrayList<>();
        for(int i : ids) {
            ImageView temp = new ImageView(this);
            temp.setImageDrawable(ContextCompat.getDrawable(this,i));
        }
        return images;
    }

    public void initializeImageView(ImageView image) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,100,0,100);
        image.setLayoutParams(layoutParams);
    }

    private Buildings getBuildingEnum() {
        String enumAsString = getIntent().getStringExtra(BUILDING_NAME);
        Buildings buildingName = Buildings.SIEBEL;
        if(enumAsString != null) {
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

}
