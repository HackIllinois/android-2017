package org.hackillinois.app2017.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.hackillinois.app2017.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin on 2/23/2017.
 */

public class IndoorMapViewer extends Activity {
    public static String BUILDING_NAME = "BUILDING_NAME";
    @BindView(R.id.indoor_map_viewer) RelativeLayout indoorMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_indoor_map_viewer);
        ButterKnife.bind(this);

        Buildings building = getBuildingEnum();
        List<ImageView> listOfImages = getBuildingImages(building);

    }

    public static Intent showMap(Activity current, Buildings building) {
        Intent intent = new Intent(current.getApplicationContext(), current.getClass());
        intent.putExtra(BUILDING_NAME,building.toString());
        return intent;
    }

    public static Intent showMap(Fragment current, Buildings building) {
        Intent intent = new Intent(current.getContext(), current.getClass());
        intent.putExtra(BUILDING_NAME,building.toString());
        return intent;
    }

    private List<ImageView> getBuildingImages(Buildings building) {
        List<Integer> ids = new ArrayList<>();
        switch (building) {
            case SIEBEL:
                ids = Arrays.asList(R.drawable.siebel_careerfair, R.drawable.siebel_basement,R.drawable.siebel_firstfloor,R.drawable.siebel_secondfloor,R.drawable.siebel_thirdfloor,R.drawable.siebel_fourthfloor);
                break;
            case ECEB:
                ids = Arrays.asList(R.drawable.eceb_careerfair, R.drawable.eceb_firstfloor,R.drawable.eceb_secondfloor,R.drawable.eceb_thirdfloor,R.drawable.eceb_fourthfloor);
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
