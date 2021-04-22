package com.nissan.functionfragments;

import com.nissan.nissancar.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FunctionStandardWebViewFragment extends Fragment {

    String url = "http://www.bifter.co.uk/";
    
    public FunctionStandardWebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.function_general_webview, container, false);
        WebView wv = (WebView) rootView.findViewById(R.id.my_point_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl(url);
        
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    public void setUrl(String url){
    	this.url = url;
    }
    

}
