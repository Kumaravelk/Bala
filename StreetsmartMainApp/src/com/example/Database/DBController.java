package com.example.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper 
{
	private static final String LOGCAT = null;

	public DBController(Context applicationcontext) {
        super(applicationcontext, "WifiMacAddress.db", null, 1);
        Log.d(LOGCAT,"Created");
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE wifi_table (wid INTEGER PRIMARY KEY, wifiid INTEGER, mac_id TEXT,retailer_id INTEGER,retailername TEXT,mall_id INTEGER,mallname TEXT,city_id INTEGER,cityname TEXT)";
        database.execSQL(query);
       
        Log.d(LOGCAT,"wifi_table Created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS wifi_table";
		database.execSQL(query);
        onCreate(database);
	}

	public void deleteWifiList(String id) 
	{
		Log.d(LOGCAT,"delete");
		SQLiteDatabase database = this.getWritableDatabase();	 
		String deleteQuery = "DELETE FROM  wifi_table where WifiDealid='"+ id +"'";
		Log.d("query",deleteQuery);		
		database.execSQL(deleteQuery);
		
	}
	
	public void deleterecords() {
		Log.d(LOGCAT,"delete");
		SQLiteDatabase database = this.getWritableDatabase();	 
		String deleteQuery = "DELETE FROM  wifi_table";
		database.execSQL(deleteQuery);
	}
	
	 public void insertData(String wifiid, String macid,String retailerid,String retailername,String mallid,String mallname,String cityid,String cityname){
	    	
	    	SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

	    	// String size="select count(*) from wifi_datatable";
	           
	         //Cursor data = sqLiteDatabase.rawQuery(size, null);
	         
	        // Log.i("Size", size);
	        // sqLiteDatabase.execSQL(size);
	         ContentValues values=new ContentValues();
	      
	        	 
	        	 Log.i(" add values","add values");
	     		// values.put("wid",wid);
	     		 values.put("wifiid",wifiid);
	     		 values.put("mac_id",macid);
	     		 values.put("retailer_id",retailerid);
	     		 values.put("retailername",retailername);
	     		 values.put("mall_id",mallid);
	     		 values.put("mallname",mallname);
	     		  values.put("city_id",cityid);
	     	     values.put("cityname",cityname);
	     	   //  values.put("wififlag", wififlag);
	     	    // values.put("updatedate",updatedate);
	     	     sqLiteDatabase.insert("wifi_table",null,values);
	        		
	        }
	 
	 public int updateMacID(HashMap<String, String> queryValues) {
		 
		   
			SQLiteDatabase database = this.getWritableDatabase();	 
		    ContentValues values = new ContentValues();
		    values.put("wifiid", queryValues.get("wifiid"));
		    values.put("mac_id", queryValues.get("mac_id"));
		    values.put("retailer_id", queryValues.get("retailer_id"));
		    values.put("retailername", queryValues.get("retailername"));
		    values.put("mall_id", queryValues.get("mall_id"));
		    values.put("mallname", queryValues.get("mallname"));
		    values.put("city_id", queryValues.get("city_id"));
		    values.put("cityname", queryValues.get("cityname"));
		  
		    
		    return database.update("wifi_table", values, "wid" + " = ?", new String[] { queryValues.get("wid") });
		}
	 
	 public void delwifi(String macid){
			SQLiteDatabase db = this.getWritableDatabase();
			String Qry;
			Qry="DELETE FROM wifi_table Where mac_id='" + macid + "'";
			db.execSQL(Qry);
		}
	 
	public ArrayList<HashMap<String, String>> getAllWifiList(String macaddress) {
		ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM wifi_table where mac_id='"+ macaddress +"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("wid", cursor.getString(0));
	        	map.put("wifiid", cursor.getString(1));
	        	map.put("mac_id", cursor.getString(2));
	        	map.put("retailer_id", cursor.getString(3));
	        	map.put("retailername", cursor.getString(4));
	        	map.put("mall_id", cursor.getString(5));
	        	map.put("mallname", cursor.getString(6));
	        	map.put("city_id", cursor.getString(7));
	        	map.put("cityname", cursor.getString(8));
	        	map.put("updatedate", cursor.getString(9));
	        	
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    // return contact list
	    return wordList;
	}
	
	public List<String> getwifimacAdd(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		
		
		
		
		SQLiteDatabase database = this.getWritableDatabase();
		
	
		
	Cursor c = database.rawQuery("SELECT mallname,mall_id FROM wifi_table where mac_id='"+ macaddress +"'", null);
	
	
	
	List<String> result = new ArrayList<String>();
	
	

	int iName = c.getColumnIndex("mallname");
	
	
	
	int iDeal = c.getColumnIndex("mall_id");
	
	Log.i("enterrr", "enterr5");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
		Log.i("enterrr", "enterr6");
	result.add(c.getString(iName));
	Log.i("enterrr", "enterr7");
	result.add(c.getString(iDeal));
	Log.i("enterrr", "enterr8");
	}
	c.close();
	return result;
	}
	
	public List<String> getwifimacNAmes() 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT mac_id FROM wifi_table", null);
	List<String> result = new ArrayList<String>();

	int iDeal = c.getColumnIndex("mac_id");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDeal));
	}
	c.close();
	return result;
	}
	
	
	public List<String> getwid(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT wid FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iwid = c.getColumnIndex("wid");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iwid));
	}
	c.close();
	return result;
	}
	
	
	public List<String> getwifiid(String macaddress)
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT wifiid FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iwifiid = c.getColumnIndex("wifiid");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iwifiid));
	}
	c.close();
	return result;
	}
	
	
	
	public List<String> getcityid(String macaddress)
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT city_id FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int icityid = c.getColumnIndex("city_id");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(icityid));
	}
	c.close();
	return result;
	}
	
	
	public List<String> getcityname(String macaddress)
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT cityname FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int icityname = c.getColumnIndex("cityname");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(icityname));
	}
	c.close();
	return result;
	}
	
	
	
	
	
	
	public List<String> getwifiRetailerNAmes(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT retailername FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iDeal = c.getColumnIndex("retailername");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDeal));
	}
	c.close();
	return result;
	}
	
	public List<String> getwifiRetailerid(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT retailer_id FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iDeal = c.getColumnIndex("retailer_id");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDeal));
	}
	c.close();
	return result;
	}
	
	
	public List<String> getwifimallid(String macaddress)
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT mall_id FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iDeal = c.getColumnIndex("mall_id");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDeal));
	}
	c.close();
	return result;
	}
	
	
	public List<String> getwifiMallNAmes(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT mallname FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iDeal = c.getColumnIndex("mallname");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDeal));
	}
	c.close();
	return result;
	}
	
	public List<String> getwifiDealID(String macaddress) 
	{
	// TODO Auto-generated method stub
	//String[] columns = new String[]{ latitude};
		SQLiteDatabase database = this.getWritableDatabase();
	Cursor c = database.rawQuery("SELECT mall_id FROM wifi_table where mac_id='"+ macaddress +"'", null);
	List<String> result = new ArrayList<String>();

	int iDealid = c.getColumnIndex("mall_id");

	for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
	{
	result.add(c.getString(iDealid));
	}
	c.close();
	return result;
	}

		public void insertWifiMac(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("wifiid", queryValues.get("WIFIID"));
		values.put("mac_id", queryValues.get("MACID"));
		values.put("retailer_id", queryValues.get("RETAILERID"));
		values.put("retailername", queryValues.get("RETALIERNAME"));
		values.put("mall_id", queryValues.get("MALLID"));
		values.put("mallname", queryValues.get("MALLNAME"));
		values.put("city_id", queryValues.get("CITYID"));
		values.put("cityname", queryValues.get("CITYNAME"));
		values.put("updatedate", queryValues.get("UPDATEDATE"));
	
		database.insert("wifi_table", null, values);
		database.close();
	}

	

}
