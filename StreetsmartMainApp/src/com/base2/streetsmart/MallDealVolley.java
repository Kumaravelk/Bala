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
import com.base2.streetsmart.GeoDealVolley;
import com.base2.streetsmart.GeoDealVolley.AllCategoryGEODeal;
import com.base2.streetsmart.GeoDealVolley.GEODeal;
import com.base2.streetsmart.GeoDealVolley.GeoDealItem;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MallDealVolley extends Activity
{
private String TAG=GeoDealVolley.class.getSimpleName();
	
	private ListView MallDealList;
	private ProgressDialog pdialog;
	private MallDealAdapter Malladapter;
	private List<MallDealItem> MallList=new ArrayList<MallDealItem>();
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
	
	ImageView filter,noimage,clearfliter,malllocationarea;
	
	TextView tv, cityname, category,mallname;
	
	String MALLURL = COMM_URL+"/streetsmartadmin4/shopping/filtersort";
	
	String Mallname,mallid="0",City_id;
	
	
	SessionManager session;
	String sname,suserid,semail,smobile,scityid,scityname,scountry,sgeonotify,secate_id,secate_name,sesubcate_id,sebrand,seretailer,secredit,secod,seemi,seexchange;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.malls);
		
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
			
			mallid = user.get(SessionManager.KEY_MALLID);
			Mallname = user.get(SessionManager.KEY_MALLNAME);
        
        categories = (RelativeLayout) findViewById(R.id.categorys);
		category = (TextView) findViewById(R.id.categoryname);
		filter = (ImageView) findViewById(R.id.filterimage);
		clearfliter = (ImageView) findViewById(R.id.clearfilterimage);
		MallDealList = (ListView) findViewById(R.id.MallDeallist);
		area = (RelativeLayout) findViewById(R.id.MallSelect);
		mallname = (TextView) findViewById(R.id.mallname);
		noimage = (ImageView)findViewById(R.id.noimage);
		malllocationarea =(ImageView) findViewById(R.id.malllocationarea);
		
		category.setText("All Category");
		
		
		if(mallid.equalsIgnoreCase("0"))
		{
			Mall_function();
		}
		else
		{
			mallname.setText("Deals in  "+Mallname);
		}
		
		
		
		Malladapter = new MallDealAdapter();
		
		MallDealList.setAdapter(Malladapter);
		
		MallDealList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String Deal_id = ((TextView) arg1.findViewById(R.id.malldeal_id)).getText()
						.toString();
				
			//	Log.i("GEO DEAL ID", Deal_id);
				Intent ddin = new Intent(MallDealVolley.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);
			}
		});
		
		
		if(sebrand.equalsIgnoreCase("0") && seretailer.equalsIgnoreCase("0") && secredit.equalsIgnoreCase("0") && secod.equalsIgnoreCase("0") && seemi.equalsIgnoreCase("0") && seexchange.equalsIgnoreCase("0"))
		{
			filter.setImageResource(R.drawable.filter_single);
			clearfliter.setImageResource(R.drawable.clearfilter);
		}
		else
		{
			filter.setImageResource(R.drawable.filter_selected);
			clearfliter.setImageResource(R.drawable.clearfilter_fill);
		}
		
		//cityname.setText(Subarea_id);
		
		//Log.i("mallid", mallid);
		
	/*	if(mallid.equalsIgnoreCase("0"))
        {
          Mall_function();
        }
        else
        {*/
        	//mallname.setText("Deals in  "+scityname);
        	
        //}
		
		filter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent filterintent = new Intent(MallDealVolley.this,
						FilterActivity.class);
				
				Bundle bu = new Bundle();
				
				bu.putString("Val", "99");
				
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
				new GEODeal().execute(MALLURL);
				
				
			}
		});

		categories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				Intent categoryintent = new Intent(MallDealVolley.this,
						JsonParser.class);
				categoryintent.putExtra("ActivityNo", ActivityNo);
				startActivityForResult(categoryintent, 4);
				overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);
			}
		});

		 
		malllocationarea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Mall_function();

			}
		});
		
		area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Mall_function();

			}
		});

		//mallname.setText("Deals in  "+Mallname);
		//category.setText(sesubcate_name);
		
		pdialog=new ProgressDialog(this);
		pdialog.setMessage("Sniffing Deals for you");
		//pDialog.setCancelable(false);
		pdialog.show();
		
		   RequestQueue rq = Volley.newRequestQueue(this);
			/* JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
		                url, null,
		                new Response.Listener<JSONObject>() {*/
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

					@Override
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
						Log.i("mall", mallid);
						Log.i("category_id", secate_id);
						Log.i("area_id", "0");
						*/
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
						headers.put("mall", mallid);
						headers.put("category_id", secate_id);
						headers.put("area_id", "0");
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
			//	 Log.i("Area Name Sub1", jsonArray.toString());
				for (int i = 0; i < jsonArray.length(); i++)

				{
					JSONArray jArray = (JSONArray) jsonArray.getJSONArray(i);

					for (int j = 0; j < jArray.length(); j++) {
	        
						 

						JSONObject json = (JSONObject) jArray.get(j);
	           
	                    
	                  //  Toast.makeText(getApplicationContext(), item.toString(),Toast.LENGTH_LONG).show();
						MallDealItem mallitem = new MallDealItem();
						mallitem.setMalldealoffername((String) json.get("offername"));
						mallitem.setMalldealid((String) json.get("deal_id"));
						mallitem.setMalldealretailername((String) json.get("retailer_name"));
						mallitem.setMalldealpromo((String) json.get("promotext"));
						mallitem.setMalldealofferimage((String) json.get("retailer_logo"));
						mallitem.setMalldealPrices((String) json.get("price"));
						
						mallitem.setMalldealtype((String) json.get("dealtype"));
						mallitem.setMallcredit((String) json.get("creditcard"));
						mallitem.setMallpercentage((String) json.get("percentage"));
						
						mallitem.setMalldate((String) json.get("edate"));
	                    //nm.setPubDate(item.optString("pubDate"));
	                    MallList.add(mallitem);
	                     
	            }
	        }
			}
	        catch(Exception e){
	            e.printStackTrace();
	        }
			
			int count = MallList.size();
			String count1 = String.valueOf(count);  
			if(count1.equalsIgnoreCase("0"))
			{	  
				MallList.clear();	 
				noimage.setImageResource(R.drawable.noimg);
				pdialog.dismiss();
				
				
			}else
			{
				noimage.setImageResource(0);
				 
			}

			
			Malladapter.notifyDataSetChanged();
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
				str_sort = data.getStringExtra("Sort");
				
				category.setText(sesubcate_name);
				//cityname.setText(scityname);
				filter.setImageResource(R.drawable.filter_selected);
				clearfliter.setImageResource(R.drawable.clearfilter_fill);
			
			}

			if (requestCode == 4) {
				cate_name = data.getStringExtra("Categoryname");
				secate_id = data.getStringExtra("Categoryid");
				subcate_name = data.getStringExtra("subcategoryname");
				sesubcate_id = data.getStringExtra("subcategoryid");
				category.setText(subcate_name);
				//session.createcategorymethod(secate_id,cate_name,sesubcate_id,subcate_name);
			}
			if (requestCode == 26) 
			{
				mallid = data.getStringExtra("mallid");
				Mallname = data.getStringExtra("Mall_name");
				City_id = data.getStringExtra("Cityid");
				//Toast.makeText(getApplicationContext(), Mallname,Toast.LENGTH_LONG).show();
				malllocationarea.setImageResource(R.drawable.locationselect);
				if(mallid.equalsIgnoreCase("0"))
				{
					mallname.setText("Deals in  "+scityname);
				}
				else
				{
					mallname.setText("Deals in  "+Mallname);
					session.createMAllMethod(mallid, Mallname);
				}
			      // Toast.makeText(getApplicationContext(), message+message1, Toast.LENGTH_LONG);
				new GEODeal().execute(MALLURL);	
	        }
			
			if(secate_id.equalsIgnoreCase("0"))
			{
			//	Log.i("All", secate_id);
				new AllCategoryMallDeal().execute(MALLURL);
			}
			else
			{
			//	Log.i("one", secate_id);
			  new GEODeal().execute(MALLURL);
			}		  
		
		}
	
	public void Mall_function()
	{
		ActivityNo = "26";
		Intent mallcategoryintent = new Intent(MallDealVolley.this,NewMAllActivity.class);
		mallcategoryintent.putExtra("ActivityNo", ActivityNo);
		 startActivityForResult(mallcategoryintent, 26);
		 //new MyMallDeal().execute(URL);
		 
	}
	
	
	class GEODeal extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MallDealVolley.this);
			pDialog.setMessage("Sniffing Deals for you");
		   // pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			MallList = getGEOListData(scity);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pDialog.isShowing())
				pDialog.dismiss();

			int arraysize = MallList.size();
			if (arraysize == 0) {
				MallList.clear();
				func_geodeal();
			
				noimage.setImageResource(R.drawable.noimg);
			} 
			else {

				noimage.setImageResource(0);
				func_geodeal();

			}
			
			// pDialog.dismiss();
		}
	}
	
	private ArrayList<MallDealItem> getGEOListData(String scity) {
		// TODO Auto-generated method stub
		
		ArrayList<MallDealItem> MallList = new ArrayList<MallDealItem>();

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
			nameValuePairs.add(new BasicNameValuePair("mall", mallid));
			nameValuePairs.add(new BasicNameValuePair("category_id", secate_id));
			nameValuePairs.add(new BasicNameValuePair("area_id", "0"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
			
			for (int i = 0; i < jsonArray.length(); i++)
				
			{
				JSONArray jArray = (JSONArray)jsonArray.getJSONArray(i);
				for(int j=0;j<jArray.length();j++)
				{

					MallDealItem mallitem = new MallDealItem();
				JSONObject json = (JSONObject) jArray.get(j);
			
				mallitem.setMalldealoffername((String) json.get("offername"));
				mallitem.setMalldealid((String) json.get("deal_id"));
				mallitem.setMalldealretailername((String) json.get("retailer_name"));
				mallitem.setMalldealpromo((String) json.get("promotext"));
				mallitem.setMalldealofferimage((String) json.get("retailer_logo"));
				mallitem.setMalldealPrices((String) json.get("price"));
				
				mallitem.setMalldealtype((String) json.get("dealtype"));
				mallitem.setMallcredit((String) json.get("creditcard"));
				mallitem.setMallpercentage((String) json.get("percentage"));
				
				mallitem.setMalldate((String) json.get("edate"));
				
				
				
				MallList.add(mallitem);

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
		return MallList;
	
	}
	
	public void func_geodeal()
	{
		
        Malladapter = new MallDealAdapter();
		
		MallDealList.setAdapter(Malladapter);
		
		MallDealList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String Deal_id = ((TextView) arg1.findViewById(R.id.malldeal_id)).getText()
						.toString();
				
			//	Log.i("GEO DEAL ID", Deal_id);
				Intent ddin = new Intent(MallDealVolley.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);
			}
		});
	}
	
	
	
	
	
	class AllCategoryMallDeal extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MallDealVolley.this);
			pDialog.setMessage("Sniffing Deals for you");
		   // pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			MallList = AllgetGEOListData(scity);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pDialog.isShowing())
				pDialog.dismiss();

			int arraysize = MallList.size();
			if (arraysize == 0) {
				MallList.clear();
				func_Allgeodeal();
			
				noimage.setImageResource(R.drawable.noimg);
			} 
			else {

				noimage.setImageResource(0);
				func_geodeal();

			}
			
			// pDialog.dismiss();
		}
	}
	
	private ArrayList<MallDealItem> AllgetGEOListData(String scity) {
		// TODO Auto-generated method stub
		
		ArrayList<MallDealItem> MallList = new ArrayList<MallDealItem>();

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
			nameValuePairs.add(new BasicNameValuePair("mall", mallid));
			nameValuePairs.add(new BasicNameValuePair("category_id", secate_id));
			nameValuePairs.add(new BasicNameValuePair("area_id", "0"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
			
			for (int i = 0; i < jsonArray.length(); i++)
				
			{
				JSONArray jArray = (JSONArray)jsonArray.getJSONArray(i);
				for(int j=0;j<jArray.length();j++)
				{

					MallDealItem mallitem = new MallDealItem();
				JSONObject json = (JSONObject) jArray.get(j);
			
				mallitem.setMalldealoffername((String) json.get("offername"));
				mallitem.setMalldealid((String) json.get("deal_id"));
				mallitem.setMalldealretailername((String) json.get("retailer_name"));
				mallitem.setMalldealpromo((String) json.get("promotext"));
				mallitem.setMalldealofferimage((String) json.get("retailer_logo"));
				mallitem.setMalldealPrices((String) json.get("price"));
				
				mallitem.setMalldealtype((String) json.get("dealtype"));
				mallitem.setMallcredit((String) json.get("creditcard"));
				mallitem.setMallpercentage((String) json.get("percentage"));
				
				mallitem.setMalldate((String) json.get("edate"));
				
				
				
				MallList.add(mallitem);

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
		return MallList;
	
	}
	
	public void func_Allgeodeal()
	{
		
        Malladapter = new MallDealAdapter();
		
		MallDealList.setAdapter(Malladapter);
		
		MallDealList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String Deal_id = ((TextView) arg1.findViewById(R.id.malldeal_id)).getText()
						.toString();
				
			//	Log.i("GEO DEAL ID", Deal_id);
				Intent ddin = new Intent(MallDealVolley.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);
			}
		});
	}
	
	
	 private class MallDealAdapter extends BaseAdapter
		{

			private LayoutInflater inflater;
			ImageLoader imageLoader = AppController.getInstance().getImageLoader();
			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return MallList.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return MallList.get(arg0);
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
					view=inflater.inflate(R.layout.mall_list, null);
				if(imageLoader == null)
					 imageLoader = AppController.getInstance().getImageLoader();
				
				
				NetworkImageView imageview=(NetworkImageView)view.findViewById(R.id.mallretailerlogo);
				
				TextView malloffername = (TextView) view.findViewById(R.id.malldealoffer);
				TextView mallpromotext = (TextView) view.findViewById(R.id.malldealpromo);
				
				TextView mallprice = (TextView) view.findViewById(R.id.malldealprice);
				
				TextView malldealid = (TextView)view.findViewById(R.id.malldeal_id);
				
				
				
				TextView mallretailername = (TextView)view.findViewById(R.id.mallretailername);
				
				ImageView dealtype = (ImageView)view.findViewById(R.id.malldealtype);
				
				ImageView creditcard = (ImageView)view.findViewById(R.id.mallcreditcard);
				
				TextView malldate = (TextView)view.findViewById(R.id.malldate);
				
				 
				
				MallDealItem malldealitem=MallList.get(position);
				
				
				imageview.setImageUrl(malldealitem.getMalldealofferimage(), imageLoader);
				
				
				malloffername.setText(malldealitem.getMalldealoffername());
				mallpromotext.setText(malldealitem.getMalldealpromo());
				mallretailername.setText(malldealitem.getMalldealretailername());
				mallprice.setText("Rs. "+malldealitem.getMalldealPrices()+"/-  OR   "+malldealitem.getMallpercentage()+"% OFF");
				malldealid.setText(malldealitem.getMalldealid());
				//Dealholder.mallpercentage.setText(malldealitem.getMallpercentage()+"% OFF");
				
				
	            String newdate = malldealitem.getMalldate().toString();
				
			
				
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
				    
				    malldate.setText(str);
				
	           String mall_dealtype = malldealitem.getMalldealtype().toString();
				
				String mall_creditcard = malldealitem.getMallcredit().toString();
				
				if(mall_dealtype.equalsIgnoreCase("4"))
				{
					dealtype.setVisibility(View.VISIBLE);
				}
				else
				{
					dealtype.setVisibility(View.INVISIBLE);
				}
				
				if(mall_creditcard.equalsIgnoreCase("1"))
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

	
	class MallDealItem
	{
		
		String malldealofferimage;
		String malldealretailername;
		String malldealofferthumps;
		String malldealPrices;
		String mallpercentage;
		String malldate;
		String malldealtype;
		String mallcredit;
		String malldealid;
		String malldealoffername;
		String malldealpromo;
		
		
		public String getMalldealoffername() {
			return malldealoffername;
		}
		public void setMalldealoffername(String malldealoffername) {
			this.malldealoffername = malldealoffername;
		}
		public String getMalldealpromo() {
			return malldealpromo;
		}
		public void setMalldealpromo(String malldealpromo) {
			this.malldealpromo = malldealpromo;
		}
		public String getMalldealofferimage() {
			return malldealofferimage;
		}
		public void setMalldealofferimage(String malldealofferimage) {
			this.malldealofferimage = malldealofferimage;
		}
		public String getMalldealretailername() {
			return malldealretailername;
		}
		public void setMalldealretailername(String malldealretailername) {
			this.malldealretailername = malldealretailername;
		}
		public String getMalldealofferthumps() {
			return malldealofferthumps;
		}
		public void setMalldealofferthumps(String malldealofferthumps) {
			this.malldealofferthumps = malldealofferthumps;
		}
		public String getMalldealPrices() {
			return malldealPrices;
		}
		public void setMalldealPrices(String malldealPrices) {
			this.malldealPrices = malldealPrices;
		}
		public String getMallpercentage() {
			return mallpercentage;
		}
		public void setMallpercentage(String mallpercentage) {
			this.mallpercentage = mallpercentage;
		}
		public String getMalldate() {
			return malldate;
		}
		public void setMalldate(String malldate) {
			this.malldate = malldate;
		}
		public String getMalldealtype() {
			return malldealtype;
		}
		public void setMalldealtype(String malldealtype) {
			this.malldealtype = malldealtype;
		}
		public String getMallcredit() {
			return mallcredit;
		}
		public void setMallcredit(String mallcredit) {
			this.mallcredit = mallcredit;
		}
		public String getMalldealid() {
			return malldealid;
		}
		public void setMalldealid(String malldealid) {
			this.malldealid = malldealid;
		}
		
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
