package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.util.ArrayList;

import com.base2.streetsmart.R;
import com.example.AdapterClass.NavDrawerListAdapter;
import com.example.Database.DBController;
import com.example.Session.SessionManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
 
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class HomeTabActivity extends TabActivity    {

	Intent streetIntent, mallsIntent, geoIntent, onlineIntent, arIntent;
	TabHost tabHost;
	TabSpec street, malls, geo, online, ar;
	Resources ressources;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private FrameLayout frame;
	private float lastTranslate = 0.0f;
	public static int selectedPosition = 0;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	String suserid, sName, semail, sMobile, scity, scityname, scountry,
			sgeonotify, spushnotify, semailnotify;

	String cate_id, cate_name, subcate_id, subcate_name;

	SessionManager session;
	Fragment fragment = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hometab);

		/* Actionbar Tital */

		/*
		 * Bundle bundle = getIntent().getExtras();
		 * 
		 * cate_id = bundle.getString("Category_Id"); cate_name =
		 * bundle.getString("Category_name"); subcate_id =
		 * bundle.getString("subcategoryid"); subcate_name =
		 * bundle.getString("subcategoryname");
		 */

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		 
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		
		
		 

		/*
		 * @SuppressWarnings("deprecation") SharedPreferences Login =
		 * getSharedPreferences("Loginsess", MODE_PRIVATE);
		 * SharedPreferences.Editor editor = Login.edit(); suserid =
		 * Login.getString("userid", ""); sName = Login.getString("Name", "");
		 * semail = Login.getString("email", ""); sMobile =
		 * Login.getString("Mobile", ""); scity =Login.getString("city", "");
		 * scityname =Login.getString("cityname", ""); scountry
		 * =Login.getString("country", ""); sgeonotify
		 * =Login.getString("geonotify", ""); spushnotify
		 * =Login.getString("pushnotify", ""); semailnotify
		 * =Login.getString("emailnotify", "");
		 */

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		frame = (FrameLayout) findViewById(R.id.frame_container);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// Pages

		// What's hot, We will add a counter here

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.drawer_open, // nav drawer open - description for
										// accessibility
				R.string.drawer_close)// nav drawer close - description for
										// accessibility

		{
			/*
			 * // @SuppressLint("NewApi") public void onDrawerSlide(View
			 * drawerView, float slideOffset) { float moveFactor =
			 * (mDrawerList.getWidth() * slideOffset);
			 * 
			 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			 * frame.setTranslationX(moveFactor); } else { TranslateAnimation
			 * anim = new TranslateAnimation( lastTranslate, moveFactor, 0.0f,
			 * 0.0f); anim.setDuration(0); anim.setFillAfter(true);
			 * frame.startAnimation(anim);
			 * 
			 * lastTranslate = moveFactor; } }
			 */

			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/*
		 * if (savedInstanceState == null) { // on first time display view for
		 * first nav item displayView(0); }
		 */
		tabView();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		 
		case R.id.action_refresh:
			return true;
		case R.id.search_setting:
			intentfunc();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(false);
		 
		return super.onPrepareOptionsMenu(menu);
	}

	public void intentfunc() {
		Intent searchintent = new Intent(HomeTabActivity.this,
				SearchActivity.class);
		startActivity(searchintent);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public final void displayView(int position) {
		// update the main content by replacing fragments
		fragment = null;

		switch (position) {

		case 0:
			fragment = new HomeFragment();
			break;

		case 1:
			fragment = new WishlistFragment();
			break;
		case 2:
			fragment = new FavoritesFragment();
			break;
		case 3:
			fragment = new NotificationFragment();
			break;
		case 4:
			fragment = new AddRetailerFragment();
			break;
			
		case 5:
			fragment = new SettingsFragment();
			break;

		default:

			break;

		}

		if (fragment != null) {
			FragmentTransaction fragmentManager = getFragmentManager()
					.beginTransaction();

			fragmentManager.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		selectedPosition = position;

		Log.i("tag", String.valueOf(selectedPosition));

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		
		/*int tabcount = tabHost.getCurrentTab();
		
		Toast.makeText(getApplicationContext(), ""+tabcount, Toast.LENGTH_LONG).show();*/
		
		if(title.equals("FAVOURITES"))
		{
			getActionBar().setTitle("");
		}
		else
		{
		getActionBar().setTitle(mTitle);
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
	    // TODO Auto-generated method stub

	    if(mDrawerLayout.isDrawerOpen(mDrawerList)){
	    	mDrawerLayout.closeDrawer(mDrawerList);
	    }else{
	        super.onBackPressed();
	    }
	}
	
	/*public boolean onKeyDown(int keyCode, KeyEvent event,int selectedPosition) {
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	tabView();
	    	Log.i("tag2", String.valueOf(selectedPosition));
	         
	    	if (selectedPosition != 0)
			{
				Log.i("tag2", String.valueOf(selectedPosition));
				displayView(0);
				 
			} else {
				super.onBackPressed();
			}
	    	Toast.makeText(getApplicationContext(), selectedPosition, Toast.LENGTH_LONG).show();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event); 
	

	}*/

	/* TabView finctions */

	public void tabView() {

		ressources = getResources();
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		tabHost = getTabHost();
		
		Intent intentAndroid = new Intent().setClass(this, BigDealActivity.class);
		TabSpec tabSpecAndroid = tabHost
				.newTabSpec("HOME")
				
				.setIndicator("",
						ressources.getDrawable(R.drawable.favtabicon_change))
				.setContent(intentAndroid);

		Intent intentApple = new Intent().setClass(this, GeoDealVolley.class);
		TabSpec tabSpecApple = tabHost
				.newTabSpec("GEO")
				
				.setIndicator("",
						ressources.getDrawable(R.drawable.shoptabicon_change))
				.setContent(intentApple);

		Intent intentWindows = new Intent()
				.setClass(this, NearMeActivity.class);
		TabSpec tabSpecWindows = tabHost
				.newTabSpec("NEARME")
				 
				.setIndicator("",
						ressources.getDrawable(R.drawable.nearmetabicon_change))
				.setContent(intentWindows);

		Intent intentBerry = new Intent().setClass(this,
				MallDealVolley.class);
		TabSpec tabSpecBerry = tabHost
				.newTabSpec("MALL")
				.setIndicator("",
						ressources.getDrawable(R.drawable.malltabicon_change))
				.setContent(intentBerry);

		Intent intentAR = new Intent().setClass(this, OnlineActivity.class);
		TabSpec tabSpecAR = tabHost
				.newTabSpec("ONLINE")
				.setIndicator("",
						ressources.getDrawable(R.drawable.onlinetabicon_change))
				.setContent(intentAR);

		// add all tabs
		
		tabHost.addTab(tabSpecAndroid);
		tabHost.addTab(tabSpecApple);
		tabHost.addTab(tabSpecWindows);
		tabHost.addTab(tabSpecBerry);
		tabHost.addTab(tabSpecAR);

		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);

	}

	

}
