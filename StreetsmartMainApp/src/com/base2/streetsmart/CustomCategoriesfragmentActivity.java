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

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.base2.streetsmart.CustomCategoriesActivity.CustomCategoriesAdapter;
import com.base2.streetsmart.CustomCategoriesActivity.CustomCategory;
import com.base2.streetsmart.CustomCategoriesActivity.customizecategoryAsync;
import com.example.ItemClass.CustomCategoriesItem;
import com.example.Session.SessionManager;
 

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCategoriesfragmentActivity extends Activity {

private ArrayList<CustomCategoriesItem> arrayofcategoryList;
	
	Button hometab;
	
	ListView listView;
	
	CustomCategoriesAdapter objAdapter;
	
	CustomCategoriesItem objItem;
	
	AlertDialogManager alert = new AlertDialogManager();
	
	ConnectionDetector cd;
	
	
	final Context context = this;
	
	String categoryid;
	
	CheckBox checkbox;
	
	String cateid, catename;
		
     
    
	String page,error,message;
	
	String categorys;
	
	String CustomURL = COMM_URL+"/streetsmartadmin4/shopping/categorylist";

	SessionManager session;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,
			sgeonotify, spushnotify, semailnotify,scateories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customcategories);

		session = new SessionManager(getApplicationContext());

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
		scateories =user.get(SessionManager.KEY_CATEGORIES);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(CustomCategoriesfragmentActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
	 

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		new CustomCategory().execute(CustomURL);
		checkButtonClick(cateid, catename);
}
	
	class CustomCategory extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomCategoriesfragmentActivity.this);
			pDialog.setMessage("Loading Categories...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				arrayofcategoryList = getCustomcategory();
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

			func_customcategory();
		}
		
	}
	
	private ArrayList getCustomcategory() throws URISyntaxException 
	{

		ArrayList arrayofcategoryList = new ArrayList();

		BufferedReader bufferedReader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(CustomURL));
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
			page = stringBuffer.toString();

			
			 

			JSONArray jsonArray = new JSONArray(page);
			 

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				CustomCategoriesItem objItem = new CustomCategoriesItem();

				 
				objItem.setCat_name((String) json.get("category_name"));
				objItem.setCat_id((String) json.get("category_id"));

				arrayofcategoryList.add(objItem);

				
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
		
		return arrayofcategoryList;

	}

	
	public void func_customcategory()
	{
		
		objAdapter = new CustomCategoriesAdapter(this, R.layout.customcatelist,arrayofcategoryList);
		ListView listView = (ListView) findViewById(R.id.cutlistView);
		listView.setAdapter(objAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// When clicked, show a toast with the TextView text
				CustomCategoriesItem categoryvalues = (CustomCategoriesItem) parent.getItemAtPosition(position);
				//Toast.makeText(getApplicationContext(),"Clicked on Row: " + categoryvalues.getCat_name(),Toast.LENGTH_LONG).show();
                 
				 
				 
			}
		});
		
	}
	
	public class CustomCategoriesAdapter extends ArrayAdapter<CustomCategoriesItem> 
	{

		public ArrayList<CustomCategoriesItem> arrayofcategoryList;
		
		HashMap<CustomCategoriesItem, String> resultp = new HashMap<CustomCategoriesItem, String>();
		
		public CustomCategoriesAdapter(Context context, int textViewResourceId,ArrayList<CustomCategoriesItem> categoryvaluesList) 
		{
			super(context, textViewResourceId, categoryvaluesList);
			this.arrayofcategoryList = new ArrayList<CustomCategoriesItem>();
			this.arrayofcategoryList.addAll(categoryvaluesList);
			
		}

		private class ViewHolder {
			TextView catid;
			CheckBox catname;
			ImageView icon;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			
			
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.customcatelist, null);

				holder = new ViewHolder();
				holder.catid = (TextView) convertView.findViewById(R.id.custextid);
				// holder.icon=(ImageView)convertView.findViewById(R.id.icon);
				holder.catname = (CheckBox) convertView.findViewById(R.id.cuscheckBox);

				convertView.setTag(holder);

				holder.catname.setOnClickListener(new View.OnClickListener() 
				{
					public void onClick(View v) 
					{
						CheckBox cb = (CheckBox) v;
						CustomCategoriesItem categoryvalues = (CustomCategoriesItem) cb.getTag();
						categoryvalues.setSelected(cb.isChecked());
	
					}
				});
			} else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			CustomCategoriesItem categoryvalues = arrayofcategoryList.get(position);

			holder.catid.setText(categoryvalues.getCat_id());
			holder.catname.setText(categoryvalues.getCat_name());
			holder.catname.setChecked(categoryvalues.isSelected());
			
			holder.catname.setTag(categoryvalues);

			return convertView;

		}


	}
	
	
	
	private void checkButtonClick(final String brid, final String brname) {

		Button customcate = (Button) findViewById(R.id.custom_button);
	
		customcate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				
				ArrayList<CustomCategoriesItem> arrayofcategoryList = objAdapter.arrayofcategoryList;
				
				for (int i = 0; i < arrayofcategoryList.size(); i++) 
				{
					CustomCategoriesItem categoryvalues = arrayofcategoryList.get(i);
					if (categoryvalues.isSelected())
					{
						responseText.append(categoryvalues.getCat_id() + ",");
					}
				}

				
				String re = responseText.toString();
				categorys = re.substring(0, re.length()-1);
				
				
				try {
					customizecategory(categorys);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			}
		});

	}
	
	public void customizecategory(String categoryval) throws JSONException
	{
		new customizecategoryAsync().execute();
	}
	
	class customizecategoryAsync extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialogg;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialogg = new ProgressDialog(CustomCategoriesfragmentActivity.this);
			pDialogg.setMessage("Updating ...");
			pDialogg.setCancelable(false);
			pDialogg.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					COMM_URL+"/streetsmartadmin4/shopping/usercategory");
			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user_id", suserid));
				nameValuePairs.add(new BasicNameValuePair("category_id",categorys));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String str = EntityUtils.toString(response.getEntity());

				JSONObject jsonobj = new JSONObject(str);

				JSONObject objjson = jsonobj.getJSONObject("result");
				
				error = objjson.getString("error");
				message = objjson.getString("message");
						
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
			 
			finish();
			
		}
		
	}

}
