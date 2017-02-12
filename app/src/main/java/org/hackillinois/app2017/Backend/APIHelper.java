package org.hackillinois.app2017.Backend;

public class APIHelper {
    private static String serverAddress = "https://api.hackillinois.org";

    public static String authEndpoint = serverAddress + "/v1/auth";
    public static String userEndpoint = serverAddress + "/v1/registration/attendee";
    //public static String eventsEndpoint = serverAddress + "/v1/events";
    public static String eventsEndpoint = "http://13.90.146.188:8080/v1/events";
}
