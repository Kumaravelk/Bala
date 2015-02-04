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

import com.base2.streetsmart.CustomCategoriesActivity.customizecategoryAsync;
import com.base2.streetsmart.FirstTimeSubCategory.MyWifiList;
import com.example.AdapterClass.FavoriteRetailerExListAdapter;
import com.example.Database.DBController;
import com.example.ItemClass.FavoriteRetailerListGropusModel;
import com.example.ItemClass.FavoriteRetailerListItemsModel;
import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class FavoriteRetailerActivity extends Activity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener{

	SearchView search;
	ExpandableListView ex_listview;
	Button btn_fav,retailerskip;
	
	
	ArrayList<FavoriteRetailerListGropusModel> grouplist=new ArrayList<FavoriteRetailerListGropusModel>();
	FavoriteRetailerExListAdapter adapter;
	FavoriteRetailerListItemsModel arr=new FavoriteRetailerListItemsModel();
	
	String mallid="0",mallname = "MALL";
	
	String areaid = "0",Areaname="null";
	
	String brandid="0",retailerid="0",brandname="ALL BRANDS",retailername = "ALL RETAILER";
	
	ArrayList<String> fav_sub_cat_id=new ArrayList<String>();
	Context c;
	
	String error,message;

	SessionManager session;
	
	String retailerval;
	
	private static final String URL = "http://54.169.81.215/streetsmartadmin4/shopping/category_retailer/2,10,9,4,5,8,6,7";
	
	String cateid, catename;
	
    String categorys;
    
    DBController controller = new DBController(this);
    
    String url = "http://54.169.81.215/streetsmartadmin4/shopping/wifilist";
	
    String suserid;
	
	String wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname,wififlag;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_retailer_main);
		
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		
		categorys = user.get(SessionManager.KEY_CATEGORIES);
		
		suserid = user.get(SessionManager. KEY_USERID); 
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	
	//	Log.i("Categorysss", categorys);
	     
		
		new MyCategory().execute();
		
		controller.deleterecords();
		new MyWifiList().execute();
		
    
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.search);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		
		//adapter.checkButtonClick(cateid, catename);
	}	//setListData();
		
		

	
	class MyCategory extends AsyncTask<String, Void, String> {
		
		ProgressDialog pDialog;
		
		
		 protected void onPreExecute() {

			// TODO Auto-generated method stub

			super.onPreExecute();

			pDialog = new ProgressDialog(FavoriteRetailerActivity.this);

			pDialog.setProgressStyle(pDialog.STYLE_HORIZONTAL);
			pDialog.setMessage("Loading Category");
			pDialog.setCancelable(false);
			pDialog.show();
			
			//Toast.makeText(c, "hi Pre", Toast.LENGTH_LONG).show();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				//pDialog.setProgress(value)
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
			request.setURI(new URI ("http://54.169.81.215/streetsmartadmin4/shopping/category_retailer/"+categorys));
			
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

			for (int i = 0; i < jobj1.length() - 1; i++) { // 2 times object  omit error:false


				//	Log.i("Root Obj  : ", "" + jobj1.length() + "");

					JSONArray jarr1 = jobj1.getJSONArray("" + i + "");

	
					for(int j=0;j<jarr1.length();j++){  // 4 items
						
						
						JSONObject jobj2=jarr1.getJSONObject(j);
						
						
						ArrayList<FavoriteRetailerListItemsModel> childlist=new ArrayList<FavoriteRetailerListItemsModel>();
						
						for(int k=0;k<jobj2.length()-3;k++){  // 7 objects -3 titles
							
							
							
							JSONObject jobj3=jobj2.getJSONObject(""+k+"");
							//Sub title Items (Child)
							
							FavoriteRetailerListItemsModel child=new FavoriteRetailerListItemsModel(jobj3.getString("retailer_id"), jobj3.getString("retailer_name"),jobj3.getString("logo_image"),false);
							
							childlist.add(child);
							
						//	Log.d("Child Items", child.toString());
							
						}
						//Title Items  (Parent)
						
						FavoriteRetailerListGropusModel parentlist=new FavoriteRetailerListGropusModel(jobj2.optString("category_id"), jobj2.optString("category_name"), jobj2.optString("category_icon") ,childlist);
						grouplist.add(parentlist);
						
					//	Log.d("Parent Items", parentlist.toString());
						
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
			}catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
			
		
		 
		
	}
	
	
	protected void fun_category(){
		
		

		
	
		
    ex_listview=(ExpandableListView)findViewById(R.id.faexpandableListView);
    btn_fav=(Button)findViewById(R.id.fa_btn_add_favorite);
    retailerskip = (Button)findViewById(R.id.retailerskip);
    
    retailerskip.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in = new Intent(FavoriteRetailerActivity.this,HomeTabActivity.class);
			
			session.createMAllMethod(mallid, mallname);
			
			session.createAreaMethod(areaid, Areaname);
			
			session.createBrand(brandid,brandname);
			session.createRetailer(retailerid,retailername);
			
			startActivity(in);
			overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
			finish();
		}
	});
    
    
    adapter = new FavoriteRetailerExListAdapter(FavoriteRetailerActivity.this, grouplist);

    ex_listview.setAdapter(adapter);
	
    
    
    btn_fav.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String str=adapter.checkVal();
			String retailer = str.substring(0, str.length()-1);
			
			String retailer_id = retailer.replaceAll("0d,", "");
			
			retailerval = retailer_id.replace(",0d", "");
			
			
						
			
			try {
				customizeretailers(retailerval);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			/*Intent hometab = new Intent(FavoriteRetailerActivity.this,HomeTabActivity.class);
			
			
			//Toast.makeText(getApplicationContext(), brandname, Toast.LENGTH_LONG).show();
			
			
			
			session.createMAllMethod(mallid, mallname);
			
			session.createAreaMethod(areaid, Areaname);
			
			session.createBrand(brandid,brandname);
			session.createRetailer(retailerid,retailername);
			
			startActivity(hometab);	
			*/
		
		}
	});
    
    
  
    
    
    ex_listview.setOnGroupClickListener(new OnGroupClickListener() {
    	
    	@Override
    	public boolean onGroupClick(ExpandableListView parent, View v,
    			int groupPosition, long id) {
    		// TODO Auto-generated method stub
    	
    		//String subcategoryid = (((TextView)v.findViewById(R.id.fa_sub_cat_id)).getText().toString());
    		
    		
    
    		return false;
    	}
    });
    
    
    final int groupCount = ex_listview.getCount();

  
    
    for (int i = 0; i <= groupCount; i++)
      {
    	ex_listview.collapseGroup(i);
      }
    
    
    
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
    
