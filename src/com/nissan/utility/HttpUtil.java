package com.nissan.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import com.nissan.nissancar.LoginActivity;

public class HttpUtil {
    
    static final String TAG = "HttpUtil";
    
	static String mToken = "notoken";
    //static String mToken = "khmf67mf";
    
	static final int JS_API_LOGIN = 8001;
	static final int JS_API_GET_VERSION = 8002;
	static final int JS_API_SELF = 8003;
	static final int JS_API_OWNED_CARS = 8004;
	static final int JS_API_ADD_OWNED_CARS = 8005;
	static final int JS_API_REMOVE_OWNED_CARS = 8006;
	static final int JS_API_BONUS_HISTORY = 8007;
	static final int JS_API_ADD_SERVICE_BOOKING = 8008;
	static final int JS_API_MAINTENANCE_HISTORY = 8009;
	static final int JS_API_SELF_WITH_OWNED_CARS = 8010;
	static final int JS_API_GATHER_SERVICE_CITIES = 8011;
	static final int JS_API_GATHER_SALES_CITIES = 8012;
	static final int JS_API_GATHER_DISTRICT_IN_CITY = 8013;
	static final int JS_API_GATHER_FACTORY_AVAILABLE_DATE = 8014;
	static final int JS_API_GET_OWNED_CAR_DETAILS = 8015;
	static final int JS_API_SERVICE_BOOKING_HISTORY = 8016;
	static final int JS_API_SET_DEFAULT_BONUS_CAR = 8017;
	static final int JS_API_CHANGE_PASSWORD = 8018;
	static final int JS_API_EDIT_PERSONAL_INFO = 8019;
	static final int JS_API_BOOKING_SERVICE = 8020;
	static final int JS_API_GATHER_FACTORY_AVAILABLE_TIME = 8021;
	static final int JS_API_SVC_ADVISORS_AVAILABLE = 8022;
	static final int JS_API_MECHANICS_AVAILABLE = 8023;
	static final int JS_API_MAINTENANCE_REPAIR_DETAIL = 8024;
	static final int JS_API_SERVICE_LOCATION_DETAIL = 8025;
	static final int JS_API_ADD_FAVORITE_LOCATION = 8026;
	static final int JS_API_REMOVE_FAVORITE_LOCATION = 8027;
	static final int JS_API_DELETE_SERVICE_BOOKING = 8028;
	
    public static String getToken(){
    	return mToken;
    }
    
    public static void setToken(String tkn){
        mToken = tkn;
    }
    
    public static String checkTokenStatus(){
        if(HttpUtil.getToken().equals("notoken")){
            return "error_with_notoken";
        }
        String tokenStatus = HttpUtil.confirmTokenStatus(HttpUtil.getToken());
        return tokenStatus;
    }
    
