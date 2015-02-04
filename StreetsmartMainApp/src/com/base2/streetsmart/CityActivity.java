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
import com.example.AdapterClass.CityListAdapter;
import com.example.ItemClass.CityItem;
import com.example.Session.SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CityActivity extends Activity {
	
	
	EditText citySearch;
	
	ListView cityList;
	
	SessionManager session;
	String CityURL = COMM_URL+"/streetsmartadmin4/shopping/citylist";
	
	ArrayList<CityItem> city_item;
	
	CityListAdapter cityadap;
	
	String CityName,CityID;
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		
		ActionBar actionBar = getActionBar();
		  actionBar.hide();
		  
		  cd = new ConnectionDetector(getApplicationContext());

			 
			if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(CityActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
				 
				return;
			}
		  
		  
		
		//citySearch = (EditText)findViewById(R.id.citySearch);
		
		cityList = (ListView)findViewById(R.id.CitylistView);
		
		
		session = new SessionManager(getApplicationContext());
		
		HashMap<String, String> user = session.getUserDetails();
		
		CityID =user.get(SessionManager.KEY_CITYID); 
		CityName =user.get(SessionManager.KEY_CITYNAME); 
		//citySearch.setCursorVisible(false);
		new MyTaskCity().execute(CityURL);		
		  /* citySearch.setOnClickListener(new View.OnClickListener() {

	    	    public void onClick(View v) {
	    	    	citySearch.setFocusable(true);
	    	    	citySearch.setCursorVisible(true);
	    	    }
	      });
	      
		
		citySearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = citySearch.getText().toString()
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
		});*/
	}
	
	
	
	class MyTaskCity extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CityActivity.this);
			pDialog.setMessage("Loading Your City");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
          try
          {
			city_item = getCityListData();
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

			func_city();

		}

	}
	
	private ArrayList getCityListData() throws URISyntaxException {
		
		
		ArrayList city_details = new ArrayList();
		BufferedReader bufferedReader = null;
		try {
			
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
		cityList.setAdapter(cityadap);
		cityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				CityName = ((TextView) arg1.findViewById(R.id.cityname))
						.getText().toString();
				CityID = ((TextView) arg1.findViewById(R.id.cityid)).getText()
						.toString();
				
				
				Intent intent = new Intent();
				intent.putExtra("Cityid", CityID);
				intent.putExtra("Cityname", CityName);

				setResult(16, intent);

				finish();

			}

		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.putExtra("Cityid", CityID);
		intent.putExtra("Cityname", CityName);

		setResult(16, intent);

		finish();
	}


}
