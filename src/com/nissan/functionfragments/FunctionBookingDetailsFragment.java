package com.nissan.functionfragments;

import org.json.JSONObject;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FunctionBookingDetailsFragment extends Fragment {
    JSONObject detailsJS;
    View rootView;
    String locationName;
    String locationAddr;
    String locationNumber;
    public FunctionBookingDetailsFragment(String locationName, String addr, String phone, JSONObject details) {
        this.detailsJS = details;
        this.locationName = locationName;
        this.locationAddr = addr;
        this.locationNumber = phone;
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
        
        rootView = inflater.inflate(R.layout.function_booking_details, container, false);
        try{
        	((TextView) rootView.findViewById(R.id.booking_detail_tv01)).setText(detailsJS.getString("plateNo"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv02)).setText(detailsJS.getJSONObject("misc").getString("name"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv03)).setText(detailsJS.getJSONObject("misc").getString("mobile"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv04)).setText(detailsJS.getJSONObject("misc").getString("email"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv05)).setText(detailsJS.getString("odo"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv06)).setText(detailsJS.getString("svcAdvisor"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv07)).setText(detailsJS.getString("mechanic"));
        	String[] serviceType = detailsJS.getJSONObject("misc").getString("serviceTypeDisplay").split(",");
        	((TextView) rootView.findViewById(R.id.booking_detail_tv08)).setText((serviceType[0].contains("none")? "":serviceType[0]) + (serviceType[1].contains("none")? "":serviceType[1]));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv09)).setText(detailsJS.getJSONObject("misc").getString("toWaitDisplay"));
        	((TextView) rootView.findViewById(R.id.booking_detail_tv10)).setText("服務據點：" + locationName);
        	((TextView) rootView.findViewById(R.id.booking_detail_tv11)).setText("地址：" + locationAddr);
        	((TextView) rootView.findViewById(R.id.booking_detail_tv12)).setText("電話：" + locationNumber);
        	((TextView) rootView.findViewById(R.id.booking_detail_tv13)).setText("預約時間：" + detailsJS.getString("bookedTime"));
        	
        } catch(Exception e){
        	e.printStackTrace();
        }
        // 回到首頁
        rootView.findViewById(R.id.back_to_homepage).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, MainActivity.mainPageFragment);
			}
		});
        
        return rootView;
    }
    
}