//    Log.d("Break","________________________________________________");
    
  //  Log.d("Adapter Count :", ""+count+"");
    
	for (int i = 0; i < count; i++) {
		ex_listview.expandGroup(i);
	}
    
		
	
	
	
		
	} //fun_cat End

	
	
	  public void customizeretailers(String retailerval) throws JSONException
		{
			new customizeRetailerAsync().execute();
		}
	
	  class customizeRetailerAsync extends AsyncTask<String, Void, String>
		{
			ProgressDialog pDialogg;
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialogg = new ProgressDialog(FavoriteRetailerActivity.this);
				pDialogg.setMessage("Updating ...");
				pDialogg.setCancelable(false);
				pDialogg.show();
			}
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
				
				Log.i("Retailers",retailerval);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(COMM_URL+"/streetsmartadmin4/shopping/userfavourite");

				try {

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("userid", suserid));
					nameValuePairs.add(new BasicNameValuePair("retailer_id",retailerval));
					 

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);

					String str = EntityUtils.toString(response.getEntity());

					JSONObject jsonobj = new JSONObject(str);

					
					
					error = jsonobj.getString("error");
					message = jsonobj.getString("message");
					
					
					
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
				
				Toast.makeText(getApplicationContext(), "Preferences Updated",Toast.LENGTH_LONG).show();
				
				Intent in = new Intent(FavoriteRetailerActivity.this,HomeTabActivity.class);
				
				session.createMAllMethod(mallid, mallname);
				
				session.createAreaMethod(areaid, Areaname);
				
				session.createBrand(brandid,brandname);
				session.createRetailer(retailerid,retailername);
				
				startActivity(in);
				overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
				finish();
				
			}
			
		}
	
	
	
	
	class MyWifiList extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"android");
				HttpGet request = new HttpGet();
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(url));

				HttpResponse response = client.execute(request);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

				StringBuffer stringBuffer = new StringBuffer("");
				String line = "";

				String NL = System.getProperty("line.separator");
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line + NL);
					System.out.print(stringBuffer);
				}
				bufferedReader.close();
				String page = stringBuffer.toString();

			//	Log.i("Page", page);

				// Log.d("Response: ", "> " + jsonStr);

				JSONArray jsonArray = new JSONArray(page);

				for (int i = 0; i < jsonArray.length(); i++)

				{

					JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

					wifiid = (String) jsonObject1.get("wifi_id");
					mac_id = (String) jsonObject1.get("mac_id");
					wifiretailerid = (String) jsonObject1.get("retailer_id");
					wifiretailername = (String) jsonObject1.get("retailer_name");
					wifimallid = (String) jsonObject1.get("mall_id");
					wifimallname = (String) jsonObject1.get("mall_name");
					wificityid = (String) jsonObject1.get("city_id");
					wificityname = (String) jsonObject1.get("city_name");
					wififlag = "0";
					
					
					
					controller.insertData(wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname);
				}
			}
				
			catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	
	
}
