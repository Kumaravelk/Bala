package com.base2.streetsmart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.base2.streetsmart.GeoDealVolley.GEODeal;


import com.example.Database.DBController;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;


public class BigDealActivity extends Activity
{
	
		private String TAG=BigDealActivity.class.getSimpleName();
		private ProgressDialog pdialog;
		private listviewAdapter adapter;
		private List<BigDealItem> BigList=new ArrayList<BigDealItem>();
		private boolean doubleBackToExitPressedOnce;
		private Handler mHandler;
		JSONObject obj;
		String subcategorystr;

		TextView tv;
		String tag_json_obj = "json_obj_req";
		String Dealid ;
		
		String str, page, name;
		 
		ProgressDialog pDialog;
		View rootView;
		
		List<String>  wifiList;
		
		String wifi,wifili,wifilistval;
		
		String wifiurl = "http://54.169.81.215/streetsmartadmin4/shopping/wifilist";
		
		DBController controller = new DBController(this);
		
		RelativeLayout Select_City;
		TextView city_name;
		ListView BigDealList;
		
		String BigDealid;
		ImageView noimage,locationarea;
		
		
		
		String wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname,wififlag;
		
		String Brandvals ="0", Retailervals="0", check_cod="0", check_emi="0", check_cc="0", check_ex="0",brandname,retailername;
		
