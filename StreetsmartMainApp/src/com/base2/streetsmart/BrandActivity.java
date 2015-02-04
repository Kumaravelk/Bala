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

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.example.AdapterClass.BrandListAdapter;
import com.example.ItemClass.BrandItem;
import com.example.ItemClass.RetailerItem;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;
//import com.example.searchlistview.ListViewAdapter;
//import com.example.searchlistview.R;

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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BrandActivity extends Activity {

	private ArrayList arrayofBrandList;

	String Brandid, Brandname = "ALL BRANDS";

	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	BrandItem brandItem;
    EditText editsearch;
	ListView Brandlist;
	TextView tv;
	SessionManager session;

	String str, brandstr,subcategoryid;

	String URL = COMM_URL+"/streetsmartadmin4/shopping/brandlist";

	final Context context = this;
	BrandListAdapter adapter;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brand);
		
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.hide();
		 
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		
		Brandid = user.get(SessionManager. KEY_BRANDID);
		
		subcategoryid = user.get(SessionManager. KEY_SUBCATEGORYID);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(BrandActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
		
		//Brandname = user.get(SessionManager. KEY_BRANDNAME);
		
		new MyBrand().execute(URL);
		
		 
		

		
		
	//	Brandlist.setAdapter(new BrandListAdapter(this, arrayofBrandList));
		  tv = (TextView) findViewById(R.id.textView1);
		
          editsearch = (EditText) findViewById(R.id.search);

		
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

	class MyBrand extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(BrandActivity.this);
			pDialog.setMessage("Loading Brands");
		    pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				arrayofBrandList = getListData();
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

		     
		     
		     int arraysize = arrayofBrandList.size();
				if (arraysize == 0) {
					arrayofBrandList.clear();
					Toast.makeText(getApplicationContext(), "No Brands Available",Toast.LENGTH_LONG).show();
					brand_func();
					tv.setVisibility(1);
				}
				else {

					tv.setVisibility(1);
					brand_func();

				}
		    
		     
			// pDialog.dismiss();
		}
	}

	private ArrayList getListData() throws URISyntaxException {
		// TODO Auto-generated method stub

		ArrayList<BrandItem> arrayofBrandList = new ArrayList<BrandItem>();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/displaybrand");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		
			nameValuePairs.add(new BasicNameValuePair("subcategoryid", subcategoryid));
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray = new JSONArray(str);
			
			

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				BrandItem brandItem = new BrandItem();

				// String category_id = (String) json.get("category_name");

				brandItem.setBrandid((Integer) json.get("brand_id"));
				brandItem.setBrandname((String) json.get("brand_name"));
				brandItem.setBrandicon((String) json.get("brand_icon"));
				
				arrayofBrandList.add(brandItem);

			}

			 
		}

		catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrayofBrandList;

	}
	
	public void brand_func()
	{
		Brandlist = (ListView) findViewById(R.id.brandlist);

		Brandlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Brandid = ((TextView) arg1.findViewById(R.id.brandid))
						.getText().toString();

				Brandname = ((TextView) arg1.findViewById(R.id.brandname))
						.getText().toString();
				session.createBrand(Brandid,Brandname);
				Intent intent = new Intent();
				intent.putExtra("Brandid", Brandid);
				intent.putExtra("Brandname", Brandname);

				setResult(1, intent);
				
				finish();

			}
		});
		adapter = new BrandListAdapter(this, arrayofBrandList);
		Brandlist.setAdapter(adapter);
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
		
		Intent intent = new Intent();
		intent.putExtra("Brandid", Brandid);
		intent.putExtra("Brandname", Brandname);

		setResult(1, intent);

		finish();
	}
	
	

}
