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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class FunctionQueryMyPointFragment extends Fragment {
    
    View rootView;
    String main_bonus_plateNo;
    public FunctionQueryMyPointFragment() {
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
        
        rootView = inflater.inflate(R.layout.function_query_my_point, container, false);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        ((TextView) rootView.findViewById(R.id.last_date_tv)).setText("截至昨日 " + dateFormat.format(cal.getTime()) + " 止");
        
        // 選擇車號
        Spinner ownedCarsSpinner = (Spinner) rootView.findViewById(R.id.query_points_spinner);
        ArrayAdapter<String> owned_cars_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.ownedCars_plateNo);
        owned_cars_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownedCarsSpinner.setAdapter(owned_cars_adapter);
        ownedCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				new QueryBonusHistoryByPlateNo(OwnerInfo.ownedCars_plateNo[position]).execute();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
        for(int i=0;i<OwnerInfo.ownedCars_plateNo.length;i++){
        	final RadioButton rb = new RadioButton(getActivity());
        	rb.setText(OwnerInfo.ownedCars_plateNo[i]);
        	if(i==0) {
        		main_bonus_plateNo = OwnerInfo.ownedCars_plateNo[0];
        	}
        	rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						main_bonus_plateNo = (String) rb.getText();
					}
				}
			});
        	RadioGroup rg = ((RadioGroup) rootView.findViewById(R.id.main_bonus_car_radiogroup));
        	rg.setSelected(true);
        	rg.addView(rb);
        }
        
        ((Button) rootView.findViewById(R.id.change_main_points_car_btn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SetMainCarBonus(main_bonus_plateNo).execute();
			}
		});
        
        return rootView;
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
			((LinearLayout) rootView.findViewById(R.id.bonus_history_ll)).removeAllViews();
			for(int i=0;i<result.length();i++){
				
				try{
					bonusSum += result.getJSONObject(i).getInt("bonusTrans");
					View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.bonus_history_item_view, null);
					if(i%2==0) itemView.setBackgroundColor(0xff666666);
					else itemView.setBackgroundColor(0xff262626);
					
					((LinearLayout) rootView.findViewById(R.id.bonus_history_ll)).addView(itemView);
					
					((TextView) itemView.findViewById(R.id.bonus_history_tv1)).setText(result.getJSONObject(i).getString("transDate"));
					((TextView) itemView.findViewById(R.id.bonus_history_tv2)).setText(result.getJSONObject(i).getString("bonusTrans"));
					((TextView) itemView.findViewById(R.id.bonus_history_tv3)).setText(result.getJSONObject(i).getString("issueName"));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			((TextView) rootView.findViewById(R.id.points_sum_tv)).setText(bonusSum + " 點");
			try{
				((TextView) rootView.findViewById(R.id.last_maintenance_date)).setText("上次返廠日期為：" + result.getJSONObject(result.length()-1).getString("transDate"));
				
			} catch(Exception e){
				e.printStackTrace();
			}
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
