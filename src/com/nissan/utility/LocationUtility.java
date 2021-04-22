package com.nissan.utility;

import java.util.Calendar;

import com.nissan.nissancar.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtility {
	
	public static Location getCurrentLocation(Activity activity){
		
		final LocationManager mLocationManager = (LocationManager)  activity.getSystemService(Context.LOCATION_SERVICE);

		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
		    // Do something with the recent location fix
		    //  otherwise wait for the update below
		}
		else {
		    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener(){

				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
			            android.util.Log.v("ianTest", location.getLatitude() + " and " + location.getLongitude());
			            mLocationManager.removeUpdates(this);
					}
				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}
		    	
		    });
				
		}
		return location;
	}
	
	public static float getDistance(float longitude, float latitude){
		if(MainActivity.currentLocation != null){
			Location toWhere = new Location("To where");
			toWhere.setLongitude(longitude);
			toWhere.setLatitude(latitude);
			return MainActivity.currentLocation.distanceTo(toWhere);
		} else {
			return 0.0f;
		}
		
	}

}
