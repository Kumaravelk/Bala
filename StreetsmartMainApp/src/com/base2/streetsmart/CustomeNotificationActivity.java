package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.example.Session.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomeNotificationActivity extends Activity {

	CheckBox email, geofence, offerpush;
	TextView sigout;
	ImageView closeButton;
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	

	String str_email, str_geofence, str_offerpush;
	String offer_push, message,error1;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,
			sgeonotify, spushnotify, semailnotify;

	SessionManager session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customenotification);
		session = new SessionManager(getApplicationContext());
		
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(CustomeNotificationActivity.this,
					"Internet Connection Error",
					"Please Check the Internet Connection", false);

			 
			return;
		}

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.hide();

		email = (CheckBox) findViewById(R.id.emailpush);
		//geofence = (CheckBox) findViewById(R.id.geofence);
		offerpush = (CheckBox) findViewById(R.id.offerpush);
		sigout = (TextView) findViewById(R.id.signout);
		closeButton = (ImageView) findViewById(R.id.closeButton);

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

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		new Pushvalue().execute();

		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomeNotificationActivity.this.finish();

			}
		});

		email.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				ProgressDialog pDialog;
				pDialog = new ProgressDialog(CustomeNotificationActivity.this);
				pDialog.setMessage("Updating... ");
				 pDialog.setCancelable(false);
				pDialog.show();
				
				Toast.makeText(getApplicationContext(), "Email Notification Updated", Toast.LENGTH_LONG).show();

				/*
				 * if(email.isChecked()) { str_email = "1"; func_email(); } else
				 * { str_email = "0"; func_email(); }
				 */
				pDialog.dismiss();
			}
		});

		/*geofence.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getApplicationContext(), "GEO Fence Updated", Toast.LENGTH_LONG).show();
				
				 * if(geofence.isChecked()) { str_geofence = "1";
				 * 
				 * } else { str_geofence = "0";
				 * 
				 * }
				 

			}
		});*/

		offerpush.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				// TODO Auto-generated method stub

				if (offerpush.isChecked()) {
					 str_offerpush = "1";
					new storepush().execute();
					// Toast.makeText(getApplicationContext(), "Push Notification Updated", Toast.LENGTH_LONG).show();
				} else {
					 str_offerpush = "0";
					new storepush().execute();
					// Toast.makeText(getApplicationContext(), "Push Notification Updated", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	

	public void func_email() {
		//Toast.makeText(getApplicationContext(), str_email, Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "Email Notification Updated", Toast.LENGTH_LONG).show();
	}

	public void func_geofence() {
		//Toast.makeText(getApplicationContext(), str_geofence, Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "GEO Fence Updated", Toast.LENGTH_LONG).show();
	}

	

	class Pushvalue extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomeNotificationActivity.this);
			pDialog.setMessage("Updating...");
			// pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				getpush_value();
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

			if (offer_push.equalsIgnoreCase("1")) 
			{
				offerpush.setChecked(true);
			} else {
				offerpush.setChecked(false);
			}

		}

	}
	
	public void getpush_value() throws URISyntaxException {
		try {
			BufferedReader bufferedReader = null;
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(COMM_URL
					+ "/streetsmartadmin4/shopping/profilepage/" + suserid));
			HttpResponse response;
			response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			String page = stringBuffer.toString();

			JSONObject jsonArray = new JSONObject(page);

			JSONObject jobj = jsonArray.getJSONObject("result");

			offer_push = jobj.getString("pushnotify");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	

	class storepush extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomeNotificationActivity.this);
			pDialog.setMessage("Updating... ");
			// pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			func_offerpush();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog.isShowing());
				 pDialog.dismiss();
          if(error1.equalsIgnoreCase("false"))
          {
        	  Toast.makeText(getApplicationContext(), "Push Notification Updated", Toast.LENGTH_LONG).show();
          }
		}

	}
	public void func_offerpush() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(COMM_URL	
				+ "/streetsmartadmin4/shopping/pushnotification");

		try {
			// Toast.makeText(getApplicationContext(), name,
			// Toast.LENGTH_LONG).show();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", suserid));
			nameValuePairs
					.add(new BasicNameValuePair("pushnotify", str_offerpush));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String offerpush = EntityUtils.toString(response.getEntity());
			JSONObject jsonArray = new JSONObject(offerpush);
			JSONObject jsonobj = jsonArray.getJSONObject("result");
			error1 = jsonobj.getString("error");
			message = jsonobj.getString("message");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

}
