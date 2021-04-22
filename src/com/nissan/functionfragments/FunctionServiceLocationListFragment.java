package com.nissan.functionfragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.LocationUtility;
import com.nissan.utility.OwnerInfo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FunctionServiceLocationListFragment extends Fragment {

	View rootView;
	JSONArray locationArray;
	String city;
	String district;
	/**
	 * 跳轉維修廠清單模式的 Fragment
	 * city, district 如果是帶 null 代表是到"我的常用據點"頁面，會有不同行為
	 * 
	 * @param city
	 * @param district
	 * @param locationArray
	 */
    public FunctionServiceLocationListFragment(String city, String district, JSONArray locationArray) {
        this.city = city;
    	this.district = district;
    	this.locationArray = locationArray;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                final String tokenStatus = HttpUtil.checkTokenStatus();
                if(tokenStatus.startsWith("error")){
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity()).setTitle("錯誤訊息").setMessage(tokenStatus.split("_")[2])
                            .setPositiveButton("重新登入", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    getActivity().startActivity(intent);
                                }
                            }).show();
                        }
                    });
                }
            }
        }).start();
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        rootView = inflater.inflate(R.layout.function_service_location_list_page, container, false);
        if(city==null || district==null){
        	((TextView) rootView.findViewById(R.id.search_result_title)).setText("我的常用據點");
        	return rootView; // 如果是要進入我的常用據點，到 onResume 時候再畫圖
        }
        ((TextView) rootView.findViewById(R.id.search_result_title)).setText("依【" + city + district +"】查詢結果");
        
        for(int i = 0;i<locationArray.length();i++){
        	View item = inflater.inflate(R.layout.service_location_item_view, null);
        	if(i%2==0) item.setBackgroundColor(0xff555555);
        	else item.setBackgroundColor(0xff333333);
        	try{
        		((TextView) item.findViewById(R.id.location_list_tv1)).setText(locationArray.getJSONObject(i).getString("district"));
        		((TextView) item.findViewById(R.id.location_list_tv2)).setText(locationArray.getJSONObject(i).getString("fullName"));
        		Double longitude = locationArray.getJSONObject(i).getDouble("longitude");
        		Double latitude = locationArray.getJSONObject(i).getDouble("latitude");
        		if(MainActivity.currentLocation!=null){
        			Location location = new Location("locationLocation");
        			location.setLongitude(longitude);
        			location.setLatitude(latitude);
        			float distance = MainActivity.currentLocation.distanceTo(location) / 1000;
        			((TextView) item.findViewById(R.id.location_list_tv3)).setText(String.format("%.01f", distance) + "KM");
        		}
        	} catch(Exception e){
        		e.printStackTrace();
        	}
        	item.findViewById(R.id.more_info_btn).setOnClickListener(new OnClickListener(i));
        	((LinearLayout) rootView.findViewById(R.id.location_list_ll)).addView(item);
        	
        }
        
        return rootView;
    }
    
    @Override
	public void onResume() {
		// 如果是在我的常用據點，需要更新內容
		if(city==null || district==null){
        	new RenewPreferredLocations().execute();
        } 
		if(MainActivity.gpsLocation!=null){
			MainActivity.currentLocation = MainActivity.gpsLocation;
		}
		if(MainActivity.wifiLocation!=null){
			MainActivity.currentLocation = MainActivity.wifiLocation;
		}
		
		super.onResume();
	}
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    private class RenewPreferredLocations extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			return HttpUtil.gatherOwnerInfo(HttpUtil.getToken());
		}

		@Override
		protected void onPostExecute(String result) {
			locationArray = OwnerInfo.preferredLocationIds;
			((LinearLayout) rootView.findViewById(R.id.location_list_ll)).removeAllViews();
			for(int i = 0;i<locationArray.length();i++){
	        	View item = LayoutInflater.from(getActivity()).inflate(R.layout.service_location_item_view, null);
	        	if(i%2==0) item.setBackgroundColor(0xff555555);
	        	else item.setBackgroundColor(0xff333333);
	        	try{
	        		((TextView) item.findViewById(R.id.location_list_tv1)).setText(locationArray.getJSONObject(i).getString("district"));
	        		((TextView) item.findViewById(R.id.location_list_tv2)).setText(locationArray.getJSONObject(i).getString("fullName"));
	        		Double longitude = locationArray.getJSONObject(i).getDouble("longitude");
	        		Double latitude = locationArray.getJSONObject(i).getDouble("latitude");
	        		if(MainActivity.currentLocation!=null){
	        			Location location = new Location("locationLocation");
	        			location.setLongitude(longitude);
	        			location.setLatitude(latitude);
	        			float distance = MainActivity.currentLocation.distanceTo(location) / 1000;
	        			((TextView) item.findViewById(R.id.location_list_tv3)).setText(String.format("%.01f", distance) + "KM");
	        		}
	        	} catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	
	        	item.findViewById(R.id.more_info_btn).setOnClickListener(new OnClickListener(i));
	        	((LinearLayout) rootView.findViewById(R.id.location_list_ll)).addView(item);
	        	
	        }
			
		}
    	
    }
    
    private class OnClickListener implements View.OnClickListener{
    	int counter;
    	public OnClickListener(int counter){
    		this.counter = counter;
    	}

		@Override
		public void onClick(View v) {
			try {
				String locationId = locationArray.getJSONObject(counter).getString("id");
				String name = locationArray.getJSONObject(counter).getString("fullName");
				String addr = locationArray.getJSONObject(counter).getString("address");
				String phone = locationArray.getJSONObject(counter).getString("phone");
				new QuerySchedule(locationId, name, addr, phone).execute();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
    }
    
    private class QuerySchedule extends AsyncTask<String, Void, JSONObject>{
    	String locationId;
    	String name;
    	String addr;
    	String phone;
    	public QuerySchedule(String locationId,String name, String addr, String phone){
    		this.locationId = locationId;
    		this.name = name;
    		this.addr = addr;
    		this.phone = phone;
    	}
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			return HttpUtil.fetchServiceLocationSchedule(HttpUtil.getToken(), locationId);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true
					, new FunctionServiceLocationDetailsFragment(locationId, name, addr, phone, result), "服務據點查詢");
		}
    	
		
    }
    
}
