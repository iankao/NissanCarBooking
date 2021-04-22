package com.nissan.functionfragments;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FunctionMaintenanceRepairDetailFragment extends Fragment {

	View rootView;
	JSONArray repairDetail;
	JSONObject jsonObject;
	/**
	 * 
	 * @param repairDetail
	 * @param jsonObject 
	 */
    public FunctionMaintenanceRepairDetailFragment(JSONArray repairDetail, JSONObject jsonObject) {
        this.repairDetail = repairDetail;
        this.jsonObject = jsonObject;
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.function_maintenance_repair_detail, container, false);
        try{
        	((TextView) rootView.findViewById(R.id.details_tv1)).setText(jsonObject.getString("date"));
        	((TextView) rootView.findViewById(R.id.details_tv2)).setText(jsonObject.getString("dealerDept"));
        	((TextView) rootView.findViewById(R.id.details_tv3)).setText(jsonObject.getString("mechanic"));
        	((TextView) rootView.findViewById(R.id.details_tv4)).setText(jsonObject.getString("odoMeters"));
        	((TextView) rootView.findViewById(R.id.details_tv5)).setText(jsonObject.getString("fee"));
        } catch(Exception e){
        	e.printStackTrace();
        }
        
        for(int i=0;i<repairDetail.length();i++){
        	View itemView = inflater.inflate(R.layout.repair_detail_item_view, null);
        	if(i%2==0) itemView.setBackgroundColor(0xff666666);
        	else itemView.setBackgroundColor(0xff474747);
        	try{
	        	((TextView) itemView.findViewById(R.id.repair_list_tv1)).setText(String.valueOf(repairDetail.getJSONObject(i).getDouble("price")));
	            ((TextView) itemView.findViewById(R.id.repair_list_tv2)).setText(repairDetail.getJSONObject(i).getString("item"));
	            ((TextView) itemView.findViewById(R.id.repair_list_tv3)).setText(String.valueOf(repairDetail.getJSONObject(i).getDouble("qty")));
        	} catch(Exception e){
        		e.printStackTrace();
        	}
        	((LinearLayout) rootView.findViewById(R.id.repair_list_ll)).addView(itemView);
        }
        
        return rootView;
    }
   

}
