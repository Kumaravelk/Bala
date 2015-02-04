package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.streetsmart.R;
import com.base2.streetsmart.GeoDealVolley.AllCategoryGEODeal;
import com.base2.streetsmart.GeoDealVolley.GEODeal;
import com.example.AdapterClass.SearchAdapter;
import com.example.AdapterClass.Item;
import com.example.AdapterClass.SuggestGetSet;
import com.example.AdapterClass.SuggestionAdapter;
import com.example.Database.DBController;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	String selected = "0", page;
	ArrayList<Item> arrayOfList;
	
	String URL = COMM_URL+"/streetsmartadmin4/shopping/searchengine/"
			+ selected;
	AutoCompleteTextView av;
	String scityid;
	
	String areaid;
	
	SessionManager session;
	
	Button Clear_text;
	
	ImageView searchlocationarea;
	
	RelativeLayout searcharea;
	
	ListView lv1;

	TextView search_cityname;
	
	String cityid,scityname,Subarea_name,Subarea_id;
	
	// Button next=(Button)findViewById(R.id.button1);
	// AutoCompleteTextView acTextView = (AutoCompleteTextView)
	// findViewById(R.id.autoComplete);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		ActionBar searchaction = getActionBar();
		
		searchaction.setTitle("Search");
		
		Clear_text = (Button)findViewById(R.id.clearText);
		Button next = (Button) findViewById(R.id.button1);
		
		searchlocationarea = (ImageView)findViewById(R.id.searchlocationarea);
		search_cityname = (TextView)findViewById(R.id.searchcityname);
		searcharea = (RelativeLayout)findViewById(R.id.searcharea);
		
		session = new SessionManager(getApplicationContext());
		 HashMap<String, String> user = session.getUserDetails();
		 
		 scityid =user.get(SessionManager.KEY_CITYID); 
		 scityname = user.get(SessionManager.KEY_CITYNAME); 
		 
		 Subarea_id = user.get(SessionManager.KEY_AREAID); 
		 Subarea_name =  user.get(SessionManager.KEY_AREANAME); 
		 lv1 = (ListView) findViewById(R.id.searchlist);
		 
		 if(Subarea_id.equalsIgnoreCase("0"))
		 {
			 search_cityname.setText("Deals in " + scityname);
		 }
		 else
		 {
		 
		 search_cityname.setText("Deals in " + Subarea_name);
		 }
		
		// next.setVisibility(4);
		 av = (AutoCompleteTextView) findViewById(R.id.autoComplete);
		Typeface font = Typeface.createFromAsset(getAssets(), "ProximaNova-Regular.otf");
		final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
		acTextView.setThreshold(1);
		acTextView.setTextSize(16);
		acTextView.setTypeface(font);
	
		acTextView.setAdapter(new SuggestionAdapter(this, acTextView.getText()
				.toString()));
		

		// Toast.makeText(getApplicationContext(), " not clicked",
		// Toast.LENGTH_LONG).show();

		acTextView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						//arrayOfList.clear();
						
						lv1.setAdapter(null);
						selected = acTextView.getText().toString();
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
								.permitAll().build();
						StrictMode.setThreadPolicy(policy);
					
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(acTextView.getWindowToken(), 0);
							
						 SearchSug search = new SearchSug(selected);
					 search.execute();
						
					}

				});
		
		Clear_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				cles();
				
				/*arrayOfList.clear();
				func();*/
				
			}
		});
		
		
		searchlocationarea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent areaIntent = new Intent(SearchActivity.this,NewCityActivity.class);
				
				startActivityForResult(areaIntent, 47);
				overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);

			}
		});
		
		
		searcharea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent areaIntent = new Intent(SearchActivity.this,NewCityActivity.class);
				
				startActivityForResult(areaIntent, 47);
				overridePendingTransition(R.anim.flip_up_out, R.anim.flip_up_in);

			}
		});

	}
	
	public void cles()
	{
		av.setText("");
		lv1.setAdapter(null);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		
		if(requestCode==47)
        {
			
		   cityid=data.getStringExtra("Cityid"); 
		  
           scityname = data.getStringExtra("Cityname");
           
           Subarea_name = data.getStringExtra("subCityname");
           Subarea_id = data.getStringExtra("subCityid");
           
           Log.i("Subarea_id", Subarea_id);
           
           searchlocationarea.setImageResource(R.drawable.locationselect);
           
           if(Subarea_id .equalsIgnoreCase("0"))
           {
        	   search_cityname.setText("Deals in "+scityname);
        	
           }
           else
           {
           
        	   search_cityname.setText("Deals in "+Subarea_name);
          
           }          
          // Toast.makeText(getApplicationContext(), message+message1, Toast.LENGTH_LONG);
           
           if(selected.equalsIgnoreCase("0"))
           {
        	   
           }
           else
           {
           SearchSugarea search = new SearchSugarea(selected);
			 search.execute();
           }
        }
		
		
		
		  		
	}




	
	class SearchSug extends AsyncTask<String,String, String>
	{
		String newselect;
		public SearchSug(String selected) {
			 this.newselect = selected;
			// TODO Auto-generated constructor stub
		}
		public void SearchSug (String variableName)
		{
		     this.newselect = variableName;
		}
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Searching Offers...");
			pDialog.setCancelable(false);		
			pDialog.show();
		
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				arrayOfList = getListData(newselect);
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
			func();
			//pDialog.dismiss();
		}
	}
	
	private ArrayList<Item> getListData(String selected) throws URISyntaxException {

		//Log.i("", selected);
		
				
		String spaces = "%20";
		
		String words = selected.replace(" ", spaces);
		
		Log.i("areaid", Subarea_id);
		
		String URL1 = COMM_URL+"/streetsmartadmin4/shopping/searchengine1/"
				+ words+"/"+scityid+"/"+Subarea_id;

		ArrayList<Item> arrayOfList = new ArrayList<Item>();

		BufferedReader bufferedReader = null;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
         StrictMode.setThreadPolicy(policy);
		/*
		 * HttpClient httpclient = new DefaultHttpClient(); HttpPost httppost =
		 * new
		 * HttpPost("http://54.255.134.169/streetsmartadmin/shopping/categorylist"
		 * );
		 */

		try {

			Log.i("selected value", words);
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(URL1));
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			bufferedReader.close();
			page = stringBuffer.toString();

			/*
			 * List<NameValuePair> nameValuePairs = new
			 * ArrayList<NameValuePair>(2); nameValuePairs.add(new
			 * BasicNameValuePair("location", "chennai"));
			 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); //
			 * Execute HTTP Post Request HttpResponse response =
			 * httpclient.execute(httppost);
			 * 
			 * page = EntityUtils.toString(response.getEntity());
			 */

			Log.i("Page", page);

			JSONArray jsonArray = new JSONArray(page);
			JSONArray list = new JSONArray(page);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				Item objItem = new Item();

				// String category_id = (String) json.get("category_name");

				objItem.setSearchoffer((String) json.get("offername"));

				objItem.setSearchprice((String) json.get("price"));
				
				objItem.setSearchdealid((String)json.get("dealid"));
				
				//objItem.setSeardescrip((String)json.get("dealdescription"));
				
				objItem.setSearchdealtype((String)json.get("dealtype"));
				
				objItem.setSearchcreditcard((String)json.get("creditcard"));
				
				objItem.setSearchretailer((String)json.get("retailername"));
				
				objItem.setSearchdate((String)json.get("enddate"));
				
				objItem.setSearchpercentage((String)json.get("percentage"));
				
				objItem.setSearchpromo((String)json.get("promotext"));
				
				objItem.setSearchretailerlogo((String)json.get("retailerlogo"));

				// objItem.setBrandid((String) json.get("brand_id"));

				arrayOfList.add(objItem);

				// caname=(String) json.get("category_name");
				// caid=(String) json.get("category_id");

			}

			Log.i("Sizzee", "" + arrayOfList.size());

			// myHandler.post(myRunnable);
		
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
		
		return arrayOfList;
		
	}
	
	
	public void func() {
		Log.i("inside func","inside func");
		
		lv1.setAdapter(new SearchAdapter(this, arrayOfList));

		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				String Deal_id = ((TextView) v.findViewById(R.id.searchdeal_id)).getText()
						.toString();
			
				//Log.i("GEO DEAL ID", Deal_id);
				Intent ddin = new Intent(SearchActivity.this,DisplayDealActivity.class);

				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);

			}

		});

	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	class SearchSugarea extends AsyncTask<String,String, String>
	{
		String newselect;
		public SearchSugarea(String selected) {
			 this.newselect = selected;
			// TODO Auto-generated constructor stub
		}
		public void SearchSugarea (String variableName)
		{
		     this.newselect = variableName;
		}
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Searching Offers...");
			pDialog.setCancelable(false);		
			pDialog.show();
		
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				arrayOfList = getListDataarea(newselect);
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
			funcarea();
			//pDialog.dismiss();
		}
	}
	
	
	public void funcarea() {
		Log.i("inside func","inside func");
		

		lv1.setAdapter(new SearchAdapter(this, arrayOfList));

		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				String Deal_id = ((TextView) v.findViewById(R.id.searchdeal_id)).getText()
						.toString();
				
				
				
				//Log.i("GEO DEAL ID", Deal_id);
				Intent ddin = new Intent(SearchActivity.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);

			}

		});

	}

	private ArrayList<Item> getListDataarea(String selected) throws URISyntaxException {

		//Log.i("", selected);
		
		String spaces = "%20";
		
		String words = selected.replace(" ", spaces);
		
		Log.i("areaid", Subarea_id);
		
		String URL1 = COMM_URL+"/streetsmartadmin4/shopping/searchengine1/"
				+ words+"/"+scityid+"/"+Subarea_id;

		ArrayList<Item> arrayOfList = new ArrayList<Item>();

		BufferedReader bufferedReader = null;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
         StrictMode.setThreadPolicy(policy);
		/*
		 * HttpClient httpclient = new DefaultHttpClient(); HttpPost httppost =
		 * new
		 * HttpPost("http://54.255.134.169/streetsmartadmin/shopping/categorylist"
		 * );
		 */

		try {

			Log.i("selected value", words);
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(URL1));
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			bufferedReader.close();
			page = stringBuffer.toString();

			/*
			 * List<NameValuePair> nameValuePairs = new
			 * ArrayList<NameValuePair>(2); nameValuePairs.add(new
			 * BasicNameValuePair("location", "chennai"));
			 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); //
			 * Execute HTTP Post Request HttpResponse response =
			 * httpclient.execute(httppost);
			 * 
			 * page = EntityUtils.toString(response.getEntity());
			 */

			Log.i("Page", page);

			JSONArray jsonArray = new JSONArray(page);
			JSONArray list = new JSONArray(page);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				Item objItem = new Item();

				// String category_id = (String) json.get("category_name");

				objItem.setSearchoffer((String) json.get("offername"));

				objItem.setSearchprice((String) json.get("price"));
				
				objItem.setSearchdealid((String)json.get("dealid"));
				
				//objItem.setSeardescrip((String)json.get("dealdescription"));
				
				objItem.setSearchdealtype((String)json.get("dealtype"));
				
				objItem.setSearchcreditcard((String)json.get("creditcard"));
				
				objItem.setSearchretailer((String)json.get("retailername"));
				
				objItem.setSearchdate((String)json.get("enddate"));
				
				objItem.setSearchpercentage((String)json.get("percentage"));
				
				objItem.setSearchpromo((String)json.get("promotext"));
				
				objItem.setSearchretailerlogo((String)json.get("retailerlogo"));

				// objItem.setBrandid((String) json.get("brand_id"));

				arrayOfList.add(objItem);

				// caname=(String) json.get("category_name");
				// caid=(String) json.get("category_id");

			}
	
			Log.i("Sizzee", "" + arrayOfList.size());

			// myHandler.post(myRunnable);
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
		
		return arrayOfList;
		
	}

/*@Override
public void onBackPressed() {
	// TODO Auto-generated method stub

}*/
	
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
