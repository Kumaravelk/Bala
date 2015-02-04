package com.base2.streetsmart;

import static com.base2.pushnotifications.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.base2.pushnotifications.CommonUtilities.EXTRA_MESSAGE;
import static com.base2.pushnotifications.CommonUtilities.SENDER_ID; 
import static com.base2.streetsmart.MainActivity1.COMM_URL;

import com.base2.streetsmart.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.base2.pushnotifications.ServerUtilities;
import com.base2.pushnotifications.WakeLocker;
import com.example.AdapterClass.CustomAdapter;
import com.example.ItemClass.CountryItem;

import com.google.android.gcm.GCMRegistrar;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("CommitPrefEdits")
public class DetailedRegistrationActivity extends Activity {

	/* Push Registraion */
	AlertDialogManager alert = new AlertDialogManager();
	AsyncTask<Void, Void, Void> mRegisterTask;
	ConnectionDetector cd;
	public static String name, email;
	String gcmid, error2, message2;
	TextView lblMessage;
	

	EditText Dob;
	Button reg;
	TextView output = null;
	Spinner Mariatal, Occuption, Area;
	ImageView dobimageb, close;
	Calendar cal;
	private int mday;
	private int mmonth;
	private int myear;
	public ListContent holder;
	
	String dob1, mariatal1, area1, occuption1, UserId;
	String error1, message1, name1, creditpoints1, occuptionid, areaid,
			currentData, currentdate1;
	String suserId, sname, semail, smobile, scity;
	String page, cities, Company, cnname, URL1, dhanu = "2";

	JSONObject jsonobject, jsonobject1;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<String> citylist;
	/*ArrayList<OccupItem> world, city;
	public ArrayList<OccupItem> occupationlist = new ArrayList<OccupItem>();*/
	public ArrayList<CountryItem> arealist = new ArrayList<CountryItem>();
	public ArrayList<CountryItem> CustomListViewValuesArr = new ArrayList<CountryItem>();

	
	//OccuptionListAdapter adapter;
	CustomAdapter areaadapter;
	DetailedRegistrationActivity activity = null;
	BroadcastReceiver bract;

	private Typeface myFont;
	String areaurl = COMM_URL+"/streetsmartadmin4/shopping/specificarealist/"
			+ scity;
	String URL = COMM_URL+"/streetsmartadmin4/shopping/occupationlist";

	private String[] genderlist = { "Single", "Married" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailedregistration);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(DetailedRegistrationActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}
		
        /* Actionbar Tital  */
		  
		  ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();

		Mariatal = (Spinner) findViewById(R.id.maritalstatus);
		//Occuption = (Spinner) findViewById(R.id.occuptionspin);
		//Area = (Spinner) findViewById(R.id.area);
		reg = (Button) findViewById(R.id.register);
		dobimageb = (ImageView) findViewById(R.id.dobimage);
		close = (ImageView) findViewById(R.id.close);
		Dob = (EditText) findViewById(R.id.dob);
		cal = Calendar.getInstance();
		mday = cal.get(Calendar.DAY_OF_MONTH);
		mmonth = cal.get(Calendar.MONTH);
		myear = cal.get(Calendar.YEAR);

		@SuppressWarnings("deprecation")
		SharedPreferences Registration = getSharedPreferences("myPrefs1",MODE_PRIVATE);
		SharedPreferences.Editor editor = Registration.edit();
		suserId = Registration.getString("userid", "");
		sname = Registration.getString("Name", "");
		semail = Registration.getString("Email", "");
		smobile = Registration.getString("Mobile", "");
		scity = Registration.getString("city", "");

		activity = this;
		//new AreaData().execute();
		//new OccupationData().execute();

