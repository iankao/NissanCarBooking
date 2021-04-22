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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FunctionEditPersonalInfoFragment extends Fragment {

    String myPointsWebUrl = "http://www.bifter.co.uk/";
    
    View rootView;
    
    public FunctionEditPersonalInfoFragment() {
    }

    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	new Thread(new Runnable(){
			@Override
			public void run() {
				HttpUtil.gatherOwnerInfo(HttpUtil.getToken());
			}
        }).start();
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
        
        rootView = inflater.inflate(R.layout.function_edit_personal_info_page, container, false);
        final EditText name_et = (EditText)rootView.findViewById(R.id.name_et);
        final EditText number_et = (EditText)rootView.findViewById(R.id.number_et);
        final EditText email_et = (EditText)rootView.findViewById(R.id.email_et);
        final RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.gender_rg);
        final EditText birthday_et = (EditText)rootView.findViewById(R.id.birthday_et);
        final CheckBox edmSubscribed_cb = (CheckBox) rootView.findViewById(R.id.edmSubscribed_cb);
        
        name_et.setText(OwnerInfo.name);
        number_et.setText(OwnerInfo.mobile);
        email_et.setText(OwnerInfo.email);
        if(OwnerInfo.gender.equals("M")) 
        	rg.check(R.id.male);
        else if(OwnerInfo.gender.equals("F")) 
        	rg.check(R.id.female);
        birthday_et.setText(OwnerInfo.birthdate);
        edmSubscribed_cb.setChecked(OwnerInfo.edmSubscribed);
        
        rootView.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String securityId;
				String name;
				String email;
				String mobile;
				String gender = null;
				String birthdate;
				boolean edmSubscribed;
				
				securityId = OwnerInfo.securityId;
				name = name_et.getText().toString().trim();
				email = email_et.getText().toString().trim();
				mobile = number_et.getText().toString().trim();
				if(rg.getCheckedRadioButtonId()==R.id.male) gender = "M";
				else if(rg.getCheckedRadioButtonId()==R.id.female) gender = "F";
				birthdate = birthday_et.getText().toString().trim();
				edmSubscribed = edmSubscribed_cb.isChecked();
				
				new EditPersonalInfo(securityId, name, email, mobile, gender, birthdate, edmSubscribed).execute();
			}
		});
        
        return rootView;
    }
    
	
	private class EditPersonalInfo extends AsyncTask<String, Void, String>{

		String securityId;
		String name;
		String email;
		String mobile;
		String gender;
		String birthdate;
		boolean edmSubscribed;
		
		
		public EditPersonalInfo(String securityId, String name, String email, String mobile, String gender, String birthdate, boolean edmSubscribed){
			this.securityId = securityId;
			this.name = name;
			this.email = email;
			this.mobile = mobile;
			this.gender = gender;
			this.birthdate = birthdate;
			this.edmSubscribed = edmSubscribed;
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			return HttpUtil.editOwnerPersonalInfo(HttpUtil.getToken(), securityId, name, email, mobile, gender, birthdate, edmSubscribed);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，個人資料已變更").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				
				new Thread(new Runnable(){
					@Override
					public void run() {
						HttpUtil.gatherOwnerInfo(HttpUtil.getToken());
					}
		        }).start();
				
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
		}
		
	}
	
}
