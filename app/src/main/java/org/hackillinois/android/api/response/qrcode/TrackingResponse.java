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

    public static class Error {

        @SerializedName("type") private String type;
        @SerializedName("status") private int status;
        @SerializedName("title") private String title;
        @SerializedName("message") private String message;
        @SerializedName("source") private String source;

        public String getType() {
            return type;
        }

        public int getStatus() {
            return status;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getSource() {
            return source;
        }
    }
}
