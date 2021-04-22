package com.nissan.functionfragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;

import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.OwnerInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FunctionEditCarsFragment extends Fragment {

    String myPointsWebUrl = "http://www.bifter.co.uk/";
    
    View rootView;
    
    public FunctionEditCarsFragment() {
    }

    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	if(OwnerInfo.ownedCars!=null){
        	OwnerInfo.ownedCars_plateNo = new String[OwnerInfo.ownedCars.length()];
        	OwnerInfo.ownedCars_modelName = new String[OwnerInfo.ownedCars.length()];
        	for(int i=0;i<OwnerInfo.ownedCars.length();i++){
        		try {
        			JSONObject car = (JSONObject) OwnerInfo.ownedCars.get(i);
        			OwnerInfo.ownedCars_plateNo[i] = car.getString("plateNo");
        			OwnerInfo.ownedCars_modelName[i] = car.getString("modelName");
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        }
		super.onCreate(savedInstanceState);
	}
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        rootView = inflater.inflate(R.layout.function_edit_cars_page, container, false);
        
        for(int i=0;i<OwnerInfo.ownedCars_plateNo.length;i++){
        	View itemView = inflater.inflate(R.layout.my_lovely_car_item_view, null);
        	final int c = i;
        	itemView.findViewById(R.id.remove_this_car_btn).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new RemovingCar(HttpUtil.getToken(), OwnerInfo.ownedCars_plateNo[c]).execute();
				}
			});
        	((TextView) itemView.findViewById(R.id.my_cars_tv1)).setText(OwnerInfo.ownedCars_plateNo[i]);
        	((TextView) itemView.findViewById(R.id.my_cars_tv2)).setText(OwnerInfo.ownedCars_modelName[i]);
        	if(i%2==0) itemView.setBackgroundColor(0xff666666);
        	else itemView.setBackgroundColor(0xff474747);
        	((LinearLayout) rootView.findViewById(R.id.my_car_list_ll)).addView(itemView);
        }
        
        final EditText addCarEt = (EditText) rootView.findViewById(R.id.input_plateno_et);
        rootView.findViewById(R.id.register_your_car).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddingNewCar(HttpUtil.getToken(), addCarEt.getText().toString().trim()).execute();
			}
		});
        return rootView;
    }
    
	
	private class AddingNewCar extends AsyncTask<String, Void, String>{

		String plateNo;
		
		public AddingNewCar(String token, String plateNo){
			this.plateNo = plateNo;
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			return HttpUtil.addingNewCar(HttpUtil.getToken(), plateNo);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，車輛已加入我的愛車").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				
				new renewLayoutInfo().execute();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
			
		}
		
	}
	
	private class RemovingCar extends AsyncTask<String, Void, String>{

		String plateNo;
		
		public RemovingCar(String token, String plateNo){
			this.plateNo = plateNo;
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			return HttpUtil.removeCar(HttpUtil.getToken(), plateNo);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，車輛已移除我的愛車").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				
				new renewLayoutInfo().execute();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
			
		}
		
	}
	
	private class renewLayoutInfo extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.getOwnedCars(HttpUtil.getToken());
			
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("success")){
				LinearLayout my_car_list = ((LinearLayout) rootView.findViewById(R.id.my_car_list_ll));
				my_car_list.removeAllViews();
				for(int i=0;i<OwnerInfo.ownedCars.length();i++){
		        	View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.my_lovely_car_item_view, null);
		        	try{
		        		final String s = OwnerInfo.ownedCars.getJSONObject(i).getString("plateNo");
		        		itemView.findViewById(R.id.remove_this_car_btn).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								new RemovingCar(HttpUtil.getToken(), s).execute();
							}
						});
			        	((TextView) itemView.findViewById(R.id.my_cars_tv1)).setText(OwnerInfo.ownedCars.getJSONObject(i).getString("plateNo"));
			        	((TextView) itemView.findViewById(R.id.my_cars_tv2)).setText(OwnerInfo.ownedCars.getJSONObject(i).getString("modelName"));
		        	} catch (Exception e){
		        		e.printStackTrace();
		        	}
		        	
		        	if(i%2==0) itemView.setBackgroundColor(0xff666666);
		        	else itemView.setBackgroundColor(0xff474747);
		        	my_car_list.addView(itemView);
		        }
				//my_car_list.invalidate();
			}
			super.onPostExecute(result);
		}
		
	}
	
}
