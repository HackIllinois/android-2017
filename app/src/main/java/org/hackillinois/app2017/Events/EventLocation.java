package org.hackillinois.app2017.Events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2/21/2017.
 */

public class EventLocation {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("locationId")
    private int locationId;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    public EventLocation(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        Locations l = Locations.from(locationId);
        switch (l) {
            case DCL:
                return "Digital Computer Laboratory";
            case Siebel:
                return "Thomas Siebel Center";
            case ECEB:
                return "Electrical Computer Engineering Building";
            case Union:
                return "Illini Union";
            case KennyGym:
                return "Kenny Gym Annex";
        }
        return null;
    }

    public String getShortName() {
        Locations l = Locations.from(locationId);
        switch (l) {
            case DCL:
                return "DCL";
            case Siebel:
                return "Siebel";
            case ECEB:
                return "ECEB";
            case Union:
                return "Union";
            case KennyGym:
                return "Kenny Gym";
        }
        return null;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public enum Locations {
        DCL(1),
        Siebel(2),
        ECEB(3),
        Union(4),
        KennyGym(5);

        private int id;
        Locations(int i) {
            id = i;
        }

        public static Locations from(int i) {
            for(Locations l : Locations.values()) {
                if(l.id == i) {
                    return l;
                }
            }
            return null;
        }
    }
}
