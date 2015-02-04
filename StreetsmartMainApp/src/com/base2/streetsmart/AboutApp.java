package com.base2.streetsmart;


import com.base2.streetsmart.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AboutApp extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapp);
		
		  ActionBar actionbar = getActionBar();
	        
	        actionbar.hide();

		
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