    /**
     * 依據功能型別去取得 HttpResponse，然後再送去解析，parse出需要的資料
     * 
     * 
     * @param ApiType
     * @param nameValuePair 相對應填入的參數
     * 
     * @return HttpResponse 直接回傳再解析
     * 
     */
    public static HttpResponse sendQueryCommand(int ApiType, List<BasicNameValuePair> nameValuePair){
        
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(generateURIByApiType(ApiType)); 
        //post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = hc.execute(post);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 依據功能型別，去決定query的url應該帶什麼
     * 
     * @param apiType
     * @return
     */
    static String generateURIByApiType(int apiType){
        
    	String host = "http://nissan-svc.iux.com.tw/api/";
    	
        switch(apiType){
        case HttpUtil.JS_API_ADD_OWNED_CARS:
            return host + "Owner/addOwnedCar";
        case HttpUtil.JS_API_BONUS_HISTORY:
            return host + "Owner/bonusHistory";
        case HttpUtil.JS_API_GET_VERSION:
            return host + "Owner/getVersion";
        case HttpUtil.JS_API_LOGIN:
            return host + "Member/login?deviceId=x123&";
        case HttpUtil.JS_API_MAINTENANCE_HISTORY:
            return host + "Owner/maintenanceHistory";
        case HttpUtil.JS_API_ADD_SERVICE_BOOKING:// 新增預約保修
            return host + "Owner/addServiceBooking";
        case HttpUtil.JS_API_OWNED_CARS:// 取得 owner 所擁有車
            return host + "Owner/ownedCars";
        case HttpUtil.JS_API_REMOVE_OWNED_CARS:
            return host + "Owner/removeOwnedCar";
        case HttpUtil.JS_API_SELF:// 取得 owner 資料(不包含所擁有車)
            return host + "Owner/self?ownedCarsIncluded=false&";
        case HttpUtil.JS_API_SELF_WITH_OWNED_CARS: // 取得 owner 資料(包含所擁有車)
            return host + "Owner/self?ownedCarsIncluded=true&";
        case HttpUtil.JS_API_GATHER_SERVICE_CITIES:
            return host + "Owner/citiesServiced";
        case HttpUtil.JS_API_GATHER_SALES_CITIES:
            return host + "Owner/citiesServiced";
        case HttpUtil.JS_API_GATHER_DISTRICT_IN_CITY:
            return host + "Owner/districtsServiced";
        case HttpUtil.JS_API_GATHER_FACTORY_AVAILABLE_DATE:
        	return host + "Owner/datesAvailable";
        case HttpUtil.JS_API_GET_OWNED_CAR_DETAILS:
        	return host + "Owner/ownedCarDetail";
        case HttpUtil.JS_API_SERVICE_BOOKING_HISTORY:
        	return host + "Owner/ServiceBookingHistory";
        case HttpUtil.JS_API_SET_DEFAULT_BONUS_CAR:
        	return host + "Owner/setCarBonusDefault";
        case HttpUtil.JS_API_CHANGE_PASSWORD:
        	return host + "Owner/changePassword";
        case HttpUtil.JS_API_EDIT_PERSONAL_INFO:
        	return host + "Owner/updateSelf";
        case HttpUtil.JS_API_BOOKING_SERVICE:
        	return host + "Owner/addServiceBooking";
        case HttpUtil.JS_API_GATHER_FACTORY_AVAILABLE_TIME:
        	return host + "Owner/serviceSlotsAvailable";
        case HttpUtil.JS_API_SVC_ADVISORS_AVAILABLE:
        	return host + "Owner/svcAdvisorsAvailable";
        case HttpUtil.JS_API_MECHANICS_AVAILABLE:
        	return host + "Owner/mechanicsAvailable";
        case HttpUtil.JS_API_MAINTENANCE_REPAIR_DETAIL:
        	return host + "Owner/maintenanceDetail";
        case HttpUtil.JS_API_SERVICE_LOCATION_DETAIL:
        	return host + "Owner/locationPackage";
        case HttpUtil.JS_API_ADD_FAVORITE_LOCATION:
        	return host + "Owner/addPreferredLocation";
        case HttpUtil.JS_API_REMOVE_FAVORITE_LOCATION:
        	return host + "Owner/removePreferredLocation";
        case HttpUtil.JS_API_DELETE_SERVICE_BOOKING:
        	return host + "Owner/DeleteServiceBooking";
        }
        return null;
    }
    
    /**
     * 用來 Parse HttpResponse 成為一個 JSONObject
     * 
     * @param response
     * @return
     */
    private static JSONObject parseJSONObject(HttpResponse response){
        
        try {
            InputStream is;
            is = response.getEntity().getContent();
            BufferedReader BReader = new BufferedReader(new InputStreamReader(is));
            
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line= BReader.readLine()) != null){
                sb.append(line);
                if(FuncPreference.DEBUG) android.util.Log.d("ianTest","line:" + sb.toString().trim());
            }
            
            if(FuncPreference.DEBUG) android.util.Log.d("ianTest","whole line:" + sb.toString().trim());
            
            JSONObject JO = new JSONObject(sb.toString().trim());
            return JO;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 用來確認token狀態，如果正確，回傳 "version_版本號"<BR>
     * 如果有 error, 回傳 error 中的 "code_message "
     * 
     * 
     * @param token
     * @return String 回傳字串來判斷結果
     */
    public static String confirmTokenStatus(String token){
        
        HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GET_VERSION, NameValuePairComposer.generalNameValuePair(getToken()));
        JSONObject jo = parseJSONObject(response);
        
        try{
            JSONObject resultJSON = jo.getJSONObject("result");
            return "version_" + resultJSON.getString("version");

        } catch (Exception e) {
            e.printStackTrace();
            try{
                JSONObject errorJSON = jo.getJSONObject("error");
                String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
                return errorCode;
            } catch (Exception ex){
                
                return null;
            }
            
        }
        
    }

