package com.nissan.nissancar;

import java.util.Calendar;

import com.nissan.functionfragments.FunctionMoreFragment;
import com.nissan.functionfragments.FunctionSettingFragment;
import com.nissan.functionfragments.MainPageFragment;
import com.nissan.utility.HttpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {
    
    public static int screenWidth;
    static FunctionFrameFragment funcFrame;
    //static MainPageFrameFragment mainFrame;
    
    public static MainPageFragment mainPageFragment;
    public static FunctionSettingFragment functionSettingFragment;
    public static FunctionMoreFragment functionMoreFragment;
    
    public static Location gpsLocation;
    public static Location wifiLocation;
    public static Location currentLocation;
    LocationManager mLocationManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        
        setContentView(R.layout.activity_main);
        
        funcFrame = new FunctionFrameFragment();
        //mainFrame = new MainPageFrameFragment();

        mainPageFragment = new MainPageFragment();
        functionSettingFragment = new FunctionSettingFragment();
        functionMoreFragment = new FunctionMoreFragment();
        
        new Thread(new Runnable(){
			@Override
			public void run() {
				HttpUtil.gatherOwnerInfo(HttpUtil.getToken());
				HttpUtil.getOwnedCars(HttpUtil.getToken());
			}
        }).start();
        
        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainPageFrameFragment()).commit();
        	//FragmentController.changeContentViewInMainFrame(getFragmentManager(), false, LayoutInflater.from(this).inflate(resource, root));
        	FragmentController.changeFragmentWithMainFrame(getFragmentManager(), true, mainPageFragment);
        }
        
        mLocationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);

		gpsLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		wifiLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(gpsLocation != null && gpsLocation.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
		    // Do something with the recent location fix
		    //  otherwise wait for the update below
		}
		else {
		    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
		
		if(wifiLocation != null && wifiLocation.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
		    // Do something with the recent location fix
		    //  otherwise wait for the update below
		}
		else {
		    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		}
    }

    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);

    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



	@Override
	public void onLocationChanged(Location location) {
	        mLocationManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
	    switch (status) {
	    case LocationProvider.OUT_OF_SERVICE:
	        Toast.makeText(this, "超出服務範圍，定位服務無法使用!",
	                Toast.LENGTH_LONG).show();
	        break;
	    case LocationProvider.TEMPORARILY_UNAVAILABLE:
	        break;
	    case LocationProvider.AVAILABLE:
	        break;
	    }

		
	}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}


}
