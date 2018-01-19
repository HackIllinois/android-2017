package org.hackillinois.android.api.response.user;

import com.google.gson.annotations.SerializedName;

public class UserInfoData {
	@SerializedName("firstName") private String firstName;
	@SerializedName("firstName") private String lastName;
	@SerializedName("firstName") private String diet;
	@SerializedName("firstName") private String age;
	@SerializedName("firstName") private String graduationYear;
	@SerializedName("firstName") private String school;
	@SerializedName("firstName") private String major;
	@SerializedName("firstName") private String github;
	@SerializedName("firstName") private String linkedin;
	@SerializedName("firstName") private ResumeResponse resume;
	@SerializedName("firstName") private String id;

	public UserInfoData(
			String firstName,
			String lastName,
			String diet,
			String age,
			String graduationYear,
			String school,
			String major,
			String github,
			String linkedin,
			ResumeResponse resume,
			String id
	) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.diet = diet;
		this.age = age;
		this.graduationYear = graduationYear;
		this.school = school;
		this.major = major;
		this.github = github;
		this.linkedin = linkedin;
		this.resume = resume;
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDiet() {
		return diet;
	}

	public String getAge() {
		return age;
	}

	public String getGraduationYear() {
		return graduationYear;
	}

	public String getSchool() {
		return school;
	}

	public String getMajor() {
		return major;
	}

	public String getGithub() {
		return github;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public ResumeResponse getResume() {
		return resume;
	}

	public String getId() {
		return id;
	}
}
