package com.base2.streetsmart;
import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.example.AdapterClass.NearMeAdapter;
import com.example.ItemClass.NearMeItem;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;



import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class NearMeActivity extends Activity implements OnSeekBarChangeListener,LocationListener  
{

	RelativeLayout area,categories;
	ListView ListNearMe;
	SeekBar radius;
	TextView seekvalue,lt, ln,categoryname;
	ImageView noimage;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	private ArrayList arrayOfNear;
	NearMeItem neardeals;
	final Context context = this;
	LocationManager lm;
	Location l;
	AppLocationService appLocationService;
	SessionManager session; 
	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler;
	
	String Cate_name,Cate_id = "1",subCate_id;
	String userid;
	String nearstr;
	String ActivityNo = "22";
	String provider;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,sgeonotify, spushnotify, semailnotify;
	int valu = 1;
	int arraysize;
	double lng,lat;
	private String NearMeUrl = COMM_URL+"/streetsmartadmin4/shopping/nearestdeal";
	
	
	String secate_id = "0",secate_name,sesubcate_id="0",sesubcate_name;
	

	@SuppressLint({ "NewApi", "ServiceCast" })
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearme);
		
		/*ActionBar actionBar = getActionBar();
		actionBar.setTitle("Near Me");*/
		

		session = new SessionManager(getApplicationContext());
		cd = new ConnectionDetector(getApplicationContext());
		 
		if (!cd.isConnectingToInternet()) {
			 
			alert.showAlertDialog(NearMeActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			 
			return;
		}

		HashMap<String, String> user = session.getUserDetails();
		suserid = user.get(SessionManager. KEY_USERID); 
		sname = user.get(SessionManager.KEY_NAME); 
		semail =user.get(SessionManager.KEY_EMAIL); 
		smobile =user.get(SessionManager.KEY_MOBILE); 
		scityid =user.get(SessionManager.KEY_CITYID); 
		scityname =user.get(SessionManager.KEY_CITYNAME); 
		scountry =user.get(SessionManager.KEY_COUNTRY); 
		sgeonotify =user.get(SessionManager.KEY_GEO_NOTIFI);
		
		
		
		 secate_id = "0"; 
		  
		  secate_name = user.get(SessionManager. KEY_CATEGORYNAME); 
		  sesubcate_id = "0"; 
		  sesubcate_name = user.get(SessionManager. KEY_SUBCATEGORYNAME); 
		  

		radius = (SeekBar) findViewById(R.id.radius);
		seekvalue = (TextView) findViewById(R.id.seekvalue);
		noimage = (ImageView)findViewById(R.id.noimage);
		categoryname = (TextView)findViewById(R.id.categoryname);
		radius.setOnSeekBarChangeListener(this);
		categories = (RelativeLayout) findViewById(R.id.categorys);
		
		categoryname.setText("All Categories");
		
		appLocationService = new AppLocationService(NearMeActivity.this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try
		 {
			Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

			if (nwLocation != null) {
				lat = nwLocation.getLatitude();
				lng = nwLocation.getLongitude();
							
				new MyNearMeDeal().execute(NearMeUrl);
				
			} else {
				showSettingsAlert("NETWORK");
			}

			   
			   }
			   catch(IllegalArgumentException e)
			   {
				   e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Not getting ur gps value,please check ur location manager", Toast.LENGTH_LONG).show();
			   }
		
		categories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				overridePendingTransition(R.anim.flipup, R.anim.flipdown);
				Intent categoryintent = new Intent(NearMeActivity.this,
						JsonParser.class);
				
				categoryintent.putExtra("ActivityNo", ActivityNo);
				
				startActivityForResult(categoryintent, 22);
			}
		});
		
	}
	
	
	public void showSettingsAlert(String provider) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				NearMeActivity.this);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog
				.setMessage(provider + " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						NearMeActivity.this.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==22)
        {
			secate_id=data.getStringExtra("Categoryid"); 
			secate_name = data.getStringExtra("subcategoryname");
			sesubcate_id = data.getStringExtra("subcategoryid");
			sesubcate_name = data.getStringExtra("subcategoryname");
           
			
			categoryname.setText(sesubcate_name);
			
			//session.createcategorymethod(secate_id,secate_name,sesubcate_id,sesubcate_name);
			
			
           
        }
          // Toast.makeText(getApplicationContext(), message+message1, Toast.LENGTH_LONG);
		new MyNearMeDeal().execute(NearMeUrl);
        }

	@SuppressLint("NewApi")
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		String km = String.valueOf(progress);
		seekvalue.setText(km);
	}

	private ArrayList getListData(int values,String Cate_id) throws URISyntaxException {
		// TODO Auto-generated method stub
		
		
		Log.i("lat", ""+lat);
		Log.i("lon", ""+lng);
		
		//String lati = lat.to
		
		noimage = (ImageView)findViewById(R.id.noimage);
		int meter = values * 1000;

		String radiusval = String.valueOf(meter);
		arrayOfNear = new ArrayList();

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://54.169.81.215/streetsmartadmin4/shopping/nearestdeal");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("userid", suserid));
			nameValuePairs.add(new BasicNameValuePair("lat", ""+lat));
			nameValuePairs.add(new BasicNameValuePair("lon", ""+lng));
			nameValuePairs.add(new BasicNameValuePair("radius", radiusval));
			nameValuePairs.add(new BasicNameValuePair("category_id", secate_id));
			nameValuePairs.add(new BasicNameValuePair("subcategory_id", sesubcate_id));
			
			
			Log.i("userid",suserid);
			Log.i("lat", ""+lat);
			Log.i("lon",""+lng);
			Log.i("radius",radiusval);
			Log.i("category_id",secate_id);
			Log.i("subcategory_id",sesubcate_id);
			
		/*	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("userid","3"));
			nameValuePairs.add(new BasicNameValuePair("lat","12.954780"));
			nameValuePairs.add(new BasicNameValuePair("lon","77.680969"));
			nameValuePairs.add(new BasicNameValuePair("radius","200"));
			nameValuePairs.add(new BasicNameValuePair("category_id","10"));
			nameValuePairs.add(new BasicNameValuePair("subcategory_id","3"));
			*/
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			nearstr = EntityUtils.toString(response.getEntity());
			
			Log.i("Near", nearstr);
			
			
			
			JSONArray arrayJson = new JSONArray(nearstr);
			
			for (int i = 0; i < arrayJson.length(); i++)

			{

				JSONObject json = (JSONObject) arrayJson.get(i);

				NearMeItem neardeals = new NearMeItem();

				String NearDeal_id = (String) json.get("deal_id");

				String NearDeal_name = (String) json.get("offername");

				String NearDeal_image = (String) json.get("retailer_logo");

				String NearPromoText = (String) json.get("promotext");

				String NearThumps = (String) json.get("offerthumbnails");

				String NearPrice = (String) json.get("price");

				String NearPercentage = (String) json.get("percentage");

				String NearGender = (String) json.get("gender");
				
				String NearRetailername = (String)json.get("retailer_name");
				
				String NearDate = (String)json.get("edate");

				neardeals.setNeardeal_id(NearDeal_id);
				
				neardeals.setNearoffername(NearDeal_name);

				neardeals.setNeargender(NearGender);

				neardeals.setNearofferimage(NearDeal_image);

				neardeals.setNearofferthumbnails(NearThumps);

				neardeals.setNearpercentage(NearPercentage);

				neardeals.setNearprice(NearPrice);

				neardeals.setNearpromotext(NearPromoText);
				
				neardeals.setNearretailer_name(NearRetailername);
				
				neardeals.setNeardate(NearDate);

				arrayOfNear.add(neardeals);
				
				Log.i("Array Size",""+arrayOfNear.size());

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

		return arrayOfNear;

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

		valu = seekBar.getProgress();
		
			new MyNearMeDeal().execute(NearMeUrl);
	}
	
	public void noimage_fun(View view)
	{
		
		Log.i("NO DEAL", "View NO DEAL");
		
		noimage.setVisibility(view.VISIBLE);
	}
	
	public void func_nearme()
	{
		ListView lv1 = (ListView) findViewById(R.id.nearmedeals);
	
		
		lv1.setAdapter(new NearMeAdapter(this, arrayOfNear));
		
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String Deal_id = ((TextView) arg1.findViewById(R.id.neardeal_id)).getText()
						.toString();
				
				Intent ddin = new Intent(NearMeActivity.this,DisplayDealActivity.class);
				
				Bundle bundle = new Bundle();
				//Add your data from getFactualResults method to bundle
				bundle.putString("DealID", Deal_id);
				//Add the bundle to the intent
				ddin.putExtras(bundle);
				startActivity(ddin);
			}
		});
	}
	
	class MyNearMeDeal extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(NearMeActivity.this);
			pDialog.setMessage("Sniffing Deals for You");
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			//image = getListData();
			try {
				 
				arrayOfNear = getListData(valu,Cate_id);
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
			
		    arraysize = arrayOfNear.size();
			if(arraysize == 0)
			{
				arrayOfNear.clear();
				func_nearme();	
				noimage.setImageResource(R.drawable.noimg);
			}
			else 
			{
			noimage.setImageResource(0);
			func_nearme();
			}
			//pDialog.dismiss();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
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
	  
	  @Override
		public void onBackPressed() {
		  
		  
		 
		  
		  //WishlistFragment.onBackPressed();
		  //FavoritesFragment.onBackPressed();
		 // NotificationFragment.onBackPressed();
		 // SettingsFragment.onBackPressed();
		    if (doubleBackToExitPressedOnce) {
		        super.onBackPressed();
		        
		        return;
		    }

		    this.doubleBackToExitPressedOnce = true;
		    Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

		    new Handler().postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            doubleBackToExitPressedOnce=false;                       
		        }
		    }, 3500); 
		} 
}
