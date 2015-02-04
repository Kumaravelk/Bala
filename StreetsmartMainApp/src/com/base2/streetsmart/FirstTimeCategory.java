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
import com.base2.streetsmart.CategoryActivity.MyCategory;
import com.example.AdapterClass.CategoryListAdapter;
import com.example.ItemClass.CategoryItem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FirstTimeCategory extends Activity {
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	
	
	ListView lv1;
	
	private ArrayList arrayOfbwsList;
	
	String categorystr, categoryname, categoryid, subcategorystr,
	subcategoryname, subcategoryid;

	
	String URL = COMM_URL+"/streetsmartadmin4/shopping/categorylist";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstcategory);
		
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(FirstTimeCategory.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	  
		
		lv1 = (ListView) findViewById(R.id.bwscatelist);
		
		new MyCategory().execute(URL);
		
	}
	
	class MyCategory extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(FirstTimeCategory.this);
			pDialog.setMessage("Loading Category");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				arrayOfbwsList = getListData();
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

			func_category();

		}

	}
	
	
	@SuppressWarnings("unchecked")
	private ArrayList getListData() throws URISyntaxException {

		ArrayList arrayOfbwsList = new ArrayList();

		BufferedReader bufferedReader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(URL));
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
			String categorystr = stringBuffer.toString();

			JSONArray jsonArray = new JSONArray(categorystr);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				CategoryItem bwsItem = new CategoryItem();

				bwsItem.setCatname((String) json.get("category_name"));
				bwsItem.setCatid((String) json.get("category_id"));

				arrayOfbwsList.add(bwsItem);

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

		return arrayOfbwsList;

	}
	
	
	protected void func_category() {
		lv1.setAdapter(new CategoryListAdapter(this, arrayOfbwsList));
		
		
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				categoryname = ((TextView) arg1.findViewById(R.id.catename))
						.getText().toString();
				categoryid = ((TextView) arg1.findViewById(R.id.catid))
						.getText().toString();
				
				Intent subintent = new Intent(FirstTimeCategory.this,FirstTimeSubCategory.class);
				Bundle bundle = new Bundle();
			
				bundle.putString("Category_Id", categoryid);
				bundle.putString("Category_name", categoryname);
				
				subintent.putExtras(bundle);
				startActivity(subintent);				
			}
		});
	}


}
