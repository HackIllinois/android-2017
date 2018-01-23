package org.hackillinois.android.api.response.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private UserResponseData userResponseData;

	public String getMeta() {
		return meta;
	}

	public UserResponseData getUserResponseData() {
		return userResponseData;
	}

	public static class User {
		@SerializedName("id") private int id;
		@SerializedName("githubHandle") private String githubHandle;
		@SerializedName("email") private String email;
		@SerializedName("created") private String created;
		@SerializedName("updated") private String updated;

		public int getId() {
			return id;
		}

		public String getGithubHandle() {
			return githubHandle;
		}

		public String getEmail() {
			return email;
		}

		public String getCreated() {
			return created;
		}

		public String getUpdated() {
			return updated;
		}
	}

	public static class Roles {
		@SerializedName("role") private String role;
		@SerializedName("active") private int active;

		public String getRole() {
			return role;
		}

		public int getActive() {
			return active;
		}
	}

	public static class UserResponseData {
		@SerializedName("user") private User user;
		@SerializedName("roles") private List<Roles> roles;

		public User getUser() {
			return user;
		}

		public List<Roles> getRoles() {
			return roles;
		}
	}
}
