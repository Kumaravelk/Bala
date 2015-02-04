package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import com.base2.streetsmart.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayDealActivity extends TabActivity {
	
    
	Bundle DealID_bundle;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deal);
         
       /*   ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));*/

        ActionBar actionbar = getActionBar();
        
        actionbar.hide();
        
        TabHost tabHost = getTabHost();
        
        DealID_bundle = getIntent().getExtras();
		 
       //String DDeal_ID = "500";
		 String DDeal_ID = DealID_bundle.getString("DealID");  

		 // Toast.makeText(getApplicationContext(), DDeal_ID, Toast.LENGTH_LONG).show();
         
        // Tab for Photos
        TabSpec firstspec = tabHost.newTabSpec("View1");
        // setting Title and Icon for the Tab
        firstspec.setIndicator("Offer info");
        Intent photosIntent = new Intent(this, View1VolleyActivity.class);
        
        DealID_bundle = new Bundle();
        DealID_bundle.putString("DealID", DDeal_ID);
		photosIntent.putExtras(DealID_bundle);
		
        firstspec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec secspec = tabHost.newTabSpec("View2");       
        secspec.setIndicator("Card Offers");
        Intent songsIntent = new Intent(this, CreditCardVolley.class);
        
        DealID_bundle = new Bundle();
        DealID_bundle.putString("DealID", DDeal_ID);
        songsIntent.putExtras(DealID_bundle);
        secspec.setContent(songsIntent);
         
        // Tab for Videos
        TabSpec thirdspec = tabHost.newTabSpec("View3");
        thirdspec.setIndicator("More Offers");
        Intent videosIntent = new Intent(this, MoreDealVolley.class);
        
        DealID_bundle = new Bundle();
        DealID_bundle.putString("DealID", DDeal_ID);
        videosIntent.putExtras(DealID_bundle);
        thirdspec.setContent(videosIntent);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(firstspec); // Adding photos tab
        tabHost.addTab(secspec); // Adding songs tab
        tabHost.addTab(thirdspec); // Adding videos tab
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
