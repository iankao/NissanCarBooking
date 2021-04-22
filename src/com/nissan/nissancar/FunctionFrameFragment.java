package com.nissan.nissancar;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FunctionFrameFragment extends Fragment {
    
    View rootView;
    TextView functionTitleTV;
    String functionTitle = "";
    int mFuncType;
    
    FunctionFrameFragment(){
        
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        rootView = inflater.inflate(R.layout.fragment_function_frame, null);
        
        LayoutParams params = (LayoutParams) rootView.findViewById(R.id.nissan_title_bar_back_button).getLayoutParams();
        params.height = MainActivity.screenWidth / 8;
        params.width = MainActivity.screenWidth / 8;
        params.setMargins(MainActivity.screenWidth / 40, MainActivity.screenWidth / 40, MainActivity.screenWidth / 40, MainActivity.screenWidth / 40);
        rootView.findViewById(R.id.nissan_title_bar_back_button).setLayoutParams(params);
        
        LayoutParams divider_params = (LayoutParams) rootView.findViewById(R.id.divider).getLayoutParams();
        divider_params.height = MainActivity.screenWidth / 8;
        rootView.findViewById(R.id.divider).setLayoutParams(divider_params);
        
        changeFunctionTitle(functionTitle);
        rootView.findViewById(R.id.nissan_title_bar_back_button).setOnClickListener(new OnClickListener(){
              @Override
              public void onClick(View v) {
                  getFragmentManager().popBackStack();
              }});
        
        return rootView;
    }
    
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    public void setFunctionTitle(String title){
        functionTitle = title;
    }
    /**
     * 改變 title bar 的 title
     * 
     * @param title 設定為 null，會把 title 設定為 ""
     */
    public void changeFunctionTitle(String title){
    	((TextView) rootView.findViewById(R.id.function_title_tv)).setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.screenWidth / 18);
    	if(title == null){
    		((TextView) rootView.findViewById(R.id.function_title_tv)).setText("");
    	}
    	else{
    		((TextView) rootView.findViewById(R.id.function_title_tv)).setText(title);
    	}
        
    }

}
