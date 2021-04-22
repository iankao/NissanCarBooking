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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FunctionServiceLocationFragment extends Fragment {

	View rootView;
	JSONArray locationArray;
	ProgressDialog mProgressDialog;
    public FunctionServiceLocationFragment() {
        
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
        
        rootView = inflater.inflate(R.layout.function_service_location_page, container, false);
        new QueryServicedCities().execute();
        
        rootView.findViewById(R.id.send_to_query_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String city = ((Spinner) rootView.findViewById(R.id.choose_city_spinner)).getSelectedItem().toString().trim(); 
				String district = ((Spinner) rootView.findViewById(R.id.choose_district_spinner)).getSelectedItem().toString().trim();
				FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true
						, new FunctionServiceLocationListFragment(city, district, locationArray), "服務據點查詢結果");
			}
		});
        
        rootView.findViewById(R.id.my_preferred_locations).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true
						, new FunctionServiceLocationListFragment(null, null, OwnerInfo.preferredLocationIds), "我的常用據點");
			}
		});
        
        rootView.findViewById(R.id.find_location_nearby_rl).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mProgressDialog = new ProgressDialog(getActivity());
				mProgressDialog.setMessage("資料處理中....");
				mProgressDialog.show();
				new FindClosestLocation().execute();
			}
		});
        
        return rootView;
    }
    
    private class FindClosestLocation extends AsyncTask<String , Void, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			float distance = 10000;
			String[] data = new String[4];
			for(String cityId:OwnerInfo.servicedCitiesId){
				JSONArray districtArray = HttpUtil.gatherServicedDistrictLocationInCitiy(HttpUtil.getToken(), cityId);
				for(int i=0;i<districtArray.length();i++){
					try {
						JSONArray locationArray = districtArray.getJSONObject(i).getJSONArray("locations");
						for(int c=0;c<locationArray.length();c++){
							float nextDistance = LocationUtility.getDistance((float)locationArray.getJSONObject(c).getDouble("longitude")
									, (float)locationArray.getJSONObject(c).getDouble("latitude"));
							if(nextDistance < distance) {
								distance = nextDistance;
								data[0] = locationArray.getJSONObject(c).getString("id");
								data[1] = locationArray.getJSONObject(c).getString("fullName");
								data[2] = locationArray.getJSONObject(c).getString("fullAddress");
								data[3] = locationArray.getJSONObject(c).getString("phone");
							}
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
			return data;
		}
		@Override
        protected void onPostExecute(String[] result) {
			if(mProgressDialog!=null) mProgressDialog.dismiss();
			new QueryLocationDetail(result[0], result[1], result[2], result[3]).execute();
		}
		
    }
    
    // 選擇城市
    private class QueryServicedCities extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
        	HttpUtil.gatherServicedCities(HttpUtil.getToken());
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
			Spinner servicedCitiesSpinner = (Spinner) rootView.findViewById(R.id.choose_city_spinner);
	        ArrayAdapter<String> serviced_cities_adapter = 
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.servicedCitiesName);
	        serviced_cities_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        servicedCitiesSpinner.setAdapter(serviced_cities_adapter);
	        servicedCitiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					new QueryServicedDistrictByCity(OwnerInfo.servicedCitiesId[position]).execute();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
			});
	        
                
        }
    }

    // 選擇城市
    private class QueryServicedDistrictByCity extends AsyncTask<String, Void, String> {
    	String cityId;
    	public QueryServicedDistrictByCity(String cityId){
    		this.cityId = cityId;
    	}
    	
        @Override
        protected String doInBackground(String... params) {
        	HttpUtil.gatherServicedDistrictInCitiy(HttpUtil.getToken(), cityId);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
			
        	Spinner servicedDistrictSpinner = (Spinner) rootView.findViewById(R.id.choose_district_spinner);
	        ArrayAdapter<String> serviced_district_adapter = 
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.servicedDistName);
	        serviced_district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        servicedDistrictSpinner.setAdapter(serviced_district_adapter);
	        servicedDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					locationArray = OwnerInfo.servicedLocations[position];
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
	        	
	        });
	        rootView.findViewById(R.id.send_to_query_btn).setClickable(true);
        }
        
    }
    private class QueryLocationDetail extends AsyncTask<String, Void, JSONObject>{
    	String locationId;
    	String name;
    	String addr;
    	String phone;
    	public QueryLocationDetail(String locationId, String name, String addr, String phone){
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
