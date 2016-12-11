package org.hackillinois.app2017.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */

public abstract class Constants {

    //Values are arbitrary for now
    public static final int FOOD_CATEGORY = 50;
    public static final int TRANSPORTATION_CATEGORY = 60;
    public static final int HACKING_CATEGORY = 70;
    public static final int PRIZE_CATEGORY = 80;
    public static final int ALL_CATEGORY = 40;

    public static final Map<String, Integer> filterMap;
    static {
        Map<String, Integer> mMap = new HashMap<>();
        mMap.put("FOOD", FOOD_CATEGORY);
        mMap.put("TRANSPORTATION", TRANSPORTATION_CATEGORY);
        mMap.put("HACKING", HACKING_CATEGORY);
        mMap.put("PRIZES", PRIZE_CATEGORY);
        mMap.put("ALL", ALL_CATEGORY);
        filterMap = Collections.unmodifiableMap(mMap);
    }

}
