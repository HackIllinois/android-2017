package org.hackillinois.android.api.response.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendeeResponse {
	@SerializedName("meta") private String meta;
	@SerializedName("data") private UserInfoData userInfoData;

	public String getMeta() {
		return meta;
	}

	public UserInfoData getUserInfoData() {
		return userInfoData;
	}

	public static class Oscontributors {
		@SerializedName("osContributor") private String oscontributor;
		@SerializedName("attendeeId") private int attendeeid;
		@SerializedName("id") private int id;

		public String getOscontributor() {
			return oscontributor;
		}

		public int getAttendeeid() {
			return attendeeid;
		}

		public int getId() {
			return id;
		}
	}

	public static class Longform {
		@SerializedName("info") private String info;
		@SerializedName("attendeeId") private int attendeeid;
		@SerializedName("id") private int id;

		public String getInfo() {
			return info;
		}

		public int getAttendeeid() {
			return attendeeid;
		}

		public int getId() {
			return id;
		}
	}

	public static class Extrainfo {
		@SerializedName("website") private String website;
		@SerializedName("attendeeId") private int attendeeid;
		@SerializedName("id") private int id;

		public String getWebsite() {
			return website;
		}

		public int getAttendeeid() {
			return attendeeid;
		}

		public int getId() {
			return id;
		}
	}

	public static class Collaborators {
		@SerializedName("collaborator") private String collaborator;
		@SerializedName("attendeeId") private int attendeeid;
		@SerializedName("id") private int id;

		public String getCollaborator() {
			return collaborator;
		}

		public int getAttendeeid() {
			return attendeeid;
		}

		public int getId() {
			return id;
		}
	}

	public static class UserInfoData {
		@SerializedName("firstName") private String firstname;
		@SerializedName("lastName") private String lastname;
		@SerializedName("shirtSize") private String shirtsize;
		@SerializedName("diet") private String diet;
		@SerializedName("age") private int age;
		@SerializedName("graduationYear") private int graduationyear;
		@SerializedName("transportation") private String transportation;
		@SerializedName("school") private String school;
		@SerializedName("major") private String major;
		@SerializedName("gender") private String gender;
		@SerializedName("professionalInterest") private String professionalinterest;
		@SerializedName("github") private String github;
		@SerializedName("linkedin") private String linkedin;
		@SerializedName("interests") private String interests;
		@SerializedName("isNovice") private boolean isnovice;
		@SerializedName("isPrivate") private boolean isprivate;
		@SerializedName("hasLightningInterest") private boolean haslightninginterest;
		@SerializedName("phoneNumber") private String phonenumber;
		@SerializedName("userId") private int userid;
		@SerializedName("id") private int id;
		@SerializedName("osContributors") private List<Oscontributors> oscontributors;
		@SerializedName("longForm") private List<Longform> longform;
		@SerializedName("extraInfo") private List<Extrainfo> extrainfo;
		@SerializedName("collaborators") private List<Collaborators> collaborators;

		public String getFirstname() {
			return firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public String getShirtsize() {
			return shirtsize;
		}

		public String getDiet() {
			return diet;
		}

		public int getAge() {
			return age;
		}

		public int getGraduationyear() {
			return graduationyear;
		}

		public String getTransportation() {
			return transportation;
		}

		public String getSchool() {
			return school;
		}

		public String getMajor() {
			return major;
		}

		public String getGender() {
			return gender;
		}

		public String getProfessionalinterest() {
			return professionalinterest;
		}

		public String getGithub() {
			return github;
		}

		public String getLinkedin() {
			return linkedin;
		}

		public String getInterests() {
			return interests;
		}

		public boolean getIsnovice() {
			return isnovice;
		}

		public boolean getIsprivate() {
			return isprivate;
		}

		public boolean getHaslightninginterest() {
			return haslightninginterest;
		}

		public String getPhonenumber() {
			return phonenumber;
		}

		public int getUserid() {
			return userid;
		}

		public int getId() {
			return id;
		}

		public List<Oscontributors> getOscontributors() {
			return oscontributors;
		}

		public List<Longform> getLongform() {
			return longform;
		}

		public List<Extrainfo> getExtrainfo() {
			return extrainfo;
		}

		public List<Collaborators> getCollaborators() {
			return collaborators;
		}
	}
}
