package com.nissan.functionfragments;

import com.nissan.utility.FuncPreference;
import com.nissan.nissancar.LoginActivity;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;
import com.nissan.utility.HttpUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FunctionLognInWebViewFragment extends Fragment {

    String myPointsWebUrl = "http://nissan-svc.iux.com.tw/api/Member/login?deviceId=x12345";
    
    public FunctionLognInWebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    	myPointsWebUrl = "http://nissan-svc.iux.com.tw/api/Member/login?deviceId=" + tm.getDeviceId();
    	
        View rootView = inflater.inflate(R.layout.function_general_webview, container, false);
        WebView wv = (WebView) rootView.findViewById(R.id.my_point_webview);
        
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new JavaScriptInterface(), "Android");
        wv.getSettings().setBuiltInZoomControls(true);
        wv.setWebViewClient(new MyWebViewClient());
        wv.loadUrl(myPointsWebUrl);
        //wv.loadUrl("file:///android_asset/testjs.html");
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    class MyWebViewClient extends WebViewClient {
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            
            return super.shouldOverrideUrlLoading(view, url);
            
        }
        
        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                HttpAuthHandler handler, String host, String realm) {
            
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            	view.evaluateJavascript("javascript:sendTokenIos()", new CallBack());
//            } else {
//            	view.loadUrl("javascript:var result = sendTokenIos(); window.Android.sendTokenToAndroid(result)");
//            }
            
            
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
        
        private class CallBack implements ValueCallback<String>{

			@Override
			public void onReceiveValue(String value) {
				
				if(value.equals("null")) return;
				
				HttpUtil.setToken(value);
	            Activity loginAct = getActivity();
	            if(loginAct.equals(getActivity())){
	                Intent intent = new Intent(getActivity(), MainActivity.class);
	                startActivity(intent);
	            }
			}
        	
        }
        
    }
    
    public class JavaScriptInterface{
    	@JavascriptInterface
        public boolean sendTokenToAndroid(String token){
            
            HttpUtil.setToken(token);
            Activity loginAct = getActivity();
            if(loginAct.equals(getActivity())){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            
            return true;
        }
    }

}
