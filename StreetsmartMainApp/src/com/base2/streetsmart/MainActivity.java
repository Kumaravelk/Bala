package com.base2.streetsmart;

import com.base2.pushnotifications.AlertDialogManager;
import com.base2.pushnotifications.ConnectionDetector;
import com.base2.streetsmart.R;
import com.example.Session.SessionManager;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	
	public ViewFlipper MyViewFlipper, MyViewFlipper1, MyViewFlipper2,MyViewFlipper3;
	Button insignin,insignup;
	TextView useyouremail;
	SessionManager session;
	public static MainActivity instance = null;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
          
		  ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();
		  instance = this;
		  
		  /*NetWork Connection */
		  cd = new ConnectionDetector(getApplicationContext());
			if (!cd.isConnectingToInternet()) {
				 
				alert.showAlertDialog(MainActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
				 
				return;
			}
		  
		  
		
		 
		
		insignin = (Button) findViewById(R.id.introsignin);
	    insignup = (Button) findViewById(R.id.introsignup);
		
		
		session = new SessionManager(getApplicationContext());
		 
		
		/*---    Signin Button     ---*/
		
		insignin.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 
				Intent loginin = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(loginin);
				overridePendingTransition(R.anim.flip_up_in, R.anim.flip_up_out);

			}

		});
		
		
		/*--    SignUp Button  --*/
		
		insignup.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 
				 Intent insignup = new Intent(MainActivity.this,RegistrationActivity.class);
				 startActivity(insignup);
				 
				 overridePendingTransition(R.anim.flip_up_in, R.anim.flip_up_out);
			}
		});
		
		
	} 
	
	@Override
	public void finish() {
	    super.finish();
	    instance = null;
	}
}
