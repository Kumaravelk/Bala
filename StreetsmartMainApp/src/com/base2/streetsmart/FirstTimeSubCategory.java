package com.base2.streetsmart;
import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import com.base2.streetsmart.CategoryActivity.MySubCategory;
import com.example.AdapterClass.SubCategoryAdapter;
import com.example.Database.DBController;
import com.example.ItemClass.CategoryItem;
import com.example.Session.SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FirstTimeSubCategory extends Activity {
	

	private ArrayList arrayOfsubcateList;
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	
	String categorystr, categoryname, categoryid, subcategorystr,
	subcategoryname, subcategoryid;
	ListView SubListView;
	String mallid="0",mallname = "MALL";
	
	String areaid = "0",Areaname="null";
	
	String brandid="0",retailerid="0",brandname="ALL BRANDS",retailername = "ALL RETAILER";
	
	SessionManager session; 
	
	DBController controller = new DBController(this);
	
	String url = "http://54.169.81.215/streetsmartadmin4/shopping/wifilist";
	
	
	String wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname,wififlag;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstsubcategory);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(FirstTimeSubCategory.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		session = new SessionManager(getApplicationContext());
		Bundle bundle = getIntent().getExtras();

		//Extract the data…
		categoryid = bundle.getString("Category_Id");     
		categoryname = bundle.getString("Category_name");  
		
		//Create the text view
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SubListView = (ListView) findViewById(R.id.listSubcate);
		
		new MySubCategory().execute();
		
		controller.deleterecords();
		new MyWifiList().execute();
	}
	
	
	class MySubCategory extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(FirstTimeSubCategory.this);
			pDialog.setMessage("Loading SubCategories...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				arrayOfsubcateList = getSubCatListData();
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

			func_subcategory();

		}

	}
	
	
	private ArrayList getSubCatListData() throws URISyntaxException {
		
		//Log.i("Category id", categoryid);

		ArrayList arrayOfsubcateList = new ArrayList();

		BufferedReader bufferedReader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(COMM_URL+"/streetsmartadmin4/shopping/displaysubcategory/"+categoryid));
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
			}
			bufferedReader.close();
			
			subcategorystr = stringBuffer.toString();
			
			//Log.i("String", subcategorystr);

			JSONObject jsonObj = new JSONObject(subcategorystr);

			JSONArray arrayJson = jsonObj.getJSONArray("Subcategory");

			for (int i = 0; i < arrayJson.length(); i++) {

				JSONObject json = (JSONObject) arrayJson.get(i);

				CategoryItem subItem = new CategoryItem();
				
				String Subcate_name = (String) json.get("subcatname");
				int Subcate_id = (Integer) json.get("subcatid");
				
				subItem.setSubcatname(Subcate_name);

				subItem.setSubcateid(Subcate_id);

				arrayOfsubcateList.add(subItem);
				
				Log.i("SUbCategory",subItem.getSubcatname());
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

		return arrayOfsubcateList;

	}

	
	protected void func_subcategory() {
		
		
		SubListView.setAdapter(new SubCategoryAdapter(this, arrayOfsubcateList));
		SubListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				subcategoryname = ((TextView) arg1
						.findViewById(R.id.subcatename)).getText().toString();
				subcategoryid = ((TextView) arg1.findViewById(R.id.subcateid))
						.getText().toString();
				
				
				Intent hometab = new Intent(FirstTimeSubCategory.this,HomeTabActivity.class);
				
				
				//Toast.makeText(getApplicationContext(), brandname, Toast.LENGTH_LONG).show();
				
				
				session.createcategorymethod(categoryid,categoryname,subcategoryid,subcategoryname);
				
				session.createMAllMethod(mallid, mallname);
				
				session.createAreaMethod(areaid, Areaname);
				
				session.createBrand(brandid,brandname);
				session.createRetailer(retailerid,retailername);
				
				startActivity(hometab);	
				
				
				
				
				
			}
		});
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

				Log.i("Page", page);

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
	
	

}
