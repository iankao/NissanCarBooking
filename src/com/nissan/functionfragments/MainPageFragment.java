package com.nissan.functionfragments;

import java.io.ObjectInputStream.GetField;

import org.apache.http.HttpResponse;

import com.nissan.nissancar.FragmentController;
import com.nissan.nissancar.MainActivity;
import com.nissan.nissancar.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainPageFragment extends Fragment {
    
    public MainPageFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_mainpage_frame, container, false);
        
        rootView.findViewById(R.id.tab_home_btn).setOnClickListener(clicklistener_home);
        rootView.findViewById(R.id.tab_setting_btn).setOnClickListener(clicklistener_setting);
        rootView.findViewById(R.id.tab_more_btn).setOnClickListener(clicklistener_more);
        
        View contentView = inflater.inflate(R.layout.fragment_main2, null);
        
        FrameLayout content_fl = (FrameLayout) rootView.findViewById(R.id.main_page_content_fl);
        //inflater.inflate(R.layout.fragment_main2, content_fl, false);
        FrameLayout home_page_content_fl = (FrameLayout) contentView.findViewById(R.id.home_page_content_fl);
        home_page_content_fl.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth, MainActivity.screenWidth));
        content_fl.addView(contentView);
        
        generateFuncButton(home_page_content_fl);
        
        return rootView;
    }
    
    
    
    @Override
	public void onResume() {
		super.onResume();
	}

	private void generateFuncButton(FrameLayout contentView) {
        
        Button centerBtn = new Button(getActivity());
        centerBtn.setBackgroundResource(R.drawable.selector_button_center);
        FrameLayout.LayoutParams centerParams = new FrameLayout.LayoutParams(MainActivity.screenWidth/4, MainActivity.screenWidth/4);
        centerParams.gravity = Gravity.CENTER;
        centerBtn.setLayoutParams(centerParams);
        contentView.addView(centerBtn);
        centerBtn.setOnClickListener(clicklistener_btn_center);
        
        Button[] buttonSet = new Button[6];
        View.OnClickListener[] clickListeners = genViewClicklisteners();
        for(int i = 0;i<6;i++){
            buttonSet[i] = new Button(getActivity());
            
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MainActivity.screenWidth/5, MainActivity.screenWidth/5);
            params.leftMargin = (int) ((MainActivity.screenWidth * 2 / 5) + MainActivity.screenWidth / 3 * Math.sin(Math.PI * i / 3));
            params.topMargin = (int) ((MainActivity.screenWidth * 2 / 5) - MainActivity.screenWidth / 3 * Math.cos(Math.PI * i / 3));
            buttonSet[i].setLayoutParams(params);
            buttonSet[i].setBackgroundResource(selectors[i]);
            buttonSet[i].setOnClickListener(clickListeners[i]);
            contentView.addView(buttonSet[i]);
        }
        
    }
    
    int[] selectors = {R.drawable.selector_button_01, R.drawable.selector_button_02,R.drawable.selector_button_03,
            R.drawable.selector_button_04,R.drawable.selector_button_05,R.drawable.selector_button_06,};

    //愛車資訊的clicklistener
    View.OnClickListener clicklistener_btn_center = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionCarInformationFragment(), "愛車資訊");
            
        }
    };
    
    /**
     * 產生六個 View.onClickListener，用在首頁的六個環狀排列的 Button
     * 
     * @return View.OnClickListener[]
     */
    View.OnClickListener[] genViewClicklisteners(){
    	//最新消息的clicklistener
        View.OnClickListener clicklistener_btn001 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionTheNewsFragment(), "最新消息");
                
            }
        };
        
        //我的紅利的clicklistener
        View.OnClickListener clicklistener_btn002 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionMyPointFragment(), "我的紅利");
                
            }
        };
        
        // 預約保修的clicklistener
        View.OnClickListener clicklistener_btn003 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionBookingServiceFragment(), "預約保修");
                
            }
            
        };
        
        //保修紀錄的clicklistener
        View.OnClickListener clicklistener_btn004 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionMaintenanceHistoryFragment(), "保修紀錄");
                
            }
        };
        
        //服務據點的clicklistener
        View.OnClickListener clicklistener_btn005 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionServiceLocationFragment(), "服務據點查詢");
                
            }
        };
        
        //道路救援的clicklistener
        View.OnClickListener clicklistener_btn006 = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                FragmentController.changeFragmentWithFucntionFrame(((Activity)v.getContext()).getFragmentManager(), true, new FunctionEmergencyOnRoadFragment(), "道路救援");
                
            }
        };
        
        View.OnClickListener[] clickListeners = {clicklistener_btn001, clicklistener_btn002, clicklistener_btn003, clicklistener_btn004, clicklistener_btn005, clicklistener_btn006};
        
        return clickListeners;
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
