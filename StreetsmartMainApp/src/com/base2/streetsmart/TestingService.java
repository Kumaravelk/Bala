package com.base2.streetsmart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.base2.streetsmart.R;
import com.example.Database.DBController;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class TestingService extends Service {

	private static final String TAG = "TestingService";
	final static private long ONE_SECOND = 1000;
	final static private long TWENTY_SECONDS = ONE_SECOND * 60;

	DBController controller = new DBController(this);
	
	PendingIntent pi;
	BroadcastReceiver br;
	AlarmManager am;
	String retailername;
	
	List<String>  wifiList;
	
	String wifiadd,wifiliadd,wifilistvaladd,wifilistadd;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Log.i(TAG, "Wifii Inside onCreate of Service");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//Log.i(TAG, "Wifi Inside onDestroy of Service");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		//Log.i(TAG, "Wifii Inside onStart of Service");

		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context c, Intent intent) {
				String temp = "";
				int z = -200;
				int signallevel = 0;
				ScanResult sc = null;
				List<ScanResult> results = wifiManager.getScanResults();
				//Log.i(TAG, "Wifi Refreshing");
				Log.i("resultsVALLLL",""+results);
				ArrayList<ScanResult> resultsmatching = new ArrayList<ScanResult>();
				
				try {
					FileInputStream fin =  openFileInput("macaddressfile");
					
					int ca;
					temp="";
					while( (ca = fin.read()) != -1){
					   temp = temp + Character.toString((char)ca);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				for (ScanResult ap :results){
					
					if(temp.contains(ap.BSSID)){
						Log.i("LISt",""+ap.BSSID);
						resultsmatching.add(ap);
					}
				}
				//Log.i(TAG, "resultmatching array size is: " + resultsmatching.size() );
				//Log.i("LISt",""+ap.BSSID);
				for (int i=0; i<resultsmatching.size(); i++){
					ScanResult rm = resultsmatching.get(i);
					signallevel = rm.level;
					//Log.i(TAG, "signal level is: " + signallevel);
					if(signallevel > z){
						z = signallevel;
						sc = rm;
					}
				}
				if(sc != null){
					//createNotificaton(sc.SSID , sc.level);
					
					try
					{
						retailername = sc.BSSID;
						
						//Toast.makeText(getApplicationContext(), retailername, Toast.LENGTH_SHORT).show();
						List<String>  widList =  controller.getwid(retailername);
						List<String>  wifiID = controller.getwifiid(retailername);
						List<String>  wificityid =  controller.getcityid(retailername);
						List<String>  wificityname = controller.getcityname(retailername);
						
						
						
						List<String>  wifiRetailerList =  controller.getwifiRetailerNAmes(retailername);
						List<String>  wifiMallName = controller.getwifiMallNAmes(retailername);
						List<String>  wifiRetailerid =  controller.getwifiRetailerid(retailername);
						List<String>  wifiMallid = controller.getwifimallid(retailername);
						
						
						String widlist = widList.toString();
						String widval = widlist.substring(1, widlist.length()-1);
						
						String wifiid = wifiID.toString();
						String wifiidlist = wifiid.substring(1, wifiid.length()-1);
						
						String cityid = wificityid.toString();
						String cityidlist = cityid.substring(1, cityid.length()-1);
						
						String cityname = wificityname.toString();
						String citynamelist = cityname.substring(1, cityname.length()-1);
						

						//List<String>  wifiDealid =  controller.getwifiDealID(retailername);
						String retlist = wifiRetailerList.toString();
						String retname = retlist.substring(1, retlist.length()-1);
						String wifimall = wifiMallName.toString();
						String wifimall_name = wifimall.substring(1, wifimall.length()-1);
						String retlistid = wifiRetailerid.toString();
						String retid = retlistid.substring(1, retlistid.length()-1);
						String wifimallid = wifiMallid.toString();
						String wifimall_id = wifimallid.substring(1, wifimallid.length()-1);
						
						/*String dealid = wifiDealid.toString();
						String deal_id = dealid.substring(1, dealid.length()-1);*/
						
				
						//Toast.makeText(getApplicationContext(), "GETWIFI", Toast.LENGTH_LONG).show();
						/*Log.i("GETWIFI", retailername);
						
						Log.i("GETWIFI", retname);
						
						Log.i("GETWIFI", wifimall_name);
						Log.i("GETWIFI", retid);
						
						Log.i("GETWIFI", wifimall_id);
						Log.i("GETWIFI",""+sc.level);
						Log.i("GETWIFI", ""+sc.SSID);*/
						
						createNotificaton(retname,wifimall_name,retid,wifimall_id,sc.level ,sc.SSID);
						upadateTable(widval,wifiidlist,cityidlist,citynamelist,retname,wifimall_name,retid,wifimall_id,retailername);
						//Toast.makeText(getApplicationContext(), retailername, Toast.LENGTH_SHORT).show();
						
						
						
					}
					catch(NoSuchMethodError e)
					{
						Log.i("NO Value","Vnfjgvsdj");
						e.printStackTrace();
					}
					
				}
				//createNotificaton(sc.SSID , sc.level);
			}

			
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
		
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				wifiManager.startScan();
			}
		};
		registerReceiver(br, new IntentFilter("itsTimeToScan"));
		pi = PendingIntent.getBroadcast(this, 0, new Intent(
				"itsTimeToScan"), 0);
		am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + TWENTY_SECONDS, TWENTY_SECONDS,pi);

		return START_REDELIVER_INTENT;
	}
	
	private void upadateTable(String widval,String wifiidlist,String cityidlist,String citynamelist,String retname,String wifimall_name,String retid,String wifimall_id,String macid) {
		// TODO Auto-generated method stub
		
		Log.i("valuess", "update");
		
		/*HashMap<String, String> queryValues =  new  HashMap<String, String>();
	
		String macids = macid+"1";
		Log.i("valuess", macids);n
		queryValues.put("wid", widval);
		queryValues.put("wifiid",wifiidlist);
		queryValues.put("mac_id",macids);
		queryValues.put("retailer_id",retid);
		queryValues.put("retailername",retname);
		queryValues.put("mall_id",wifimall_id);
		queryValues.put("mallname",wifimall_name);
		queryValues.put("city_id",cityidlist);
		queryValues.put("cityname",citynamelist);
		
		controller.updateMacID(queryValues);*/
		
		controller.delwifi(macid);
		
		wifiList =  controller.getwifimacNAmes();
		
		
		 wifiadd = wifiList.toString();
	 		
         wifilistvaladd = wifiadd.substring(1, wifiadd.length()-1);
         
 		
 		 
 		 Log.i("Mohamed", ""+wifilistadd);
 		 
 		//Toast.makeText(getApplicationContext(), ""+wifilistadd, Toast.LENGTH_SHORT).show();
		
 		
		try {
			 wifilistadd = wifilistvaladd.toLowerCase().toString();
			String address = "08:86:3b:bf:42:7d , ac:f1:df:dc:4b:93 , c8:d7:19:e6:6d:7c";
			FileOutputStream writeToFile = openFileOutput("macaddressfile",
					Context.MODE_PRIVATE);
			writeToFile.write(wifilistadd.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressLint("NewApi")
	
	protected void createNotificaton(String retname, String wifimall_name,
			String retid, String wifimall_id, int level, String sSID) {
		
		Log.i("Dhanish","dhanish");
		
		// TODO Auto-generated method stub
		int requestID = (int) System.currentTimeMillis();
		Intent intent = new Intent(this, WifiListData.class);
		intent.putExtra("retailerid", retid);
		intent.putExtra("mallid", wifimall_id);
		intent.putExtra("mallname", wifimall_name);
		intent.setAction("myString"+ requestID);
		
		PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, 0);
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		//Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
		
		Notification noti = new Notification.Builder(this)
				.setContentTitle("Checkout Offers @ "+wifimall_name)
				.setContentText(retname)
				.setSmallIcon(R.drawable.ic_launcher)
	            .setSound(soundUri)
				.setContentIntent(pIntent)
				.setAutoCancel(true)
				.build();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
        //Log.i("Success","Notification Success");
		notificationManager.notify(0, noti);
	}

	

}