package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;

import com.example.Database.DBController;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

	SessionManager session;
	TextView updatepro, customcat, customnotifi, signout, chanagepass,val_favor,val_shop,val_wish,feedback,shareapp,AboutApp,wifioffer,cusretailer;
	String suserid,sname,semail,smobile,scityid,scityname,scountry,sgeonotify,spushnotify,semailnotify;
	String get_user,get_phone,get_email,get_favor,get_shop,get_wish;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	DBController controller = new DBController(getActivity());
	
	String wifi,wifili,wifilistval;
	
	String wifiurl = "http://54.169.81.215/streetsmartadmin4/shopping/wifilist";
	
	List<String>  wifiList;
	
	String wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname,wififlag;
	static View rootView;
	public SettingsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  rootView = inflater.inflate(R.layout.setting_fragment, container,
				false);

		controller =  new DBController(getActivity());
		
		val_favor = (TextView) rootView.findViewById(R.id.suggectedcount);
		val_shop = (TextView) rootView.findViewById(R.id.shoppedCount);
		val_wish = (TextView) rootView.findViewById(R.id.WishlistCount);
		//updatepro = (TextView) rootView.findViewById(R.id.updateprofile);
		customcat = (TextView) rootView.findViewById(R.id.customecate);
		customnotifi = (TextView) rootView.findViewById(R.id.notification);
		chanagepass = (TextView) rootView.findViewById(R.id.chanagepass);
		signout = (TextView) rootView.findViewById(R.id.signout);
		
		feedback = (TextView)rootView.findViewById(R.id.feedback);
		
		shareapp = (TextView)rootView.findViewById(R.id.shareapp);
		
		AboutApp = (TextView)rootView.findViewById(R.id.aboutapp);
		
		wifioffer = (TextView)rootView.findViewById(R.id.wifiupdate);
		
		cusretailer = (TextView)rootView.findViewById(R.id.customeretailer);

		session = new SessionManager(getActivity().getApplicationContext());
		 HashMap<String, String> user = session.getUserDetails();
	        
	        suserid = user.get(SessionManager. KEY_USERID);
	        sname = user.get(SessionManager.KEY_NAME);
	        semail = user.get(SessionManager.KEY_EMAIL);
	        smobile = user.get(SessionManager.KEY_MOBILE);
	        scityid = user.get(SessionManager.KEY_CITYID);
	        scityname = user.get(SessionManager.KEY_CITYNAME);
	        scountry = user.get(SessionManager.KEY_COUNTRY);
	        sgeonotify = user.get(SessionManager.KEY_GEO_NOTIFI);
	        spushnotify = user.get(SessionManager.KEY_PUSH_NOTIFI);
	        semailnotify = user.get(SessionManager.KEY_EMAIL_NOTIFI);

            cd = new ConnectionDetector(getActivity().getApplicationContext());

	        if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(getActivity().getApplicationContext(),
						"Internet Connection Error",
						"Please Check the Internet Connection", false);
				 
				return rootView;
			}
	        /*updatepro.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getActivity(),
						ProfileActivity.class);
				getActivity().startActivity(myIntent);
				getActivity().overridePendingTransition(
						R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

			}
		});*/
			
			
			wifioffer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				     Log.i("dhanishj", "dhanish");
					controller.deleterecords();
					Log.i("dhanishj1", "dhanish1");
	                new MyWifiList().execute(wifiurl);
				}
			});
			
			cusretailer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent(getActivity(),
							CustomRetailerActivity.class);
					getActivity().startActivity(myIntent);
					getActivity().overridePendingTransition(
							R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
				}
			});

		customcat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getActivity(),
						CustomCategoryActivity.class);
				getActivity().startActivity(myIntent);
				getActivity().overridePendingTransition(
						R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

			}
		});
		
		AboutApp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent aboutappintent = new Intent(getActivity(),AboutApp.class);
				getActivity().startActivity(aboutappintent);
				getActivity().overridePendingTransition(
						R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
				
			}
		});
		
		shareapp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			     Intent shareIntent =   
						 new Intent(android.content.Intent.ACTION_SEND);  
						  
						//set the type  
						shareIntent.setType("text/plain");
						  
						//add a subject  
					/*	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,   
						 "Street ");  
						  */
						//build the body of the message to be shared  
						String shareMessage = "I Simply Love this app  Street Smart,  able to discover offers near me on Shopping.Its Cool ,Just Give it a shot : www.sniffwith.us";
						

						shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,   
								shareMessage);  

						//start the chooser for sharing  
						startActivity(Intent.createChooser(shareIntent,   
						 "Street Smart"));  
				
			}
		});

		feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				          new String[] { "feedback@streetsmartshop.com" });
				
				
				String phonename = android.os.Build.MANUFACTURER;
				String PhoneModel = android.os.Build.MODEL;

				// Android version
				String AndroidVersion = android.os.Build.VERSION.RELEASE;
				
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Street Smart Android Feedback");
				
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Ph :"+phonename+" Mo :"+PhoneModel+" Ve :"+AndroidVersion+"Usr :STR564"+suserid+"569"+"  Please Dont delete the above line.Share your Feedback below this Line -----------------  ");
				
				
				startActivity(emailIntent);  
			}
		});

		customnotifi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getActivity(),
						CustomeNotificationActivity.class);
				getActivity().startActivity(myIntent);
				getActivity().overridePendingTransition(
						R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

			}
		});

		chanagepass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getActivity(),
						ChangePassword.class);
				getActivity().startActivity(myIntent);
				getActivity().overridePendingTransition(
						R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

			}
		});

		signout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				session.logoutUser();
				Toast.makeText(getActivity(),
						sname + "  Eager to See you Back",
						Toast.LENGTH_LONG).show();
				getActivity().finish();
			}
		});

		
		new Userprofile().execute();
		return rootView;
	}

	
	class Userprofile extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Your Settings....");
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			 try {	
				getuservalue();
				getshoppingvalue();
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
			
		   
			func_userprofile();
			func_shopvalue();
		
			
		}
		
	} 
	
	
	public void getuservalue() throws URISyntaxException
	{
		try
		{
			BufferedReader bufferedReader = null;
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(COMM_URL+"/streetsmartadmin4/shopping/profilepage/"+suserid));
			HttpResponse response;
			response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) 
			{
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			String page = stringBuffer.toString();
			
			 

	        JSONObject jsonArray = new JSONObject(page);
	        
	        JSONObject jobj = jsonArray.getJSONObject("result");

	        get_user = jobj.getString("name");
	        get_phone=jobj.getString("mobile");
	        get_email=jobj.getString("email");
	        
	       
			
			
		}
		 catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		} 
			

	}
	
	public void func_userprofile()
	{
		
		
		/*user_name.setText(get_user);
		user_phone.setText(get_phone);
		user_email.setText(get_email);
		*/
		
	}
	
	public void func_shopvalue()
	{
		
		
		val_favor.setText(get_favor);
		val_shop.setText(get_shop);
		val_wish.setText(get_wish);
		
	}
	
	
	
	public void getshoppingvalue() throws URISyntaxException
	{
		try
		{
			BufferedReader bufferedReader = null;
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(COMM_URL+"/streetsmartadmin4/shopping/myprofileanalystic/"+suserid));
			HttpResponse response;
			response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) 
			{
				stringBuffer.append(line + NL);
				System.out.print(stringBuffer);
			}
			String page = stringBuffer.toString();
			
			 

	        JSONObject jsonArray = new JSONObject(page);
	        
	       

	        get_favor = jsonArray.getString("favourite");
	        get_shop=jsonArray.getString("shistroy");
	        get_wish=jsonArray.getString("wishlist");
	        
	     
			
			
		}
		 catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		} 
			
	
	
	}
	
	class MyWifiList extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("synchronizing WiFi Offers...");
		   // pDialog.setCancelable(false);
			 controller.deleterecords();
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"android");
				HttpGet request = new HttpGet();
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(wifiurl));

				HttpResponse response = client.execute(request);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

				StringBuffer stringBuffer = new StringBuffer("");
				String line = "";

				String NL = System.getProperty("line.separator");
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line + NL);
					System.out.print(stringBuffer);
				}
				bufferedReader.close();
				String page = stringBuffer.toString();

				Log.i("Page", page);

				// Log.d("Response: ", "> " + jsonStr);

				JSONArray jsonArray = new JSONArray(page);

				for (int i = 0; i < jsonArray.length(); i++)

				{

					JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

					wifiid = (String) jsonObject1.get("wifi_id");
					mac_id = (String) jsonObject1.get("mac_id");
					wifiretailerid = (String) jsonObject1.get("retailer_id");
					wifiretailername = (String) jsonObject1.get("retailer_name");
					wifimallid = (String) jsonObject1.get("mall_id");
					wifimallname = (String) jsonObject1.get("mall_name");
					wificityid = (String) jsonObject1.get("city_id");
					wificityname = (String) jsonObject1.get("city_name");
					wififlag = "0";
					
					controller.insertData(wifiid,mac_id,wifiretailerid,wifiretailername,wifimallid,wifimallname,wificityid,wificityname);
				}
			}
				
			catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
			wifiList =  controller.getwifimacNAmes();
	         
	        
	         
	         wifi = wifiList.toString();
	 		
	         wifilistval = wifi.substring(1, wifi.length()-1);
	         
	 		 wifili = wifilistval.toLowerCase().toString();
	 		
	 		 
	 		Wififunction();

			

		}

	}
	

	private void Wififunction() {
		// TODO Auto-generated method stub
		Log.i("WIFI LISTssss", ""+wifili);
		try {
			//String address = "08:86:3b:bf:42:7c , ac:f1:df:dc:4b:94 , c8:d7:19:e6:6d:7d";
			FileOutputStream writeToFile = getActivity().openFileOutput("macaddressfile",
					getActivity().MODE_PRIVATE);
			writeToFile.write(wifili.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//startService(new Intent(getActivity(), TestingService.class));
		getActivity().startService(new Intent(getActivity(),TestingService.class));
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(getActivity()).activityStart(getActivity());  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    
	    EasyTracker.getInstance(getActivity()).activityStop(getActivity());  // Add this method.
	  }

	public static boolean onBackPressed3() {
		// TODO Auto-generated method stub
		
		int c;
		
		if (rootView.getVisibility() == View.VISIBLE) {
			
			c=1;
	        	rootView.setVisibility(View.GONE);
	        } else {
	        	c=0;
	            //getActivity().finish();
	 
	        }
		  
		  if(c==1){
	        	 return true;
	        }else{
	        	return false;
	        }
	}

}
