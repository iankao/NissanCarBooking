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

public class FunctionEditOwnerInfoFragment extends Fragment {

    String myPointsWebUrl = "http://www.bifter.co.uk/";
    
    public FunctionEditOwnerInfoFragment() {
    }

    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
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
        
        View rootView = inflater.inflate(R.layout.function_edit_owner_info, container, false);
        //修改密碼
        rootView.findViewById(R.id.edit_password).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionEditPasswordFragment(), "修改車主資料");
			}
		});
        //修改個人基本資料
        rootView.findViewById(R.id.edit_personal_info).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionEditPersonalInfoFragment(), "修改車主資料");
			}
		});
        //登錄愛車
		rootView.findViewById(R.id.register_your_car).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentController.changeFragmentInNissanFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionEditCarsFragment(), "修改車主資料");
			}
		});
        
        return rootView;
    }
    
}
