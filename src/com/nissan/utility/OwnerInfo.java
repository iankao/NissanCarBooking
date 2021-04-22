package com.nissan.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OwnerInfo {
	
	static final String TAG = "OwnerInfo";
	
	public static String securityId;
	public static String name;
	public static String email;
	public static String mobile;
	public static String gender;
	public static String birthdate;
	public static boolean edmSubscribed;
	public static JSONArray ownedCars;
	public static JSONArray preferredLocationIds;
	
	public static String[] ownedCars_plateNo;
	public static String[] ownedCars_modelName;
	public static String[] servicedCitiesId;
	public static String[] servicedCitiesName;
	public static String[] servicedDistName;
	public static JSONArray[] servicedLocations;
	public static String[] servicedLocationId;
	public static String[] servicedLocationName;
	public static JSONArray currentLocation;
	public static String[] availableDate;
	public static String[] availableDateWeekday;
	/**
	 * 用"iso":"\"2015-05-23T00:00:00\"" 表示的 date
	 */
	public static String[] availableDateIso;
	public static String[] availableTime;
	public static String[] availableAdvisors;
	public static String[] availableAdvisorsCode;
	public static String[] availableMechanics;
	public static String[] availableMechanicsCode;
	public static JSONArray maintanenceHistoryArray;

	public static void feedOwnerInfo(JSONObject obj) throws JSONException{
		
		securityId = obj.getString("securityId");
		name = obj.getString("name");
		email = obj.getString("email");
		mobile = obj.getString("mobile");
		gender = obj.getString("gender");
		birthdate = obj.getJSONObject("birthdate").getString("dateDisplay");
		edmSubscribed = obj.getBoolean("edmSubscribed");
		preferredLocationIds = obj.getJSONArray("preferredLocationIds");
		
		if(FuncPreference.DEBUG){
			android.util.Log.d(TAG, "securityId::" + securityId);
			android.util.Log.d(TAG, "name::" + name);
			android.util.Log.d(TAG, "email::" + email);
			android.util.Log.d(TAG, "mobile::" + mobile);
			android.util.Log.d(TAG, "gender::" + gender);
			android.util.Log.d(TAG, "birthdate::" + birthdate);
			android.util.Log.d(TAG, "edmSubscribed::" + edmSubscribed);
			android.util.Log.d(TAG, "preferredLocationIds::" + preferredLocationIds);
		}
		
	}
	/**
	 * parse owned cars
	 * 
	 * @param obj
	 * @throws JSONException
	 */
	public static void feedOwnedCars(JSONArray obj) throws JSONException{

		if(FuncPreference.DEBUG){
			android.util.Log.d(TAG, "ownedCars::" + ownedCars);
		}
		
		ownedCars = obj;
		
		if(FuncPreference.DEBUG){
			android.util.Log.d(TAG, "ownedCars::" + ownedCars);
		}
		
	}
}
