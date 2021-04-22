package com.nissan.functionfragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.OwnerInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FunctionBookingServiceFragment extends Fragment {

	View tab_content_view01;
	View tab_content_view02;
	
	View choosing_service_location_view01;
	View choosing_service_location_view02;
	
	View rootView;
	
	ExpandableListView expand;
	
	// 最後選擇到的 location
	String selectedLocationId;
	String selectedSlotTime;
	String selectedsvcAdvisorCode;
	String selectedMechanicCode;
	String type01 = "";
	String type02 = "";
	String location_name = "";
	String location_addr = "";
	String location_number = "";
    public FunctionBookingServiceFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(OwnerInfo.ownedCars!=null){
        	OwnerInfo.ownedCars_plateNo = new String[OwnerInfo.ownedCars.length()];
        	for(int i=0;i<OwnerInfo.ownedCars.length();i++){
        		try {
        			JSONObject car = (JSONObject) OwnerInfo.ownedCars.get(i);
        			OwnerInfo.ownedCars_plateNo[i] = car.getString("plateNo");
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        }
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
        
        rootView = inflater.inflate(R.layout.function_service_booking, container, false);
        
        tab_content_view01 = inflater.inflate(R.layout.function_service_booking_page01, null);
        tab_content_view02 = inflater.inflate(R.layout.function_service_booking_page02, null);
        
        choosing_service_location_view01 = inflater.inflate(R.layout.function_service_booking_sub_page01, null);
        choosing_service_location_view02 = inflater.inflate(R.layout.function_service_booking_sub_page02, null);
        
        ((RelativeLayout) rootView.findViewById(R.id.service_booking_content_rl)).addView(tab_content_view01);
        ((RelativeLayout) rootView.findViewById(R.id.choosing_service_location_content_rl)).addView(choosing_service_location_view01);
        
        final Button tab1 = (Button) rootView.findViewById(R.id.button1);
        final Button tab2 = (Button) rootView.findViewById(R.id.button2);
        
        ((Button) rootView.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tab1.setBackgroundResource(R.drawable.but_book_book_record_on);
				tab2.setBackgroundResource(R.drawable.selector_booking_service_tab);
				((RelativeLayout) rootView.findViewById(R.id.service_booking_content_rl)).removeAllViews();
				((RelativeLayout) rootView.findViewById(R.id.service_booking_content_rl)).addView(tab_content_view01);
				
			}
		});
        
        ((Button) rootView.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tab2.setBackgroundResource(R.drawable.but_book_book_record_on);
				tab1.setBackgroundResource(R.drawable.selector_booking_service_tab);
				((RelativeLayout) rootView.findViewById(R.id.service_booking_content_rl)).removeAllViews();
				((RelativeLayout) rootView.findViewById(R.id.service_booking_content_rl)).addView(tab_content_view02);
				
			}
		});
        
        final Button sub_tab1 = (Button) rootView.findViewById(R.id.sub_tab_button_01);
        final Button sub_tab2 = (Button) rootView.findViewById(R.id.sub_tab_button_02);
        
        ((Button) rootView.findViewById(R.id.sub_tab_button_01)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sub_tab1.setBackgroundResource(R.drawable.but_book_book_record_on);
				sub_tab2.setBackgroundResource(R.drawable.selector_booking_service_tab);
				((RelativeLayout) rootView.findViewById(R.id.choosing_service_location_content_rl)).removeAllViews();
				((RelativeLayout) rootView.findViewById(R.id.choosing_service_location_content_rl)).addView(choosing_service_location_view01);
				new QueryServicedCities().execute();
			}
		});
        
        ((Button) rootView.findViewById(R.id.sub_tab_button_02)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 常用據點直接取用現有資料
				OwnerInfo.servicedLocationId = new String[OwnerInfo.preferredLocationIds.length()];
				OwnerInfo.servicedLocationName = new String[OwnerInfo.preferredLocationIds.length()];
		        for(int i=0;i<OwnerInfo.preferredLocationIds.length();i++){
		        	try {
		        		OwnerInfo.servicedLocationId[i] = OwnerInfo.preferredLocationIds.getJSONObject(i).getString("id");
		        		OwnerInfo.servicedLocationName[i] = OwnerInfo.preferredLocationIds.getJSONObject(i).getString("fullName");
					} catch (JSONException e) {
						e.printStackTrace();
					}
		        }
		        Spinner preferredLocationSpinner = (Spinner) choosing_service_location_view02.findViewById(R.id.choose_my_preferred_location_spinner);
		        ArrayAdapter<String> preferred_location_adapter = 
		        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.servicedLocationName);
		        preferred_location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        preferredLocationSpinner.setAdapter(preferred_location_adapter);
		        preferredLocationSpinner.setOnItemSelectedListener(new LocationSelectedListener(OwnerInfo.preferredLocationIds));
		        
				sub_tab2.setBackgroundResource(R.drawable.but_book_book_record_on);
				sub_tab1.setBackgroundResource(R.drawable.selector_booking_service_tab);
				((RelativeLayout) rootView.findViewById(R.id.choosing_service_location_content_rl)).removeAllViews();
				((RelativeLayout) rootView.findViewById(R.id.choosing_service_location_content_rl)).addView(choosing_service_location_view02);
				
			}
		});
        
        expand = (ExpandableListView) tab_content_view02.findViewById(R.id.maintenance_history_expandlist);
        
        // 從這裡開始在 view 裏頭填值
        
        // 選擇車號
        Spinner ownedCarsSpinner = (Spinner) rootView.findViewById(R.id.my_car_number_spinner);
        ArrayAdapter<String> owned_cars_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.ownedCars_plateNo);
        owned_cars_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownedCarsSpinner.setAdapter(owned_cars_adapter);
        
        // 選擇車號 (在維修紀錄裡的 spinner)
        Spinner maintanenceHistoryPlaceNoSpinner = (Spinner) tab_content_view02.findViewById(R.id.chooseing_placeno_spinner);
        maintanenceHistoryPlaceNoSpinner.setAdapter(owned_cars_adapter);
        maintanenceHistoryPlaceNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        	@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
        		QueryServiceBookingHistory queryServiceBookingHistoryTask = new QueryServiceBookingHistory(OwnerInfo.ownedCars_plateNo[position]);
        		queryServiceBookingHistoryTask.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
        
        
        ((EditText) rootView.findViewById(R.id.my_name_edit_text)).setText(OwnerInfo.name);
        ((EditText) rootView.findViewById(R.id.my_phone_number_edit_text)).setText(OwnerInfo.mobile);
        ((EditText) rootView.findViewById(R.id.my_email_add_edit_text)).setText(OwnerInfo.email);
        
        new QueryServicedCities().execute();
        
        CheckBox maintenance_cb = (CheckBox) rootView.findViewById(R.id.maintenance_cb);
        CheckBox repair_cb = (CheckBox) rootView.findViewById(R.id.repair_cb);
        
        maintenance_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					type01 = "1";
				}
				else{
					type01 = "";
				}
			}
		});
        repair_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					type02 = "2";
				}
				else{
					type02 = "";
				}
			}
		});
        
        rootView.findViewById(R.id.booking_service_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String plateNo = ((Spinner) rootView.findViewById(R.id.my_car_number_spinner)).getSelectedItem().toString().trim();
//				String originBookedTime = ((Spinner) rootView.findViewById(R.id.service_date_spinner)).getSelectedItem().toString().trim();
//				String bookedTime = originBookedTime.split("(")[0].split("/")[0] + "-" + originBookedTime.split("(")[0].split("/")[1] 
//						+ "-" + originBookedTime.split("(")[0].split("/")[2] + "T" + ((Spinner) rootView.findViewById(R.id.service_time_spinner)).getSelectedItem().toString().trim();
		    	String odo = ((EditText) rootView.findViewById(R.id.current_driven_distance_edit_text)).getText().toString().trim();
//		    	String mechanicCode;
//		    	String svcAdvisorCode;
		    	String serviceType = type01 + "," + type02;
		    	String notes = ((EditText) rootView.findViewById(R.id.feed_back_et)).getText().toString().trim();
				String name = ((EditText) rootView.findViewById(R.id.my_name_edit_text)).getText().toString().trim();
				String email = ((EditText) rootView.findViewById(R.id.my_email_add_edit_text)).getText().toString().trim();
				String mobile = ((EditText) rootView.findViewById(R.id.my_phone_number_edit_text)).getText().toString().trim();
		    	
				new AddServiceBooking(plateNo, selectedLocationId, selectedSlotTime
						, odo, selectedMechanicCode, selectedsvcAdvisorCode, serviceType, notes, name, email, mobile).execute();
			}
		});
        
        return rootView;
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
			Spinner servicedCitiesSpinner = (Spinner) choosing_service_location_view01.findViewById(R.id.choose_your_cities_spinner);
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
    // 選完城市後選區
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
	        
	        Spinner servicedDistInCitySpinner = (Spinner) choosing_service_location_view01.findViewById(R.id.choose_your_district_spinner);
	        ArrayAdapter<String> serviced_dist_adapter = 
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.servicedDistName);
	        serviced_dist_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        servicedDistInCitySpinner.setAdapter(serviced_dist_adapter);
	        servicedDistInCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					OwnerInfo.servicedLocationName = new String[OwnerInfo.servicedLocations[position].length()];
					OwnerInfo.servicedLocationId = new String[OwnerInfo.servicedLocations[position].length()];
					OwnerInfo.currentLocation = OwnerInfo.servicedLocations[position];
					for(int i=0;i<OwnerInfo.servicedLocations[position].length();i++){
						try{
							OwnerInfo.servicedLocationId[i] = ((JSONObject) OwnerInfo.servicedLocations[position].get(i)).getString("id");
							OwnerInfo.servicedLocationName[i] = ((JSONObject) OwnerInfo.servicedLocations[position].get(i)).getString("fullName");
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					
					Spinner servicedFactoryInDistSpinner = (Spinner) choosing_service_location_view01.findViewById(R.id.choose_your_factory_spinner);
			        ArrayAdapter<String> serviced_factory_adapter = 
			        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.servicedLocationName);
			        serviced_factory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        servicedFactoryInDistSpinner.setAdapter(serviced_factory_adapter);
			        servicedFactoryInDistSpinner.setOnItemSelectedListener(new LocationSelectedListener(OwnerInfo.currentLocation));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
					
				}
			});
	        
        }
        
    }
    
    private class LocationSelectedListener implements AdapterView.OnItemSelectedListener {
    	JSONArray locationsArray;
    	public LocationSelectedListener(JSONArray locationsArray){
    		this.locationsArray = locationsArray;
    	}
    	
		@Override
		public void onItemSelected(AdapterView<?> parent,
				View view, int position, long id) {
			String addr = "";
			String number = "";
			String thisLocationId = "";
			String name = "";
			JSONObject factoryJSONObject = null;
			try{
				factoryJSONObject = (JSONObject) locationsArray.get(position);
				name = factoryJSONObject.getString("fullName");
				addr = factoryJSONObject.getString("address");
				location_addr = addr = factoryJSONObject.getString("fullAddress");
				number = factoryJSONObject.getString("phone");
				thisLocationId = factoryJSONObject.getString("id");
			} catch(Exception e){
				e.printStackTrace();
			}
			((TextView) tab_content_view01.findViewById(R.id.branch_address)).setText("地址：" + location_addr);
			((TextView) tab_content_view01.findViewById(R.id.branch_number)).setText("電話：" + location_number);
			final JSONObject obj = factoryJSONObject;
			
			// 選擇了常用據點後，把此值填入 selectedLocationId 備用
			selectedLocationId =thisLocationId;
			location_name = name;
			//location_addr = addr;
			location_number = number;
			// 導入 google map 的按鈕
			tab_content_view01.findViewById(R.id.view_direction_in_googlemap_rl).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Double longitude = 0.0;
					Double latitude = 0.0;
					
					if(obj==null) {
						android.util.Log.d("Error Log",
								"error occur, factory location JSONObject is null, unable to fetch location info");
						return;
					}
					
					try{
						longitude = obj.getDouble("longitude");
						latitude = obj.getDouble("latitude");
					} catch(Exception e){
						e.printStackTrace();
					}
					
					String url = "http://maps.google.com/maps?saddr="+longitude.toString()+","+latitude.toString()+"&daddr="+latitude.toString()+","+longitude.toString()+"&mode=driving";
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
						    Uri.parse(url));
					getActivity().startActivity(intent);
				}
			});
			
			new QueryAvailableDateByFactoryId(selectedLocationId).execute();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
    
    private class QueryAvailableDateByFactoryId extends AsyncTask<String, Void, String> {
    	String factoryId;
    	public QueryAvailableDateByFactoryId(String factoryId){
    		this.factoryId = factoryId;
    	};
    	
		@Override
		protected String doInBackground(String... params) {
			HttpUtil.gatherServicedDate(HttpUtil.getToken(), factoryId);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Spinner availableDateSpinner = (Spinner) tab_content_view01.findViewById(R.id.service_date_spinner);
			String[] availableDateAndWeekday = new String[OwnerInfo.availableDate.length];
			for(int i=0;i<OwnerInfo.availableDate.length;i++){
				availableDateAndWeekday[i] = OwnerInfo.availableDate[i] + " ("+ OwnerInfo.availableDateWeekday[i] + ")";
			}
	        ArrayAdapter<String> available_date_adapter =
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, availableDateAndWeekday);
	        available_date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        availableDateSpinner.setAdapter(available_date_adapter);
	        availableDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					new QueryAvailableTimeByFactoryId(factoryId, OwnerInfo.availableDateIso[position]).execute();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
	        	
	        });
		}
    	
    }
    
    private class QueryAvailableTimeByFactoryId extends AsyncTask<String, Void, String> {
    	String factoryId;
    	String date;
    	public QueryAvailableTimeByFactoryId(String factoryId, String dateIso){
    		this.factoryId = factoryId;
    		this.date = (dateIso.split("T")[0]).substring(1);
    	};
    	
		@Override
		protected String doInBackground(String... params) {
			HttpUtil.gatherAvailableTime(HttpUtil.getToken(), factoryId, date);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Spinner availableTimeSpinner = (Spinner) tab_content_view01.findViewById(R.id.service_time_spinner);
	        
			if(OwnerInfo.availableTime==null) return;
			
			ArrayAdapter<String> available_time_adapter =
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.availableTime);
	        available_time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        availableTimeSpinner.setAdapter(available_time_adapter);
	        availableTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					new QueryAvailableAdvisorsBySlotTime(factoryId, date + " " + OwnerInfo.availableTime[position]).execute();
					new QueryAvailableMechanicsBySlotTime(factoryId, date + " " + OwnerInfo.availableTime[position]).execute();
					selectedSlotTime = date + " " + OwnerInfo.availableTime[position];
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}
    	
    }
    private class QueryAvailableAdvisorsBySlotTime extends AsyncTask<String, Void, String> {
    	String locationId;
    	String slotTime;
    	public QueryAvailableAdvisorsBySlotTime(String locationId, String slotTime){
    		this.locationId = locationId;
    		this.slotTime = slotTime;
    	};
    	
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.gatherAvailableSVCAdvisors(HttpUtil.getToken(), locationId, slotTime);
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(OwnerInfo.availableAdvisors==null) return;
			
			Spinner availableAdvisorsSpinner = (Spinner) tab_content_view01.findViewById(R.id.choose_waiter_spinner);
			
			ArrayAdapter<String> available_advisors_adapter =
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.availableAdvisors);
			available_advisors_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			availableAdvisorsSpinner.setAdapter(available_advisors_adapter);
			availableAdvisorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					selectedsvcAdvisorCode = OwnerInfo.availableAdvisorsCode[position];
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
			
		}
    	
    }
    private class QueryAvailableMechanicsBySlotTime extends AsyncTask<String, Void, String> {
    	String locationId;
    	String slotTime;
    	public QueryAvailableMechanicsBySlotTime(String locationId, String slotTime){
    		this.locationId = locationId;
    		this.slotTime = slotTime;
    	};
    	
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.gatherAvailableMechanics(HttpUtil.getToken(), locationId, slotTime);
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(OwnerInfo.availableMechanics==null) return;
			
			Spinner availableMechanicsSpinner = (Spinner) tab_content_view01.findViewById(R.id.choose_engineer_spinner);
			
			ArrayAdapter<String> available_mechanics_adapter =
	        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.availableMechanics);
			available_mechanics_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			availableMechanicsSpinner.setAdapter(available_mechanics_adapter);
			availableMechanicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					selectedMechanicCode = OwnerInfo.availableMechanicsCode[position];
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
			
		}
    	
    }
    private class QueryServiceBookingHistory extends AsyncTask<String, Void, JSONArray>{

    	String plateNo = null;
    	public QueryServiceBookingHistory(String plateNo){
    		this.plateNo = plateNo;
    	}
    	
		@Override
		protected JSONArray doInBackground(String... params) {
			return HttpUtil.fetchServiceBookingHistory(HttpUtil.getToken(), plateNo);
		}
		
		@Override
		protected void onPostExecute(JSONArray result) {
			HistoryExpandableListViewAdapter adapter = new HistoryExpandableListViewAdapter(result);
			expand.setAdapter(adapter);
		}
    	
    }
    
    private class AddServiceBooking extends AsyncTask<String, Void, JSONObject>{

    	String plateNo;
    	String locationId;
    	String bookedTime;
    	String odo;
    	String mechanicCode;
    	String svcAdvisorCode;
    	String serviceType;
    	String notes;
    	String name;
    	String email;
    	String mobile;
    	public AddServiceBooking(String plateNo, String locationId, String bookedTime, String odo, String mechanicCode
    			, String svcAdvisorCode, String serviceType, String notes, String name, String email, String mobile){
    		this.plateNo = plateNo;
    		this.locationId = locationId;
    		this.bookedTime = bookedTime;
    		this.odo = odo;
    		this.mechanicCode = mechanicCode;
    		this.svcAdvisorCode = svcAdvisorCode;
    		this.serviceType = serviceType;
    		this.notes = notes;
    		this.name = name;
    		this.email = email;
    		this.mobile = mobile;
    	}
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			boolean toWait = false;
			RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.if_wait_in_factory_radiogroup);
			if(rg.getCheckedRadioButtonId()==R.id.yes) toWait = true;
			else if(rg.getCheckedRadioButtonId()==R.id.no) toWait = false;
			
			return HttpUtil.addServiceBooking(HttpUtil.getToken(), plateNo, locationId
					, bookedTime, odo, mechanicCode, svcAdvisorCode, serviceType, toWait, notes, name, email, mobile);
		}
		
		@Override
		protected void onPostExecute(final JSONObject result) {
			if(result==null){
				return;
			}
			try{
				String error_message = result.getString("message");
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(error_message).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}catch(Exception e){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("恭喜，預約成功!").setTitle("訊息")
				.setPositiveButton("了解", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true
								, new FunctionBookingDetailsFragment(location_name, location_addr, location_number, result), "確認預約結果");
					}
				});
				builder.create().show();
			}
			
		}
    	
    }
    
    // 維修歷史紀錄的 expandable list view adapter
    private class HistoryExpandableListViewAdapter extends BaseExpandableListAdapter{

    	JSONArray historyArray;
    	String locationId;
    	String entryId;
    	public HistoryExpandableListViewAdapter(JSONArray historyArray){
    		this.historyArray = historyArray;
    	}
    	
		@Override
		public int getGroupCount() {
			return historyArray.length();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			try {
				return this.historyArray.getJSONObject(groupPosition);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.function_maintanence_history_expandlistview, null);
			}
			
			String[] date = null;
			try{
				date = ((JSONObject) getGroup(groupPosition)).getString("dtBookedTime").split("T");
			}catch(Exception e){
				e.printStackTrace();
			}
			((TextView) convertView.findViewById(R.id.servie_date_tv)).setText(date[0] + " " + date[1]);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.function_maintanence_history_expandlistview_childview, null);
			}
			try{
				((TextView) convertView.findViewById(R.id.value_01)).setText(((JSONObject) getGroup(groupPosition)).getString("dealerDept"));
				((TextView) convertView.findViewById(R.id.value_02)).setText(((JSONObject) getGroup(groupPosition)).getString("svcAdvisor"));
				((TextView) convertView.findViewById(R.id.value_03)).setText(((JSONObject) getGroup(groupPosition)).getString("mechanic"));
				((TextView) convertView.findViewById(R.id.value_04)).setText(((JSONObject) getGroup(groupPosition)).getString("status"));
				locationId = ((JSONObject) getGroup(groupPosition)).getString("locationId");
				entryId = ((JSONObject) getGroup(groupPosition)).getString("entryId");
			} catch(Exception e){
				e.printStackTrace();
			}
			
			convertView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String plateNo = ((Spinner) tab_content_view02.findViewById(R.id.chooseing_placeno_spinner)).getSelectedItem().toString().trim();
					new CancelBooking(plateNo, locationId, entryId).execute();
				}
			});
			
			convertView.findViewById(R.id.call_btn).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new QueryAndCallLocation(locationId).execute();
				}
			});

			convertView.findViewById(R.id.direction_btn).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new QueryDirection(locationId).execute();
					
				}
			});
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition,
				int childPosition) {
			return false;
		}
    	
    }
    private class QueryDirection extends AsyncTask<String, Void, JSONObject>{

    	String locationId;
    	public QueryDirection(String locationId){
    		this.locationId = locationId;
    	}
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			return HttpUtil.fetchServiceLocationData(HttpUtil.getToken(), this.locationId);
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
    
    private class CancelBooking extends AsyncTask<String, Void, String>{
    	String plateNo;
    	String locationId;
    	String entryId;
    	public CancelBooking(String plateNo, String locationId, String entryId){
    		this.plateNo = plateNo;
    		this.locationId = locationId;
    		this.entryId = entryId;
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			return HttpUtil.deleteServiceBooking(HttpUtil.getToken(), plateNo, locationId, entryId);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.equals("true")){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("取消成功!").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
			
		}
    	
    }
    
    private class QueryAndCallLocation extends AsyncTask<String, Void, JSONObject>{
    	String locationId;
    	public QueryAndCallLocation(String locationId){
    		this.locationId = locationId;
    	}
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			return HttpUtil.fetchServiceLocationData(HttpUtil.getToken(), locationId);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				String number = result.getString("phone");
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + number));
				startActivity(callIntent);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
    	
    }
    
}
