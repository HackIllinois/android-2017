package org.hackillinois.app2017.Map;

/**
 * @author dl-eric
 */

public class DirectionObject {
    private String description;
    private String distance;

    public DirectionObject(String description, String distance) {
        this.description = description;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public String getDescription() {
        return description;
    }
}
