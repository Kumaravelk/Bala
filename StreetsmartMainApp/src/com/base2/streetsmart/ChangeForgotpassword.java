package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

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

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

public class ChangeForgotpassword extends Activity 
{
	
	
	
	CheckBox cbShowPass;
	EditText pass_current_et, pass_new_et, pass_confirm_et;
	String error1, userid1, message1, email1,npass;
	ImageView close;
	Button btn_savePass;
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeforgotpassword);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.hide();
		 
		  
		  cd = new ConnectionDetector(getApplicationContext());

			 
			if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(ChangeForgotpassword.this,
						"Internet Connection Error",
						"Please Check the Internet Connection", false);
				 
				return;
			}
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null)
			email1 = extras.getString("Emailid");
		 
           
           
		cbShowPass = (CheckBox) findViewById(R.id.cb_password);
		pass_current_et = (EditText) findViewById(R.id.EditText_PasswordCur);
		pass_current_et.setText(email1);
		pass_current_et.setEnabled(false);
		
		pass_new_et = (EditText) findViewById(R.id.EditText_PasswordNew);
		pass_confirm_et = (EditText) findViewById(R.id.EditText_PasswordConfirm);
		close = (ImageView) findViewById(R.id.close);
		btn_savePass = (Button) findViewById(R.id.btn_savePass);
		
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeForgotpassword.this.finish();
			}
		});

		
		cbShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (buttonView.isChecked()) {
					 
					pass_new_et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					pass_confirm_et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					 
					pass_new_et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					pass_confirm_et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}

			}

		});

	

	
	

	 
     btn_savePass.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String cpass = pass_current_et.getText().toString();
			String npass = pass_new_et.getText().toString();
			String cnpass = pass_confirm_et.getText().toString();

			  if (npass.length() < 1 && cpass.length() > 1) {
				pass_confirm_et.setError("Type a New password!");
			} else if (cnpass.length() < 1) {
				pass_confirm_et.setError("Type a Confirm password!");
			} else if (!npass.equals(cnpass)) {
				pass_confirm_et.setError("Passwords don't match!");
			} else 
			{
				new changepassword().execute(); 
				 
			}
			
		 }
	 });
	 
	 
	}
	

	
	
	/*   Chanage Password Class   */
	
	
	class changepassword extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ChangeForgotpassword.this);
			pDialog.setMessage("Changing Your Passcode...");
			pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
			 
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
 			
			try {
				 
				changepasswordData();
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
			
			func_cnpass();
		
			 
		}
	}

	public void changepasswordData() throws JSONException {

		// Get the result of country

		  
		 npass = pass_new_et.getText().toString();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/userforgotchangepassword");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("email", email1));
			nameValuePairs.add(new BasicNameValuePair("newpwd", npass));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			String str = EntityUtils.toString(response.getEntity());

			 
			JSONObject jsonobj = new JSONObject(str);

			JSONObject objjson = jsonobj.getJSONObject("result");

			error1 = objjson.getString("error");
			message1 = objjson.getString("message");

			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	
	public void func_cnpass()
	{
		
		if(error1.equalsIgnoreCase("false"))
		{
     	Toast.makeText(getApplicationContext(), "Your Passcode changed Successfully", Toast.LENGTH_LONG).show();
     	Intent lin =new Intent(ChangeForgotpassword.this,LoginActivity.class);
        startActivity(lin);
        overridePendingTransition(R.anim.flip_left_in,R.anim.flip_left_out);
        
		finish();
		 
		}
		else
		{
		Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG).show();
		}
     	
        
		 
		
	}
}
