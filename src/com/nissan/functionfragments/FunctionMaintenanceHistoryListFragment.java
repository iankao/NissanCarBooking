package com.nissan.functionfragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;
import com.nissan.utility.OwnerInfo;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FunctionMaintenanceHistoryListFragment extends Fragment {

	View rootView;
	String plateNo;
    JSONArray historyArray;
	
    public FunctionMaintenanceHistoryListFragment(String plateNo, JSONArray jsonArray) {
    	this.plateNo = plateNo;
        this.historyArray = jsonArray;
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
		android.util.Log.d("ianTest","onResume onResume onResume::::");
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
        
    	
        rootView = inflater.inflate(R.layout.function_maintenance_history_list, container, false);
        
        for(int i=0;i<historyArray.length();i++){
        	View itemView = inflater.inflate(R.layout.history_list_item_view, null);
        	
        	if(i%2==0) itemView.setBackgroundColor(0xff666666);
        	else itemView.setBackgroundColor(0xff474747);
        	try{
        		final JSONObject historyObject = historyArray.getJSONObject(i);
        		final String rno = historyArray.getJSONObject(i).getString("repairNo");
	        	((TextView) itemView.findViewById(R.id.history_list_tv1)).setText(historyArray.getJSONObject(i).getString("date"));
	            ((TextView) itemView.findViewById(R.id.history_list_tv2)).setText(historyArray.getJSONObject(i).getString("dealerDept"));
	            itemView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new QueryMaintenanceDetail(plateNo, rno, historyObject).execute();
						
					}
				});
        	} catch(Exception e){
        		e.printStackTrace();
        	}
        	
        	
        	
        	((LinearLayout) rootView.findViewById(R.id.history_list_ll)).addView(itemView);
        }
        
        return rootView;
    }
    
    /**
     * 取得區間裡的保修紀錄
     * 
     * @author iiiii
     *
     */
    private class QueryMaintenanceDetail extends AsyncTask<String, Void, JSONArray>{

    	String plateNo;
    	String rno;
    	JSONObject jsonObject;
    	
    	QueryMaintenanceDetail(String plateNo, String rno, JSONObject jsonObject){
    		this.plateNo = plateNo;
    		this.rno = rno;
    		this.jsonObject = jsonObject;
    	}
    	
		@Override
		protected JSONArray doInBackground(String... params) {
			
			return HttpUtil.fetchMaintenanceRepairDetail(HttpUtil.getToken(), plateNo, rno);
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
			FragmentController.changeFragmentInNissanFrame(getFragmentManager(), true, new FunctionMaintenanceRepairDetailFragment(result, jsonObject), "保修紀錄明細");
		}
    	
    }

}
