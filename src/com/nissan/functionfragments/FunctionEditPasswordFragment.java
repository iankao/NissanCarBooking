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
import android.widget.EditText;
import android.widget.TextView;

public class FunctionEditPasswordFragment extends Fragment {

    String myPointsWebUrl = "http://www.bifter.co.uk/";
    
    View rootView;
    
    public FunctionEditPasswordFragment() {
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
        
        rootView = inflater.inflate(R.layout.function_edit_password_page, container, false);
        ((TextView) rootView.findViewById(R.id.owner_id_tv)).setText(OwnerInfo.securityId);
        
        final EditText et1 = (EditText) rootView.findViewById(R.id.old_password_et);
        final EditText et2 = (EditText) rootView.findViewById(R.id.new_password_et);
        final EditText et3 = (EditText) rootView.findViewById(R.id.confirm_password_et);
        
        rootView.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String oldPwd = et1.getText().toString().trim();
				String newPwd = et2.getText().toString().trim();
				String cofirmPwd = et3.getText().toString().trim();
				
				if(!newPwd.equals(cofirmPwd)){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("請重新確認新密碼").setTitle("訊息")
					.setPositiveButton("了解", null);
					builder.create().show();
					return;
				}
				if(newPwd.contains(" ")||newPwd.contains("\"")){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("含有不符規則字元，請修改").setTitle("訊息")
					.setPositiveButton("了解", null);
					builder.create().show();
					return;
				}
				if(newPwd.length() < 6 || newPwd.length() > 20){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("字元長度過長或過短，請輸入6至20位數字元").setTitle("訊息")
					.setPositiveButton("了解", null);
					builder.create().show();
					return;
				}
				new ChangePassword(oldPwd, newPwd).execute();
				
			}
		});
        
        return rootView;
    }
    
	
	private class ChangePassword extends AsyncTask<String, Void, String>{

		String oldPwd;
		String newPwd;
		
		public ChangePassword(String oldP, String newP){
			this.oldPwd = oldP;
			this.newPwd = newP;
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			return HttpUtil.changeOwnerPassword(HttpUtil.getToken(), oldPwd, newPwd);
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("設定成功，密碼已變更").setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
				((EditText) rootView.findViewById(R.id.old_password_et)).setText("");
				((EditText) rootView.findViewById(R.id.new_password_et)).setText("");
				((EditText) rootView.findViewById(R.id.confirm_password_et)).setText("");
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(result).setTitle("訊息")
				.setPositiveButton("了解", null);
				builder.create().show();
			}
		}
		
	}
	
}
