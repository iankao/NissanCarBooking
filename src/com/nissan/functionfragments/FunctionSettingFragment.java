package com.nissan.functionfragments;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FunctionSettingFragment extends Fragment {
    
    public FunctionSettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	String versionName = "";
		try {
			versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	
        //View rootView = inflater.inflate(R.layout.function_setting_page, container, false);
        
        View rootView = inflater.inflate(R.layout.fragment_mainpage_frame, container, false);
        View contentView = inflater.inflate(R.layout.function_setting_page, container, false);
        
        rootView.findViewById(R.id.tab_home_btn).setOnClickListener(clicklistener_home);
        rootView.findViewById(R.id.tab_setting_btn).setOnClickListener(clicklistener_setting);
        rootView.findViewById(R.id.tab_more_btn).setOnClickListener(clicklistener_more);
        
        contentView.findViewById(R.id.app_rate_rl).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
			}
		});
        
        contentView.findViewById(R.id.terms_rl).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FunctionStandardWebViewFragment publicWebView = new FunctionStandardWebViewFragment();
				publicWebView.setUrl("http://nissan.iux.com.tw/appsvc/info/terms");
				FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, publicWebView, "會員條款");
			
			}
		});
        
        contentView.findViewById(R.id.privacy_rl).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FunctionStandardWebViewFragment publicWebView = new FunctionStandardWebViewFragment();
				publicWebView.setUrl("http://nissan.iux.com.tw/appsvc/info/privacy");
				FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, publicWebView, "隱私權聲明");
			
			}
		});
        
        FrameLayout content_fl = (FrameLayout) rootView.findViewById(R.id.main_page_content_fl);
        content_fl.addView(contentView);
        
        ((TextView) contentView.findViewById(R.id.version_tv02)).setText(versionName);
        
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    //首頁 的clicklistener
    View.OnClickListener clicklistener_home = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, MainActivity.mainPageFragment);
            
        }
    };
    
    //設定 的clicklistener
    View.OnClickListener clicklistener_setting = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, MainActivity.functionSettingFragment);
            
        }
    };
    //更多 的clicklistener
    View.OnClickListener clicklistener_more = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, MainActivity.functionMoreFragment);
            
        }
    };
}
