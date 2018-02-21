package org.hackillinois.android.api.response.qrcode;

import com.google.gson.annotations.SerializedName;

public class TrackingResponse {

	@SerializedName("meta") private String meta;
	@SerializedName("error") private Error error;

	public String getMeta() {
		return meta;
	}

	public Error getError() {
		return error;
	}
}
