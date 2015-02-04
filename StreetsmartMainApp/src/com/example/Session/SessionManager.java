package com.example.Session;

import java.util.HashMap;

import com.base2.streetsmart.HomeTabActivity;
import com.base2.streetsmart.LoginActivity;
import com.base2.streetsmart.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager 
{
	 
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	
	 
	public static final String PREF_NAME = "Streetsmart";
	public static final String IS_LOGIN = "IsLoggedIn";
	
	public static final String KEY_USERID = "userid"; 
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_CITYID = "cityid";
	public static final String KEY_CITYNAME = "cityname";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_GEO_NOTIFI = "geonotifi";
	public static final String KEY_PUSH_NOTIFI = "pushnotifi";
	public static final String KEY_EMAIL_NOTIFI = "emailnotifi";
	public static final String KEY_AGE = "age";
	public static final String KEY_GENDER = "gender";
	public static final String KEY_OCCUPY = "occupy";
	public static final String KEY_CATEGORIES = "categories";
	 
	
	
	
	public static final String KEY_CATEGORYID = "CATEID";
	public static final String KEY_CATEGORYNAME = "CATENAME";
	public static final String KEY_SUBCATEGORYID = "SUBCATEID";
	public static final String KEY_SUBCATEGORYNAME= "SUBCATENAME";
	
	
	public static final String KEY_BRANDID = "BRANDID";
	public static final String KEY_RETALIERID = "RETALIERID";
	public static final String KEY_CREDITCARD = "CREDITCARD";
	public static final String KEY_COD= "COD";
	public static final String KEY_EMI = "EMI";
	public static final String KEY_EX = "EX";
	public static final String KEY_BRANDNAME = "BRANDNAME";
	public static final String KEY_RETAILERNAME = "RETAILERNAME";
	
	public static final String KEY_MALLID = "MALLID";
	public static final String KEY_MALLNAME = "MALLNAME";
	
	
	public static final String KEY_AREAID = "AREAID";
	public static final String KEY_AREANAME = "AREANAME";
	
	
	
	
	
	
	
	
	
	// Constructor
	public SessionManager(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		editor.commit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String userid1,String name1, String email1,String mobile1,String city1,String cityname1,String country1,String geonotify1,String pushnotify1,String emailnotify1,String age1,String gender1,String occupy1,String categories1)
	{
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
        
		editor.putString(KEY_USERID, userid1);
		editor.putString(KEY_NAME, name1);
		editor.putString(KEY_EMAIL, email1);
		editor.putString(KEY_MOBILE, mobile1);
		editor.putString(KEY_CITYID, city1);
		editor.putString(KEY_CITYNAME, cityname1);
		editor.putString(KEY_COUNTRY, country1);
		editor.putString(KEY_GEO_NOTIFI, geonotify1);
		editor.putString(KEY_PUSH_NOTIFI, pushnotify1);
		editor.putString(KEY_EMAIL_NOTIFI, emailnotify1);
		editor.putString(KEY_AGE, age1);
		editor.putString(KEY_GENDER, gender1);
		editor.putString(KEY_OCCUPY, occupy1);
		editor.putString(KEY_CATEGORIES, categories1);
		editor.commit();
	}	
	
	public void checkLogin()
	{
		
		if(!this.isLoggedIn())
		{	
			Intent i = new Intent(_context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
		else
		{
			 Intent in =new Intent(_context, HomeTabActivity.class);
			 in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 _context.startActivity(in);			
		}
		 		
	}
			
	public HashMap<String, String> getUserDetails()
	{
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USERID, pref.getString(KEY_USERID, null));
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
		user.put(KEY_CITYID, pref.getString(KEY_CITYID, null));
		user.put(KEY_CITYNAME, pref.getString(KEY_CITYNAME, null));
		user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));
		user.put(KEY_GEO_NOTIFI, pref.getString(KEY_GEO_NOTIFI, null));
		user.put(KEY_PUSH_NOTIFI, pref.getString(KEY_PUSH_NOTIFI, null));
		user.put(KEY_EMAIL_NOTIFI, pref.getString(KEY_EMAIL_NOTIFI, null));
		user.put(KEY_AGE, pref.getString(KEY_AGE, null));
		user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
		user.put(KEY_OCCUPY, pref.getString(KEY_OCCUPY, null));
		user.put(KEY_CATEGORIES, pref.getString(KEY_CATEGORIES,null));
		
		
		user.put(KEY_CATEGORYID, pref.getString(KEY_CATEGORYID, null));
		user.put(KEY_CATEGORYNAME, pref.getString(KEY_CATEGORYNAME, null));
		user.put(KEY_SUBCATEGORYID, pref.getString(KEY_SUBCATEGORYID, null));
		user.put(KEY_SUBCATEGORYNAME, pref.getString(KEY_SUBCATEGORYNAME, null));
		
		
		user.put(KEY_BRANDID, pref.getString(KEY_BRANDID, null));
		user.put(KEY_RETALIERID, pref.getString(KEY_RETALIERID, null));
		user.put(KEY_CREDITCARD, pref.getString(KEY_CREDITCARD, null));
		user.put(KEY_COD, pref.getString(KEY_COD, null));
		user.put(KEY_EMI, pref.getString(KEY_EMI, null));
		user.put(KEY_EX, pref.getString(KEY_EX, null));
		user.put(KEY_BRANDNAME, pref.getString(KEY_BRANDNAME, null));
		user.put(KEY_RETAILERNAME, pref.getString(KEY_RETAILERNAME, null));
		
		user.put(KEY_MALLID, pref.getString(KEY_MALLID, null));
		user.put(KEY_MALLNAME, pref.getString(KEY_MALLNAME, null));
		
		user.put(KEY_AREAID, pref.getString(KEY_AREAID, null));
		user.put(KEY_AREANAME, pref.getString(KEY_AREANAME, null));
		
		
		 
		return user;
	}
	
	
	public void logoutUser()
	{
		
		editor.clear();
		editor.commit();
		
		Intent i = new Intent(_context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
		
	}
	
	public boolean isLoggedIn()
	{
		return pref.getBoolean(IS_LOGIN, false);
	}

	public void createcategorymethod(String cate_id, String cate_name,
			String subcate_id, String subcate_name) {
		// TODO Auto-generated method stub
		
		editor.putString(KEY_CATEGORYID, cate_id);
		editor.putString(KEY_CATEGORYNAME, cate_name);
		editor.putString(KEY_SUBCATEGORYID, subcate_id);
		editor.putString(KEY_SUBCATEGORYNAME, subcate_name);
		
		editor.commit();
		
	}
	
	
	public void createfliterMethod(String Brandvals,String Retailervals,String check_cod,String check_emi,String check_cc,String check_ex,String Brandname,String Retailername)
	{
		
		editor.putString(KEY_BRANDID, Brandvals);
		editor.putString(KEY_RETALIERID, Retailervals);
		editor.putString(KEY_CREDITCARD, check_cc);
		editor.putString(KEY_COD, check_cod);
		editor.putString(KEY_EMI, check_emi);
		editor.putString(KEY_EX, check_ex);
		editor.putString(KEY_BRANDNAME, Brandname);
		editor.putString(KEY_RETAILERNAME, Retailername);
		
		editor.commit();
		
	}
	
	
	public void createMAllMethod(String mallid,String Mallname)
	{
		editor.putString(KEY_MALLID, mallid);
		editor.putString(KEY_MALLNAME, Mallname);
		
		editor.commit();
	}
	
	public void createAreaMethod(String areaid,String Areaname)
	{
		editor.putString(KEY_AREAID, areaid);
		editor.putString(KEY_AREANAME, Areaname);
		
		editor.commit();
	}
	
	public void createBrand(String brandid,String brandname)
	{
		editor.putString(KEY_BRANDID, brandid);
	
	    editor.putString(KEY_BRANDNAME, brandname);
	 
	
	    editor.commit();
	}
	
	public void createRetailer(String retailerid,String retailername)
	{
		
		editor.putString(KEY_RETALIERID, retailerid);	
	   
	    editor.putString(KEY_RETAILERNAME, retailername);
	
	    editor.commit();
	}
	
	
}
