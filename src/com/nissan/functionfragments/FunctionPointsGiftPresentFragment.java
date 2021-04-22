package com.nissan.functionfragments;

import com.nissan.nissancar.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FunctionPointsGiftPresentFragment extends Fragment {

    String myPointsWebUrl = "http://nissan.iux.com.tw/appsvc/owners/bonus-collection";
    
    public FunctionPointsGiftPresentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.function_general_webview, container, false);
        WebView wv = (WebView) rootView.findViewById(R.id.my_point_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl(myPointsWebUrl);
        
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    

}
