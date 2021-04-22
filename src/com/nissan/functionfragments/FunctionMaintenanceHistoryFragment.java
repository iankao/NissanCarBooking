package com.nissan.functionfragments;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.OwnerInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.TextView;

public class FunctionMaintenanceHistoryFragment extends Fragment {

	View rootView;
    String plateNo;
    String year;
    String month;
    public FunctionMaintenanceHistoryFragment() {
        
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	
        rootView = inflater.inflate(R.layout.function_maintenance_history, container, false);
        
        // 選擇車號
        Spinner ownedCarsSpinner = (Spinner) rootView.findViewById(R.id.car_number_spinner);
        ArrayAdapter<String> owned_cars_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, OwnerInfo.ownedCars_plateNo);
        owned_cars_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownedCarsSpinner.setAdapter(owned_cars_adapter);
        ownedCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				plateNo = OwnerInfo.ownedCars_plateNo[position];
				QueryOwnedCarDetails queryOwnedCarDetails = new QueryOwnedCarDetails(OwnerInfo.ownedCars_plateNo[position]);
				queryOwnedCarDetails.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
        	
        });
        
        Calendar cal = Calendar.getInstance();
        int length = cal.get(Calendar.YEAR) - 2007;
        String[] year = new String[length];
        int startYesr = 2008;
        for(int i=0;i<length;i++){
        	year[i] = String.valueOf(startYesr + i);
        }
        String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        
        // 選擇開始年
        final Spinner startYearSpinner = (Spinner) rootView.findViewById(R.id.from_year_spinner);
        ArrayAdapter<String> from_year_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, year);
        from_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startYearSpinner.setAdapter(from_year_adapter);
        
        // 選擇開始月份
        final Spinner startMonthSpinner = (Spinner) rootView.findViewById(R.id.from_month_spinner);
        ArrayAdapter<String> from_month_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, month);
        from_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMonthSpinner.setAdapter(from_month_adapter);
        
        // 選擇結束年
        final Spinner endYearSpinner = (Spinner) rootView.findViewById(R.id.to_year_spinner);
        ArrayAdapter<String> to_year_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, year);
        to_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endYearSpinner.setAdapter(to_year_adapter);
        
        // 選擇結束月份
        final Spinner endMonthSpinner = (Spinner) rootView.findViewById(R.id.to_month_spinner);
        ArrayAdapter<String> to_month_adapter = 
        		new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, month);
        to_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMonthSpinner.setAdapter(to_month_adapter);
        
        rootView.findViewById(R.id.start_query_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String startDate = startYearSpinner.getSelectedItem().toString() + "-" + startMonthSpinner.getSelectedItem().toString() + "-1";
				String endDate = endYearSpinner.getSelectedItem().toString() + "-" + endMonthSpinner.getSelectedItem().toString() + "-1";
				android.util.Log.d("ianTest","startDate::" + startDate);
				android.util.Log.d("ianTest","endDate::" + endDate);
				new QueryMaintenanceHistory(plateNo, startDate, endDate).execute();
			}
		});
        
        return rootView;
    }
    /**
     * 取得最近一次保修紀錄
     * 
     * @author iiiii
     *
     */
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
				((TextView) rootView.findViewById(R.id.last_maintenance_date_value)).setText(result.getJSONObject("lastMaintenance").getString("date"));
				((TextView) rootView.findViewById(R.id.my_car_distance_value)).setText(result.getJSONObject("lastMaintenance").getString("odoMeters"));
				((TextView) rootView.findViewById(R.id.last_maintenance_factory_value)).setText(result.getJSONObject("lastMaintenance").getString("dealerDept"));
				((TextView) rootView.findViewById(R.id.last_maintanence_waiter_value)).setText(result.getJSONObject("lastMaintenance").getString("svcAdvisor"));
				((TextView) rootView.findViewById(R.id.last_maintanence_engineer_value)).setText(result.getJSONObject("lastMaintenance").getString("mechanic"));
			} catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
    }
    /**
     * 取得區間裡的保修紀錄
     * 
     * @author iiiii
     *
     */
    private class QueryMaintenanceHistory extends AsyncTask<String, Void, JSONArray>{

    	String plateNo;
    	String startDate;
    	String endDate;
    	
    	QueryMaintenanceHistory(String plateNo, String startDate ,String endDate){
    		this.plateNo = plateNo;
    		this.startDate = startDate;
    		this.endDate = endDate;
    	}
    	
		@Override
		protected JSONArray doInBackground(String... params) {
			
			return HttpUtil.fetchMaintenanceHistory(HttpUtil.getToken(), plateNo, startDate, endDate);
		}
		
		@Override
        protected void onPostExecute(JSONArray result) {
			if(result==null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("取得資料錯誤，請再試一次").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				return;
			}
			FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true, new FunctionMaintenanceHistoryListFragment(plateNo, result), "保修紀錄查詢結果");
		}
    	
    }

}
