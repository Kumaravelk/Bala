package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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


import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CreditCardVolley extends Activity {

	private String TAG=CreditCardVolley.class.getSimpleName();
	private ListView CreditCardDealList;
	private ProgressDialog pdialog;
	private CreditCardAdapter creditcardadapter;
	private List<CreditCardItem> CreditCardList=new ArrayList<CreditCardItem>();
	JSONObject obj;
	
	ImageView noimage;
	SessionManager session;
	
	String DDeal_ID ;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view2);
		
		
		 Bundle DealID_bundle = getIntent().getExtras();
		 DDeal_ID = DealID_bundle.getString("DealID");
		 
		 noimage = (ImageView)findViewById(R.id.noimage);
		 
		 
		 
		 CreditCardDealList = (ListView)findViewById(R.id.creditcardlistView);
		
		 creditcardadapter=new CreditCardAdapter();
		 CreditCardDealList.setAdapter(creditcardadapter);
		
			
		 pdialog=new ProgressDialog(this);
			pdialog.setMessage("Loading Credit Card Offers...");
			
			pdialog.show();
			
			JsonObjectRequest arrayReq=new JsonObjectRequest(COMM_URL+"/streetsmartadmin4/shopping/displaydeal/"+DDeal_ID, null, new Listener<JSONObject>() {
				
				JSONArray arrayJson;
				
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					
					dissmissDialog();

					try {
						arrayJson = response.getJSONArray("creditcard");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(int i=0;i<arrayJson.length();i++){
						try {
							JSONObject json = (JSONObject) arrayJson.get(i);
							
							CreditCardItem creditcard=new CreditCardItem();
							//model.setImage(obj.getString("image"));
							String cardcondition = (String) json.get("cardcondition");
							String bankname = (String) json.get("bank_name");
							String privilage_type = (String) json.get("privilage_type_name");
							String card_type = (String) json.get("card_type_name");
							String card_type_icon = (String) json.get("card_type_icon");
							String bankicon = (String) json.get("bank_icon");
							
							
							
							creditcard.setBankname(bankname);
							creditcard.setPrivilage(privilage_type);
							creditcard.setCardtype(card_type);
							creditcard.setBankicon(bankicon); 
							creditcard.setCardicon(card_type_icon);
							
							creditcard.setCardcondition(cardcondition);
							
							CreditCardList.add(creditcard);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					int count = CreditCardList.size();
					String count1 = String.valueOf(count);  
					if(count1.equalsIgnoreCase("0"))
					{	  
						CreditCardList.clear();	 
						noimage.setImageResource(R.drawable.noimg);
						
						
						
					}else
					{
						noimage.setImageResource(0);
						 
					}
					creditcardadapter.notifyDataSetChanged();
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					VolleyLog.d(TAG, "ERROR"+error.getMessage());
				}
			});
			 AppController.getInstance().addToRequestQueue(arrayReq);
		}
		private void dissmissDialog() {
			// TODO Auto-generated method stub
			if(pdialog !=null){
				if(pdialog.isShowing()){
					pdialog.dismiss();
				}
				pdialog=null;
			}
		}
		
		
					
		
		
		private class CreditCardAdapter extends BaseAdapter
		{
			private LayoutInflater inflater;
			ImageLoader imageLoader = AppController.getInstance().getImageLoader();
			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return CreditCardList.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return CreditCardList.get(arg0);
			}

			@Override
			public long getItemId(int arg0)
			{
				// TODO Auto-generated method stub
				return arg0;
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			public View getView(int position, View view, ViewGroup parent)
			{
				// TODO Auto-generated method stub
				
				if(inflater ==null)
					inflater=(LayoutInflater)getLayoutInflater();
				if(view==null)
					view=inflater.inflate(R.layout.creditcard, null);
				if(imageLoader == null)
					 imageLoader = AppController.getInstance().getImageLoader();
				
				
				
				NetworkImageView bankicon=(NetworkImageView) view.findViewById(R.id.banklogo);
				
				NetworkImageView cardicon=(NetworkImageView) view.findViewById(R.id.cardlogo);
				
				
				
				TextView text_bankname = (TextView)view.findViewById(R.id.bankname);
				TextView text_privilage = (TextView)view.findViewById(R.id.privilage);
				TextView text_cardtype = (TextView)view.findViewById(R.id.cardtypename);
				TextView text_cardcondition = (TextView)view.findViewById(R.id.cardcondition);
				
				 
				
				CreditCardItem cardItem=CreditCardList.get(position);
				
				text_bankname.setText(cardItem.getBankname());
				text_privilage.setText(cardItem.getPrivilage());
				text_cardtype.setText(cardItem.getCardtype());
				text_cardcondition.setText(cardItem.getCardcondition());
				
				bankicon.setImageUrl(cardItem.getBankicon(), imageLoader);
				cardicon.setImageUrl(cardItem.getCardicon(), imageLoader);
				 
				
				
				return view;
			}
			
		}
		
		
		class CreditCardItem 
		{

			String bankname;
			public String getBankname() {
				return bankname;
			}
			public void setBankname(String bankname) {
				this.bankname = bankname;
			}
			public String getPrivilage() {
				return privilage;
			}
			public void setPrivilage(String privilage) {
				this.privilage = privilage;
			}
			public String getCardtype() {
				return cardtype;
			}
			public void setCardtype(String cardtype) {
				this.cardtype = cardtype;
			}
			public String getCardcondition() {
				return cardcondition;
			}
			public void setCardcondition(String cardcondition) {
				this.cardcondition = cardcondition;
			}
			
			public String getBankicon() {
				return bankicon;
			}
			public void setBankicon(String bankicon) {
				this.bankicon = bankicon;
			}
			public String getCardicon() {
				return cardicon;
			}
			public void setCardicon(String cardicon) {
				this.cardicon = cardicon;
			}

			String privilage;
			String cardtype;
			String cardcondition;
			String bankicon;
			String cardicon;
			
			
			
			
		

		}

	
	
}
