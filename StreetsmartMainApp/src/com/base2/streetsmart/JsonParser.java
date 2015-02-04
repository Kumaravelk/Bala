package com.base2.streetsmart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
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

import com.example.AdapterClass.MyListAdapter;
import com.example.ItemClass.ListGropus;
import com.example.ItemClass.ListItems;
import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class JsonParser extends Activity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener{

	SearchView search;
	ExpandableListView ex_listview;
	//ExListAdapter adapter;
	//ExpandableListAdapter adapter;

	ArrayList<ListGropus> grouplist=new ArrayList<ListGropus>();
	MyListAdapter adapter;
	
	Context c;

	String val,val1;
	
	SessionManager session;
	
	String secate_id, sesubcate_id, secate_name, sesubcate_name;
	
	String categoryname,categoryid,subcategoryid,subcategoryname;

	
	private static final String URL = "http://54.169.81.215/streetsmartadmin4/shopping/categorysubcategorylist";
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityhome);
		
		ActionBar actionBar = getActionBar();
		  actionBar.hide();
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();

		secate_id = user.get(SessionManager.KEY_CATEGORYID);

		secate_name = user.get(SessionManager.KEY_CATEGORYNAME);
		sesubcate_id = user.get(SessionManager.KEY_SUBCATEGORYID);
		sesubcate_name = user.get(SessionManager.KEY_SUBCATEGORYNAME);

	
		
		new MyCategory().execute();
		
    
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.search);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		
		
	}	//setListData();
		
		

	
	class MyCategory extends AsyncTask<String, Void, String> {
		
		ProgressDialog pDialog;
		
		
		 protected void onPreExecute() {

			// TODO Auto-generated method stub

			super.onPreExecute();

			pDialog = new ProgressDialog(JsonParser.this);

			pDialog.setMessage("Loading Category");
			pDialog.setCancelable(false);
			pDialog.show();
			
			//Toast.makeText(c, "hi Pre", Toast.LENGTH_LONG).show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				
				//result=
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
			
			JSONObject jobj1 = new JSONObject(str_category);

			for (int i = 0; i < jobj1.length() - 1; i++) { // 9 times object

			//	Log.i("9 Obj 1 : ", "" + jobj1.length() + "");

				JSONObject jobj2 = jobj1.getJSONObject("" + i + "");

	
				ArrayList<ListItems> childlist=new ArrayList<ListItems>();
				

				for (int j = 0; j < jobj2.length() - 3; j++) { // 8 times object

					JSONArray jarr = jobj2.getJSONArray("" + j + ""); // array
																		// one
																		// time

					JSONObject jobj3 = jarr.getJSONObject(0); // (JSONObject)
									
					
					// jarr.get(j);

					ListItems child=new ListItems(jobj3.getString("subcat_id"), jobj3.getString("subcat_name"));
					
					childlist.add(child);
					
					
				}
				
				ListGropus parentlist=new ListGropus(jobj2.optString("category_id"), jobj2.optString("category_name"), childlist);
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
		
    ex_listview=(ExpandableListView)findViewById(R.id.expandableListView);
    
    adapter = new MyListAdapter(JsonParser.this, grouplist);

    ex_listview.setAdapter(adapter);
	
    ex_listview.setOnChildClickListener(new OnChildClickListener() {
    	
    	@Override
    	public boolean onChildClick(ExpandableListView parent, View v,
    			int groupPosition, int childPosition, long id) {
    		// TODO Auto-generated method stub
    		
    		subcategoryid = (((TextView)v.findViewById(R.id.tv_sub_cat_id)).getText().toString());
    		subcategoryname = (((TextView)v.findViewById(R.id.tv_sub_cat_name)).getText().toString());
    	//	Toast.makeText(getApplicationContext(), val+"******"+val1, Toast.LENGTH_LONG).show();
    		
    		session.createcategorymethod(categoryid, categoryname,
					subcategoryid, subcategoryname);

			Intent intent = new Intent();
			intent.putExtra("Categoryname", categoryname);
			intent.putExtra("Categoryid", categoryid);
			intent.putExtra("subcategoryid", subcategoryid);
			intent.putExtra("subcategoryname", subcategoryname);
			// intent.putExtra("Brandname",Brandname);

			setResult(4, intent);
			setResult(8, intent);
			setResult(22, intent);

			finish();
    		
    		
    		return false;
    	}
    });
       
       
       ex_listview.setOnGroupClickListener(new OnGroupClickListener() {
    	
    	@Override
    	public boolean onGroupClick(ExpandableListView parent, View v,
    			int groupPosition, long id) {
    		// TODO Auto-generated method stub
    		categoryname = (((TextView)v.findViewById(R.id.tv_c_name)).getText().toString());
    		categoryid = (((TextView)v.findViewById(R.id.tv_c_id)).getText().toString());
    		
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
    
  //  Log.d("Break","________________________________________________");
    
  //  Log.d("Adapter Count :", ""+count+"");
    
	for (int i = 0; i < count; i++) {
		ex_listview.expandGroup(i);
	}
    
	
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
			intent.putExtra("Categoryname", secate_name);
			intent.putExtra("Categoryid", secate_id);
			intent.putExtra("subcategoryid", sesubcate_id);
			intent.putExtra("subcategoryname", sesubcate_name);
			// intent.putExtra("Brandname",Brandname);

			setResult(4, intent);
			setResult(8, intent);
			setResult(22, intent);

			finish();

		}
		

	
}