    /**
     * 取得 OwnerInfo 暫存在記憶體當中
     * 
     * @param token
     * @return
     */
    public static String gatherOwnerInfo(String token){
        
        HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SELF, NameValuePairComposer.generalNameValuePair(getToken()));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONObject resultJSON = jo.getJSONObject("result");
            OwnerInfo.feedOwnerInfo(resultJSON);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
        
    }
    
    /**
     * 取得 OwnerInfo 暫存在記憶體當中
     * 
     * @param token
     * @return
     */
    public static String getOwnedCars(String token){
        
        HttpResponse response = sendQueryCommand(HttpUtil.JS_API_OWNED_CARS, NameValuePairComposer.generalNameValuePair(getToken()));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            OwnerInfo.feedOwnedCars(resultJSON);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
        
    }
    /**
     * 取得所有城市
     * 
     * @param token
     * @return
     */
    public static String gatherServicedCities(String token){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GATHER_SERVICE_CITIES, NameValuePairComposer.servicedCitiesNameValuePair(getToken(), "service"));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            OwnerInfo.servicedCitiesName = new String[resultJSON.length()];
            OwnerInfo.servicedCitiesId = new String[resultJSON.length()];
            for(int i=0;i<resultJSON.length();i++){
            	OwnerInfo.servicedCitiesId[i] = ((JSONObject) resultJSON.get(i)).getString("id");
            	OwnerInfo.servicedCitiesName[i] = ((JSONObject) resultJSON.get(i)).getString("name");
            }
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 根據 城市的 id 取得對應的區，以及 locations(裡面包含該區所有維修廠的資料)
     * 
     * @param token
     * @param cityId
     * @return
     */
    public static String gatherServicedDistrictInCitiy(String token, String cityId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GATHER_DISTRICT_IN_CITY, NameValuePairComposer.servicedDistInCitynameValuePair(getToken(), "service", cityId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            OwnerInfo.servicedDistName = new String[resultJSON.length()];
            OwnerInfo.servicedLocations = new JSONArray[resultJSON.length()];
            for(int i=0;i<resultJSON.length();i++){
            	JSONObject jsonObject = (JSONObject) resultJSON.get(i);
            	OwnerInfo.servicedDistName[i] = jsonObject.getString("name");
            	OwnerInfo.servicedLocations[i] = jsonObject.getJSONArray("locations");
            }
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 根據 城市的 id 取得對應的區，以及 location的  longitude, latitude
     * 
     * @param token
     * @param cityId
     * @return
     */
    public static JSONArray gatherServicedDistrictLocationInCitiy(String token, String cityId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GATHER_DISTRICT_IN_CITY, NameValuePairComposer.servicedDistInCitynameValuePair(getToken(), "service", cityId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
//            JSONArray resultJSON = jo.getJSONArray("result");
            return jo.getJSONArray("result");
//            OwnerInfo.servicedDistName = new String[resultJSON.length()];
//            OwnerInfo.servicedLocations = new JSONArray[resultJSON.length()];
//            for(int i=0;i<resultJSON.length();i++){
//            	JSONObject jsonObject = (JSONObject) resultJSON.get(i);
//            	OwnerInfo.servicedDistName[i] = jsonObject.getString("name");
//            	OwnerInfo.servicedLocations[i] = jsonObject.getJSONArray("locations");
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 用 維修廠的 id 去取得可預約日期
     * 
     * @param token
     * @param factoryId
     * @return
     */
    public static String gatherServicedDate(String token, String factoryId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GATHER_FACTORY_AVAILABLE_DATE, NameValuePairComposer.availableDateNameValuePair(getToken(), factoryId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            OwnerInfo.availableDate = new String[resultJSON.length()];
            OwnerInfo.availableDateIso = new String[resultJSON.length()];
            OwnerInfo.availableDateWeekday = new String[resultJSON.length()];
            for(int i=0;i<resultJSON.length();i++){
            	JSONObject jsonObject = resultJSON.getJSONObject(i);
            	OwnerInfo.availableDate[i] = jsonObject.getString("dateDisplay");
            	OwnerInfo.availableDateIso[i] = jsonObject.getString("iso");
            	OwnerInfo.availableDateWeekday[i] = jsonObject.getString("weekday");
            }
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 用 維修廠的 id 去取得可預約時間
     * 
     * @param token
     * @param factoryId
     * @param date
     * @return
     */
    public static String gatherAvailableTime(String token, String factoryId, String date){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GATHER_FACTORY_AVAILABLE_TIME, NameValuePairComposer.availableTimeNameValuePair(getToken(), factoryId, date));
        JSONObject jo = parseJSONObject(response);
        
        if(jo==null){
        	return "error_fetch_response_error";
        }
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            ArrayList<String> availableTimeList = new ArrayList<String>();
            for(int i=0;i<resultJSON.length();i++){
            	JSONObject jsonObject = resultJSON.getJSONObject(i);
            	if(jsonObject.getBoolean("available")) availableTimeList.add(jsonObject.getString("startTime"));
            }
            OwnerInfo.availableTime = new String[availableTimeList.size()];
            OwnerInfo.availableTime = availableTimeList.toArray(OwnerInfo.availableTime);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 用 維修廠的 id 去取得可以服務的招待
     * 
     * @param token
     * @param locationId
     * @param slotTime
     * @return
     */
    public static String gatherAvailableSVCAdvisors(String token, String locationId, String slotTime){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SVC_ADVISORS_AVAILABLE, NameValuePairComposer.availableSVCAdvisorsNameValuePair(getToken(), locationId, slotTime));
        JSONObject jo = parseJSONObject(response);
        
        if(jo==null){
        	return "error_fetch_response_error";
        }
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            ArrayList<String> availableAdvisorsList = new ArrayList<String>();
            ArrayList<String> availableAdvisorsCodeList = new ArrayList<String>();
            for(int i=0;i<resultJSON.length();i++){
            	JSONObject jsonObject = resultJSON.getJSONObject(i);
            	if(jsonObject.getBoolean("available")) {
            		availableAdvisorsList.add(jsonObject.getString("name"));
            		availableAdvisorsCodeList.add(jsonObject.getString("code"));
            	}
            }
            OwnerInfo.availableAdvisors = new String[availableAdvisorsList.size()];
            OwnerInfo.availableAdvisors = availableAdvisorsList.toArray(OwnerInfo.availableAdvisors);
            OwnerInfo.availableAdvisorsCode = new String[availableAdvisorsCodeList.size()];
            OwnerInfo.availableAdvisorsCode = availableAdvisorsList.toArray(OwnerInfo.availableAdvisorsCode);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 用 維修廠的 id 去取得可以服務的招待
     * 
     * @param token
     * @param locationId
     * @param slotTime
     * @return
     */
    public static String gatherAvailableMechanics(String token, String locationId, String slotTime){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_MECHANICS_AVAILABLE, NameValuePairComposer.availableSVCAdvisorsNameValuePair(getToken(), locationId, slotTime));
        JSONObject jo = parseJSONObject(response);
        
        if(jo==null){
        	return "error_fetch_response_error";
        }
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorCode;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            JSONArray resultJSON = jo.getJSONArray("result");
            ArrayList<String> availableMechanicsList = new ArrayList<String>();
            ArrayList<String> availableMechanicsCodeList = new ArrayList<String>();
            for(int i=0;i<resultJSON.length();i++){
            	JSONObject jsonObject = resultJSON.getJSONObject(i);
            	if(jsonObject.getBoolean("available")) {
            		availableMechanicsList.add(jsonObject.getString("name"));
            		availableMechanicsCodeList.add(jsonObject.getString("code"));
            	}
            }
            OwnerInfo.availableMechanics = new String[availableMechanicsList.size()];
            OwnerInfo.availableMechanicsCode = new String[availableMechanicsCodeList.size()];
            OwnerInfo.availableMechanics = availableMechanicsList.toArray(OwnerInfo.availableMechanics);
            OwnerInfo.availableMechanicsCode = availableMechanicsCodeList.toArray(OwnerInfo.availableMechanicsCode);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "exception_parse_json";
        }
    }
    /**
     * 取得維修的歷史資料
     * 
     * @param token
     * @param plateNo
     * @param startDate YYYY-MM-DD
     * @param endDate   YYYY-MM-DD
     * @return
     */
    public static JSONArray fetchMaintenanceHistory(String token, String plateNo, String startDate, String endDate){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_MAINTENANCE_HISTORY, NameValuePairComposer.maintenanceHistoryNameValuePair(getToken(), plateNo, startDate, endDate));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result").getJSONArray("history");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 取得維修場的營業時段(包含備註如"夜間時段需預約")
     * 
     * @param token
     * @param locationId
     * @return
     */
    public static JSONObject fetchServiceLocationSchedule(String token, String locationId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SERVICE_LOCATION_DETAIL, NameValuePairComposer.availableDateNameValuePair(getToken(), locationId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result").getJSONObject("schedule");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根據 locationId 取得維修場的詳細資料(包含電話，位置，住址...)
     * 
     * @param token
     * @param locationId
     * @return
     */
    public static JSONObject fetchServiceLocationData(String token, String locationId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SERVICE_LOCATION_DETAIL, NameValuePairComposer.availableDateNameValuePair(getToken(), locationId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result").getJSONObject("location");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 把維修廠加入我的最愛
     * 
     * @param token
     * @param locationId
     * @return
     */
    public static String addPreferredLocation(String token, String locationId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_ADD_FAVORITE_LOCATION, NameValuePairComposer.availableDateNameValuePair(getToken(), locationId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
        	return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        try{
        	if(jo.getBoolean("result")) return "true";
        	else return "false";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }
    
    /**
     * 把維修廠自我的最愛移除我的最愛
     * 
     * @param token
     * @param locationId
     * @return
     */
    public static String removePreferredLocation(String token, String locationId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_REMOVE_FAVORITE_LOCATION, NameValuePairComposer.availableDateNameValuePair(getToken(), locationId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
        	return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        try{
        	if(jo.getBoolean("result")) return "true";
        	else return "false";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }
    
    /**
     * 取得維修的歷史資料
     * 
     * @param token
     * @param plateNo
     * @param rno repair number
     * @return
     */
    public static JSONArray fetchMaintenanceRepairDetail(String token, String plateNo, String rno){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_MAINTENANCE_REPAIR_DETAIL, NameValuePairComposer.repairDetailNameValuePair(getToken(), plateNo, rno));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONArray("result");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 取得 愛車資訊 相關細節
     * 
     * @param token
     * @param plateNo
     * @return
     */
    public static JSONObject fetchOwnedCarDetails(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_GET_OWNED_CAR_DETAILS, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 取得"預約"維修的歷史
     * 
     * @param token
     * @param plateNo
     * @return
     */
    public static JSONArray fetchServiceBookingHistory(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SERVICE_BOOKING_HISTORY, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result").getJSONArray("history");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 取得紅利積點的歷史
     * 
     * @param token
     * @param plateNo
     * @return
     */
    public static JSONArray fetchBonusHistoryByPateNo(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_BONUS_HISTORY, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        
        try{
            return jo.getJSONObject("result").getJSONArray("history");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Boolean setBonusMainCar(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_SET_DEFAULT_BONUS_CAR, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return null;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getBoolean("result");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public static String changeOwnerPassword(String token, String oldPwd, String newPwd){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_CHANGE_PASSWORD, NameValuePairComposer.changePWDNameValuePair(getToken(), oldPwd, newPwd));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return "error_occured";
        }
        
    }
    
    public static String editOwnerPersonalInfo(String token, String securityId, String name, String email, String mobile, String gender, String birthdate, boolean edmSubscribed){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_EDIT_PERSONAL_INFO, NameValuePairComposer.changePWDNameValuePair(getToken(), securityId, name, email, mobile, gender, birthdate, edmSubscribed));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return "error_occured";
        }
        
    }
    
    public static String addingNewCar(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_ADD_OWNED_CARS, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return "error_occured";
        }
        
    }
    
    public static String removeCar(String token, String plateNo){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_REMOVE_OWNED_CARS, NameValuePairComposer.ownedCarsNameValuePair(getToken(), plateNo));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return "error_occured";
        }
        
    }
    
    public static JSONObject addServiceBooking(String token, String plateNo, String locationId, String bookedTime, String odo, String mechanicCode, String svcAdvisorCode
    		, String serviceType, boolean toWait, String notes, String name, String email, String mobile){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_ADD_SERVICE_BOOKING, NameValuePairComposer.addServiceBookingNameValuePair(token
    			, plateNo, locationId, bookedTime, odo, mechanicCode, svcAdvisorCode, serviceType, toWait, notes, name, email, mobile));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON;
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getJSONObject("result");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public static String deleteServiceBooking(String token, String plateNo, String locationId, String entryId){
    	HttpResponse response = sendQueryCommand(HttpUtil.JS_API_DELETE_SERVICE_BOOKING, NameValuePairComposer.deleteServiceBookingNameValuePair(token
    			, plateNo, locationId, entryId));
        JSONObject jo = parseJSONObject(response);
        
        try {
        	JSONObject errorJSON;
        	errorJSON = jo.getJSONObject("error");
        	// 如果有發生 error,把訊息 parse 出來
        	String errorCode = "error_" + errorJSON.getString("code") + "_" + errorJSON.getString("message");
        	android.util.Log.d(TAG,"error occur::" + errorCode);
            return errorJSON.getString("message");
		} catch (JSONException e1) {
			// 如果 "error" 的 JSONObject 是 null, 也會跑到 exception
			
		}
        
        try{
            return jo.getBoolean("result")? "true":"flase";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
}
