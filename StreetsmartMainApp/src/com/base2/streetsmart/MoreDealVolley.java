package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.base2.streetsmart.CreditCardVolley.CreditCardItem;
import com.example.Session.SessionManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MoreDealVolley extends Activity {
	
	
	private String TAG=CreditCardVolley.class.getSimpleName();
	private ListView MoreDealList;
	private ProgressDialog pdialog;
	private MoreDealAdapter moredealadapter;
	private List<MoreDealItem> MoreList=new ArrayList<MoreDealItem>();
	JSONObject obj;
	
	
	ImageView noimage;
	SessionManager session;
	
	String DDeal_ID ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_deal);
		
		 Bundle DealID_bundle = getIntent().getExtras();
		 DDeal_ID = DealID_bundle.getString("DealID");  
		 
		 noimage = (ImageView)findViewById(R.id.noimage);
		 
		 MoreDealList = (ListView)findViewById(R.id.moredeallistView);
			
		 moredealadapter=new MoreDealAdapter();
		 MoreDealList.setAdapter(moredealadapter);
		
			
		 pdialog=new ProgressDialog(this);
			pdialog.setMessage("Loading More Deal Offers...");
			
			pdialog.show();
			
			JsonObjectRequest arrayReq=new JsonObjectRequest(COMM_URL+"/streetsmartadmin4/shopping/displaydeal/"+DDeal_ID, null, new Listener<JSONObject>() {
				
				JSONArray arrayJson;
				
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					
					dissmissDialog();

					try {
						arrayJson = response.getJSONArray("type");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(int i=0;i<arrayJson.length();i++){
						try {
							JSONObject json = (JSONObject) arrayJson.get(i);
							
							MoreDealItem more_deal=new MoreDealItem();
							//model.setImage(obj.getString("image"));
							
							
							String subcatname = (String) json.get("subcatname");
							String productname = (String) json.get("productname");

							String moreoffername = (String) json.get("offertext");
							String morecondition = (String) json.get("conditions");

							String morebrandname = (String) json.get("brandname");
							String moreprices = (String) json.get("priceorpercentage");
							
							String morebimage = (String) json.get("dimage");
							String morebrandimage = (String) json.get("producticon");
							
							
							more_deal.setSubcatname(subcatname);
							more_deal.setProductname(productname);
							more_deal.setOffertext(moreoffername);
							more_deal.setConditions(morecondition);
							more_deal.setBrandname(morebrandname);
							more_deal.setPriceorpercentage(moreprices);
							more_deal.setMoredealimage(morebimage);
							more_deal.setMorebrandcion(morebrandimage);
							
							MoreList.add(more_deal);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					int count = MoreList.size();
					String count1 = String.valueOf(count);  
					if(count1.equalsIgnoreCase("0"))
					{	  
						MoreList.clear();	 
						noimage.setImageResource(R.drawable.noimg);
						pdialog.dismiss();
						
						
					}else
					{
						noimage.setImageResource(0);
						 
					}
					moredealadapter.notifyDataSetChanged();
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
		
		
					
		
		
		private class MoreDealAdapter extends BaseAdapter
		{
			private LayoutInflater inflater;
			ImageLoader imageLoader = AppController.getInstance().getImageLoader();
			@Override
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return MoreList.size();
			}

			@Override
			public Object getItem(int arg0) 
			{
				// TODO Auto-generated method stub
				return MoreList.get(arg0);
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
					view=inflater.inflate(R.layout.moredeal, null);
				if(imageLoader == null)
					 imageLoader = AppController.getInstance().getImageLoader();
				
				
				
				NetworkImageView moredealimage=(NetworkImageView) view.findViewById(R.id.morebigimage);
				
				NetworkImageView morebrandimage=(NetworkImageView) view.findViewById(R.id.morebrandicon);
				
				
				TextView text_moresubcatname = (TextView) view.findViewById(R.id.subcatename);
				
				TextView text_moreproductname = (TextView)view.findViewById(R.id.moreproductname);
				
				TextView text_moreoffername = (TextView) view.findViewById(R.id.moreoffername);
				
				
				TextView text_morebrandname = (TextView) view.findViewById(R.id.morebrandname);
				
				TextView text_moreprice = (TextView)view.findViewById(R.id.moreprice);
				
				 
				
				MoreDealItem moredeal=MoreList.get(position);
				
				text_moresubcatname.setText(moredeal.getSubcatname());
				
				text_moreproductname.setText(moredeal.getProductname());
				
				text_moreoffername.setText(moredeal.getOffertext());
				
				
				text_morebrandname.setText(moredeal.getBrandname());
				
				text_moreprice.setText(moredeal.getPriceorpercentage()+" OFF");
				
				moredealimage.setImageUrl(moredeal.getMoredealimage(), imageLoader);
				morebrandimage.setImageUrl(moredeal.getMorebrandcion(), imageLoader);
				 
				return view;
			}
			
		}
		
		class MoreDealItem 
		{
		 
			String subcatname;
			public String getSubcatname() {
				return subcatname;
			}
			public void setSubcatname(String subcatname) {
				this.subcatname = subcatname;
			}
			public String getOffertext() {
				return offertext;
			}
			public void setOffertext(String offertext) {
				this.offertext = offertext;
			}
			public String getConditions() {
				return conditions;
			}
			public void setConditions(String conditions) {
				this.conditions = conditions;
			}
			public String getProductname() {
				return productname;
			}
			public void setProductname(String productname) {
				this.productname = productname;
			}
			public String getBrandname() {
				return brandname;
			}
			public void setBrandname(String brandname) {
				this.brandname = brandname;
			}
			public String getPriceorpercentage() {
				return priceorpercentage;
			}
			public void setPriceorpercentage(String priceorpercentage) {
				this.priceorpercentage = priceorpercentage;
			}
			String offertext;
			String conditions;
			String productname;
			String brandname;
			String priceorpercentage;
			String moredealimage;
			String morebrandcion;
			
			public String getMoredealimage() {
				return moredealimage;
			}
			public void setMoredealimage(String moredealimage) {
				this.moredealimage = moredealimage;
			}
			public String getMorebrandcion() {
				return morebrandcion;
			}
			public void setMorebrandcion(String morebrandcion) {
				this.morebrandcion = morebrandcion;
			}
			
			
			
		}

}
