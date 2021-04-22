package com.nissan.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class NameValuePairComposer {
	/**
     * 產生只含有 token parameter 的 BasicNameValuePair
     */
    static List<BasicNameValuePair> generalNameValuePair(String token){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, type parameter 的 BasicNameValuePair<BR>
     * for citiesServiced api use, type 有兩種:"service"=維修廠, "sales"=展式中心
     */
    static List<BasicNameValuePair> servicedCitiesNameValuePair(String token, String type){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("type", type));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, type, cityId parameter 的 BasicNameValuePair<BR>
     * for citiesServiced api use, type 有兩種:"service"=維修廠, "sales"=展式中心
     */
    static List<BasicNameValuePair> servicedDistInCitynameValuePair(String token, String type, String id){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("type", type));
        nameValuePair.add(new BasicNameValuePair("cityId", id));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, locationId parameter 的 BasicNameValuePair<BR>
     * 依維修廠取得可維修日期
     * 
     */
    static List<BasicNameValuePair> availableDateNameValuePair(String token, String locationId){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("locationId", locationId));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, locationId parameter 的 BasicNameValuePair<BR>
     * 依維修廠取得可維修時間
     * 
     */
    static List<BasicNameValuePair> availableTimeNameValuePair(String token, String locationId, String date){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("locationId", locationId));
        nameValuePair.add(new BasicNameValuePair("date", date));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, locationId, slotTime parameter 的 BasicNameValuePair<BR>
     * 依維修廠取得可維修時間
     * 
     */
    static List<BasicNameValuePair> availableSVCAdvisorsNameValuePair(String token, String locationId, String slotTime){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("locationId", locationId));
        nameValuePair.add(new BasicNameValuePair("slotTime", slotTime));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, plateNo parameter 的 BasicNameValuePair<BR>
     * 為了取得所有車子的資料(許多只用到 token, plateNo 的都會用此)
     * 
     */
    static List<BasicNameValuePair> ownedCarsNameValuePair(String token, String plateNo){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("plateNo", plateNo));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, plateNo, rno parameter 的 BasicNameValuePair<BR>
     * 為了取得維修細節的資料(許多只用到 token, plateNo 的都會用此)
     * 
     */
    static List<BasicNameValuePair> repairDetailNameValuePair(String token, String plateNo, String rno){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("plateNo", plateNo));
        nameValuePair.add(new BasicNameValuePair("rno", rno));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, plateNo parameter 的 BasicNameValuePair<BR>
     * 為了取得維修廠的歷史紀錄(許多只用到 token, plateNo 的都會用此)
     * 
     */
    static List<BasicNameValuePair> maintenanceHistoryNameValuePair(String token, String plateNo, String start, String end){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("plateNo", plateNo));
        nameValuePair.add(new BasicNameValuePair("start", start));
        nameValuePair.add(new BasicNameValuePair("end", end));
        return nameValuePair;
    }
    
    /**
     * 產生含有 token, oldPwd, newPwd parameter 的 BasicNameValuePair<BR>
     * 修改密碼要用的
     */
    static List<BasicNameValuePair> changePWDNameValuePair(String token, String oldPwd, String newPwd){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("oldPassword", oldPwd));
        nameValuePair.add(new BasicNameValuePair("newPassword", newPwd));
        return nameValuePair;
    }

    /**
     * 產生含有 token, oldPwd, newPwd parameter 的 BasicNameValuePair<BR>
     * 修改密碼要用的
     */
    static List<BasicNameValuePair> changePWDNameValuePair(String token, String securityId, String name, String email, String mobile, String gender, String birthdate, boolean edmSubscribed){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("securityId", securityId));
        nameValuePair.add(new BasicNameValuePair("name", name));
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("mobile", mobile));
        nameValuePair.add(new BasicNameValuePair("gender", gender));
        nameValuePair.add(new BasicNameValuePair("birthdate", birthdate));
        nameValuePair.add(new BasicNameValuePair("edmSubscribed", edmSubscribed?"true":"false"));
        return nameValuePair;
    }
    
    /**
     * 產生含有 String token, String plateNo, String locationId
    		, String bookedTime, String odo, String mechanicCode, String svcAdvisorCode, String serviceType, boolean toWait, String notes 的 BasicNameValuePair<BR>
     * 預約保修要用的
     */
    static List<BasicNameValuePair> addServiceBookingNameValuePair(String token, String plateNo, String locationId
    		, String bookedTime, String odo, String mechanicCode, String svcAdvisorCode, String serviceType, boolean toWait
    		, String notes, String name, String email, String mobile){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("plateNo", plateNo));
        nameValuePair.add(new BasicNameValuePair("locationId", locationId));
        nameValuePair.add(new BasicNameValuePair("bookedTime", bookedTime));
        nameValuePair.add(new BasicNameValuePair("odo", odo));
        nameValuePair.add(new BasicNameValuePair("mechanicCode", mechanicCode));
        nameValuePair.add(new BasicNameValuePair("svcAdvisorCode", svcAdvisorCode));
        nameValuePair.add(new BasicNameValuePair("serviceType", serviceType));
        nameValuePair.add(new BasicNameValuePair("toWait", toWait?"true":"false"));
        nameValuePair.add(new BasicNameValuePair("notes", notes));
        nameValuePair.add(new BasicNameValuePair("name", name));
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("mobile", mobile));
        return nameValuePair;
    }
    
    /**
     * 產生含有 String token, String plateNo, String locationId, String entryId<BR>
     * 預約保修要用的
     */
    static List<BasicNameValuePair> deleteServiceBookingNameValuePair(String token, String plateNo, String locationId, String entryId){
        List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("plateNo", plateNo));
        nameValuePair.add(new BasicNameValuePair("locationId", locationId));
        nameValuePair.add(new BasicNameValuePair("entryId", entryId));
        return nameValuePair;
    }
    
}
