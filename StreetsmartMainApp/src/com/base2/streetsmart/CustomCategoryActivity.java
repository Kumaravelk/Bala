package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
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


import com.example.AdapterClass.CustomCategoryAdapter;
import com.example.ItemClass.CustomCategoryListModel;
import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class CustomCategoryActivity extends Activity {


	
	SessionManager session;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,
			sgeonotify, spushnotify, semailnotify,scateories;
	
	public CustomCategoryActivity activity=null;
	ListView cus_cat_list;
	String cus_categorys;
	String cus_cat_error,cus_cat_message;
	Button cus_cat_next;
	Context con;
	
	CustomCategoryAdapter adapter;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	
	ArrayList cus_cat_arr=new ArrayList();
	
	//String CustomURL = COMM_URL+"/streetsmartadmin4/shopping/categorylist";
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcategories);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();

		suserid = user.get(SessionManager.KEY_USERID);
		sname = user.get(SessionManager.KEY_NAME);
		semail = user.get(SessionManager.KEY_EMAIL);
		smobile = user.get(SessionManager.KEY_MOBILE);
		scityid = user.get(SessionManager.KEY_CITYID);
		scityname = user.get(SessionManager.KEY_CITYNAME);
		scountry = user.get(SessionManager.KEY_COUNTRY);
		sgeonotify = user.get(SessionManager.KEY_GEO_NOTIFI);
		spushnotify = user.get(SessionManager.KEY_PUSH_NOTIFI);
		semailnotify = user.get(SessionManager.KEY_EMAIL_NOTIFI);
		scateories =user.get(SessionManager.KEY_CATEGORIES);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(CustomCategoryActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		new MyCategory().execute();
	
		
		
		
	}
	
	
	
class MyCategory extends AsyncTask<String, Void, String> {
		
		ProgressDialog pDialog;
		
		
		 protected void onPreExecute() {

			// TODO Auto-generated method stub

			super.onPreExecute();

			pDialog = new ProgressDialog(CustomCategoryActivity.this);

			pDialog.setMessage("Loading Your Category");
			pDialog.setCancelable(false);
			pDialog.show();
			
			//Toast.makeText(c, "hi Pre", Toast.LENGTH_LONG).show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				cus_cat_arr=getCusCatListData();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			
			if(pDialog.isShowing()){
				
				pDialog.dismiss();
				
				
				fun_cus_category();
			}
		}
			
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getCusCatListData() throws URISyntaxException{
		
		//BufferedReader reader=null;
		InputStream is = null;
		
		try{
		
			//	Log.d("Value Check :", ""+41+"");
			 DefaultHttpClient httpClient = new DefaultHttpClient();
		      HttpPost httpPost = new HttpPost("http://54.169.81.215/streetsmartadmin4/shopping/unselectedcategories");
		   
		      
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		      nameValuePairs.add(new BasicNameValuePair("userid",suserid));
		     

		      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		      
		      HttpResponse httpResponse = httpClient.execute(httpPost);
		      HttpEntity httpEntity = httpResponse.getEntity();
		      is = httpEntity.getContent();
			
			//reader =new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			
		      BufferedReader reader = new BufferedReader(new InputStreamReader(
			          is, "iso-8859-1"), 8);
		    
		      StringBuilder sb = new StringBuilder();
		    
		      String line = null;
		      while ((line = reader.readLine()) != null) {
		        sb.append(line + "n");
		      }
		      is.close();
		    String  str_category = sb.toString();
			
		//	Log.i("String", str_category);
			
			
			
			JSONArray jArray=new JSONArray(str_category);
			
			for (int i = 0; i < jArray.length(); i++) {
				
				JSONObject jObject=(JSONObject)jArray.get(i);
				
				CustomCategoryListModel model=new CustomCategoryListModel();
				
				model.setCus_cat_id((String)jObject.get("category_id"));
				model.setCus_cat_name((String)jObject.get("category_name"));
				model.setFlag((Integer)jObject.get("flag"));
				
				
				cus_cat_arr.add(model);
				
			}
			
			
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
		return cus_cat_arr;
	
		
	}
	
	
	protected void fun_cus_category(){
		
		
		cus_cat_list=(ListView)findViewById(R.id.cutlistView);
		cus_cat_next=(Button)findViewById(R.id.custom_button);
		
		adapter=new CustomCategoryAdapter(this, cus_cat_arr);
		cus_cat_list.setAdapter(adapter);
		
		
		cus_cat_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			String str=adapter.checkButtonClick();
			
		//	Log.d("Sel Val ", str);
			
				cus_categorys = str.substring(0, str.length()-1);
				
				
			//	Log.d("Catrgory ", cus_categorys);
				try {
					
					customizecategory(cus_categorys);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//Toast.makeText(getBaseContext(), "Selected Favorite SubCategory :" +str+cus_categorys, Toast.LENGTH_LONG).show();
			}
		});
		
	}
	
	
	
	public void customizecategory(String categoryval) throws JSONException
	{
		
		new customizecategoryAsync().execute();
	}
	
	class customizecategoryAsync extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialogg;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialogg = new ProgressDialog(CustomCategoryActivity.this);
			pDialogg.setMessage("Updating ...");
			pDialogg.setCancelable(false);
			pDialogg.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			 
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://54.169.81.215/streetsmartadmin4/shopping/usercategory");

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user_id",suserid ));
				nameValuePairs.add(new BasicNameValuePair("category_id",cus_categorys));
				 
         //      Log.d("CATEGORY Valuew", cus_categorys);
               
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String str = EntityUtils.toString(response.getEntity());

				JSONObject jsonobj = new JSONObject(str);

				JSONObject objjson = jsonobj.getJSONObject("result");
				
				cus_cat_error = objjson.getString("error");
				cus_cat_message = objjson.getString("message");
				
				
				
			}
			catch(IOException e)
			{
				
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
			if (pDialogg.isShowing())
				pDialogg.dismiss();
			
			Toast.makeText(getApplicationContext(), " Preferences Updated ",Toast.LENGTH_LONG).show();
			 
			 if(MainActivity.instance != null) 
			  {
			        try 
			        {  
			        	MainActivity.instance.finish(); 
			        } catch (Exception e)
			        {
			        	
			        }
				}

			finish();
			
		}
		
	}


	
}
