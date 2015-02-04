package com.base2.streetsmart;

import com.base2.streetsmart.R;
import com.example.Session.SessionManager;
 
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.widget.ImageView;


public class MainActivity1 extends Activity {

	SessionManager session;
	ImageView img;
	
	public static final String COMM_URL = "http://54.169.81.215";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);
		
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		   
		
		img = (ImageView) findViewById(R.id.loadingImage);
		img.setBackgroundResource(R.drawable.loading);
		
		session = new SessionManager(getApplicationContext());
		//Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
		session.checkLogin();
		MainActivity1.this.finish();
	}

	 

}
