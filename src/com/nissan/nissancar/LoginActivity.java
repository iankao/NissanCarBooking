package com.nissan.nissancar;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.nissan.functionfragments.FunctionLognInWebViewFragment;
import com.nissan.utility.HttpUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginActivity extends Activity {

    
    ViewGroup contentView;
    View btnView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View loginView = LayoutInflater.from(this).inflate(R.layout.nissan_login_layout, null);
        loginView.findViewById(R.id.start_to_use_app_btn).setOnClickListener(start_to_use_app);
        
        contentView = (ViewGroup) loginView.findViewById(R.id.login_content_fl);
        btnView = loginView.findViewById(R.id.start_to_use_app_btn);
        
        setContentView(loginView);
    }
    
    View.OnClickListener start_to_use_app =  new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            contentView.removeAllViews();
            btnView.setVisibility(View.GONE);
            FragmentController.changeFragment(getFragmentManager(), false, R.id.login_content_fl, 
                    new FunctionLognInWebViewFragment());
            
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        
    }

    @Override
    protected void onPause() {
        
        super.onPause();
    }

}
