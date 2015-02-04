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
import com.base2.streetsmart.CityActivity.MyTaskCity;
import com.example.AdapterClass.CityListAdapter;
import com.example.AdapterClass.SubAreaAdapter;
import com.example.ItemClass.CityItem;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class CityAreaActivity extends Activity
{
	

     EditText city_search,area_search;
     
     ListView CityList,AreaList;
     
     CityListAdapter cityadap;
     
     SubAreaAdapter areaadap;
     
     RelativeLayout subArea;
     
     ArrayList<CityItem> city_item;
     
     String CityName,CityID,AreaName,AreaID;
     
     String City_ID;
     
     ArrayList subarea_details;
     
     SessionManager session;
     
     String CityURL = COMM_URL+"/streetsmartadmin4/shopping/citylist";
     
     AlertDialogManager alert = new AlertDialogManager();
     ConnectionDetector cd;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cityarea);
		
		ActionBar actionBar = getActionBar();
		  actionBar.hide();
		
		city_search = (EditText)findViewById(R.id.cityareasearch);
		area_search = (EditText)findViewById(R.id.subareasearch);
		CityList = (ListView)findViewById(R.id.Citylist);
		AreaList =(ListView)findViewById(R.id.AreaListView);
		subArea = (RelativeLayout)findViewById(R.id.subarea);
		city_search.setCursorVisible(false);
		area_search.setCursorVisible(false);
         session = new SessionManager(getApplicationContext());
		
		HashMap<String, String> user = session.getUserDetails();
		
		CityID =user.get(SessionManager.KEY_CITYID); 
		CityName =user.get(SessionManager.KEY_CITYNAME); 
		AreaName = user.get(SessionManager.KEY_AREANAME); 
		AreaID = user.get(SessionManager.KEY_AREAID); 
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(CityAreaActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
		
         new MyTaskCity().execute(CityURL);		
         city_search.setOnClickListener(new View.OnClickListener() {

     	    public void onClick(View v) {
     	    	//city_search.setFocusable(true);
     	    	//city_search.setCursorVisible(true);
     	    }
       });
         area_search.setOnClickListener(new View.OnClickListener() {

     	    public void onClick(View v) {
     	    	
     	    	
     	    	area_search.setFocusable(true);
     	    	area_search.setCursorVisible(true);
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
         
         
         area_search.addTextChangedListener(new TextWatcher() {

 			@Override
 			public void afterTextChanged(Editable arg0) {
 				// TODO Auto-generated method stub
 				String text = area_search.getText().toString()
 						.toLowerCase(Locale.getDefault());
 				 areaadap.filter(text);
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
			pDialog = new ProgressDialog(CityAreaActivity.this);
			pDialog.setMessage("Loading Cities");
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

			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(city_search.getWindowToken(), 0);
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
				
				Area_city(CityID);

			}

		});

	}
	
	
	
	public void Area_city(String city_id) {

		City_ID = city_id;
		// TODO Auto-generated method stub

		subArea.setVisibility(View.VISIBLE);

		new MySubArea().execute();
	}
	
	class MySubArea extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CityAreaActivity.this);
			pDialog.setMessage("Loading Areas");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				subarea_details = getSubAreaListData();
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

			func_subarea();
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(area_search.getWindowToken(), 0);

		}

	}
	
	public ArrayList getSubAreaListData() throws URISyntaxException {

		ArrayList subarea_details = new ArrayList();
		try {
			BufferedReader bufferedReader = null;
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(
					COMM_URL+"/streetsmartadmin4/shopping/specificarealist/"
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
			String page = stringBuffer.toString();

			JSONArray jsonArray = new JSONArray(page);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				SubCityItem sub_area = new SubCityItem();

				sub_area.setArea_name((String) json.get("area_name"));

				sub_area.setArea_id((String) json.get("area_id"));

				subarea_details.add(sub_area);

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

		return subarea_details;

	}
	
	public void func_subarea() {
		areaadap = new SubAreaAdapter(this, subarea_details);

		AreaList.setAdapter(areaadap);
		// subcity.setAdapter(new SubAreaAdapter(this, subarea_details));

		AreaList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				AreaName = ((TextView) arg1.findViewById(R.id.subareaname))
						.getText().toString();
				AreaID = ((TextView) arg1.findViewById(R.id.subareaid))
						.getText().toString();

				session.createAreaMethod(AreaID, AreaName);
				
			//Toast.makeText(getApplicationContext(), AreaID, Toast.LENGTH_SHORT).show();
				
				
				Intent intent = new Intent();
				intent.putExtra("Cityname", CityName);
				intent.putExtra("Cityid", CityID);
				intent.putExtra("subCityid", AreaID);
				intent.putExtra("subCityname", AreaName);

				setResult(17, intent);

				finish();


			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent();
		intent.putExtra("Cityname", CityName);
		intent.putExtra("Cityid", CityID);
		intent.putExtra("subCityid", AreaID);
		intent.putExtra("subCityname", AreaName);

		setResult(17, intent);

		finish();
		
		
	}
	
}