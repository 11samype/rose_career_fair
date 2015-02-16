package edu.rosehulman.rosecareerfair;

import java.util.ArrayList;

import android.content.Context;

public class Profile {
	private ArrayList<String> mFavorites = new ArrayList<String>();
	private String mName;
	
	public Profile(String name, String password, ArrayList<String> favorites){
		mFavorites = favorites;
		mName = name;
	
	}
	
	public ArrayList<String> getFavorites(){
		return mFavorites;
	}
	
	public String getName(){
		return mName;
	}
}
