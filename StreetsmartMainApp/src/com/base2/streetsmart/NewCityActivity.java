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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AdapterClass.CityExListAdapter;
import com.example.ItemClass.CityListGropusModel;
import com.example.ItemClass.CityListItemsModel;
import com.example.Session.SessionManager;

public class NewCityActivity extends Activity  implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	SearchView search;
	ExpandableListView ex_listview;
	
	ArrayList<CityListGropusModel> grouplist=new ArrayList<CityListGropusModel>();
	
	//ExListAdapter adapter;
	//ExpandableListAdapter adapter;
	CityExListAdapter adapter;
	//ListModel model;
	
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild ;
	ArrayList<String> child_data;
	
	 String CityName,CityID,AreaName,AreaID;
	 
	 SessionManager session;
	
	private static final String URL = "http://54.169.81.215/streetsmartshop/shopping/specificcityarealist/1";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_area_activity_main);
	
	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		 session = new SessionManager(getApplicationContext());
			
			HashMap<String, String> user = session.getUserDetails();
			
			CityID =user.get(SessionManager.KEY_CITYID); 
			CityName =user.get(SessionManager.KEY_CITYNAME); 
			AreaName = user.get(SessionManager.KEY_AREANAME); 
			AreaID = user.get(SessionManager.KEY_AREAID); 

		
			ActionBar actionBar = getActionBar();
		  actionBar.hide();
		
		
		new MyCategory().execute();
		
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.newcitysearch);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		
		
	//	expandAll();
		
		
	
	}
	
	
	
	class MyCategory extends AsyncTask<String, Void, String> {
		
		ProgressDialog pDialog;
		
		
		 protected void onPreExecute() {

			// TODO Auto-generated method stub

			super.onPreExecute();

			pDialog = new ProgressDialog(NewCityActivity.this);

			pDialog.setMessage("Loading Your City");
			pDialog.setCancelable(false);
			pDialog.show();
			
			//Toast.makeText(c, "hi Pre", Toast.LENGTH_LONG).show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				
				
						getListData();
			
			//	Log.d("Result : ",result.toString());
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
				
				
				fun_category();
			}
		}
			
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getListData() throws URISyntaxException{
		
		BufferedReader reader=null;
		
		listDataHeader = new ArrayList<String>();
	    listDataChild = new HashMap<String, List<String>>();
	    child_data=new ArrayList<String>();
	   // par_data=new ArrayList<String>();
	    
	    //PARENT data
	    
	    String city_name,city_id;
	    
		try{
			
			
			HttpClient client=new DefaultHttpClient();
			
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,

                    "android");
			
			HttpGet request=new HttpGet();
			
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI (URL));
			
			HttpResponse response=client.execute(request);
			
			
			reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer strBuffer=new StringBuffer("");
			
			String line="";
			
			
			String NL = System.getProperty("line.seperator");

			while ((line = reader.readLine()) != null) {

				strBuffer.append(line + NL);
			}

			reader.close();
			
			
			
			String str_category=strBuffer.toString();
			
	//	Log.i("String :", str_category);
			
			JSONObject jobj1=new JSONObject(str_category);

			
			//First JSON OBJECT 
			
			for (int i = 0; i < jobj1.length() - 1; i++) { // 1 times object

			//	Log.i("Json Obj 1 : ", "" + jobj1.length() + "");

				JSONObject jobj2 = jobj1.getJSONObject("" + i + "");

				
				ArrayList<CityListItemsModel> childlist=new ArrayList<CityListItemsModel>();

				for (int j = 0; j < jobj2.length() - 2; j++) { // 205 times object

			//		Log.d("JObject 2 : ", ""+jobj2.length()+"");
			//		Log.d("JObject 2 : ", "_________________________________");
					
			//		Log.d("JObject 2 : ", ""+j+"");
					
					JSONObject jobj3 = jobj2.getJSONObject("" + j + "");

					// model=new ListModel();

					// model.setMall_id((String)jobj3.getString("mall_id"));
					// model.setMall_id((String)jobj3.getString("mall_name"));

					//String mall_id, mall_name;
					
					
					
					CityListItemsModel child=new CityListItemsModel(jobj3.getString("area_id"), jobj3.getString("area_name"));
					
					childlist.add(child);
				
					
			//		Log.i("Main  : ", "____________________________________");
			//		Log.i("Main Child Data : ", child.toString());
				}

				CityListGropusModel parentlist=new CityListGropusModel(jobj2.optString("city_id"), jobj2.optString("city_name"), childlist);
				grouplist.add(parentlist);
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
			}catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
			
		
		 
		
	}
	
	
	protected void fun_category(){
		
		

		
	//Toast.makeText(getApplicationContext(), "Hi fun_cat", Toast.LENGTH_LONG).show();
		
    ex_listview=(ExpandableListView)findViewById(R.id.cityexpandableListView);
    
	adapter = new CityExListAdapter(NewCityActivity.this, grouplist);

    ex_listview.setAdapter(adapter);
	
    
    ex_listview.setOnChildClickListener(new OnChildClickListener() {
    	
    	@Override
    	public boolean onChildClick(ExpandableListView parent, View v,
    			int groupPosition, int childPosition, long id) {
    		// TODO Auto-generated method stub
    		
    		AreaName = ((TextView) v.findViewById(R.id.city_area_name))
					.getText().toString();
			AreaID = ((TextView) v.findViewById(R.id.city_area_id))
					.getText().toString();

			session.createAreaMethod(AreaID, AreaName);
			
		//Toast.makeText(getApplicationContext(), AreaID, Toast.LENGTH_SHORT).show();
			
			
			Intent intent = new Intent();
			intent.putExtra("Cityname", CityName);
			intent.putExtra("Cityid", CityID);
			intent.putExtra("subCityid", AreaID);
			intent.putExtra("subCityname", AreaName);

			setResult(17, intent);
			setResult(47, intent);

			finish();
    		return false;
    	}
    });
       
       
       ex_listview.setOnGroupClickListener(new OnGroupClickListener() {
    	
    	@Override
    	public boolean onGroupClick(ExpandableListView parent, View v,
    			int groupPosition, long id) {
    		// TODO Auto-generated method stub
    	
    		CityName = ((TextView) v.findViewById(R.id.tv_newcity_name))
					.getText().toString();
			CityID = ((TextView) v.findViewById(R.id.tv_newcity_id)).getText()
					.toString();
    		
    		return false;
    	}
    });
  
    
    ex_listview.setOnGroupExpandListener(new OnGroupExpandListener() {

		@Override
		 public void onGroupExpand(int groupPosition) {
            int len = adapter.getGroupCount();
            for (int i = 0; i < len; i++) {
                if (i != groupPosition) {
                	ex_listview.collapseGroup(i);
                }
            }
        }
		
	
	});
    
    
    int count = adapter.getGroupCount();
	for (int i = 0; i < count; i++) {
		ex_listview.expandGroup(i);
	}
    
  //  Log.i("Check CHILD ", listDataChild.toString());
	//Log.i("Check PARENT ", listDataHeader.toString());
	

    
		//ExListGroup=ExListData.getdata();
		
		//ExListTitle=new ArrayList<String>(ExListGroup.keySet());
		//adapter=new ExListAdapter(this, ExListTitle, ExListGroup);
		
	
		
	}


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
				ex_listview.expandGroup(i);
			}
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
		setResult(47, intent);

		finish();
	}
}
