package org.hackillinois.app2017.Map;

/**
 * @author dl-eric
 */

public class Header {
    private String name;
    private String distance;
    private String time;
    private String endAddress;

    public Header(String name, String distance, String time, String endAddress) {
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.endAddress = endAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndAddress() {
        return endAddress;
    }
}
