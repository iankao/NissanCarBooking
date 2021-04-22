package com.nissan.functionfragments;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FunctionMoreFragment extends Fragment {
    
    public FunctionMoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_mainpage_frame, container, false);
        
        rootView.findViewById(R.id.tab_home_btn).setOnClickListener(clicklistener_home);
        rootView.findViewById(R.id.tab_setting_btn).setOnClickListener(clicklistener_setting);
        rootView.findViewById(R.id.tab_more_btn).setOnClickListener(clicklistener_more);
        
        View contentView = inflater.inflate(R.layout.function_more_page, container, false);
        contentView.setPadding(MainActivity.screenWidth / 10, MainActivity.screenWidth / 16, MainActivity.screenWidth / 10, MainActivity.screenWidth / 16);
        
//        rootView.findViewById(R.id.more_upper_content_ll).setPadding(0, MainActivity.screenWidth / 10, 0, MainActivity.screenWidth / 10);
        
        RelativeLayout.LayoutParams upper_params = (RelativeLayout.LayoutParams) contentView.findViewById(R.id.more_upper_content_ll).getLayoutParams();
        contentView.findViewById(R.id.more_upper_content_ll).setPadding(0, MainActivity.screenWidth / 10, 0, 0);
        upper_params.height = MainActivity.screenWidth * 4 / 5;
        contentView.findViewById(R.id.more_upper_content_ll).setLayoutParams(upper_params);
        
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.findViewById(R.id.more_others_app_buttons_ll).getLayoutParams();
        params.height = MainActivity.screenWidth / 5;
        
        contentView.findViewById(R.id.more_others_app_buttons_ll).setLayoutParams(params);
        
        FrameLayout content_fl = (FrameLayout) rootView.findViewById(R.id.main_page_content_fl);
        content_fl.addView(contentView);
        // the buttons
        {
        	int[] buttonsId = {R.id.more_page_btn1, R.id.more_page_btn2, R.id.more_page_btn3, R.id.more_page_btn4};
        	
        	View.OnClickListener listeners_01 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FunctionStandardWebViewFragment publicWebView = new FunctionStandardWebViewFragment();
					publicWebView.setUrl("http://nissan.iux.com.tw/appsvc/owners/parts/list/");
					FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, publicWebView, "原廠零配件");
				}
			};
			
			View.OnClickListener listeners_02 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FunctionStandardWebViewFragment publicWebView = new FunctionStandardWebViewFragment();
					publicWebView.setUrl("http://nissan.iux.com.tw/appsvc/owners/services");
					FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, publicWebView, "愛車服務說明");
				}
			};
			
			View.OnClickListener listeners_03 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:0800088888"));
					startActivity(callIntent);
				}
			};
			
			View.OnClickListener listeners_04 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentController.changeFragmentWithFucntionFrame(getFragmentManager(), true, new FunctionOpinionsFeedbackFragment(), "意見反映");
				}
			};
        	
        	View.OnClickListener[] listeners = {listeners_01, listeners_02, listeners_03, listeners_04};
        	for(int i=0;i<4;i++){
        		rootView.findViewById(buttonsId[i]).setOnClickListener(listeners[i]);
        	}
        	
        }
        // sub buttons
        {
        	int[] subButtonsId = {R.id.more_sub_btn1, R.id.more_sub_btn2, R.id.more_sub_btn3, R.id.more_sub_btn4};
        	
        	View.OnClickListener listeners_sub01 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setData(Uri.parse("https://www.facebook.com/nissan.tw"));
				    startActivity(i);
				}
			};
			
			View.OnClickListener listeners_sub02 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setData(Uri.parse("http://line.naver.jp/ti/p/%40nissan.tw"));
				    startActivity(i);}
				};
			
			View.OnClickListener listeners_sub03 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setData(Uri.parse("https://www.youtube.com/user/NISSANTAIWAN"));
				    startActivity(i);
				}
			};
			
			View.OnClickListener listeners_sub04 = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setData(Uri.parse("https://www.youtube.com/user/NISSANTAIWAN"));
				    startActivity(i);				}
			};
        	
        	View.OnClickListener[] listeners = {listeners_sub01, listeners_sub02, listeners_sub03, listeners_sub04};
        	for(int i=0;i<4;i++){
        		rootView.findViewById(subButtonsId[i]).setOnClickListener(listeners[i]);
        	}
        	
        }
        
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
