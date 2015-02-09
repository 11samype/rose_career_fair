package edu.rosehulman.rosecareerfair;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class Company1 implements Parcelable {
	
	private String mName;
	private String mBio;
	private String mLogo; // assuming string is url for now, could use Image object but would use more space
	private boolean mFavorite;
	private String[] mMajors;
	private boolean[] mWorkType;
	
	//public static final String[] MAJORS =  {"CSSE", "CPE", "ME"};
	
	public Company1() {
		setName("Company");
		setBio("Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio BioBio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio Bio");
		setLogo("URL");
		setFavorite(false);
		setMajors(new String[] {"CS", "SE", "ME"});
		setWorkType(new boolean[] {true, false}); // [0] - intern, [1] - full time
	}
	
	public Company1(String name, String bio, String logo, boolean favorite, String[] majors, boolean[] workType) {
		setName(name);
		setBio(bio);
		setLogo(logo);
		setFavorite(favorite);
		setMajors(majors);
		setWorkType(workType);
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getBio() {
		return mBio;
	}

	public void setBio(String mBio) {
		this.mBio = mBio;
	}

	public String getLogo() {
		return mLogo;
	}

	public void setLogo(String mLogo) {
		this.mLogo = mLogo;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void setFavorite(boolean mFavorite) {
		this.mFavorite = mFavorite;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(this.mName);
		dest.writeString(this.mBio);
		dest.writeString(this.mLogo);
		dest.writeString(String.valueOf(this.mFavorite));
		dest.writeStringArray(this.mMajors);
		dest.writeBooleanArray(this.mWorkType);
		
	}
	
	public static final Parcelable.Creator<Company1> CREATOR = new Parcelable.Creator<Company1>() {

		@Override
		public Company1 createFromParcel(Parcel source) {
			return new Company1(source);
		}

		@Override
		public Company1[] newArray(int size) {
			return new Company1[size];
		}
		
	};
	
	private Company1(Parcel in) {
		
		this.mName = in.readString();
		this.mBio = in.readString();
		this.mLogo = in.readString();
		this.mFavorite = Boolean.parseBoolean(in.readString());
		this.mMajors = in.createStringArray();
		this.mWorkType = in.createBooleanArray();
		
	}

	public String[] getMajors() {
		return mMajors;
	}

	public void setMajors(String[] mMajors) {
		this.mMajors = mMajors;
	}

	public boolean[] getWorkType() {
		return mWorkType;
	}

	public void setWorkType(boolean[] mWorkType) {
		this.mWorkType = mWorkType;
	}
	
}
