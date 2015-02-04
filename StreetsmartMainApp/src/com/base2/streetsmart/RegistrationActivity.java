package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.example.AdapterClass.CustomAdapter;
import com.example.ItemClass.CountryItem;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegistrationActivity extends Activity {

	JSONObject jsonobject, jsonobject1;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<String> citylist;
	ArrayList<CountryItem> world, cityname;
	String page, cities;
	String CityName;
	String cnname;
	String URL1;
	String dhanu = "2";
	
	TextView SKip;
	 
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	
	public ArrayList<CountryItem> CustomListViewValuesArr = new ArrayList<CountryItem>();
	public ArrayList<CountryItem> worldlist = new ArrayList<CountryItem>();
	public ArrayList<CountryItem> citieslist = new ArrayList<CountryItem>();
	 
	CustomAdapter adapter;
	RegistrationActivity activity = null;
	public ViewFlipper MyViewFlipper;
	Context context = this;
	
	EditText name, email, password, mobile,setedit;
	Button buttonsignup,set;
	TextView next, previous,activationcode,output = null;
	Spinner city, country, gender;
	String City, Gender, Country,Password,Mobile;
	ImageView closeregister,closeRegistration,closedialogbtn;
	
	String name1, email1, password1, mobile1, error1, acterror1, userid1,message1, actmassage1;
	String Name, Email,getcode;
	String regEx = "[a-z0-9._-]+@[a-z0-9]+\\.+[a-z]+";
	private Typeface myFont;
	
	Dialog alet;
	Dialog dialog;
	
	 
	private String[] genderlist = { "Male", "Female" };
	
	

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();
		  
		activity = this;
		
		cd = new ConnectionDetector(getApplicationContext());

		 
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(RegistrationActivity.this,
					"Internet Connection Error",
					"Please Check the Internet Connection", false);
			 
			return;
		}
		
		
		
		
		
		

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		name = (EditText) findViewById(R.id.signupname);
		email = (EditText) findViewById(R.id.signupemail);
		password = (EditText) findViewById(R.id.signuppassword);
		mobile = (EditText) findViewById(R.id.signupmobile);
		gender = (Spinner) findViewById(R.id.signupGender);
		city = (Spinner) findViewById(R.id.signupCity);
		country = (Spinner) findViewById(R.id.signupcountry);
		closeregister = (ImageView) findViewById(R.id.closeRegister);
		closeRegistration = (ImageView) findViewById(R.id.closeRegistration);
		buttonsignup = (Button) findViewById(R.id.button1Signup);
		next = (TextView) findViewById(R.id.next);
		previous = (TextView) findViewById(R.id.Previous);
		 activationcode = (TextView) findViewById(R.id.activationcode);
		
		
		activationcode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent actin = new Intent(RegistrationActivity.this,ActivationCodeActivity.class);
				startActivity(actin);
				overridePendingTransition(R.anim.flip_left_in,R.anim.flip_left_out);	
				 
			}
		});

		MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

		Animation animationFlipIn = AnimationUtils.loadAnimation(this,
				R.anim.flip_left_in);
		Animation animationFlipOut = AnimationUtils.loadAnimation(this,
				R.anim.flip_left_out);

		MyViewFlipper.setInAnimation(animationFlipIn);
		MyViewFlipper.setOutAnimation(animationFlipOut);

		
		myFont = Typeface.createFromAsset(getAssets(), "ProximaNova-Light.otf");
        MyArrayAdapter ma = new MyArrayAdapter(this);
		//ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, genderlist);
		
		//adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//gender.setTypeface(font);
		gender.setAdapter(ma);

		gender.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				String selgender = ((TextView) view.findViewById(R.id.textView1)).getText().toString();
				 
				if(selgender.equalsIgnoreCase("male"))
				{
				 Gender=selgender.replace("Male", "male");
				}else
				{
				 Gender=selgender.replace("Female","female");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
				Validation1();
				 
			}
		});

		previous.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MyViewFlipper.showPrevious();

			}
		});
		
		closeregister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RegistrationActivity.this.finish();

			}
		});
		
		closeRegistration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    RegistrationActivity.this.finish();		
			}
		});
		
		
		buttonsignup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(city.getWindowToken(), 0);
				Validation2();
 
			 

			}
		});

	}
	
	
	
	class Register extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegistrationActivity.this);
			pDialog.setMessage("Signing up....");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) 
		{
			// TODO Auto-generated method stub
				
			try {
				 
				RegistrationData(Country, City, Gender);
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
				   
			func_Register();
			
		}
		
	}
	
	

	public void RegistrationData(String Country, String City, String Gender) throws JSONException 
	{
		Name = name.getText().toString();
		Email = email.getText().toString();
		Password = password.getText().toString();
		Mobile = mobile.getText().toString();
		 

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/userregister");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("name", Name));
			nameValuePairs.add(new BasicNameValuePair("email", Email));
			nameValuePairs.add(new BasicNameValuePair("password", Password));
			nameValuePairs.add(new BasicNameValuePair("mobile", Mobile));
			nameValuePairs.add(new BasicNameValuePair("gender", Gender));
			nameValuePairs.add(new BasicNameValuePair("country", Country));
			nameValuePairs.add(new BasicNameValuePair("city", City));
			nameValuePairs.add(new BasicNameValuePair("appid", ""));

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

			SharedPreferences Registration = getSharedPreferences("myPrefs1", MODE_PRIVATE);
			SharedPreferences.Editor editor = Registration.edit();
			editor.putString("userid", userid1);
			editor.putString("Name", name1);
			editor.putString("Email",email1);
			editor.putString("Mobile",mobile1);
			editor.putString("city", City);
			editor.commit();

			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}
	
	   public void func_Register()
	   {
		if (error1 == "false") 
		{
			Toast.makeText(getApplicationContext(), message1,Toast.LENGTH_LONG).show();

			dialog = new Dialog(this,R.style.DialogTheme);
	        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.dialoginput);
	        dialog.setCancelable(false);
	        final Window window = dialog.getWindow();
	        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
	        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	        
	       // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
	        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg);
	        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
	        dialog.show();
        
	        closedialogbtn = (ImageView) dialog.findViewById(R.id.closedialogbtn);
	        closedialogbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				dialog.dismiss();	
				}
			});
		    setedit = (EditText) dialog.findViewById(R.id.editText);
		    set = (Button) dialog.findViewById(R.id.set_text);
		    SKip =(TextView)dialog.findViewById(R.id.skip);
		    
		    SKip.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//Intent homepageintent = new Intent(RegistrationActivity.this,MainActivity.class);
					//startActivity(homepageintent);
					finish();
					dialog.dismiss();
				}
			});

		    set.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getcode = setedit.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(setedit.getWindowToken(), 0);
				
					new Verify().execute();
					
				}
			});

			//dialog.create();
			dialog.show();
		} 
		
		else {
			Toast.makeText(getApplicationContext(), message1,
					Toast.LENGTH_LONG).show();
		}
	}

	class Verify extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegistrationActivity.this);
			pDialog.setMessage("Verifying....");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				verify(getcode);

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
	

	public void verify(String code) throws JSONException {
		 
		 
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				COMM_URL+"/streetsmartadmin4/shopping/activate");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", userid1));
			nameValuePairs.add(new BasicNameValuePair("verification", code));
			nameValuePairs.add(new BasicNameValuePair("email", email1));

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
				
				Toast.makeText(getApplicationContext(), actmassage1,Toast.LENGTH_SHORT).show();
				Intent Drin = new Intent(RegistrationActivity.this,DetailedRegistrationActivity.class);

				// Drin.putExtra("Userid", userid1);
				startActivity(Drin);

				overridePendingTransition(
						R.anim.flip_left_in,
						R.anim.flip_left_out);	
				dialog.dismiss();
				
				this.finish();
				 
			} else 
			{

			Toast.makeText(getApplicationContext(), actmassage1,Toast.LENGTH_SHORT).show();
			}
			 
			
	}
	
	
	

	/* Country */
	
	
	class Country extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RegistrationActivity.this);
			pDialog.setMessage("Loading....");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) 
		{
			// TODO Auto-generated method stub
				
			try {
				 
				CountryData();
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
				   
			func_County();
			
		}

	}

	public void CountryData() throws JSONException 
	{
 		BufferedReader bufferedReader = null;
		BufferedReader bufferedReader1 = null;
			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"android");
				HttpGet request = new HttpGet();

				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(COMM_URL+"/streetsmartadmin4/shopping/countrylist"));

				HttpResponse response = client.execute(request);

				bufferedReader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
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

					final CountryItem wp = new CountryItem();
					wp.setRank((String) json.get("country_id"));
					wp.setCountry((String) json.get("country_name"));
					worldlist.add(wp);
					CustomListViewValuesArr.add(wp);
					
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			 
			 
		}

		public void func_County() 
		{

			final Spinner mySpinner = (Spinner) findViewById(R.id.signupcountry);
			final Spinner mySpinner1 = (Spinner) findViewById(R.id.signupCity);

			Resources res = getResources();
			adapter = new CustomAdapter(activity, R.layout.spinner_rows,worldlist, res);

			mySpinner.setAdapter(adapter);

			mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			{

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) 
						{

							 Country = ((TextView) arg1
									.findViewById(R.id.company)).getText()
									.toString();
							 

							URL1 =COMM_URL+"/streetsmartadmin4/shopping/specificcitylist/"+ Country;
							 

							func();

							Resources res1 = getResources();
							adapter = new CustomAdapter(activity,
									R.layout.spinner_rows, citieslist, res1);
							mySpinner1.setAdapter(adapter);

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) 
						{
							// TODO Auto-generated method stub
						}
					});

		      	mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			   {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {

							City = ((TextView) arg1
									.findViewById(R.id.company)).getText()
									.toString();
							CityName = ((TextView) arg1.findViewById(R.id.sub))
									.getText().toString();

									 

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
					});

	}
		
		
	/*	City Function*/

	@SuppressLint("NewApi")
	public void func() {
		citieslist.clear();

		BufferedReader bufferedReader1 = null;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {

			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(URL1));

			HttpResponse response1 = client.execute(request);

			bufferedReader1 = new BufferedReader(new InputStreamReader(
					response1.getEntity().getContent()));

			StringBuffer stringBuffer1 = new StringBuffer("");
			String line1 = "";

			String NL1 = System.getProperty("line.separator");
			while ((line1 = bufferedReader1.readLine()) != null) {
				stringBuffer1.append(line1 + NL1);
				System.out.print(stringBuffer1);
			}
			bufferedReader1.close();

			cities = stringBuffer1.toString();

			JSONArray jsonArray1 = new JSONArray(cities);

			for (int i = 0; i < jsonArray1.length(); i++) {

				JSONObject json1 = (JSONObject) jsonArray1.get(i);

				CountryItem wp = new CountryItem();

				wp.setRank((String) json1.get("city_id"));
				wp.setCountry((String) json1.get("city_name"));
				citieslist.add(wp);
			}

		} catch (Exception e) {
			// Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
	
	
	}
	
	
	/* First Registration */

	public void Validation1() {

		String Name = name.getText().toString();
		String Email = email.getText().toString();
		String Password = password.getText().toString();

		if (Name.length() < 1)
		{
			name.requestFocus();
			name.setError("Hey Your Nick Name ?");
			
		} else if (Email.length() < 1)
		{
			email.requestFocus();
			email.setError("Your Email Please");
		} else if (Email.length() > 1) 
		 {
			Email = email.getText().toString().trim();
			Matcher matcherObj = Pattern.compile(regEx).matcher(Email);

			if (matcherObj.matches()) 
			{
				if (Password.length() < 1) {
					password.requestFocus();
					password.setError("Your Secret Code Please");
				} else if (Password.length() < 6) {
					password.requestFocus();
					password.setError("Enter atleast 6 characters");
				}else
				{
					MyViewFlipper.showNext();
					new Country().execute();
				}
				 
			} 
			else 
			{
				email.requestFocus();
				email.setError("Invalid Email");

			}
		} 

	}
	
   /* Second Registration */


	public void Validation2() {
		String Mobile = mobile.getText().toString();
		String initialPart = Mobile.substring(0, 1); 
		if (Mobile.length() < 1) {
			mobile.requestFocus();
			mobile.setError("Hey Your Mobile Number");
		} else if (Mobile.length() < 10) {
			mobile.requestFocus();
			mobile.setError("Oops Wrong Mobile Number");
		} else if(initialPart.equalsIgnoreCase("9")||initialPart.equalsIgnoreCase("8")||initialPart.equalsIgnoreCase("7"))
		{
			new Register().execute();
		     	
		}else
		{
			mobile.requestFocus();
			mobile.setError("Invalid Mobile Number");
		}
		

	}

	
	
	/* Back Button Press */

	@Override
    public void onBackPressed()
	{
            super.onBackPressed();
            this.finish();
    }
	
	private class MyArrayAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyArrayAdapter(RegistrationActivity con) {
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
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(R.layout.spinnertext, null);
                holder = new ListContent();

                holder.name = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {

                holder = (ListContent) v.getTag();
            }

            holder.name.setTypeface(myFont);
            holder.name.setText("" + genderlist[position]);

            return v;
        }

    }

    static class ListContent {

        TextView name;

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
