package com.base2.streetsmart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String TAG = "tag";
	Button signin;
	EditText mobile, password;
	TextView forgotp, activationcode1;
	ImageView closeBtn;
	String name1, email1, mobile1, country1, error1, city1, userid1, message1,
			cityname1, geonotify1, pushnotify1, emailnotify1, occupy1, gender1,
			age1, categories1,fav_flag;
	String Mobile;
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	static String strflag = "0";
	
	String mallid="0",mallname = "MALL";
	
	String areaid = "0",Areaname="null";
	
	String brandid="0",retailerid="0",brandname="ALL BRANDS",retailername = "ALL RETAILER";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylogin);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.hide();
		session = new SessionManager(getApplicationContext());
		mobile = (EditText) findViewById(R.id.signinemail);
		password = (EditText) findViewById(R.id.signinpword);
		signin = (Button) findViewById(R.id.button1Signin);
		forgotp = (TextView) findViewById(R.id.forgot);
		closeBtn = (ImageView) findViewById(R.id.closeButton);
		activationcode1 = (TextView) findViewById(R.id.activationcode);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(LoginActivity.this,
					"Internet Connection Error",
					"Please Check the Internet Connection", false);
			 
			return;
		}
		
		

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		activationcode1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				  Intent actin = new Intent(LoginActivity.this,ActivationCodeActivity.class);
				  startActivity(actin);
				  overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
				 
			}
		});

		forgotp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent fin = new Intent(LoginActivity.this,
						Forgotpassword.class);
				startActivity(fin);
				overridePendingTransition(R.anim.flip_left_in,
						R.anim.flip_left_out);
				finish();
			}
		});

		closeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				LoginActivity.this.finish();

			}
		});

		signin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String Mobile = mobile.getText().toString();
				String Password = password.getText().toString();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

				if (Mobile.length() < 1) {
					mobile.requestFocus();
					mobile.setError("Enter Your Mobile Number");
				} else if (Mobile.length() < 10) {
					mobile.requestFocus();
					mobile.setError("Enter Minimum 10 Numbers");
				} else if (Password.length() < 1) {
					password.requestFocus();
					password.setError("Enter Your Passcode");
				} else if (Password.length() < 5) {
					password.requestFocus();
					password.setError("Enter Atleast 5 Characters");
				} else {
					new Login().execute();
				}
			}
		});

	}

	class Login extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Signing In");
			pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);

			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				loginData();
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

			Login_func();

		}
	}

	public void Login_func() 
	{
		
		
		
		if (error1.equalsIgnoreCase("false")) {
			
			session.createMAllMethod(mallid, mallname);
			
			session.createAreaMethod(areaid, Areaname);
			
			session.createBrand(brandid,brandname);
			session.createRetailer(retailerid,retailername);
			
			if(fav_flag.equalsIgnoreCase("0"))
			{

			Toast.makeText(getApplicationContext(),
					name1 + " Great Offers Ahead for You", Toast.LENGTH_LONG).show();
			Intent in = new Intent(LoginActivity.this, FavoriteRetailerActivity.class);
			startActivity(in);
			overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
			
			this.finish();
			}
			else
			{
				Intent in = new Intent(LoginActivity.this, HomeTabActivity.class);
				startActivity(in);
				overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);
				
				this.finish();
				if(MainActivity.instance != null) {
			        try {  
			        	MainActivity.instance.finish(); 
			        } catch (Exception e) {}
				}

			}
			

		} else {
			Toast.makeText(getApplicationContext(), message1,
					Toast.LENGTH_LONG).show();
		}

	}

	public void loginData() throws JSONException {

		Mobile = mobile.getText().toString();
		String Password = password.getText().toString();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(COMM_URL
				+ "/streetsmartadmin4/shopping/userlogin");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("mobile", Mobile));
			nameValuePairs.add(new BasicNameValuePair("password", Password));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());

			JSONObject jsonobj = new JSONObject(str);

			JSONObject objjson = jsonobj.getJSONObject("result");

			error1 = objjson.getString("error");
			message1 = objjson.getString("message");
			userid1 = objjson.getString("user_id");
			name1 = objjson.getString("name");
			email1 = objjson.getString("email");
			mobile1 = objjson.getString("mobile");
			city1 = objjson.getString("city");
			cityname1 = objjson.getString("cityname");
			country1 = objjson.getString("country");
			geonotify1 = objjson.getString("geonotify");
			pushnotify1 = objjson.getString("pushnotify");
			age1 = objjson.getString("age");
			gender1 = objjson.getString("gender");
			occupy1 = objjson.getString("occupy");
			categories1 = objjson.getString("categories");
			fav_flag = objjson.getString("fav_flag");
			

			session.createLoginSession(userid1, name1, email1, mobile1, city1,
					cityname1, country1, geonotify1, pushnotify1, emailnotify1,
					age1, gender1, occupy1, categories1);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }

}
