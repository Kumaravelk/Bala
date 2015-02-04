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
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Forgotpassword extends Activity {

	EditText fpassword,setedit;
	Button reset,set;
	ImageView closebutton,closedialogbtn;
	String error1, message1, acterror1, femail,userid1, getcode,actmsg;
	Context context = this;
	Dialog dialog;
	
	TextView skip;
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);

		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(Forgotpassword.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}

		fpassword = (EditText) findViewById(R.id.forpassword);
		
		reset = (Button) findViewById(R.id.freset);
		closebutton =(ImageView) findViewById(R.id.closeButton);
		
		 
		
		closebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Forgotpassword.this.finish();
			}
		});
		
		
		
		reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				femail = fpassword.getText().toString();
				 
				
				new forgotpasswordData().execute();
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(fpassword.getWindowToken(), 0);
				
			}
		});

	}
	
	
	
	
	class forgotpasswordData extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Forgotpassword.this);
			pDialog.setMessage("Reset Process");
			pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT); 
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
 			
			try {
				 
				forgotpasswordDataList();
				
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
			
			 fun_forgotpassword(userid1);
			 
		}
	}

	
	
	public void forgotpasswordDataList() throws JSONException 
	{

		
		// Get the result of country
		
		 
		
		 
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(COMM_URL+"/streetsmartadmin4/shopping/forgotuserpassword");

		 
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email",femail));
			nameValuePairs.add(new BasicNameValuePair("flag", "2"));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String str = EntityUtils.toString(response.getEntity());
			
			 
			JSONObject jsonobj = new JSONObject(str);

			JSONObject objjson = jsonobj.getJSONObject("result");

			error1 = objjson.getString("error");
			message1 = objjson.getString("message");
			userid1 = objjson.getString("user_id");

			
			

			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	
	 public void fun_forgotpassword(final String userid1)
	 {
		 if (error1 == "false") {


				dialog = new Dialog(this,R.style.DialogTheme);
		        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		        dialog.setContentView(R.layout.fwddialoginput);
		        dialog.setCancelable(false);
		        final Window window = dialog.getWindow();
		        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		        
		       // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
		        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg);
		        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
		        dialog.show();
	        
		        closedialogbtn = (ImageView) dialog.findViewById(R.id.fclosedialogbtn);
		       
		        closedialogbtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					dialog.dismiss();	
					}
				});
		        
			    setedit = (EditText) dialog.findViewById(R.id.feditText);
			    set = (Button) dialog.findViewById(R.id.fset_text);
			    
			   
			    
				set.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 
						getcode = setedit.getText().toString();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(setedit.getWindowToken(), 0);
						new forgotpasswordActivate().execute(getcode);

					}
				});
				
				 
				dialog.show();
				
				

			} else {
				Toast.makeText(getApplicationContext(), message1.toString(),
						Toast.LENGTH_LONG).show();
			}
		 
	 }
	 
	 
	 
	 
	 
	 class forgotpasswordActivate extends AsyncTask<String, Void, String>
		{
			ProgressDialog pDialog;
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(Forgotpassword.this);
				pDialog.setMessage("Reset Process");
				pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
				 
				pDialog.setCancelable(false);
				pDialog.show();
			}
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
	 			
				try {
					 
					verify(getcode,userid1);
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
				
				
				activation_fun();
				
			
					
				
				
			}
		}
	 
	 public void activation_fun()
	 {
		 
		 if(acterror1.equalsIgnoreCase("false"))
		 {
		 
		    Toast.makeText(getApplicationContext(), actmsg, Toast.LENGTH_LONG).show();
			Intent cfin = new Intent(Forgotpassword.this,ChangeForgotpassword.class);
			cfin.putExtra("Emailid",femail);
			startActivity(cfin);
			dialog.dismiss();
			finish();
		 }
		 else
		 {
			Toast.makeText(getApplicationContext(), actmsg, Toast.LENGTH_LONG).show();
			
		 }
	 }
	 
	 
	
	 
	 
	public void verify(String code, String userid1) throws JSONException {
		 
		femail = fpassword.getText().toString();
		 
		 
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/passwordactivate");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", userid1));
			nameValuePairs.add(new BasicNameValuePair("verification", code));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String activestr = EntityUtils.toString(response.getEntity());

			JSONObject actobj = new JSONObject(activestr);

			JSONObject objact = actobj.getJSONObject("result");

			actmsg = objact.getString("message");
			acterror1 = objact.getString("error");
			

			//Toast.makeText(getApplicationContext(), acterror1,Toast.LENGTH_SHORT).show();
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		
	}
	


	
}
