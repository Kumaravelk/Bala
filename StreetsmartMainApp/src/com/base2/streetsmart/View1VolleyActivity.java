package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.base2.streetsmart.R;
import com.base2.streetsmart.FavoriteRetailerActivity.customizeRetailerAsync;
import com.base2.streetsmart.NearMeActivity.MyNearMeDeal;

import com.example.Session.SessionManager;

import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class View1VolleyActivity extends Activity {

	NetworkImageView deallistimage, dealretailer_icon;
	ImageView direction;
	Button wishlist, call, share, redeem;
	TextView text_address, text_location, text_percentage, text_promotext,
			text_tandc, text_dealdescription, text_offername, enddate,
			text_price, text_retailername, credit_points;
	CheckBox ck_emi, ck_cod, ck_cc, ck_refund, ck_noex, ck_tim;

	double lng, lat;

	String faverror, favmessage;

	double rlat, rlong;

	private ArrayList arrayOfDeals;
	SessionManager session;
	private String TAG = SuperAdminNotification.class.getSimpleName();
	private ListView listview;
	private ProgressDialog pdialog;
	ImageLoader imageLoader;
	ScrollView parentScroll, childScroll, childScroll1, childScroll2;
	AppLocationService appLocationService;
	GPSTracker gps;

	String URL = COMM_URL + "/streetsmartadmin4/shopping/displaydeal";
	String dealid, campaign, sdate, edate, offername, promotext, offerimage,
			offerthumbnails, price, percentage, dealdescription, tandc,
			location, address, mall_id, keywords, gender, emi, cashod,
			creditcard, refund, noexchange, timming, valdealid, retailername,
			phoneno, Retailerid;
	String creditpoints, retailericon;
	String DDeal_ID;
	String retailerlat, retailerlong;
	String suserid, sname, semail, smobile, scityid, scityname, scountry,
			sgeonotify, spushnotify, semailnotify;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view1);

		/* Session */

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

		/* Diclatration */

		wishlist = (Button) findViewById(R.id.dealwhislist);
		call = (Button) findViewById(R.id.call_button);
		share = (Button) findViewById(R.id.sharebutton);
		redeem = (Button) findViewById(R.id.redeem);

		direction = (ImageView) findViewById(R.id.direction);
		deallistimage = (NetworkImageView) findViewById(R.id.dealimage);
		dealretailer_icon = (NetworkImageView) findViewById(R.id.dealretailericon);

		text_address = (TextView) findViewById(R.id.address);
		text_promotext = (TextView) findViewById(R.id.promotext);
		text_retailername = (TextView) findViewById(R.id.dealretailername);
		credit_points = (TextView) findViewById(R.id.creditpoints);
		text_tandc = (TextView) findViewById(R.id.tandc);
		text_dealdescription = (TextView) findViewById(R.id.dealdescription);
		text_offername = (TextView) findViewById(R.id.offername);

		ck_emi = (CheckBox) findViewById(R.id.emi);
		ck_cod = (CheckBox) findViewById(R.id.cod);
		ck_cc = (CheckBox) findViewById(R.id.creditcard);
		ck_noex = (CheckBox) findViewById(R.id.noexchange);

		// text_price = (TextView)findViewById(R.id.viewprice);
		enddate = (TextView) findViewById(R.id.viewdate);
		text_percentage = (TextView) findViewById(R.id.viewpercentage);

		parentScroll = (ScrollView) findViewById(R.id.parent_scroll);
		childScroll = (ScrollView) findViewById(R.id.child_scroll);
		childScroll1 = (ScrollView) findViewById(R.id.child_scroll1);
		childScroll2 = (ScrollView) findViewById(R.id.child_scroll1);

		parentScroll.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				findViewById(R.id.child_scroll).getParent()
						.requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});

		childScroll.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		childScroll1.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				// Disallow the touch request for parent scroll on touch of
				// child view
				findViewById(R.id.child_scroll).getParent()
						.requestDisallowInterceptTouchEvent(false);
				// findViewById(R.id.parent_scroll).getParent().requestDisallowInterceptTouchEvent(false);
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		childScroll2.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				// Disallow the touch request for parent scroll on touch of
				// child view
				findViewById(R.id.child_scroll).getParent()
						.requestDisallowInterceptTouchEvent(false);
				findViewById(R.id.child_scroll1).getParent()
						.requestDisallowInterceptTouchEvent(false);
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// String DDeal_ID ="55";

		Bundle DealID_bundle = getIntent().getExtras();
		DDeal_ID = DealID_bundle.getString("DealID");

		/*----------------------------------------------------*/
		/* Redeem */

		redeem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new customizeRetailerAsync().execute();
				// Toast.makeText(getApplicationContext(),
				// "We are working on this - Coming Soon",
				// Toast.LENGTH_LONG).show();
			}
		});

		/* ---------------------------------------------------- */
		/* Direction */

		direction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "Searching",To)
				Toast.makeText(getApplicationContext(),
						"Please Wait Loading Map.....", Toast.LENGTH_LONG)
						.show();

				rlat = Double.parseDouble(retailerlat);
				rlong = Double.parseDouble(retailerlong);

				// Toast.makeText(getApplicationContext(), rlat+"-----"+rlong,
				// Toast.LENGTH_LONG).show();

				gps = new GPSTracker(View1VolleyActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					Bundle bud = new Bundle();

					bud.putString("userlat", "" + latitude);
					bud.putString("userlong", "" + longitude);

					bud.putString("retailerlat", "" + rlat);
					bud.putString("retailerlong", "" + rlong);

					Intent dirctionintent = new Intent(
							View1VolleyActivity.this, Direction.class);
					dirctionintent.putExtras(bud);
					startActivity(dirctionintent);

				}

				else {

					gps.showSettingsAlert();

				}
			}
		});

		/*--------------------------------------------------------------------*/
		/* CALL */

		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), phoneno,
				// Toast.LENGTH_LONG).show();
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phoneno));
				startActivity(callIntent);

			}
		});

		/*------------------------------------------------------------------------------*/
		/* Share */

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);

				// set the type
				shareIntent.setType("text/plain");

				// add a subject
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						offername);

				// build the body of the message to be shared
				String shareMessage = promotext;
				String shareMsg = shareMessage + " " + retailername;

				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareMsg);

				// start the chooser for sharing
				startActivity(Intent.createChooser(shareIntent, "Street Smart"));

			}
		});

		/*----------------------------------------------------------------------------*/
		/* WISHLIST */

		wishlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(COMM_URL
						+ "/streetsmartadmin4/shopping/wishlist");

				try {
					// Toast.makeText(getApplicationContext(), name,
					// Toast.LENGTH_LONG).show();

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs
							.add(new BasicNameValuePair("userid", suserid));
					nameValuePairs.add(new BasicNameValuePair("dealid",
							DDeal_ID));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);

					String wishstr = EntityUtils.toString(response.getEntity());

					JSONObject jsonArray = new JSONObject(wishstr);

					String message = jsonArray.getString("message");

					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_LONG).show();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

			}
		});

		Deal_favourite();

		pdialog = new ProgressDialog(View1VolleyActivity.this);
		pdialog.setMessage("Loading Offer Info..");
		pdialog.show();

		imageLoader = AppController.getInstance().getImageLoader();
		JsonObjectRequest arrayReq = new JsonObjectRequest(
				"http://54.169.81.215/streetsmartadmin4/shopping/displaydeal/"
						+ DDeal_ID, null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						dissmissDialog();

						// Deal_favourite();
						try {
							// String resstr = response.toString();

							dealid = response.getString("DealID");
							campaign = response.getString("campaign");
							sdate = response.getString("sdate");
							edate = response.getString("edate");
							offername = response.getString("offername");
							promotext = response.getString("promotext");
							retailername = response.getString("Retailername");
							offerimage = response.getString("offerimage");
							retailericon = response.getString("Retailerlogo");
							// offerthumbnails=jsonArray.getString("offerthumbnails");
							price = response.getString("price");
							percentage = response.getString("percentage");
							dealdescription = response
									.getString("dealdescription");
							tandc = response.getString("tandc");
							// location=jsonArray.getString("location");
							address = response.getString("address");
							// mall_id=jsonArray.getString("mall_message");
							// keywords=jsonArray.getString("keywords");
							// gender=jsonArray.getString("gender");

							emi = response.getString("emi");
							cashod = response.getString("cashod");
							// creditcard=jsonArray.getString("creditcard");
							refund = response.getString("refund");
							noexchange = response.getString("noexchange");
							creditpoints = response.getString("creditpoints");
							phoneno = response.getString("landline_num");

							retailerlat = response.getString("Latitude");
							retailerlong = response.getString("Longitiude");

							Retailerid = response.getString("Retailerid");

							text_offername.setText(offername);
							text_promotext.setText(promotext);
							text_retailername.setText(retailername);
							text_percentage.setText(percentage
									+ " % OFF  OR Rs. " + price + " OFF");

							credit_points.setText(creditpoints);
							text_tandc.setText(tandc);

							text_dealdescription.setText(dealdescription);
							text_address.setText(address);

							Log.i("Retailerid", Retailerid);

							String inputPattern = "yyyy-MM-dd HH:mm:ss";
							String outputPattern = "MMM dd";
							SimpleDateFormat inputFormat = new SimpleDateFormat(
									inputPattern);
							SimpleDateFormat outputFormat = new SimpleDateFormat(
									outputPattern);

							Date date = null;
							String str = null;

							try {
								date = inputFormat.parse(edate);
								str = outputFormat.format(date);
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							enddate.setText(str);

							/*
							 * Log.i("EMI",emi); Log.i("cashod",cashod);
							 * Log.i("creditcard",creditcard);
							 * Log.i("refund",refund);
							 */

							if (emi.equalsIgnoreCase("1")) {
								ck_emi.setChecked(true);
							} else {
								ck_emi.setChecked(false);
							}
							if (cashod.equalsIgnoreCase("1")) {
								ck_cod.setChecked(true);
							} else {
								ck_cod.setChecked(false);
							}
							/*
							 * if(creditcard.equalsIgnoreCase("1")) {
							 * ck_cc.setChecked(true); } else {
							 * ck_cc.setChecked(false); }
							 */
							/*
							 * if(refund.equalsIgnoreCase("1")) {
							 * ck_refund.setChecked(true); } else {
							 * ck_refund.setChecked(false); }
							 */
							if (noexchange.equalsIgnoreCase("1")) {
								ck_noex.setChecked(true);
							} else {
								ck_noex.setChecked(false);
							}

							deallistimage.setImageUrl(offerimage, imageLoader);
							dealretailer_icon.setImageUrl(retailericon,
									imageLoader);

						}

						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// adapter.notifyDataSetChanged();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "ERROR" + error.getMessage());
					}
				});
		AppController.getInstance().addToRequestQueue(arrayReq);

	}

	public void showSettingsAlert(String provider) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				View1VolleyActivity.this);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog.setMessage(provider
				+ " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						View1VolleyActivity.this.startActivity(intent);
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

	public void Deal_favourite() {
		Log.i("dhan", "dhan");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(COMM_URL
				+ "/streetsmartadmin4/shopping/favourite");

		try {
			// Toast.makeText(getApplicationContext(), name,
			// Toast.LENGTH_LONG).show();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("userid", suserid));
			nameValuePairs.add(new BasicNameValuePair("dealid", DDeal_ID));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String favourite = EntityUtils.toString(response.getEntity());

			JSONObject jsonArray = new JSONObject(favourite);

			String message = jsonArray.getString("message");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	class customizeRetailerAsync extends AsyncTask<String, Void, String> {
		ProgressDialog pDialogg;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialogg = new ProgressDialog(View1VolleyActivity.this);
			pDialogg.setMessage("Add Retailers ...");
			pDialogg.setCancelable(false);
			pDialogg.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(COMM_URL
					+ "/streetsmartadmin4/shopping/dealsfavourite");

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("userid", suserid));
				nameValuePairs.add(new BasicNameValuePair("retailerid",
						Retailerid));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String str = EntityUtils.toString(response.getEntity());

				JSONObject jsonobj = new JSONObject(str);

				faverror = jsonobj.getString("error");
				favmessage = jsonobj.getString("message");

			} catch (IOException e) {

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

			Toast.makeText(getApplicationContext(), favmessage,
					Toast.LENGTH_LONG).show();

		}

	}

	private void dissmissDialog() {
		// TODO Auto-generated method stub
		if (pdialog != null) {
			if (pdialog.isShowing()) {
				pdialog.dismiss();
			}
			pdialog = null;
		}
	}

}
