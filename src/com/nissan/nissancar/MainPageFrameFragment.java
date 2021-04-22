package com.nissan.nissancar;

import com.nissan.functionfragments.FunctionMoreFragment;
import com.nissan.functionfragments.FunctionSettingFragment;
import com.nissan.functionfragments.MainPageFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 首頁的框架
 * 
 * @author iiiii
 *
 */
public class MainPageFrameFragment extends Fragment {
    
    View rootView;
    TextView functionTitleTV;
    String functionTitle;
    int mFuncType;
    
    MainPageFrameFragment(){
        
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mainpage_frame, null);
        
        LayoutParams title_params = rootView.findViewById(R.id.main_page_title_bar).getLayoutParams();
        title_params.height = (int) (MainActivity.screenWidth / 3);
        title_params.width = (int) (MainActivity.screenWidth * 7 / 9 );
        rootView.findViewById(R.id.bottom_option_ll).setLayoutParams(title_params);
        
        LayoutParams params = rootView.findViewById(R.id.bottom_option_ll).getLayoutParams();
        params.width = MainActivity.screenWidth;
        params.height = (int) (MainActivity.screenWidth / 5.5f);
        rootView.findViewById(R.id.bottom_option_ll).setLayoutParams(params);
        
        rootView.findViewById(R.id.tab_home_btn).setOnClickListener(clicklistener_home);
        rootView.findViewById(R.id.tab_setting_btn).setOnClickListener(clicklistener_setting);
        rootView.findViewById(R.id.tab_more_btn).setOnClickListener(clicklistener_more);
        return rootView;
    }
    
    public void setFunctionTitle(String title){
        functionTitle = title;
    }
    
    //首頁 的clicklistener
    View.OnClickListener clicklistener_home = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, new MainPageFragment());
            
        }
    };
    //設定 的clicklistener
    View.OnClickListener clicklistener_setting = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, new FunctionSettingFragment());
            
        }
    };
    //更多 的clicklistener
    View.OnClickListener clicklistener_more = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithMainFrame(((Activity)v.getContext()).getFragmentManager(), false, new FunctionMoreFragment());
            
        }
    };
}