		/* Close */

		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DetailedRegistrationActivity.this.finish();
			}
		});

		/* DOB */

		dobimageb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);

			}
		});
		Dob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);

			}
		});

		/* Mariatal Status */

		myFont = Typeface.createFromAsset(getAssets(), "ProximaNova-Light.otf");
		MyArrayAdapter ma = new MyArrayAdapter(this);
		 
		Mariatal.setAdapter(ma); 
		Mariatal.setPrompt("Select Your Marital state"); 
		Mariatal.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				 String selmariatal1 = ((TextView) view
							.findViewById(R.id.textView1)).getText().toString(); 
                   
				if (selmariatal1.equalsIgnoreCase("Single")) {
					mariatal1 = selmariatal1.replace("Single", "1");
				} else {
					mariatal1 = selmariatal1.replace("Married", "2");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				 holder.name.setHint("Select Country");
			}
		});

		/* Area */

		/*Area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v,
					int position, long id) {

				String Company = ((TextView) v.findViewById(R.id.company))
						.getText().toString();
				String CompanyUrl = ((TextView) v.findViewById(R.id.sub))
						.getText().toString();

				String OutputMsg = "Selected Company : \n\n" + Company + "\n"
						+ CompanyUrl;
				output.setText(OutputMsg);

				// func();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
				
			}

		});*/

		/* Ocuuption */

		reg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(Dob.getWindowToken(), 0);
				
				Validation1();
				

			}
		});

	}

	@Override
	@Deprecated
	@SuppressLint("SimpleDateFormat")
	protected Dialog onCreateDialog(int id) 
	{
		DatePickerDialog _date = new DatePickerDialog(this,
				datePickerListener, myear, mmonth, mday) {
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				if (year > myear)
					view.updateDate(myear, mmonth, mday);

				if (monthOfYear > mmonth && year == myear)
					view.updateDate(myear, mmonth, mday);

				if (dayOfMonth >mday && year == myear
						&& monthOfYear == mmonth)
					view.updateDate(myear, mmonth, mday);

			}
		};
		return _date;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	{
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) 
		{
			Dob.setText(selectedYear + "-" + (selectedMonth + 1) + "-"+ selectedDay);

		}
	};

	/* Detailed Registration */

	class DetailedRegistrationData extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailedRegistrationActivity.this);
			pDialog.setMessage("Updating Your Bio....");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				detailRegData(mariatal1, occuptionid, areaid);
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
			if (pDialog.isShowing())
				pDialog.dismiss();

			func_detaildRegData();

		}

	}

	public void detailRegData(String mariatal1, String occuptionname,
			String areaname) throws JSONException {

		dob1 = Dob.getText().toString();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/registerindetail");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", suserId));
			nameValuePairs.add(new BasicNameValuePair("dob", dob1));
			nameValuePairs.add(new BasicNameValuePair("maritalstatus",mariatal1));
			nameValuePairs.add(new BasicNameValuePair("occupy", "9"));
			nameValuePairs.add(new BasicNameValuePair("area", "0"));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String str = EntityUtils.toString(response.getEntity());

			JSONObject jsonobj = new JSONObject(str);

			JSONObject objjson = jsonobj.getJSONObject("result");

			error1 = objjson.getString("error");
			
			message1 = objjson.getString("message");
			
			name1 = objjson.getString("name");
			
			creditpoints1 = objjson.getString("creditpoints");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	public void func_detaildRegData() {
		
		if (error1.equals("false")) {
			Toast.makeText(getApplicationContext(),
					name1 + "   " + message1, Toast.LENGTH_LONG).show();
			

			
			
			/*Intent cin = new Intent(DetailedRegistrationActivity.this,GMainActivity.class);
			startActivity(cin);*/
			 
			Intent cin = new Intent(DetailedRegistrationActivity.this,CustomCategoriesActivity.class);
			startActivity(cin);
			overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
			finish();
		} else {
			Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG)
					.show();
		}
	}

	/* occupation ListView */

	/*class OccupationData extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailedRegistrationActivity.this);
			pDialog.setMessage("Processing...");
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				OccupationlistData();

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
			if (pDialog.isShowing())
				pDialog.dismiss();

			fuc_occuption();
			
			 * Intent cin = new Intent(DetailedRegistrationActivity.this,
			 * CustomCategoriesActivity.class); startActivity(cin);
			 
		}

	}

	public void OccupationlistData() throws JSONException {
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
				System.out.print(stringBuffer);
			}
			bufferedReader.close();
			page = stringBuffer.toString();

			JSONArray jsonArray = new JSONArray(page);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject json = (JSONObject) jsonArray.get(i);

				final OccupItem wp = new OccupItem();
				wp.setOccupyid((String) json.get("occupation_id"));
				wp.setOccupyname((String) json.get("occupation"));
				occupationlist.add(wp);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void fuc_occuption() {

		Resources res = getResources();
		adapter = new OccuptionListAdapter(activity, R.layout.spinner_rows,
				occupationlist, res);

		Occuption.setAdapter(adapter);

		Occuption
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						occuptionid = ((TextView) arg1
								.findViewById(R.id.company)).getText()
								.toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

	}

	 Area ListView 

	class AreaData extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailedRegistrationActivity.this);
			pDialog.setMessage("Processing...");
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				ArealistData();

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
			if (pDialog.isShowing())
				pDialog.dismiss();

			fuc_Area();
			
			 * Intent cin = new Intent(DetailedRegistrationActivity.this,
			 * CustomCategoriesActivity.class); startActivity(cin);
			 
		}

	}

	public void ArealistData() throws JSONException {

		BufferedReader bufferedReader = null;
		try {
			arealist.clear();
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();

			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(
					COMM_URL+"/streetsmartadmin4/shopping/specificarealist/"
							+ scity));

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
				final CountryItem wp = new CountryItem();
				wp.setRank((String) json.get("area_id"));
				wp.setCountry((String) json.get("area_name"));
				arealist.add(wp);
				CustomListViewValuesArr.add(wp);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void fuc_Area() {
		final Spinner mySpinner = (Spinner) findViewById(R.id.area);

		Resources res = getResources();
		areaadapter = new CustomAdapter(activity, R.layout.spinner_rows,
				arealist, res);
		mySpinner.setAdapter(areaadapter);
		mySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						areaid = ((TextView) arg1.findViewById(R.id.company))
								.getText().toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

	}
*/
	public void Validation1() {

		String DOB = Dob.getText().toString();

		if (DOB.length() < 1) {
			Dob.requestFocus();
			Dob.setError("Select Your Date of Birthday");
		} else {
			pushnotification();
			
			new DetailedRegistrationData().execute();
		}

	}

	/* Push Notification */
	 
 
	  
	  
	  
	public void pushnotification() 
	{

		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) 
		{
		
			alert.showAlertDialog(DetailedRegistrationActivity.this,
					"Internet Connection Error",
					"Please Check the Internet Connection", false);

			return;
		}
		

		name = sname;
		email = semail;
		Log.i("Name", name);
		Log.i("Email",email);

		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);
		 

		// Check if regid already presents

		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
             
			// Toast.makeText(getApplicationContext(), regId,
			// Toast.LENGTH_LONG).show();
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				/*Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();*/
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, name, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	 
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
            Log.i("Hi", newMessage);
			// Showing received message
			lblMessage.append(newMessage + "\n");
			//Toast.makeText(getApplicationContext(),"New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	
	

	@Override
	protected void onDestroy() 
	{
		if (mRegisterTask != null)
		{
			mRegisterTask.cancel(true);
		}
		try 
		{
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) 
		{
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	/* Mariatal Status List adapter */

	public class MyArrayAdapter extends BaseAdapter {

		public LayoutInflater mInflater;

		public MyArrayAdapter(DetailedRegistrationActivity con) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return genderlist.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			View v = convertView;
			if (v == null) 
			{
				v = mInflater.inflate(R.layout.spinnertext, null);
				holder = new ListContent();
				 
				holder.name = (TextView) v.findViewById(R.id.textView1);
				 holder.name.setHint("Select Country");
				v.setTag(holder);
			} else {

				holder = (ListContent) v.getTag();
			}

			holder.name.setTypeface(myFont);
			
			
			holder.name.setText(genderlist[position]);
			
			
			/*if (position == 0) {
				 holder.name.setHint("Select Country");
	            } else {
	            	holder.name.setText(genderlist[position]);
	            }*/
			return v;
		}

	}

	static class ListContent 
	{

		 
		TextView name;
		
		

	}

}