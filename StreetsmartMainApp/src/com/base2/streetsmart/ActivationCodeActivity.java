package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.base2.streetsmart.RegistrationActivity.Country;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

public class ActivationCodeActivity extends Activity {

	ImageView closeButton;
	EditText emailid, activationcode;
	Button activationbutton;
	String name1, email1, password1, mobile1, error1, acterror1, userid1,
			message1, actmassage1;
	String Email, Code, getcode;
	String regEx = "[a-z0-9._-]+@[a-z0-9]+\\.+[a-z]+";
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activationcode);
        
		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();
		  getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		  cd = new ConnectionDetector(getApplicationContext());

			 
			if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(ActivationCodeActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
				 
				return;
			}
		  
		  
		  
		  
		  
		closeButton = (ImageView) findViewById(R.id.closeButton);
		emailid = (EditText) findViewById(R.id.emailid);
		activationcode = (EditText) findViewById(R.id.activationcode);
		activationbutton = (Button) findViewById(R.id.activationbutton);
		
		
		
		
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ActivationCodeActivity.this.finish();
			}
		});

		activationbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Email = emailid.getText().toString();
				Code = activationcode.getText().toString();
				
				Validation1();
				
			}
		});

	}

	class Verify extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ActivationCodeActivity.this);
			pDialog.setMessage("Verifying ....");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				verify();

			}

			catch (JSONException e) {
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

			fuct_verification();

		}

	}

	public void verify() throws JSONException {

		
		Log.i("Val", Code+""+Email);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(COMM_URL
				+ "/streetsmartadmin4/shopping/verification");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("email", Email));
			nameValuePairs.add(new BasicNameValuePair("verification", Code));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String activestr = EntityUtils.toString(response.getEntity());

			JSONObject actobj = new JSONObject(activestr);

			JSONObject objact = actobj.getJSONObject("result");
			acterror1 = objact.getString("error");

			actmassage1 = objact.getString("message");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	public void fuct_verification() 
	{

		if (acterror1.equalsIgnoreCase("false")) 
		{

			Toast.makeText(getApplicationContext(), actmassage1,
					Toast.LENGTH_SHORT).show();
			Intent Drin = new Intent(ActivationCodeActivity.this,
					DetailedRegistrationActivity.class);

			// Drin.putExtra("Userid", userid1);
			startActivity(Drin);

			overridePendingTransition(R.anim.flip_left_in, R.anim.flip_left_out);

			this.finish();

		} else {

			Toast.makeText(getApplicationContext(), actmassage1,
					Toast.LENGTH_SHORT).show();
		}

	}
	
	
	public void Validation1() {

		 
		String Email = emailid.getText().toString();
		String Activationcode = activationcode.getText().toString();

		 if (Email.length() < 1)
		{
			 emailid.requestFocus();
			 emailid.setError("Enter Email Address");
		} else if (Email.length() > 1) 
		 {
			Email = emailid.getText().toString().trim();
			Matcher matcherObj = Pattern.compile(regEx).matcher(Email);

			if (matcherObj.matches()) 
			{
				if (Activationcode.length() < 1) {
					activationcode.requestFocus();
					activationcode.setError("Enter Your ActivationCode");
				} else if (Activationcode.length() < 2) {
					activationcode.requestFocus();
					activationcode.setError("ActivationCode code lenth too small ");
				}else
				{
					new Verify().execute(Code, Email);
					 
				}
				 
			} 
			else 
			{
				emailid.requestFocus();
				emailid.setError("Invalid Email");

			}
		} 

	}

}
