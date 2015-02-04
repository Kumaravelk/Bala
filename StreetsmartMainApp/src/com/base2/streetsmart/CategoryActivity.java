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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base2.streetsmart.R;
import com.example.AdapterClass.CategoryListAdapter;
import com.example.AdapterClass.CityListAdapter;
import com.example.AdapterClass.SubCategoryAdapter;
import com.example.ItemClass.CategoryItem;
import com.example.Session.SessionManager;

public class CategoryActivity extends Activity {

	RelativeLayout area, subcate, mallcate;
	ListView SubListView, lv1;

	private ArrayList arrayOfbwsList;
	private ArrayList arrayOfsubcateList;
	CategoryItem bwsItem;
	final Context context = this;
	ProgressDialog pDialog;
	EditText subcatsearch;
	
	SessionManager session;
	
	SubCategoryAdapter subcatadap;
	
	String secate_id, sesubcate_id, secate_name, sesubcate_name;

	String Activityno;
	String categorystr, categoryname, categoryid, subcategorystr,
			subcategoryname, subcategoryid;

	String URL = COMM_URL + "/streetsmartadmin4/shopping/categorylist";

	String SubURL = COMM_URL+"/streetsmartadmin4/shopping/displaysubcategory/"
			+ categoryid;

	String MallURL = COMM_URL+"/streetsmartadmin4/shopping/citymall";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		  ActionBar actionbar = getActionBar();
	        
	        actionbar.hide();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Bundle getcateno = getIntent().getExtras();
		Activityno = getcateno.getString("ActivityNo");

		lv1 = (ListView) findViewById(R.id.bwscatelist);
		SubListView = (ListView) findViewById(R.id.listSubcate);
		subcate = (RelativeLayout) findViewById(R.id.subcate);

		subcatsearch = (EditText) findViewById(R.id.subcatsearch);
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();

		secate_id = user.get(SessionManager.KEY_CATEGORYID);

		secate_name = user.get(SessionManager.KEY_CATEGORYNAME);
		sesubcate_id = user.get(SessionManager.KEY_SUBCATEGORYID);
		sesubcate_name = user.get(SessionManager.KEY_SUBCATEGORYNAME);
		subcatsearch.setCursorVisible(false);
		new MyCategory().execute(URL);

		subcatsearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				subcatsearch.setFocusable(true);
				subcatsearch.setCursorVisible(true);
			}
		});

		subcatsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = subcatsearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				subcatadap.filter(text);
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

	/* Categorys List Data */

	class MyCategory extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CategoryActivity.this);
			pDialog.setMessage("Loading Categories...");
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
			categorystr = stringBuffer.toString();

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

				/*
				 * if (Activityno.equalsIgnoreCase("22")) { Intent intent = new
				 * Intent(); intent.putExtra("cateid", categoryid);
				 * intent.putExtra("catename", categoryname);
				 * 
				 * setResult(22, intent);
				 * 
				 * finish();
				 * 
				 * }
				 * 
				 * else {
				 */
				subcategoryMethod();
				// }
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		

	}

	/* SubCategorys Details */

	public void subcategoryMethod() {

		subcate.setVisibility(View.VISIBLE);

		new MySubCategory().execute();

	}

	class MySubCategory extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CategoryActivity.this);
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

		ArrayList arrayOfsubcateList = new ArrayList();

		BufferedReader bufferedReader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(
					COMM_URL+"/streetsmartadmin4/shopping/displaysubcategory/"
							+ categoryid));
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

		subcatadap = new SubCategoryAdapter(this, arrayOfsubcateList);
		// Arealistview.setAdapter(new CityListAdapter(this, city_item));
		SubListView.setAdapter(subcatadap);
		/*
		 * SubListView .setAdapter(new SubCategoryAdapter(this,
		 * arrayOfsubcateList));
		 */
		SubListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				subcategoryname = ((TextView) arg1
						.findViewById(R.id.subcatename)).getText().toString();
				subcategoryid = ((TextView) arg1.findViewById(R.id.subcateid))
						.getText().toString();

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
			}
		});

	}

	/* BackPress Funcation */

	@Override
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