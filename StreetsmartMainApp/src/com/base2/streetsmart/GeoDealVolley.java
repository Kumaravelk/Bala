package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

public class GeoDealVolley extends Activity
{
	private String TAG=GeoDealVolley.class.getSimpleName();
	
	private ListView GeoDealList;
	private ProgressDialog pdialog;
	private GeoDealAdapter Geoadapter;
	private List<GeoDealItem> GeoList=new ArrayList<GeoDealItem>();
	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler;
	
	String str_prices,str_sort,cate_name,subcate_name,cityid,Subarea_name;
	
	
	String ActivityNo = "4";
	String Activityno = "17";
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	String sesubcate_name,Subarea_id,message1,retailermessage1;
	
	String tag_json_obj = "json_obj_req";
	String Dealid ;
	
	String scity = "2";
	
     RelativeLayout area, categories;
	
	ImageView filter,noimage,clearfliter,geolocationarea;
	
	TextView tv, cityname, category;
	
	String GEOURL = COMM_URL+"/streetsmartadmin4/shopping/filtersort"; 
	
	
	SessionManager session;
	String sname,suserid,subareaname,semail,smobile,scityid,scityname,scountry,sgeonotify,secate_id,secate_name,sesubcate_id,sebrand,seretailer,secredit,secod,seemi,seexchange;

	 
	
