package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.streetsmart.R;
import com.example.AdapterClass.wifiDealPageAdapter;
import com.example.ItemClass.wifiDealList;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WifiListData extends Activity {

	
	ArrayList<wifiDealList> wifi_details;
	
	wifiDealPageAdapter wifidealadapter;
	
	wifiDealList wifi_list;
	
	String Dealurl = COMM_URL+"/streetsmartadmin4/shopping/wifideals";
	
	ListView wifiDealList;
	
	String retailerid,mallid,mallname;
	
	ImageView noimage;
	
	TextView mall_name;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        Bundle bundle = getIntent().getExtras();
        
        retailerid = bundle.getString("retailerid");
        
        mallid = bundle.getString("mallid");
        
        mallname = bundle.getString("mallname");
        
        
        wifiDealList = (ListView) findViewById(R.id.wifiDeallist);
        noimage = (ImageView)findViewById(R.id.noimage);
        
        mall_name = (TextView)findViewById(R.id.wifimallname);
        
        mall_name.setText("Deals in  "+mallname);
        
		new GEODeal().execute(Dealurl);
		
	}
	
	
	
	class GEODeal extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(WifiListData.this);
			pDialog.setMessage("Sniff Wifi Deals");
		   // pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			wifi_details = getwifiListData();
			return null;
		}

		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pDialog.isShowing())
				pDialog.dismiss();

		
			int arraysize = wifi_details.size();
			if (arraysize == 0) {
				wifi_details.clear();
				func_geodeal();
				Toast.makeText(getApplicationContext(), "No Deal Available", Toast.LENGTH_LONG).show();
				noimage.setImageResource(R.drawable.noimg);
			} 
			else {

				noimage.setImageResource(0);
				func_geodeal();

			}
		
			
			 //pDialog.dismiss();
		}
	}
	
	private ArrayList<wifiDealList> getwifiListData() {
		// TODO Auto-generated method stub
		
		
		
		
		ArrayList<wifiDealList> wifi_details = new ArrayList<wifiDealList>();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/wifideals");
		
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("mallid", mallid));
			nameValuePairs.add(new BasicNameValuePair("retailerid",retailerid));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
			
			for (int i = 0; i < jsonArray.length(); i++)
				
			{
			
				JSONObject json = (JSONObject) jsonArray.get(i);
				
				wifiDealList newsitem = new wifiDealList();
				
				//newsitem.setWifidealoffername((String) json.get("offername"));
				
				
				newsitem.setWifidealoffername((String) json.get("offername"));
				newsitem.setWifidealdealid((String) json.get("deal_id"));
				newsitem.setWifidealretailername((String) json.get("retailer_name"));
				newsitem.setWifidealpromo((String) json.get("promotext"));
				newsitem.setWifidealofferimage((String) json.get("retailer_logo"));
				newsitem.setWifidealPrices((String) json.get("price"));
				newsitem.setWifidealType((String) json.get("dealtype"));
				//newsitem.setWifidealcredit((String) json.get("creditcard"));
				newsitem.setWifipercentage((String) json.get("percentage"));
				newsitem.setWifiDate((String) json.get("edate"));
				
				
				/*Log.i("Offername",newsitem.getGeodealoffername());
				Log.i("deal_id",newsitem.getGeodealdealid());
				Log.i("retailer_name",newsitem.getGeodealretailername());
				Log.i("promotext",newsitem.getGeodealpromo());
				Log.i("retailer_logo",newsitem.getGeodealofferimage());
				Log.i("price",newsitem.getGeodealPrices());*/
				
			         wifi_details.add(newsitem);

			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		
		return wifi_details;
	
	}
	
	public void func_geodeal()
	{

		wifiDealList.setAdapter(new wifiDealPageAdapter(this, wifi_details));
		
		wifiDealList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String Deal_id = ((TextView) arg1.findViewById(R.id.wifideal_id)).getText()
						.toString();
				
				
				Intent ddin = new Intent(WifiListData.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);
			}
		});
	}
	

	
	 @Override
	  public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	  
	  @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			
			Intent backintent = new Intent(WifiListData.this,HomeTabActivity.class);
			startActivity(backintent);
			
		}

}
