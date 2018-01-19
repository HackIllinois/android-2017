package org.hackillinois.android.api;

public class ApiEndpoints {
	private static final String SERVER_ADDRESS = "http://ec2-54-227-68-47.compute-1.amazonaws.com";

	public static final String AUTH = SERVER_ADDRESS + "/v1/auth";
	public static final String REGISTRATION = SERVER_ADDRESS + "/v1/registration/attendee";
	public static final String EVENTS = SERVER_ADDRESS + "/v1/eventsapi";
	public static final String ANNOUNCEMENTS = SERVER_ADDRESS + "/v1/announcement/all";
	public static final String RESUME = SERVER_ADDRESS + "/v1/upload/resume/";
}
