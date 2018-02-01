package org.hackillinois.android.api;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.login.LoginRequest;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.api.response.user.AttendeeResponse;
import org.hackillinois.android.api.response.user.UserResponse;
import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HackIllinoisAPI {
	public static final String SERVER_ADDRESS = "http://api.test.hackillinois.org";
	public static final String AUTH = SERVER_ADDRESS + "/v1/auth";
	Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
	HackIllinoisAPI api = new Retrofit.Builder()
			.baseUrl(SERVER_ADDRESS)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
			.create(HackIllinoisAPI.class);

	@POST("/v1/auth")
	Call<LoginResponse> verifyUser(@Body LoginRequest request);

	@GET("/v1/registration/attendee")
	Call<AttendeeResponse> getAttendeeInfo(@Header("Authorization") String auth);

	@GET("/v1/user")
	Call<UserResponse> getUserInfo(@Header("Authorization") String auth);

	@GET("/v1/auth/github")
	Call<LoginResponse> verifyUser(@Query("code") String code);

	@GET("/v1/auth/refresh")
	Call<LoginResponse> refreshUser(@Header("Authorization") String auth);

	@GET("/v1/event/")
	Call<EventResponse> getEvents();

	@GET("/v1/announcement/all")
	Call<AnnouncementResponse> getAnnouncements(
			@Query("before") DateTime beforeDate,
			@Query("after") DateTime afterDate,
			@Query("limit") Integer limit
	);

	@GET("/v1/announcement/all")
	Call<AnnouncementResponse> getAnnouncements();
}
