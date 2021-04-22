package com.nissan.nissancar;

import com.nissan.functionfragments.FunctionCarInformationFragment;
import com.nissan.utility.FuncPreference;
import com.nissan.utility.HttpUtil;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

public class FragmentController {
    
    /**
     * 直接把 Fragment 加入指定的 container
     * 
     * @param fm
     * @param backStack
     * @param containerViewId
     * @param fragment
     */
    public static void changeFragment(FragmentManager fm, boolean backStack, int containerViewId, Fragment fragment){
        
        if(fm == null){
            android.util.Log.d("FragmentController", "FramgmentManager is null !!!!");
            return;
        }
        
        FragmentTransaction ft = fm.beginTransaction();
        
        if(backStack){
            ft.addToBackStack(null);
        }
            
        ft.replace(containerViewId, fragment);
        ft.commit();
    }

    /**
     * 加入到有Nissan Mark的框架底下，順便改變框架的title
     * 
     * @param fm
     * @param backStack
     * @param fragment
     * @param title
     */
    public static void changeFragmentInNissanFrame(FragmentManager fm, boolean backStack, Fragment fragment, String title){
        
        MainActivity.funcFrame.changeFunctionTitle(title);
        
        changeFragment(fm, backStack, R.id.function_content_fl, fragment);
        
    }
    
    /**
     * 加入 fragment 到有 mainpage 框架底下的 container
     * 
     * @param fm
     * @param backStack
     * @param fragment
     */
    public static void changeFragmentInMainpageFrame(FragmentManager fm, boolean backStack, Fragment fragment){
        
        changeFragment(fm, backStack, R.id.main_page_content_fl, fragment);
        
    }
    
    /**
     * 加入 fragment 到有 function 框架底下的 container
     * 
     * @param fm
     * @param backStack
     * @param fragment
     */
    public static void changeFragmentInFunctionFrame(FragmentManager fm, boolean backStack, Fragment fragment){
        
        changeFragment(fm, backStack, R.id.function_content_fl, fragment);
        
    }
    
    /**
     * 加入一個有 Mainpage 框架的 Fragment
     * 
     * @param fm
     * @param backStack
     * @param fragment
     */
    public static void changeFragmentWithMainFrame(FragmentManager fm, boolean backStack, Fragment fragment){
        
        //changeFragment(fm, backStack, R.id.container, MainActivity.mainFrame);
        
        changeFragment(fm, backStack, R.id.container, fragment);
        
    }
    
    
    /**
     * 加入一個有 功能頁面 外框的 Fragment
     * 
     * @param fm
     * @param backStack
     * @param fragment
     * @param funcTitle 帶入 null 就不會設定 title，並且把 title view 設定為 gone
     */
    public static void changeFragmentWithFucntionFrame(FragmentManager fm, boolean backStack, Fragment fragment, String funcTitle){
        
    	MainActivity.funcFrame.setFunctionTitle(funcTitle);
    		
        changeFragment(fm, backStack, R.id.container, MainActivity.funcFrame);
        
        changeFragment(fm, false, R.id.function_content_fl, fragment);
        
    }
    
}