		SessionManager session;
		String suserid, sname, semail, smobile, scityid, scityname, scountry,sgeonotify, spushnotify, semailnotify,sage,sgender,soccupy,scate,scateories;
		
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.homme);
			
			 session = new SessionManager(getApplicationContext());
			 noimage = (ImageView) findViewById(R.id.noimage);
			 locationarea = (ImageView)findViewById(R.id.locationarea);
			  HashMap<String, String> user = session.getUserDetails();
			  
			  
			  suserid = user.get(SessionManager. KEY_USERID); 
			  sname = user.get(SessionManager.KEY_NAME); 
			  semail =user.get(SessionManager.KEY_EMAIL); 
			  smobile =user.get(SessionManager.KEY_MOBILE); 
			  scityid =user.get(SessionManager.KEY_CITYID); 
			  scityname =user.get(SessionManager.KEY_CITYNAME); 
			  scountry =user.get(SessionManager.KEY_COUNTRY); 
			  sgeonotify =user.get(SessionManager.KEY_GEO_NOTIFI);
			  sage =user.get(SessionManager.KEY_AGE);
			  sgender =user.get(SessionManager.KEY_GENDER);
			  soccupy =user.get(SessionManager.KEY_OCCUPY);
			  scate =user.get(SessionManager.KEY_CATEGORYID);
			  scateories =user.get(SessionManager.KEY_CATEGORIES);
			
			  BigDealList = (ListView) findViewById(R.id.BigDeals);
			   adapter=new listviewAdapter();
			   BigDealList.setAdapter(adapter);
			   
			   BigDealList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {

						BigDealid = ((TextView) arg1.findViewById(R.id.bigdealid))
								.getText().toString();

						// TODO Auto-generated method stub
						Intent ddin = new Intent(BigDealActivity.this,
								DisplayDealActivity.class);
						overridePendingTransition(R.anim.flip_left_in,
								R.anim.flip_left_out);
						Bundle bundle = new Bundle();

						bundle.putString("DealID", BigDealid);
						ddin.putExtras(bundle);
						startActivity(ddin);
						 

					}
				});
			   
			   session.createfliterMethod(Brandvals, Retailervals, check_cod, check_emi, check_cc, check_ex,brandname,retailername);
			
			city_name = (TextView) findViewById(R.id.cityname);
			Select_City = (RelativeLayout) findViewById(R.id.CitySelect);
			//noimage = (ImageView) findViewById(R.id.noimage);
			city_name.setText("Deals in "+scityname);
			
			

			locationarea.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
					Intent areaIntent = new Intent(BigDealActivity.this,CityActivity.class);
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);
					
					startActivityForResult(areaIntent, 16);
					

				}
			});
			

			Select_City.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
					Intent areaIntent = new Intent(BigDealActivity.this,CityActivity.class);
					overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);
					
					startActivityForResult(areaIntent, 16);
					

				}
			});
			
			
             new MyWifiList().execute(wifiurl);
			
			pdialog=new ProgressDialog(this);
			pdialog.setMessage("Sniffing deals for you");
			//pDialog.setCancelable(false);
			pdialog.show();
			
			 RequestQueue rq = Volley.newRequestQueue(this);
				/* JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
			                url, null,
			                new Response.Listener<JSONObject>() {*/
		        StringRequest postReq = new StringRequest(Request.Method.POST, "http://54.169.81.215/streetsmartadmin4/shopping/displaydealfavourite", new Response.Listener<String>() 
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
							headers.put("userid", suserid);
							headers.put("cityid", scityid);
							headers.put("categoryid", scateories);

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
					// Log.i("Area Name Sub1",response );
					for (int i = 0; i < jsonArray.length(); i++)

					{
						JSONArray jArray = (JSONArray) jsonArray.getJSONArray(i);

						for (int j = 0; j < jArray.length(); j++) {
		        

							JSONObject json = (JSONObject) jArray.get(j);
		           
							
							/*JSONArray jsonArray = new JSONArray(str);
							
							for (int i = 0; i < jsonArray.length(); i++)
								
							{
								
								JSONArray jArray = (JSONArray)jsonArray.getJSONArray(i);
								
								for(int j=0;j<jArray.length();j++)
								{

								BigDealListItem newsitem = new BigDealListItem();
								JSONObject json = (JSONObject) jArray.get(j);*/
							
							
							
		                    
		                  //  Toast.makeText(getApplicationContext(), item.toString(),Toast.LENGTH_LONG).show();
							BigDealItem newsitem = new BigDealItem();
							newsitem.setBigdeal_id((String) json.get("deal_id"));
							newsitem.setBigoffername((String) json.get("offername"));

							newsitem.setOfferimage((String) json.get("offerimage"));
							newsitem.setBigpromotext((String) json.get("promotext"));
							newsitem.setRetailerlogo((String) json.get("retailer_logo"));
							//newsitem.setRetailername((String) json.get("retailer_name"));
		                    //nm.setPubDate(item.optString("pubDate"));
		                    BigList.add(newsitem);
		                     
		            }
		        }
				}
		        catch(Exception e){
		            e.printStackTrace();
		        }
				int count = BigList.size();
				String count1 = String.valueOf(count);  
				if(count1.equalsIgnoreCase("0"))
				{	  
					BigList.clear(); 
					noimage.setImageResource(R.drawable.noimg);
					pdialog.dismiss();
				}else
				{
					noimage.setImageResource(0);
					 
				}
				
				adapter.notifyDataSetChanged();
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
						
						int count = BigList.size();
						if(count ==0)
						{
							BigList.clear();
							 
							noimage.setImageResource(R.drawable.noimg);
							
						}else
						{
							noimage.setImageResource(0);
						}
					}
					pdialog=null;
				}
			}
			
			
			protected void onActivityResult(int requestCode, int resultCode, Intent data) 
			{
				// TODO Auto-generated method stub
				super.onActivityResult(requestCode, resultCode, data);

				if (requestCode == 16) {

					locationarea.setImageResource(R.drawable.locationselect);
				}
			}
			
			
			class MyWifiList extends AsyncTask<String, Void, String> {
				ProgressDialog pDialog;

				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					
					//Toast.makeText(getApplicationContext(), "delete",Toast.LENGTH_LONG).show();
					 controller.deleterecords();
					
				}

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub

					try {

						HttpClient client = new DefaultHttpClient();
						client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
								"android");
						HttpGet request = new HttpGet();
						request.setHeader("Content-Type", "text/plain; charset=utf-8");
						request.setURI(new URI(wifiurl));

						HttpResponse response = client.execute(request);
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));

						StringBuffer stringBuffer = new StringBuffer("");
						String line = "";

						String NL = System.getProperty("line.separator");
						while ((line = bufferedReader.readLine()) != null) {
							stringBuffer.append(line + NL);
							System.out.print(stringBuffer);
						}
						bufferedReader.close();
						String page = stringBuffer.toString();

						//Log.i("Page", page);

						// Log.d("Response: ", "> " + jsonStr);

						JSONArray jsonArray = new JSONArray(page);

						for (int i = 0; i < jsonArray.length(); i++)

						{

							JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

							wifiid = (String) jsonObject1.get("wifi_id");
							mac_id = (String) jsonObject1.get("mac_id");
							wifiretailerid = (String) jsonObject1.get("retailer_id");
							wifiretailername = (String) jsonObject1.get("retailer_name");
							wifimallid = (String) jsonObject1.get("mall_id");
							wifimallname = (String) jsonObject1.get("mall_name");
							wificityid = (String) jsonObject1.get("city_id");
							wificityname = (String) jsonObject1.get("city_name");
							//wififlag = "0";
							
							controller.insertData(wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname);
						}
					}
						
					catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					
					wifiList =  controller.getwifimacNAmes();
			         
			        // Log.i("wifiList", ""+wifiList);
			         
			         wifi = wifiList.toString();
			 		
			         wifilistval = wifi.substring(1, wifi.length()-1);
			         
			 		 wifili = wifilistval.toLowerCase().toString();
			 		
			 		   //("Wifi List", wifili);
			 		//Toast.makeText(getApplicationContext(), wifili, Toast.LENGTH_SHORT).show();
			 		Wififunction();

				}

			}
			
			private void Wififunction() {
				// TODO Auto-generated method stub
			//	Log.i("WIFI LISTssss", ""+wifili);
				try {
					//String address = "08:86:3b:bf:42:7c , ac:f1:df:dc:4b:94 , c8:d7:19:e6:6d:7d";
					FileOutputStream writeToFile = openFileOutput("macaddressfile",
							Context.MODE_PRIVATE);
					writeToFile.write(wifili.getBytes());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				startService(new Intent(this, TestingService.class));
			}

			
			@SuppressLint("SimpleDateFormat")
			private class listviewAdapter extends BaseAdapter
			{

				private LayoutInflater inflater;
				ImageLoader imageLoader = AppController.getInstance().getImageLoader();
				ImageLoader imageLoader1 = AppController.getInstance().getImageLoader();
				@Override
				public int getCount() 
				{
					// TODO Auto-generated method stub
					return BigList.size();
				}

				@Override
				public Object getItem(int arg0) 
				{
					// TODO Auto-generated method stub
					return BigList.get(arg0);
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
						view=inflater.inflate(R.layout.bigdeals_list, null);
					if(imageLoader == null)
						 imageLoader = AppController.getInstance().getImageLoader();
					if(imageLoader1 == null)
						 imageLoader1 = AppController.getInstance().getImageLoader();
					
					
					NetworkImageView dealimage=(NetworkImageView) view.findViewById(R.id.dealimage);
					
					NetworkImageView retailerlogo=(NetworkImageView) view.findViewById(R.id.retailerlogo);
					
					
					TextView deal_id = (TextView)view.findViewById(R.id.bigdealid);
					TextView dealoffer =(TextView)view.findViewById(R.id.bigdealoffer);
					TextView bigdealpromo = (TextView)view.findViewById(R.id.bigdealpromo);
					
					
					BigDealItem itemmodel=BigList.get(position);
					
					dealimage.setImageUrl(itemmodel.getOfferimage(), imageLoader);
					retailerlogo.setImageUrl(itemmodel.getRetailerlogo(), imageLoader);
					
				//	Log.i("OfferNAme", itemmodel.getBigoffername());
					
					dealoffer.setText(itemmodel.getBigoffername());
					bigdealpromo.setText(itemmodel.getBigpromotext());
					
					deal_id.setText(itemmodel.getBigdeal_id());
					
										 
					 
					
					
					return view;
				}
				
			}
			
			private class BigDealItem
			{
				String bigdeal_id;
				public String getBigdeal_id() {
					return bigdeal_id;
				}
				public void setBigdeal_id(String bigdeal_id) {
					this.bigdeal_id = bigdeal_id;
				}
				public String getRetailer_id() {
					return retailer_id;
				}
				public void setRetailer_id(String retailer_id) {
					this.retailer_id = retailer_id;
				}
				public String getBigpromotext() {
					return bigpromotext;
				}
				public void setBigpromotext(String bigpromotext) {
					this.bigpromotext = bigpromotext;
				}
				public String getOfferimage() {
					return offerimage;
				}
				public void setOfferimage(String offerimage) {
					this.offerimage = offerimage;
				}
				public String getOfferthumbnails() {
					return offerthumbnails;
				}
				public void setOfferthumbnails(String offerthumbnails) {
					this.offerthumbnails = offerthumbnails;
				}
				public String getPrice() {
					return price;
				}
				public void setPrice(String price) {
					this.price = price;
				}
				public String getPercentage() {
					return percentage;
				}
				public void setPercentage(String percentage) {
					this.percentage = percentage;
				}
				public String getGender() {
					return gender;
				}
				public void setGender(String gender) {
					this.gender = gender;
				}
				public String getBigoffername() {
					return bigoffername;
				}
				public void setBigoffername(String bigoffername) {
					this.bigoffername = bigoffername;
				}
				public String getDealtype() {
					return dealtype;
				}
				public void setDealtype(String dealtype) {
					this.dealtype = dealtype;
				}
				public String getRetailerlogo() {
					return retailerlogo;
				}
				public void setRetailerlogo(String retailerlogo) {
					this.retailerlogo = retailerlogo;
				}
				String retailer_id;
				String bigpromotext;
				String offerimage;
				String offerthumbnails;
				String price;
				String percentage;	
				String gender;
				String bigoffername;
				String dealtype;
				String retailerlogo;
				
			}
			
			@Override
		    public void onDestroy() 
			{
		        super.onDestroy();
		        dissmissDialog();
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
			  public void onBackPressed()
			  {
				  
				  //WishlistFragment.onBackPressed();
				  //FavoritesFragment.onBackPressed();
				 // NotificationFragment.onBackPressed();
				 // SettingsFragment.onBackPressed();
				    if (doubleBackToExitPressedOnce) 
				    {
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
			