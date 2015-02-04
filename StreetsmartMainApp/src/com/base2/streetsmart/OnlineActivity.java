package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import com.base2.streetsmart.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OnlineActivity extends Activity {
	
	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler;
	ListView CategoryList ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online);
		
		/*TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("COMING SOON");
		*/
		/*String[] category = new String[] { "amazon.in",
				"eBay.in", "flipkart.com",
				"junglee.com", "homeshop18.com",
				"snapdeal.com",
				"tradus.com",
				"pepperfry", "myntra.com",
				"shoppersstop.com", "yatra.com",
				"makemytrip.com", "groupon.co.in",
				"goibibo.com", "yebhi.com"};

	    CategoryList = (ListView) findViewById(R.id.categorylist);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				category);
		CategoryList.setAdapter(adapter);

		CategoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			//	Intent cate = new Intent(Category.this, DealPage.class);
			//	startActivity(cate);
				overridePendingTransition(R.anim.anim_slide_in_right,
						R.anim.anim_slide_out_left);
				 
			}

		});*/

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
		    }, 5000); 
		} 
}

/*public class OnlineActivity extends Fragment   {

  ListView CategoryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online);
		
		
		
		String[] category = new String[] { "amazon.in",
				"eBay.in", "flipkart.com",
				"junglee.com", "homeshop18.com",
				"snapdeal.com",
				"tradus.com",
				"pepperfry", "myntra.com",
				"shoppersstop.com", "yatra.com",
				"makemytrip.com", "groupon.co.in",
				"goibibo.com", "yebhi.com"};
		
		  public OnlineActivity(){}

		    @Override

		    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
		    {
		    	 ListView l = (ListView) rootView.findViewById(R.id.wishlist);

		        View view = inflater.inflate(R.layout.activity_wishlist_fragment, container, false);
		        ListView ls = (ListView)view.findViewById(R.id.wishlist);
		        
		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, category);
		        ls.setAdapter(adapter);
		        
		        return view;

		    }

		    
		 
		 

	}*/
	


