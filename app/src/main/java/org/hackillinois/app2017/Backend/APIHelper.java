package org.hackillinois.app2017.Backend;

public class APIHelper {
	private static final String SERVER_ADDRESS = "https://api.hackillinois.org";

	public static final String AUTH_ENDPOINT = SERVER_ADDRESS + "/v1/auth";
	public static final String USER_ENDPOINT = SERVER_ADDRESS + "/v1/registration/attendee";
	public static final String EVENTS_ENDPOINT = SERVER_ADDRESS + "/v1/eventsapi";
	//public static String EVENTS_ENDPOINT = "http://13.90.146.188:8080/v1/events";
	public static final String ANNOUNCEMENTS_ENDPOINT = SERVER_ADDRESS + "/v1/announcement/all";
	public static final String RESUME_ENDPOINT = SERVER_ADDRESS + "/v1/upload/resume/";
}
