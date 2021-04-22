package com.nissan.functionfragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FunctionCarInformationFragment extends Fragment {

	View rootView = null;
	String main_bonus_car;
    public FunctionCarInformationFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(rootView==null) rootView = inflater.inflate(R.layout.function_your_car_information, container, false);
        
        // 選擇車號
        Spinner ownedCarsSpinner = (Spinner) rootView.findViewById(R.id.choosing_plate_no_spinner);
        ArrayAdapter<String> owned_cars_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.ownedCars_plateNo);
        owned_cars_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownedCarsSpinner.setAdapter(owned_cars_adapter);
        ownedCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				new QueryOwnedCarDetails(OwnerInfo.ownedCars_plateNo[position]).execute();
				new QueryBonusHistoryByPlateNo(OwnerInfo.ownedCars_plateNo[position]).execute();
				main_bonus_car = OwnerInfo.ownedCars_plateNo[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
        	
        });
        rootView.findViewById(R.id.setting_main_credit_car_rl).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SetMainCarBonus(main_bonus_car).execute();
				
			}
		});
        rootView.findViewById(R.id.edit_owner_personal_profile_btn).setOnClickListener(clicklistener_edit_owner_info);
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    View.OnClickListener clicklistener_edit_owner_info = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionEditOwnerInfoFragment(), "修改車主資料");
        }
    };
    
    private class QueryOwnedCarDetails extends AsyncTask<String, Void, JSONObject>{
    	String plateNo = null;
    	public QueryOwnedCarDetails(String plateNo){
    		this.plateNo = plateNo;
    	}
    	
		@Override
		protected JSONObject doInBackground(String... params) {
			return HttpUtil.fetchOwnedCarDetails(HttpUtil.getToken(), plateNo);
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			try{
				((TextView) rootView.findViewById(R.id.my_car_type_value)).setText(result.getJSONObject("model").getString("modelName"));
				((TextView) rootView.findViewById(R.id.last_service_time_stamp)).setText(result.getJSONObject("lastMaintenance").getString("date"));
			} catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
    }
    
    private class QueryBonusHistoryByPlateNo extends AsyncTask<String, Void, JSONArray>{

    	String plateNo;
    	
    	QueryBonusHistoryByPlateNo(String plateNo){
    		this.plateNo = plateNo;
    	}
    	
		@Override
		protected JSONArray doInBackground(String... params) {
			return HttpUtil.fetchBonusHistoryByPateNo(HttpUtil.getToken(), plateNo);
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			int bonusSum = 0;
			for(int i=0;i<result.length();i++){
				
				try{
					bonusSum += result.getJSONObject(i).getInt("bonusTrans");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			((TextView) rootView.findViewById(R.id.points_left)).setText(bonusSum + " 點");
		}
    	
    }

    private class SetMainCarBonus extends AsyncTask<String, Void, Boolean>{

    	String plateNo;
    	
    	SetMainCarBonus(String plateNo){
    		this.plateNo = plateNo;
    	}
    	
		@Override
		protected Boolean doInBackground(String... params) {
			return HttpUtil.setBonusMainCar(HttpUtil.getToken(), plateNo);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，已更變主要紅利積點車輛 :" + plateNo).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定失敗，錯誤發生，請洽客服人員").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
		}
    	
    }
}