	    @Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.geo);
			
			cd = new ConnectionDetector(getApplicationContext());

			 
			if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(GeoDealVolley.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
				 
				return;
			}
			
			session = new SessionManager(getApplicationContext());

			HashMap<String, String> user = session.getUserDetails();
			  suserid = user.get(SessionManager. KEY_USERID); 
			  sname = user.get(SessionManager.KEY_NAME); 
			  semail =user.get(SessionManager.KEY_EMAIL); 
			  smobile =user.get(SessionManager.KEY_MOBILE); 
			  scityid =user.get(SessionManager.KEY_CITYID); 
			  scityname =user.get(SessionManager.KEY_CITYNAME); 
			  scountry =user.get(SessionManager.KEY_COUNTRY); 
			  sgeonotify =user.get(SessionManager.KEY_GEO_NOTIFI);
			   
			  secate_id = "0";
			  secate_name = user.get(SessionManager. KEY_CATEGORYNAME); 
			  sesubcate_id = "0";
			  sesubcate_name = user.get(SessionManager. KEY_SUBCATEGORYNAME); 
			  
			    sebrand = user.get(SessionManager. KEY_BRANDID); 
				seretailer = user.get(SessionManager. KEY_RETALIERID); 
				secredit = user.get(SessionManager. KEY_CREDITCARD); 
				secod = user.get(SessionManager. KEY_COD); 
				seemi = user.get(SessionManager. KEY_EMI); 
				seexchange = user.get(SessionManager.KEY_EX); 
				
				
				Subarea_id = user.get(SessionManager.KEY_AREAID); 
				subareaname = user.get(SessionManager.KEY_AREANAME); 
	        
	        
	        
	        categories = (RelativeLayout) findViewById(R.id.categorys);
			category = (TextView) findViewById(R.id.categoryname);
			filter = (ImageView) findViewById(R.id.filterimage);
			clearfliter = (ImageView) findViewById(R.id.clearfilterimage);
			GeoDealList = (ListView) findViewById(R.id.GEODeallist);
			area = (RelativeLayout) findViewById(R.id.CitySelect);
			cityname = (TextView) findViewById(R.id.cityname);
			noimage = (ImageView)findViewById(R.id.noimage);
			geolocationarea = (ImageView)findViewById(R.id.geolocationarea);
			
			category.setText("All Categories");
			//cityname.setText("Deals in "+subareaname);
			if(subareaname.equalsIgnoreCase("null"))
			{
			
			cityname.setText("Deals in "+scityname);
			}
			else
			{
				cityname.setText("Deals in "+subareaname);
			}
			
			 Geoadapter = new GeoDealAdapter();
				
				GeoDealList.setAdapter(Geoadapter);
				GeoDealList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						
						String Deal_id = ((TextView) arg1.findViewById(R.id.geodeal_id)).getText()
								.toString();
						
						//Log.i("GEO DEAL ID", Deal_id);
						Intent ddin = new Intent(GeoDealVolley.this,DisplayDealActivity.class);
						
						Bundle bundle = new Bundle();
						//Add your data from getFactualResults method to bundle
						bundle.putString("DealID", Deal_id);
						//Add the bundle to the intent
						ddin.putExtras(bundle);
						startActivity(ddin);
					}
				});
			
			
			
		
			
			
			
			
			filter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent filterintent = new Intent(GeoDealVolley.this,
							FilterActivity.class);
					
					
					
					Bundle bu = new Bundle();
					
					bu.putString("Val", "98");
					
					filterintent.putExtras(bu);
					
					startActivityForResult(filterintent, 6);
					
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);

				}
			});
			
			clearfliter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					sebrand = "0";
					seretailer = "0";
					secod = "0";
					secredit = "0";
					seemi = "0";
					seexchange = "0";
					message1 = "All Retailers";
					retailermessage1 = "All Brand";
					
					filter.setImageResource(R.drawable.filter_single);
					clearfliter.setImageResource(R.drawable.clearfilter);
					
					session.createfliterMethod(sebrand,seretailer,secod,seemi,secredit,seexchange,message1,retailermessage1);
				
					new GEODeal().execute(GEOURL);
					
				}
			});

			categories.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
					Intent categoryintent = new Intent(GeoDealVolley.this,
							JsonParser.class);
					categoryintent.putExtra("ActivityNo", ActivityNo);
					startActivityForResult(categoryintent, 4);
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);
				}
			});

			 
			geolocationarea.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent areaIntent = new Intent(GeoDealVolley.this,NewCityActivity.class);
					
					startActivityForResult(areaIntent, 17);
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);

				}
			});
			
			
			area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent areaIntent = new Intent(GeoDealVolley.this,NewCityActivity.class);
					
					startActivityForResult(areaIntent, 17);
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);

				}
			});


			//cityname.setText("Deals in "+scityname);
			//category.setText(sesubcate_name);
			
			
			
			pdialog=new ProgressDialog(this);
			pdialog.setMessage("Sniffing Deals for you");
			//pDialog.setCancelable(false);
			pdialog.show();
			
			 RequestQueue rq = Volley.newRequestQueue(this);
				
		        StringRequest postReq = new StringRequest(Request.Method.POST, "http://54.169.81.215/streetsmartadmin4/shopping/filtersort1", new Response.Listener<String>() 
				{
					public void onResponse(String response) 
					{
						pdialog.dismiss();
					   parseJSON(response);
					 
						 
					 }
					}, new Response.ErrorListener() 
					  {

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							System.out.println("Error ["+error+"]");
							
						}
					})
		        
		        {

					protected Map<String, String> getParams() throws AuthFailureError 
					{
						HashMap<String, String> headers = new HashMap<String, String>();
						//Map<String, String> params = new HashMap<String, String>();
						
						
						/*Log.i("user_id", suserid);
						Log.i("subcategory_id",sesubcate_id);
						Log.i("city_id",scityid);
						Log.i("sort", "4");
						Log.i("brand_id", sebrand);
						Log.i("retailer_id",seretailer);
						Log.i("price", "0");
						Log.i("coh", secod);
						Log.i("emi", seemi);
						Log.i("creditcard", secredit);
						Log.i("exchange", seexchange);
						Log.i("mall", "0");
						Log.i("category_id", secate_id);
						Log.i("area_id", Subarea_id);*/
						
						headers.put("user_id", suserid);
						headers.put("subcategory_id",sesubcate_id);
						headers.put("city_id",scityid);
						headers.put("sort", "4");
						headers.put("brand_id", sebrand);
						headers.put("retailer_id",seretailer);
						headers.put("price", "0");
						headers.put("coh", secod);
						headers.put("emi", seemi);
						headers.put("creditcard", secredit);
						headers.put("exchange", seexchange);
						headers.put("mall", "0");
						headers.put("category_id", secate_id);
						headers.put("area_id", Subarea_id);
						
						return headers;
					}
				
				};
	
				//rq.add(postReq);
				
				AppController.getInstance().addToRequestQueue(postReq);
				
	}
	    
	    private void parseJSON(String response)
		{
			//JSONObject jrootobj = new JSONObject(response);
			try{
				JSONArray jsonArray = new JSONArray(response);
				
				for (int i = 0; i < jsonArray.length(); i++)

				{
					JSONArray jArray = (JSONArray) jsonArray.getJSONArray(i);

					for (int j = 0; j < jArray.length(); j++) {
	        
						 

						JSONObject json = (JSONObject) jArray.get(j);
	           
													
	                    
	                  //  Toast.makeText(getApplicationContext(), item.toString(),Toast.LENGTH_LONG).show();
						GeoDealItem newsitem = new GeoDealItem();
						newsitem.setGeodealoffername((String) json.get("offername"));
						newsitem.setGeodealdealid((String) json.get("deal_id"));
						newsitem.setGeodealretailername((String) json.get("retailer_name"));
						newsitem.setGeodealpromo((String) json.get("promotext"));
						newsitem.setGeodealofferimage((String) json.get("retailer_logo"));
						newsitem.setGeodealPrices((String) json.get("price"));
						newsitem.setGeodealType((String) json.get("dealtype"));
						newsitem.setGeodealcredit((String) json.get("creditcard"));
						newsitem.setGeopercentage((String) json.get("percentage"));
						newsitem.setGeoDate((String) json.get("edate"));
						GeoList.add(newsitem);
	            }
	        }
			}
	        catch(Exception e){
	            e.printStackTrace();
	        }
			int count = GeoList.size();
			String count1 = String.valueOf(count);  
			if(count1.equalsIgnoreCase("0"))
			{	  
				GeoList.clear(); 
				noimage.setImageResource(R.drawable.noimg);
				pdialog.dismiss();
			}else
			{
				noimage.setImageResource(0);
				 
			}
			Geoadapter.notifyDataSetChanged();
		}

		/**
		 * Set up the {@link android.app.ActionBar}, if the API is available.
		 */
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void setupActionBar() 
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		 

		private void dissmissDialog() 
		{
			// TODO Auto-generated method stub
			if(pdialog !=null)
			{
				if(pdialog.isShowing())
				{
					pdialog.dismiss();
					
					
				}
				pdialog=null;
			}
			
		}
	    
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == 6) {

				sebrand = data.getStringExtra("Brand_id");
				seretailer = data.getStringExtra("Retailer_id");
				str_prices = data.getStringExtra("Prices");
				secod = data.getStringExtra("COD");
				seemi = data.getStringExtra("EMI");
				secredit = data.getStringExtra("CC");
				seexchange = data.getStringExtra("EX");
			
				
				category.setText(sesubcate_name);
				cityname.setText("Deals in "+scityname);
			
					filter.setImageResource(R.drawable.filter_selected);
					clearfliter.setImageResource(R.drawable.clearfilter_fill);
					 new GEODeal().execute(GEOURL);	
			}

			if (requestCode == 4) {
				
				cate_name = data.getStringExtra("Categoryname");
				secate_id = data.getStringExtra("Categoryid");
				subcate_name = data.getStringExtra("subcategoryname");
				sesubcate_id = data.getStringExtra("subcategoryid");
				
			//	Log.i("Category vals", subcate_name);
				
				category.setText(subcate_name);
				
				
				
				//Toast.makeText(getApplicationContext(), secate_id+sesubcate_id, Toast.LENGTH_LONG);
			
				//session.createcategorymethod(secate_id,cate_name,sesubcate_id,subcate_name);

			}
			if(requestCode==17)
	        {
				
			   cityid=data.getStringExtra("Cityid"); 
			  
	           scityname = data.getStringExtra("Cityname");
	           
	           Subarea_name = data.getStringExtra("subCityname");
	           Subarea_id = data.getStringExtra("subCityid");
	           
	           geolocationarea.setImageResource(R.drawable.locationselect);
	           if(Subarea_id .equalsIgnoreCase("0"))
	           {
	        	   cityname.setText("Deals in "+scityname);
	        	   category.setText("All Categories");
	           }
	           else
	           {
	           
	           cityname.setText("Deals in "+Subarea_name);
	          
	           }          
	          // Toast.makeText(getApplicationContext(), message+message1, Toast.LENGTH_LONG);
	           new GEODeal().execute(GEOURL);
	        }
			
			
			
			if(secate_id.equalsIgnoreCase("0"))
			{
				new AllCategoryGEODeal().execute(GEOURL);
			}
			else
			{
			  new GEODeal().execute(GEOURL);
			}		  
			
		}
	    
	    class GEODeal extends AsyncTask<String, Void, String> {

			ProgressDialog pDialog;

			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(GeoDealVolley.this);
				pDialog.setMessage("Sniff Deals for you");
			   // pDialog.setCancelable(false);
				pDialog.show();

			}
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				GeoList = getGEOListData(scity);
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				if (pDialog.isShowing())
					pDialog.dismiss();

				int arraysize = GeoList.size();
				if (arraysize == 0) {
					GeoList.clear();
					func_geodeal();
					 
					noimage.setImageResource(R.drawable.noimg);
				} 
				else {

					noimage.setImageResource(0);
					func_geodeal();

				}
				
				 //pDialog.dismiss();
			}
		}
		
		private ArrayList<GeoDealItem> getGEOListData(String scity) {
			// TODO Auto-generated method stub
			
			
			/*Log.i("user_id",suserid);
			Log.i("subcategory_id",sesubcate_id);
			Log.i("city_id",scityid);
			Log.i("sort","4");
			Log.i("brand_id","0");
			Log.i("retailer_id","0");
			Log.i("price","0");
			Log.i("coh","0");
			Log.i("emi","0");
			Log.i("creditcard",secredit);
			Log.i("exchange","0");
			Log.i("mall","0");
			Log.i("category_id",secate_id);
			Log.i("area_id",Subarea_id);*/
			
			ArrayList<GeoDealItem> GeoList = new ArrayList<GeoDealItem>();

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					COMM_URL+"/streetsmartadmin4/shopping/filtersort");
			
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user_id", suserid));
				nameValuePairs.add(new BasicNameValuePair("subcategory_id",sesubcate_id));
				nameValuePairs.add(new BasicNameValuePair("city_id",scityid));
				nameValuePairs.add(new BasicNameValuePair("sort", "4"));
				nameValuePairs.add(new BasicNameValuePair("brand_id", sebrand));
				nameValuePairs.add(new BasicNameValuePair("retailer_id",seretailer));
				nameValuePairs.add(new BasicNameValuePair("price", "0"));
				nameValuePairs.add(new BasicNameValuePair("coh", secod));
				nameValuePairs.add(new BasicNameValuePair("emi", seemi));
				nameValuePairs.add(new BasicNameValuePair("creditcard", secredit));
				nameValuePairs.add(new BasicNameValuePair("exchange", seexchange));
				nameValuePairs.add(new BasicNameValuePair("mall", "0"));
				nameValuePairs.add(new BasicNameValuePair("category_id", secate_id));
				nameValuePairs.add(new BasicNameValuePair("area_id", Subarea_id));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				String str = EntityUtils.toString(response.getEntity());

				JSONArray jsonArray = new JSONArray(str);
				
				for (int i = 0; i < jsonArray.length(); i++)
					
				{
					JSONArray jArray = (JSONArray)jsonArray.getJSONArray(i);
					for(int j=0;j<jArray.length();j++)
					{

						GeoDealItem newsitem = new GeoDealItem();
					JSONObject json = (JSONObject) jArray.get(j);
				
					newsitem.setGeodealoffername((String) json.get("offername"));
					newsitem.setGeodealdealid((String) json.get("deal_id"));
					newsitem.setGeodealretailername((String) json.get("retailer_name"));
					newsitem.setGeodealpromo((String) json.get("promotext"));
					newsitem.setGeodealofferimage((String) json.get("retailer_logo"));
					newsitem.setGeodealPrices((String) json.get("price"));
					newsitem.setGeodealType((String) json.get("dealtype"));
					newsitem.setGeodealcredit((String) json.get("creditcard"));
					newsitem.setGeopercentage((String) json.get("percentage"));
					newsitem.setGeoDate((String) json.get("edate"));
					
					
					/*Log.i("Offername",newsitem.getGeodealoffername());
					Log.i("deal_id",newsitem.getGeodealdealid());
					Log.i("retailer_name",newsitem.getGeodealretailername());
					Log.i("promotext",newsitem.getGeodealpromo());
					Log.i("retailer_logo",newsitem.getGeodealofferimage());
					Log.i("price",newsitem.getGeodealPrices());*/
					
					GeoList.add(newsitem);

				}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			return GeoList;
		
		}
		
		public void func_geodeal()
		{

               Geoadapter = new GeoDealAdapter();
			
			GeoDealList.setAdapter(Geoadapter);
			GeoDealList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					
					String Deal_id = ((TextView) arg1.findViewById(R.id.geodeal_id)).getText()
							.toString();
					
					//Log.i("GEO DEAL ID", Deal_id);
					Intent ddin = new Intent(GeoDealVolley.this,DisplayDealActivity.class);
					
					Bundle bundle = new Bundle();
					//Add your data from getFactualResults method to bundle
					bundle.putString("DealID", Deal_id);
					//Add the bundle to the intent
					ddin.putExtras(bundle);
					startActivity(ddin);
				}
			});
		}
	    
		class AllCategoryGEODeal extends AsyncTask<String, Void, String> {

			ProgressDialog pDialog;

			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(GeoDealVolley.this);
				pDialog.setMessage("Sniff Deals for you");
			   // pDialog.setCancelable(false);
				pDialog.show();

			}

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				GeoList = AllCategorygetGEOListData(scity);
				return null;
			}

			

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				if (pDialog.isShowing())
					pDialog.dismiss();

				int arraysize = GeoList.size();
				if (arraysize == 0) {
					GeoList.clear();
					func_geodeal();
					 
					noimage.setImageResource(R.drawable.noimg);
				} 
				else {

					noimage.setImageResource(0);
					func_AllCategorygeodeal();

				}
				
				 //pDialog.dismiss();
			}
		}
		
		private ArrayList<GeoDealItem> AllCategorygetGEOListData(String scity) {
			// TODO Auto-generated method stub
			//Log.i("subcategory_id",sesubcate_id);
			//Log.i("category_id",secate_id);
			/*Log.i("user_id",suserid);
			Log.i("subcategory_id",sesubcate_id);
			Log.i("city_id",scityid);
			Log.i("sort","4");
			Log.i("brand_id","0");
			Log.i("retailer_id","0");
			Log.i("price","0");
			Log.i("coh","0");
			Log.i("emi","0");
			Log.i("creditcard",secredit);
			Log.i("exchange","0");
			Log.i("mall","0");
			Log.i("category_id",secate_id);
			Log.i("area_id",Subarea_id);*/
			
			ArrayList<GeoDealItem> GeoList = new ArrayList<GeoDealItem>();

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					COMM_URL+"/streetsmartadmin4/shopping/filtersort1");
			
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user_id", suserid));
				nameValuePairs.add(new BasicNameValuePair("subcategory_id",sesubcate_id));
				nameValuePairs.add(new BasicNameValuePair("city_id",scityid));
				nameValuePairs.add(new BasicNameValuePair("sort", "4"));
				nameValuePairs.add(new BasicNameValuePair("brand_id", sebrand));
				nameValuePairs.add(new BasicNameValuePair("retailer_id",seretailer));
				nameValuePairs.add(new BasicNameValuePair("price", "0"));
				nameValuePairs.add(new BasicNameValuePair("coh", secod));
				nameValuePairs.add(new BasicNameValuePair("emi", seemi));
				nameValuePairs.add(new BasicNameValuePair("creditcard", secredit));
				nameValuePairs.add(new BasicNameValuePair("exchange", seexchange));
				nameValuePairs.add(new BasicNameValuePair("mall", "0"));
				nameValuePairs.add(new BasicNameValuePair("category_id", secate_id));
				nameValuePairs.add(new BasicNameValuePair("area_id", Subarea_id));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				String str = EntityUtils.toString(response.getEntity());

				JSONArray jsonArray = new JSONArray(str);
				
				for (int i = 0; i < jsonArray.length(); i++)
					
				{
					JSONArray jArray = (JSONArray)jsonArray.getJSONArray(i);
					for(int j=0;j<jArray.length();j++)
					{

						GeoDealItem newsitem = new GeoDealItem();
					JSONObject json = (JSONObject) jArray.get(j);
				
					newsitem.setGeodealoffername((String) json.get("offername"));
					newsitem.setGeodealdealid((String) json.get("deal_id"));
					newsitem.setGeodealretailername((String) json.get("retailer_name"));
					newsitem.setGeodealpromo((String) json.get("promotext"));
					newsitem.setGeodealofferimage((String) json.get("retailer_logo"));
					newsitem.setGeodealPrices((String) json.get("price"));
					newsitem.setGeodealType((String) json.get("dealtype"));
					newsitem.setGeodealcredit((String) json.get("creditcard"));
					newsitem.setGeopercentage((String) json.get("percentage"));
					newsitem.setGeoDate((String) json.get("edate"));
					
					
					/*Log.i("Offername",newsitem.getGeodealoffername());
					Log.i("deal_id",newsitem.getGeodealdealid());
					Log.i("retailer_name",newsitem.getGeodealretailername());
					Log.i("promotext",newsitem.getGeodealpromo());
					Log.i("retailer_logo",newsitem.getGeodealofferimage());
					Log.i("price",newsitem.getGeodealPrices());*/
					
					GeoList.add(newsitem);

				}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			return GeoList;
		
		}
		
		public void func_AllCategorygeodeal()
		{

               Geoadapter = new GeoDealAdapter();
			
			GeoDealList.setAdapter(Geoadapter);
			GeoDealList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					
					String Deal_id = ((TextView) arg1.findViewById(R.id.geodeal_id)).getText()
							.toString();
					
					//Log.i("GEO DEAL ID", Deal_id);
					Intent ddin = new Intent(GeoDealVolley.this,DisplayDealActivity.class);
					
					Bundle bundle = new Bundle();
					//Add your data from getFactualResults method to bundle
					bundle.putString("DealID", Deal_id);
					//Add the bundle to the intent
					ddin.putExtras(bundle);
					startActivity(ddin);
				}
			});
		}
		
		
		
		
	    
	    
	    private class GeoDealAdapter extends BaseAdapter
		{

			private LayoutInflater inflater;
			ImageLoader imageLoader = AppController.getInstance().getImageLoader();
			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return GeoList.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return GeoList.get(arg0);
			}

			@Override
			public long getItemId(int arg0)
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			public View getView(int position, View view, ViewGroup parent)
			{
				// TODO Auto-generated method stub
				
				if(inflater ==null)
					inflater=(LayoutInflater)getLayoutInflater();
				if(view==null)
					view=inflater.inflate(R.layout.geo_list, null);
				if(imageLoader == null)
					 imageLoader = AppController.getInstance().getImageLoader();
				
				
				NetworkImageView imageview=(NetworkImageView)view.findViewById(R.id.dealthump);
				
				TextView offername = (TextView) view.findViewById(R.id.dealoffer);
				TextView promotext = (TextView) view.findViewById(R.id.dealpromo);
				
				TextView price = (TextView) view.findViewById(R.id.dealprice);
				
				TextView dealid = (TextView)view.findViewById(R.id.geodeal_id);
				
				
				
				TextView retailername = (TextView)view.findViewById(R.id.retailername);
				
				ImageView dealtype = (ImageView)view.findViewById(R.id.geodealtype);
				
				ImageView creditcard = (ImageView)view.findViewById(R.id.geocreditcard);
				
				TextView geodate = (TextView)view.findViewById(R.id.geodate);
				
				 
				
				GeoDealItem itemmodel=GeoList.get(position);
				
				imageview.setImageUrl(itemmodel.getGeodealofferimage(), imageLoader);
				
				
				offername.setText(itemmodel.getGeodealoffername());
				promotext.setText(itemmodel.getGeodealpromo());
				retailername.setText(itemmodel.getGeodealretailername());
				price.setText("Rs."+itemmodel.getGeodealPrices()+"/-  OR   "+itemmodel.getGeopercentage()+"% OFF");
				dealid.setText(itemmodel.getGeodealdealid());
				geodate.setText(itemmodel.getGeoDate());
				//Dealholder.geopercentage.setText(dealitem.getGeopercentage()+"% OFF");
				
				String newdate = itemmodel.getGeoDate().toString();
				
			
				
				String inputPattern = "yyyy-MM-dd HH:mm:ss";
				String outputPattern = "MMM dd";
				SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
				SimpleDateFormat  outputFormat = new SimpleDateFormat(outputPattern);

				 Date date = null;
				  String str = null;

				    try {
				        date = inputFormat.parse(newdate);
				        str = outputFormat.format(date);
				    } catch (ParseException e) 
				    {
				        e.printStackTrace();
				    } catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				   
				
				
			        
			        geodate.setText(str);
				
				String geo_dealtype = itemmodel.getGeodealType().toString();
				
				String geo_creditcard = itemmodel.getGeodealcredit().toString();
				
				if(geo_dealtype.equalsIgnoreCase("4"))
				{
					dealtype.setVisibility(View.VISIBLE);
				}
				else
				{
					dealtype.setVisibility(View.INVISIBLE);
				}
				
				if(geo_creditcard.equalsIgnoreCase("1"))
				{
					creditcard.setVisibility(View.VISIBLE);
				}
				else
				{
					creditcard.setVisibility(View.INVISIBLE);
				}
				
				return view;
			}
			
		}
	    
	    
	    class GeoDealItem
	    {
	    	String geodealdealid;
	    	public String getGeodealdealid() {
				return geodealdealid;
			}
			public void setGeodealdealid(String geodealdealid) {
				this.geodealdealid = geodealdealid;
			}
			public String getGeodealType() {
				return geodealType;
			}
			public void setGeodealType(String geodealType) {
				this.geodealType = geodealType;
			}
			public String getGeodealoffername() {
				return geodealoffername;
			}
			public void setGeodealoffername(String geodealoffername) {
				this.geodealoffername = geodealoffername;
			}
			public String getGeodealpromo() {
				return geodealpromo;
			}
			public void setGeodealpromo(String geodealpromo) {
				this.geodealpromo = geodealpromo;
			}
			public String getGeodealofferimage() {
				return geodealofferimage;
			}
			public void setGeodealofferimage(String geodealofferimage) {
				this.geodealofferimage = geodealofferimage;
			}
			public Bitmap getGeodealre_listimage() {
				return geodealre_listimage;
			}
			public void setGeodealre_listimage(Bitmap geodealre_listimage) {
				this.geodealre_listimage = geodealre_listimage;
			}
			public String getGeodealretailername() {
				return geodealretailername;
			}
			public void setGeodealretailername(String geodealretailername) {
				this.geodealretailername = geodealretailername;
			}
			public String getGeodealofferthumps() {
				return geodealofferthumps;
			}
			public void setGeodealofferthumps(String geodealofferthumps) {
				this.geodealofferthumps = geodealofferthumps;
			}
			public String getGeodealPrices() {
				return geodealPrices;
			}
			public void setGeodealPrices(String geodealPrices) {
				this.geodealPrices = geodealPrices;
			}
			public String getGeodealcredit() {
				return geodealcredit;
			}
			public void setGeodealcredit(String geodealcredit) {
				this.geodealcredit = geodealcredit;
			}
			public String getGeopercentage() {
				return geopercentage;
			}
			public void setGeopercentage(String geopercentage) {
				this.geopercentage = geopercentage;
			}
			public String getGeoDate() {
				return geoDate;
			}
			public void setGeoDate(String geoDate) {
				this.geoDate = geoDate;
			}
			String geodealType;
	    	String geodealoffername;
	    	String geodealpromo;
	    	String geodealofferimage;
	    	Bitmap geodealre_listimage;
	    	String geodealretailername;
	    	String geodealofferthumps;
	    	String geodealPrices;
	    	String geodealcredit;
	    	String geopercentage;
	    	String geoDate;

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
			  
			  
			 
			  
			  //WishlistFragment.onBackPressed();
			  //FavoritesFragment.onBackPressed();
			 // NotificationFragment.onBackPressed();
			 // SettingsFragment.onBackPressed();
			    if (doubleBackToExitPressedOnce) {
			        super.onBackPressed();
			        
			        return;
			    }

			    this.doubleBackToExitPressedOnce = true;
			    Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

			    new Handler().postDelayed(new Runnable() {

			        @Override
			        public void run() {
			            doubleBackToExitPressedOnce=false;                       
			        }
			    }, 3500); 
			} 
	    }