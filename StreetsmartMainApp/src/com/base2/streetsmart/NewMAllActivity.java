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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.AdapterClass.NewMAllAdapter;
import com.example.ItemClass.MallListGropus;
import com.example.ItemClass.MallListItems;
import com.example.ItemClass.MallListModel;
import com.example.Session.SessionManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewMAllActivity extends Activity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	SearchView search;
	ExpandableListView ex_listview;
	
	ArrayList<MallListGropus> grouplist=new ArrayList<MallListGropus>();
	
	//ExListAdapter adapter;
	//ExpandableListAdapter adapter;
	NewMAllAdapter adapter;
	MallListModel model;
	
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild ;
	ArrayList<String> child_data;
	
	String CityName,CityID,MallName,MallID = "0";
	
	SessionManager session;
	
	private static final String URL = "http://54.169.81.215/streetsmartadmin4/shopping/specificcitymalllist/1";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_mall_list);
	
	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		
		session = new SessionManager(getApplicationContext());
		
		HashMap<String, String> user = session.getUserDetails();
		
		MallID =user.get(SessionManager.KEY_CITYID); 
		MallName =user.get(SessionManager.KEY_CITYNAME); 
	
		
		new MyCategory().execute();
		
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.mall_search);
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

			pDialog = new ProgressDialog(NewMAllActivity.this);

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

			//	Log.i("9 Obj 1 : ", "" + jobj1.length() + "");

				JSONObject jobj2 = jobj1.getJSONObject("" + i + "");

				MallListModel model = new MallListModel();

				// PARENT ITEMS

				
				
				//city_id = jobj2.optString("city_id");
				//city_name = jobj2.optString("city_name");

				//String parent_join = city_id.concat("*" + city_name);

				//listDataHeader.add(parent_join);

			//	Log.i("Main CAT NAME : ", city_name);

				//child_data = new ArrayList<String>();
				
				ArrayList<MallListItems> childlist=new ArrayList<MallListItems>();

				for (int j = 0; j < jobj2.length() - 2; j++) { // 36 times object

					JSONObject jobj3 = jobj2.getJSONObject("" + j + "");

					// model=new ListModel();

					// model.setMall_id((String)jobj3.getString("mall_id"));
					// model.setMall_id((String)jobj3.getString("mall_name"));

					//String mall_id, mall_name;
					
					
					
					MallListItems child=new MallListItems(jobj3.getString("mall_id"), jobj3.getString("mall_name"));
					
					childlist.add(child);
					
					//mall_id = jobj3.getString("mall_id");
				//	mall_name = jobj3.getString("mall_name");

				//	String sub_cat_join = mall_id.concat("*" + mall_name);

				//	child_data.add(sub_cat_join);

				//	listDataChild.put(parent_join, child_data);
					
			//		Log.i("Main  : ", "____________________________________");
			//		Log.i("Main Child Data : ", child.toString());
				}

				MallListGropus parentlist=new MallListGropus(jobj2.optString("city_id"), jobj2.optString("city_name"), childlist);
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
		
		
    ex_listview=(ExpandableListView)findViewById(R.id.mallexpandableListView);
    
	adapter = new NewMAllAdapter(NewMAllActivity.this, grouplist);

    ex_listview.setAdapter(adapter);
	
    ex_listview.setOnChildClickListener(new OnChildClickListener() {
    	
    	@Override
    	public boolean onChildClick(ExpandableListView parent, View v,
    			int groupPosition, int childPosition, long id) {
    		// TODO Auto-generated method stub
    		
    		MallName = ((TextView) v.findViewById(R.id.tv_mall_name))
					.getText().toString();
			MallID = ((TextView) v.findViewById(R.id.tv_mall_id))
					.getText().toString();
			
			//Toast.makeText(getApplicationContext(), MallName, Toast.LENGTH_LONG).show();
			
			
			/*Log.i("Cityid", CityID);
			Log.i("Cityname", CityName);
			Log.i("mallid", MallID);
			Log.i("mallname", MallName);*/
			
			session.createMAllMethod(MallID, MallName);
			Intent intent = new Intent();
			intent.putExtra("Cityid", CityID);
			intent.putExtra("mallid", MallID);
			intent.putExtra("Mall_name", MallName);
			
			setResult(26, intent);
			
			finish();
    		
			return false;
    	}
    });
       
       
       ex_listview.setOnGroupClickListener(new OnGroupClickListener() {
    	
    	@Override
    	public boolean onGroupClick(ExpandableListView parent, View v,
    			int groupPosition, long id) {
    		// TODO Auto-generated method stub
    	
    		CityName = ((TextView) v.findViewById(R.id.tv_mallcity_name))
    				
					.getText().toString();
			CityID = ((TextView) v.findViewById(R.id.tv_mallcity_id)).getText()
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
