package com.base2.streetsmart;
import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.util.HashMap;

import com.base2.streetsmart.R;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class FilterActivity extends Activity {

	
   RelativeLayout sort_Brand,sort_Retailer;
   Button done,closefilter;
   TextView Brandmessage,Retailermessage,Price,SortText,Bra_Name,Ret_Name;
   SeekBar PriceRange;
	
   String Brandvalues,retailervalues,GetPrices,check_cc,check_emi,check_cod,check_ex,sortstr,Brandvals,Retailervals;
   
   CheckBox filter_emi,filter_cod,filter_cc,filter_ex;
   
   RadioGroup radioSort;
   
   Bundle GetVal;
   
   String Get_Val;
   
   SessionManager session;
   String message,message1,retailermessage1;
   
   String sebrand,seretailer,secredit,secod,seemi,seexchange,sebrandname,seretailername;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flitersort);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		
		GetVal = getIntent().getExtras();
			 
	       //String DDeal_ID = "500";
		Get_Val = GetVal.getString("Val");
		
		//Toast.makeText(getApplicationContext(), Get_Val, Toast.LENGTH_LONG).show();
		
		session = new SessionManager(getApplicationContext());
		
		sort_Brand = (RelativeLayout)findViewById(R.id.brand_sort);
		sort_Retailer = (RelativeLayout)findViewById(R.id.retailer_sort);
		
		done = (Button)findViewById(R.id.button1);
		closefilter = (Button)findViewById(R.id.filterclose);
		
		
		
		Brandmessage = (TextView)findViewById(R.id.brandmessage);
		Retailermessage = (TextView)findViewById(R.id.retailermessage);
		//Price = (TextView)findViewById(R.id.Price);
		SortText = (TextView)findViewById(R.id.sortid);
		Bra_Name = (TextView)findViewById(R.id.brandnames);
		Ret_Name = (TextView)findViewById(R.id.retailernames);
		
		//PriceRange = (SeekBar)findViewById(R.id.pricerange);
		
		filter_emi = (CheckBox)findViewById(R.id.fi_emi);
		filter_cod=(CheckBox)findViewById(R.id.fi_cod);
		filter_cc=(CheckBox)findViewById(R.id.fi_creditcard);
		filter_ex=(CheckBox)findViewById(R.id.fi_exchange);
		
		
		HashMap<String, String> user = session.getUserDetails();
		
		sebrand = user.get(SessionManager. KEY_BRANDID); 
		seretailer = user.get(SessionManager. KEY_RETALIERID); 
		secredit = user.get(SessionManager. KEY_CREDITCARD); 
		secod = user.get(SessionManager. KEY_COD); 
		seemi = user.get(SessionManager. KEY_EMI); 
		seexchange = user.get(SessionManager.KEY_EX);
		sebrandname = user.get(SessionManager. KEY_BRANDNAME); 
		seretailername = user.get(SessionManager.KEY_RETAILERNAME); 
		
		
       if (secredit.equalsIgnoreCase("1")) {
		
    	   filter_cc.setChecked(true);
	    }
       else
       {
    	   filter_cc.setChecked(false);
       }
       
       if (secod.equalsIgnoreCase("1")) {
   		
    	   filter_cod.setChecked(true);
	    }
       else
       {
    	   filter_cod.setChecked(false);
       }
		
       if (seemi.equalsIgnoreCase("1")) {
   		
    	   filter_emi.setChecked(true);
	    }
       else
       {
    	   filter_emi.setChecked(false);
       }
		
       if (seexchange.equalsIgnoreCase("1")) {
   		
    	   filter_ex.setChecked(true);
	    }
       else
       {
    	   filter_ex.setChecked(false);
       }
       
       if(sebrand.equalsIgnoreCase("0"))
       {
    	   Bra_Name.setText("ALL BRANDS");
       }
       else
       {
    	   Bra_Name.setText(sebrandname);
       }
       
       if(seretailer.equalsIgnoreCase("0"))
       {
    	   Ret_Name.setText("ALL RETAILERS");
       }
       else
       {
    	   Ret_Name.setText(seretailername);
       }
       
       
			
		/*PriceRange.setProgress(0);
		PriceRange.incrementProgressBy(10);
		PriceRange.setMax(200000);
	
		PriceRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		        progress = progress / 10;
		        progress = progress * 10;
		        Price.setText(String.valueOf(progress));
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {

		    }

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {

		    }
		});*/
	
		
		sort_Brand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				overridePendingTransition(R.anim.flipup, R.anim.flipdown);
				Intent brandintent = new Intent(FilterActivity.this,BrandActivity.class);
				
				
				 startActivityForResult(brandintent, 1);
				
			}
		});
		
		sort_Retailer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				overridePendingTransition(R.anim.flipup, R.anim.flipdown);
				Intent retailerintent = new Intent(FilterActivity.this,RetailerActivity.class);
				
				Bundle bu = new Bundle();
				
				bu.putString("Val", Get_Val);
				
				retailerintent.putExtras(bu);
				
				startActivityForResult(retailerintent, 2);
			}
		});
		
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Brandvals = Brandmessage.getText().toString();
				
				Retailervals = Retailermessage.getText().toString();
				
				/*GetPrices = Price.getText().toString();
				
				sortstr = SortText.getText().toString();*/
				
				/*if(GetPrices.equalsIgnoreCase("Slide to Price Range"))
				{
					GetPrices = "0";
				}
				else
				{
					GetPrices = Price.getText().toString();
				}*/
				
				if(filter_cc.isChecked())
				{
					check_cc = "1";
				}
				else
				{
					check_cc = "0";
				}
				if(filter_cod.isChecked())
				{
					check_cod = "1";
				}
				else
				{
					check_cod = "0";
				}
				if(filter_emi.isChecked())
				{
					check_emi = "1";
				}
				else
				{
					check_emi = "0";
				}
				if(filter_ex.isChecked())
				{
					check_ex = "1";
				}
				else
				{
					check_ex = "0";
				}
				
				
				
				//session.createcategorymethod(categoryid,categoryname,subcategoryid,subcategoryname);
				
				session.createfliterMethod(Brandvals,Retailervals,check_cod,check_emi,check_cc,check_ex,message1,retailermessage1);
				
				Intent intent=new Intent();
		        intent.putExtra("Brand_id",Brandvals);
		        intent.putExtra("Retailer_id",Retailervals);
		        intent.putExtra("Prices", GetPrices);
		        intent.putExtra("COD", check_cod);
		        intent.putExtra("EMI", check_emi);
		        intent.putExtra("CC", check_cc);
		        intent.putExtra("EX", check_ex);
		        intent.putExtra("Sort", sortstr);
		        
		        setResult(6,intent);
		        
		        finish();
				
			}
		});
		
		
		closefilter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
		        intent.putExtra("Brand_id","0");
		        intent.putExtra("Retailer_id","0");
		        intent.putExtra("Prices", "0");
		        intent.putExtra("COD", "0");
		        intent.putExtra("EMI", "0");
		        intent.putExtra("CC", "0");
		        intent.putExtra("EX", "0");
		        intent.putExtra("Sort", "0");
		        
		        setResult(6,intent);
		        
		        finish();
			}
		});
		
	}
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
              super.onActivityResult(requestCode, resultCode, data);
                
               // check if the request code is same as what is passed  here it is 2
            
              if(requestCode==1)
              {
                 message=data.getStringExtra("Brandid"); 
                 message1 = data.getStringExtra("Brandname");
                 
                 Brandvalues = message;
                 Brandmessage.setText(Brandvalues);
                 
                 Bra_Name.setText(message1);
                 
                // Toast.makeText(getApplicationContext(), message+message1, Toast.LENGTH_LONG);
    
              }
              if(requestCode==2)
              {
            	  String retailermessage=data.getStringExtra("Retailerid"); 
                  retailermessage1 = data.getStringExtra("Retailername");
                  
                  Ret_Name.setText(retailermessage1);
                  retailervalues = retailermessage;
                  Retailermessage.setText(retailervalues);
                  
                  Ret_Name.setText(retailermessage1);
                  
                  
              }
  }
	
    /*public void Sorting(View view)
	 {
		 switch (view.getId()) {
		  
		  case R.id.priceRadio:
			  
			  //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
			  SortText.setText("1");
			  
		   break;

		  case R.id.distanceRadio:

			    // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
				 SortText.setText("2");
				 
			   break;

		  case R.id.ratingRadio:
			  
			 // Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
			  SortText.setText("3");
		   break;
		   
		  case R.id.peopleRadio:
			 // Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
			  SortText.setText("4");
		   break; 
		   
		  case R.id.noneRadio:
			//  Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_LONG).show();
			  SortText.setText("0");
		   break; 
		  } 
		*/
		
		 
	// }
    
    @Override
    	public void onBackPressed() {
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
}


