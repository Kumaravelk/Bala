package com.base2.streetsmart;
import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.streetsmart.R;
import com.base2.streetsmart.BrandActivity.MyBrand;
import com.example.AdapterClass.BrandListAdapter;
import com.example.AdapterClass.RetailerListAdapter;
import com.example.ItemClass.RetailerItem;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RetailerActivity extends Activity {

	private ArrayList arrayofRetailerList;

	String Retailerid,Retailername = "ALL RETAILERS";
	RetailerListAdapter adapter;
	RetailerItem RetailerItem;
	ListView Retailerlist;
    EditText editsearch;
	String str,Retailerstr;
	TextView tv;
	SessionManager session;
	
	String URL = COMM_URL+"/streetsmartadmin4/shopping/retailerlist";
	
	String MAllURL = COMM_URL+"/streetsmartadmin4/shopping/retailermallsdetails";
    String scityid;

    String area_id,mall_id;
	final Context context = this;
	
	 Bundle GetVal;
	   
	   String Get_Val;
	   
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer);
		
		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		  
		  
		  GetVal = getIntent().getExtras();
			 
	       //String DDeal_ID = "500";
		Get_Val = GetVal.getString("Val");
		
		//Toast.makeText(getApplicationContext(), Get_Val, Toast.LENGTH_LONG).show();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		 
		 session = new SessionManager(getApplicationContext());

			HashMap<String, String> user = session.getUserDetails();
			
			Retailerid = user.get(SessionManager. KEY_RETALIERID);
		 
			 scityid =user.get(SessionManager.KEY_CITYID); 
			 
			 area_id =user.get(SessionManager.KEY_AREAID); 
			 
			 mall_id = user.get(SessionManager.KEY_MALLID);
			
			//Toast.makeText(getApplicationContext(), scityid+"-"+area_id, Toast.LENGTH_LONG).show();
			
			 
			// arrayOfList =null;
		
		
		
		
	//	Brandlist.setAdapter(new BrandListAdapter(this, arrayofBrandList));
		
			 tv = (TextView) findViewById(R.id.textView1);
         editsearch = (EditText) findViewById(R.id.search);
         
         
         if(Get_Val.equalsIgnoreCase("98"))
         {
            new MyRetailer().execute(URL);
         }
         else
         {
        	 new MyRetailerMall().execute(MAllURL);
         }

		
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
	
		
		
	}
	
	
	class MyRetailer extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RetailerActivity.this);
			pDialog.setMessage("Loading Retailers");
		   pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				arrayofRetailerList = getListData();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pDialog.isShowing())
				pDialog.dismiss();

		     
            
		     
		     int arraysize = arrayofRetailerList.size();
				if (arraysize == 0) {
					arrayofRetailerList.clear();
					Toast.makeText(getApplicationContext(), "No Retailers Available",Toast.LENGTH_LONG).show();
					tv.setVisibility(1);
					retailer_func();
					 
				}
				else {

					tv.setVisibility(1);
					retailer_func();

				}

			
			// pDialog.dismiss();
		}
	}
	
	public void retailer_func()
	{

		Retailerlist = (ListView) findViewById(R.id.retailerlist);
			

		Retailerlist.setOnItemClickListener(new OnItemClickListener() {

			
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					Retailerid = ((TextView)arg1.findViewById(R.id.retailerid)).getText().toString();
					Retailername = ((TextView)arg1.findViewById(R.id.retailername)).getText().toString();
					session.createRetailer(Retailerid,Retailername);
					    Intent intent=new Intent();
				        intent.putExtra("Retailerid",Retailerid);
				        intent.putExtra("Retailername",Retailername);
				        
				        setResult(2,intent);
				        finish();
				}
			});
				
		
			
		//Retailerlist.setAdapter(new RetailerListAdapter(this, arrayofRetailerList));
		
		
		
		adapter = new RetailerListAdapter(this, arrayofRetailerList);
		Retailerlist.setAdapter(adapter);
	}

	private ArrayList getListData() throws URISyntaxException {
		// TODO Auto-generated method stub
 
		
		ArrayList<RetailerItem> arrayofBrandList = new ArrayList<RetailerItem>();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/listretailer");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("city_id", scityid));
			nameValuePairs.add(new BasicNameValuePair("area_id", area_id));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
				        
				    	/*
				    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				        nameValuePairs.add(new BasicNameValuePair("location", "chennai"));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        // Execute HTTP Post Request
				        HttpResponse response = httpclient.execute(httppost);
				        
				        page = EntityUtils.toString(response.getEntity());
				        */
				       
				      
				        
				      
				    	
				        
				        
				       // JSONArray jsonArray = new JSONArray(Retailerstr);

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject json = (JSONObject) jsonArray.get(i);
							
							RetailerItem RetailerItem = new RetailerItem();
							
							//String category_id = (String) json.get("category_name");
							
							RetailerItem.setRetailerid((String) json.get("retailer_id"));
							RetailerItem.setRetailername((String) json.get("retailer_name"));
							RetailerItem.setRetailericon((String) json.get("logo_image"));

							

							arrayofBrandList.add(RetailerItem);
							
							
						
						}
						 

						//myHandler.post(myRunnable);				
				    }
				    
				    catch(JSONException e) {
						e.printStackTrace();
					}
					 catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				return arrayofBrandList;
				

			}
	
	
	class MyRetailerMall extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RetailerActivity.this);
			pDialog.setMessage("Loading Retailers");
		   pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				arrayofRetailerList = getListDataMall();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pDialog.isShowing())
				pDialog.dismiss();

		     
            
		     
		     int arraysize = arrayofRetailerList.size();
				if (arraysize == 0) {
					arrayofRetailerList.clear();
					Toast.makeText(getApplicationContext(), "No Retailers Available",Toast.LENGTH_LONG).show();
					tv.setVisibility(1);
					retailer_func();
					 
				}
				else {

					tv.setVisibility(1);
					retailer_func();

				}

			
			// pDialog.dismiss();
		}
	}
	
	
	private ArrayList getListDataMall() throws URISyntaxException {
		// TODO Auto-generated method stub
 
		
		ArrayList<RetailerItem> arrayofBrandList = new ArrayList<RetailerItem>();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/retailermallsdetails");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			nameValuePairs.add(new BasicNameValuePair("mall_id", mall_id));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
				        
				    	/*
				    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				        nameValuePairs.add(new BasicNameValuePair("location", "chennai"));
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        // Execute HTTP Post Request
				        HttpResponse response = httpclient.execute(httppost);
				        
				        page = EntityUtils.toString(response.getEntity());
				        */
				       
				      
				        
				      
				    	
				        
				        
				       // JSONArray jsonArray = new JSONArray(Retailerstr);

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject json = (JSONObject) jsonArray.get(i);
							
							RetailerItem RetailerItem = new RetailerItem();
							
							//String category_id = (String) json.get("category_name");
							
							RetailerItem.setRetailerid((String) json.get("retailer_id"));
							RetailerItem.setRetailername((String) json.get("retailer_name"));
							RetailerItem.setRetailericon((String) json.get("logo_image"));

							

							arrayofBrandList.add(RetailerItem);
							
							
						
						}
						 

						//myHandler.post(myRunnable);				
				    }
				    
				    catch(JSONException e) {
						e.printStackTrace();
					}
					 catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				return arrayofBrandList;
				

			}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		  Intent intent=new Intent();
	        intent.putExtra("Retailerid",Retailerid);
	        intent.putExtra("Retailername",Retailername);
	        
	        setResult(2,intent);
	        finish();
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
	
}
