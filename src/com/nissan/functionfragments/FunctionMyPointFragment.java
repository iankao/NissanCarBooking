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
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class FunctionMyPointFragment extends Fragment {

    String myPointsWebUrl = "http://www.bifter.co.uk/";
    
    public FunctionMyPointFragment() {
    }

    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
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
        
        View rootView = inflater.inflate(R.layout.function_my_point, container, false);
        rootView.findViewById(R.id.query_points_btn).setOnClickListener(clicklistener_query_bonus_history);
        rootView.findViewById(R.id.points_getting_rule_btn).setOnClickListener(clicklistener_getting_rules);
        rootView.findViewById(R.id.points_gift_present_btn).setOnClickListener(clicklistener_gifts_presentation);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        ((TextView) rootView.findViewById(R.id.points_last_staic_date_tv)).setText("截至昨日 " + dateFormat.format(cal.getTime()) + " 止");
        
        for(String plateNo:OwnerInfo.ownedCars_plateNo){
        	
        	View v = inflater.from(getActivity()).inflate(R.layout.points_collected_item_view, null);
        	((TextView) v.findViewById(R.id.points_by_car_tv)).setText(plateNo + " 紅利: ");
        	((ViewGroup) rootView.findViewById(R.id.car_points_list_ll)).addView(v);
        	new QueryBonusHistoryByPlateNo(plateNo, v).execute();
        }
        
        return rootView;
    }
	
	View.OnClickListener clicklistener_query_bonus_history = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionQueryMyPointFragment(), "紅利積點查詢");
        }
    };
    
    View.OnClickListener clicklistener_getting_rules = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionGettingPointRuleFragment(), "紅利積點規則");
            
        }
    };
    
    View.OnClickListener clicklistener_gifts_presentation = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionPointsGiftPresentFragment(), "紅利精品展示");
        }
    };
    
    private class QueryBonusHistoryByPlateNo extends AsyncTask<String, Void, JSONArray>{

    	String plateNo;
    	View view;
    	QueryBonusHistoryByPlateNo(String plateNo, View view){
    		this.plateNo = plateNo;
    		this.view = view;
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
			String r = (String) ((TextView) view.findViewById(R.id.points_by_car_tv)).getText();
			((TextView) view.findViewById(R.id.points_by_car_tv)).setText(r + bonusSum + " 點");
		}
    	
    }

}
