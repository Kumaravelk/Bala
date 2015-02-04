package com.base2.streetsmart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


import com.example.AdapterClass.CustomRetailerExListAdapter;
import com.example.ItemClass.CustomRetailerListGropusModel;
import com.example.ItemClass.CustomRetailerListItemsModel;
import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class AddRetailerFragment extends Fragment implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener ,android.support.v4.app.FragmentManager.OnBackStackChangedListener{

	
	
	
	
/*
	public void onBackPressed()
	{
	    // Catch back action and pops from backstack
	    // (if you called previously to addToBackStack() in your transaction)
	    if (getFragmentManager().getBackStackEntryCount() > 0){
	        getFragmentManager().popBackStack();
	    }
	    // Default action on back pressed
	    else super.on;
	}*/
	
	
	SearchView cus_ret_search;
	ExpandableListView cus_ret_ex_list;
	Button btn_cus_fav_ret;
	String fav_ret_id;
	String fav_error, fav_message;

	SessionManager session;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,
			sgeonotify, spushnotify, semailnotify, scateories;

	ArrayList<CustomRetailerListGropusModel> grouplist = new ArrayList<CustomRetailerListGropusModel>();

	CustomRetailerExListAdapter adapter;

	// CustomCategoryListItemsModel arr=new CustomCategoryListItemsModel();

	ArrayList<String> fav_sub_cat_id = new ArrayList<String>();

	Context c;

	private static final String URL = "http://54.169.81.215/streetsmartadmin4/shopping/unselectedcategory_retailer/1";

	String cateid, catename;

	static View rootView;
	
	Fragment fragment;


	public AddRetailerFragment() {

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.custom_retailer_fragment, container, false);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		session = new SessionManager(getActivity());

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
		scateories = user.get(SessionManager.KEY_CATEGORIES);

		new MyCategory().execute();

		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);
		
		cus_ret_search = (SearchView) rootView.findViewById(R.id.cus_ret_search);
		  
		cus_ret_search.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));
		cus_ret_search.setIconifiedByDefault(false);
		cus_ret_search.setOnQueryTextListener(this);
		cus_ret_search.setOnCloseListener(this);

		return rootView;
	}

	class MyCategory extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		protected void onPreExecute() {

			// TODO Auto-generated method stub

			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());

			 
			pDialog.setMessage("Loading Retailers");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				getListData();

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if (pDialog.isShowing()) {

				pDialog.dismiss();

				fun_category();
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getListData() throws URISyntaxException {

		BufferedReader reader = null;

		try {

			HttpClient client = new DefaultHttpClient();

			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,

			"android");

			HttpGet request = new HttpGet();

			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(URL));

			HttpResponse response = client.execute(request);

			reader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer strBuffer = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.seperator");

			while ((line = reader.readLine()) != null) {

				strBuffer.append(line + NL);
			}

			reader.close();

			String str_category = strBuffer.toString();

			// Log.i("String :", str_category);

			JSONObject jobj1 = new JSONObject(str_category);

			for (int i = 0; i < jobj1.length() - 1; i++) { // 2 times object
															// omit error:false

				// Log.i("Root Obj  : ", "" + jobj1.length() + "");

				JSONArray jarr1 = jobj1.getJSONArray("" + i + "");

				for (int j = 0; j < jarr1.length(); j++) { // 5 items

					// Log.i("Root Obj3  : ",
					// "________________________________________");
					// Log.i("Root Obj3  : ", "" + j + "");
					JSONObject jobj2 = jarr1.getJSONObject(j);

					ArrayList<CustomRetailerListItemsModel> childlist = new ArrayList<CustomRetailerListItemsModel>();

					for (int k = 0; k < jobj2.length() - 3; k++) { // 7 objects
																	// -3 titles

						// Log.i("Root Obj3  : ",
						// "________________________________________");

						JSONObject jobj3 = jobj2.getJSONObject("" + k + "");
						// Sub title Items (Child)

						Integer f_flag = (Integer) jobj3.get("flag");

						// Log.i("Root Obj3  : ", "" + f_flag + "");

						CustomRetailerListItemsModel child = new CustomRetailerListItemsModel(
								jobj3.getString("retailer_id"),
								jobj3.getString("retailer_name"), f_flag, false);

						childlist.add(child);

						// Log.d("Child Items", ""+childlist.size()+"");

					}

					// Title Items (Parent)

					CustomRetailerListGropusModel parentlist = new CustomRetailerListGropusModel(
							jobj2.optString("category_id"),
							jobj2.optString("category_name"),
							jobj2.optString("category_icon"), childlist);
					grouplist.add(parentlist);

					// Log.d("Parent Items", parentlist.toString());

				}

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
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	protected void fun_category() {

		// Toast.makeText(getApplicationContext(), "Hi fun_cat",
		// Toast.LENGTH_LONG).show();

		cus_ret_ex_list = (ExpandableListView) rootView
				.findViewById(R.id.cus_ret_expandableListView);
		btn_cus_fav_ret = (Button) rootView.findViewById(R.id.cus_fav_ret);

		adapter = new CustomRetailerExListAdapter(getActivity(), grouplist);

		cus_ret_ex_list.setAdapter(adapter);

		btn_cus_fav_ret.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String str = adapter.checkVal();

				// Toast.makeText(getBaseContext(),
				// "Selected Favorite SubCategory :" +str,
				// Toast.LENGTH_LONG).show();

				if(str.isEmpty())
				{
					Toast.makeText(getActivity(), "Please Select Anyone Retailer", Toast.LENGTH_LONG).show();
				}
				else
				{
						fav_ret_id = str.substring(0, str.length() - 1);
						 try {
							customizecategory(fav_ret_id);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		
				}
			}
		});

		final int groupCount = cus_ret_ex_list.getCount();

		// Log.d("Break","________________________________________________");

		// Log.d("Adapter Count :", ""+groupCount+"");

		for (int i = 0; i <= groupCount; i++) {
			cus_ret_ex_list.collapseGroup(i);
		}

		cus_ret_ex_list.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				int len = adapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						cus_ret_ex_list.collapseGroup(i);
					}
				}
			}

		});

		int count = adapter.getGroupCount();

		// Log.d("Break","________________________________________________");

		// Log.d("Adapter Count :", ""+count+"");

		for (int i = 0; i < count; i++) {
			cus_ret_ex_list.expandGroup(i);
		}

	} // fun_cat End

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		adapter.filterData("");
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		adapter.filterData(query);
		expandAll();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		adapter.filterData(newText);
		expandAll();
		return false;
	}

	// method to expand all groups
	private void expandAll() {
		int count = adapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			cus_ret_ex_list.expandGroup(i);
		}
	}

	public void customizecategory(String categoryval) throws JSONException {

		new customizecategoryAsync().execute();
	}

	class customizecategoryAsync extends AsyncTask<String, Void, String> {
		ProgressDialog pDialogg;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialogg = new ProgressDialog(getActivity());
			pDialogg.setMessage("Updating ...");
			pDialogg.setCancelable(false);
			pDialogg.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://54.169.81.215/streetsmartadmin4/shopping/userfavourite");

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("userid", suserid));
				nameValuePairs.add(new BasicNameValuePair("retailer_id",
						fav_ret_id));

				// Log.d("CATEGORY Valuew", fav_ret_id);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String str = EntityUtils.toString(response.getEntity());

				JSONObject jsonobj = new JSONObject(str);

				// JSONObject objjson = jsonobj.getJSONObject("result");

				fav_error = jsonobj.getString("error");
				fav_message = jsonobj.getString("message");

			} catch (IOException e) {

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

			Toast.makeText(getActivity(), "Preferences Updated",
					Toast.LENGTH_LONG).show();

			getActivity().getFragmentManager().popBackStackImmediate();
			//getActivity().finish();
			
			
		}

	}

	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		
		getActivity().getFragmentManager().popBackStackImmediate();
	}

	
	

}
