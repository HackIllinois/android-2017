package org.hackillinois.android.api;

import org.hackillinois.android.api.response.login.LoginRequest;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.api.response.user.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HackIllinoisAPI {
	@POST("/v1/auth")
	Call<LoginResponse> verifyUser(@Body LoginRequest request);

	@GET("/v1/registration/attendee")
	Call<UserInfoResponse> getUserInfo(@Header("Authorization") String auth);

	@GET("/v1/auth/github")
	Call<LoginResponse> verifyUser(@Query("code") String code);


	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(ApiEndpoints.SERVER_ADDRESS)
			.addConverterFactory(GsonConverterFactory.create())
			.build();

	HackIllinoisAPI api = retrofit.create(HackIllinoisAPI.class);
}
