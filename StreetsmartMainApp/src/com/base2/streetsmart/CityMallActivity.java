package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.base2.streetsmart.CityAreaActivity.MySubArea;
import com.base2.streetsmart.CityAreaActivity.MyTaskCity;
import com.example.AdapterClass.CityListAdapter;
import com.example.AdapterClass.MallsListAdapter;
import com.example.AdapterClass.SubAreaAdapter;
import com.example.ItemClass.CityItem;
import com.example.ItemClass.MallsItem;
import com.example.ItemClass.SubCityItem;
import com.example.Session.SessionManager;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CityMallActivity extends Activity {
	
	EditText city_search,mall_search;
    
    ListView CityList,MallList;
    
    CityListAdapter cityadap;
    
    MallsListAdapter malladap;
    
    RelativeLayout MallLayout;
    
    ArrayList<CityItem> city_item;
    
    String CityName,CityID,MallName,MallID = "0";
    
    String City_ID;
    
    SessionManager session;
    
    ArrayList malllist_details;
    
    String CityURL = COMM_URL+"/streetsmartadmin4/shopping/citylist";
    
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citymall);
		
		city_search = (EditText)findViewById(R.id.citymallsearch);
		mall_search = (EditText)findViewById(R.id.mallsearch);
		CityList = (ListView)findViewById(R.id.CityMallList);
		MallList =(ListView)findViewById(R.id.MallListView);
		MallLayout = (RelativeLayout)findViewById(R.id.malllayout);
		city_search.setCursorVisible(false);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(CityMallActivity.this,
					"Internet Connection Error",
					"Please Check the Internet Connection", false);
			 
			return;
		}
		
		
		  session = new SessionManager(getApplicationContext());
			
			HashMap<String, String> user = session.getUserDetails();
			
			MallID =user.get(SessionManager.KEY_CITYID); 
			MallName =user.get(SessionManager.KEY_CITYNAME); 
		
		mall_search.setCursorVisible(false);
		
        new MyTaskCity().execute(CityURL);		
        city_search.setOnClickListener(new View.OnClickListener() {

    	    public void onClick(View v) {
    	    	city_search.setFocusable(true);
    	    	city_search.setCursorVisible(true);
    	    }
      });
        mall_search.setOnClickListener(new View.OnClickListener() {

    	    public void onClick(View v) {
    	    	mall_search.setFocusable(true);
    	    	mall_search.setCursorVisible(true);
    	    }
      });
		
        city_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			
				
				String text = city_search.getText().toString()
						.toLowerCase(Locale.getDefault());
				cityadap.filter(text);
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
        
        
        mall_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = mall_search.getText().toString()
						.toLowerCase(Locale.getDefault());
				malladap.filter(text);
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
	
	class MyTaskCity extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CityMallActivity.this);
			pDialog.setMessage("Loading Your City");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			city_item = getCityListData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog.isShowing())
				pDialog.dismiss();

			func_city();

		}

	}
	
	private ArrayList<CityItem> getCityListData() {
		ArrayList<CityItem> city_details = new ArrayList<CityItem>();
		try {
			BufferedReader bufferedReader = null;
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(CityURL));
			HttpResponse response;
			response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			String page = stringBuffer.toString();

			JSONArray jsonArray = new JSONArray(page);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				CityItem areai = new CityItem();

				areai.setCityname((String) json.get("city_name"));

				areai.setCityid((String) json.get("city_id"));

				areai.setCountryid((String) json.get("country_id"));

				city_details.add(areai);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return city_details;

	}
	
	public void func_city() {
		// TODO Auto-generated method stub

		
		cityadap = new CityListAdapter(this, city_item);
		// Arealistview.setAdapter(new CityListAdapter(this, city_item));
		CityList.setAdapter(cityadap);
		CityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				CityName = ((TextView) arg1.findViewById(R.id.cityname))
						.getText().toString();
				CityID = ((TextView) arg1.findViewById(R.id.cityid)).getText()
						.toString();
				
				MallListMethod(CityID);

			}

		});

	}
	
	
	
	public void MallListMethod(String city_id) {

		City_ID = city_id;
		// TODO Auto-generated method stub

		MallLayout.setVisibility(View.VISIBLE);

		new MyMall().execute();
	}
	
		class MyMall extends AsyncTask<String, Void, String> {
			ProgressDialog pDialog;

			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(CityMallActivity.this);
				pDialog.setMessage("Loading Malls...");
				pDialog.setCancelable(true);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub

				try {
					malllist_details = getMallListData(City_ID);
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

				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mall_search.getWindowToken(), 0);
				func_malls();

			}

		}

		public ArrayList getMallListData(String City_ID) throws URISyntaxException {

			ArrayList malllist_details = new ArrayList();
			
			

			try {
				BufferedReader bufferedReader = null;
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"android");
				HttpGet request = new HttpGet();
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(
						COMM_URL+"/streetsmartadmin4/shopping/mallcity/"
								+ City_ID));
				HttpResponse response;
				response = client.execute(request);
				bufferedReader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer stringBuffer = new StringBuffer("");
				String line = "";

				String NL = System.getProperty("line.separator");

				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line + NL);
					System.out.print(stringBuffer);
				}
				String mallstr = stringBuffer.toString();

				JSONObject jsonObj = new JSONObject(mallstr);

				JSONArray arrayJson = jsonObj.getJSONArray("result");

				for (int i = 0; i < arrayJson.length(); i++) {

					JSONObject json = (JSONObject) arrayJson.get(i);

					MallsItem areai = new MallsItem();

					areai.setMall_name((String) json.get("mall_name"));

					areai.setMall_id((String) json.get("mall_id"));
					
					

					malllist_details.add(areai);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return malllist_details;

		}

		public void func_malls() {
			
			malladap = new MallsListAdapter(this, malllist_details);

			MallList.setAdapter(malladap);
			// Malllistview.setAdapter(new MallsListAdapter(this,
			// malllist_details));

			MallList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub

					MallName = ((TextView) arg1.findViewById(R.id.textmallname))
							.getText().toString();
					MallID = ((TextView) arg1.findViewById(R.id.textmallid))
							.getText().toString();
					
					//Toast.makeText(getApplicationContext(), MallName, Toast.LENGTH_LONG).show();
					session.createMAllMethod(MallID, MallName);
					Intent intent = new Intent();
					intent.putExtra("Cityid", CityID);
					intent.putExtra("mallid", MallID);
					intent.putExtra("Mall_name", MallName);
					
					setResult(26, intent);
					
					finish();
					
				}
			});
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("Cityid", CityID);
			intent.putExtra("mallid", MallID);
			intent.putExtra("Mall_name", MallName);

			setResult(26, intent);
			
			finish();
		}
		
		
	}