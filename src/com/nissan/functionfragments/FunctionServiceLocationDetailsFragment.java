package com.nissan.functionfragments;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.OwnerInfo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FunctionServiceLocationDetailsFragment extends Fragment {
	String locationId;
	String name;
    String addr;
    String phone;
    JSONObject detailsJS;
    View rootView;
    public FunctionServiceLocationDetailsFragment(String locationId, String name, String addr, String phone, JSONObject details) {
    	this.locationId = locationId;
        this.name = name;
    	this.addr = addr;
        this.phone = phone;
        this.detailsJS = details;
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
   	public void onResume() {
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

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        rootView = inflater.inflate(R.layout.function_maintenance_branch, container, false);
        {// 在這個 block 設定 TextView 裡顯示的文字
        	((TextView) rootView.findViewById(R.id.branch_name_tv)).setText(name);
        	
        	((TextView) rootView.findViewById(R.id.service_point_point_tv_02)).setClickable(true);
        	SpannableString content = new SpannableString(addr);
        	content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        	((TextView) rootView.findViewById(R.id.service_point_point_tv_02)).setText(content);
        	
        	((TextView) rootView.findViewById(R.id.service_point_phone_tv_02)).setClickable(true);
        	SpannableString content2 = new SpannableString(phone);
        	content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        	((TextView) rootView.findViewById(R.id.service_point_phone_tv_02)).setText(content2);
        }
        LinearLayout scheduleLL = (LinearLayout) rootView.findViewById(R.id.schedule_ll);

        int itemLength = 0;
		try {
			locationId = detailsJS.getString("locationId");
			itemLength = detailsJS.getJSONArray("items").length();
			String notes = detailsJS.getString("notes");
			if(!notes.equals("")) ((TextView) rootView.findViewById(R.id.ps_notes)).setText("※備註：" + notes);
			else ((TextView) rootView.findViewById(R.id.ps_notes)).setText("");
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
        
        for(int i=0;i<itemLength;i++){
        	
			try {
				JSONArray items = detailsJS.getJSONArray("items");
				int weekday = items.getJSONObject(i).getInt("weekday");
				String dayOrNight = items.getJSONObject(i).getString("id");
			
	        	if(dayOrNight.split("-")[1].equals("d")){
		        	// 日間的勾勾圖
		        	((LinearLayout) scheduleLL.getChildAt(weekday)).getChildAt(1).setVisibility(View.VISIBLE);
	        	}
	        	else{
		        	// 夜間的勾勾圖
		        	((LinearLayout) scheduleLL.getChildAt(weekday)).getChildAt(2).setVisibility(View.VISIBLE);
	        	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        // 設定 BUTTON 的文字與功能
        if(checkIfLocationFavorite(locationId)){
	        ((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setText("移除我的常用據點");
			((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new RemoveFromFavorite(locationId).execute();
				}
			});
        }
        else{
        	((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setText("加入我的常用據點");
			((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new AddToFavorite(locationId).execute();
				}
			});
        }
        
        rootView.findViewById(R.id.book_service_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phone.split("-")[0] + phone.split("-")[1]));
				startActivity(callIntent);
			}
		});
        
        rootView.findViewById(R.id.direction_to_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new QueryDirection().execute();
			}
			
		});
        
        return rootView;
    }
    
    private boolean checkIfLocationFavorite(String locationId){
    	try{
    		int legth = OwnerInfo.preferredLocationIds.length();
    		for(int i=0;i<legth;i++){
        		if(OwnerInfo.preferredLocationIds.getJSONObject(i).getString("id").equals(locationId)) return true;
        	}
        	return false;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	
    }
    
    private class QueryDirection extends AsyncTask<String, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			return HttpUtil.fetchServiceLocationData(HttpUtil.getToken(), locationId);
		}
    	
		@Override
		protected void onPostExecute(JSONObject result) {
			// Destination of route
			String str_dest = "";
			try {
				str_dest = "daddr=" + result.getString("latitude") + "," + result.getString("longitude");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Building the url to the web service
			Intent directionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + MainActivity.currentLocation.getLatitude() + "," + MainActivity.currentLocation.getLongitude() + "&" + str_dest));
			startActivity(directionIntent);
		}
    }
    
    private class AddToFavorite extends AsyncTask<String, Void, String>{
    	String locationId;
    	public AddToFavorite(String locationId){
    		this.locationId = locationId;
    	}
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.addPreferredLocation(HttpUtil.getToken(), locationId);
		}
    	
		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，已加入常用據點").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setText("移除我的常用據點");
				((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new RemoveFromFavorite(locationId).execute();
					}
				});
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
		}
		
    }
    
    private class RemoveFromFavorite extends AsyncTask<String, Void, String>{
    	String locationId;
    	public RemoveFromFavorite(String locationId){
    		this.locationId = locationId;
    	}
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.removePreferredLocation(HttpUtil.getToken(), locationId);
		}
    	
		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，已移除常用據點").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setText("加入我的常用據點");
				((Button) rootView.findViewById(R.id.adding_my_favorite_tv)).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new AddToFavorite(locationId).execute();
					}
				});
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
		}
		
    }

}
