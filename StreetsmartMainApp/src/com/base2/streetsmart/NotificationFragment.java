package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NotificationFragment extends Fragment {
	
	public NotificationFragment()
	{
		
	}
	
	private String TAG=NotificationFragment.class.getSimpleName();
	private ListView listview;
	private ProgressDialog pdialog;
	private listviewAdapter adapter;
	private List<ItemModel> modelList=new ArrayList<ItemModel>();
	JSONObject obj;
	String subcategorystr;
	String Dealid ;

	String str, page, name;
	 
	ProgressDialog pDialog;
	static View rootView;
    ImageView noimage;
	 
    SessionManager session;
    String suserid, sname, semail, smobile, scityid, scityname, scountry,sgeonotify, spushnotify, semailnotify,sage,sgender,soccupy,scate,scateories;
	 
	TextView tv;
	String tag_json_obj = "json_obj_req";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.activity_superadminnotification, container, false);
         
       // return rootView;
        
        session = new SessionManager(getActivity().getApplicationContext());
        noimage = (ImageView) rootView.findViewById(R.id.noimage);

		  HashMap<String, String> user = session.getUserDetails();
	        
		  suserid = user.get(SessionManager. KEY_USERID); 
		  sname = user.get(SessionManager.KEY_NAME); 
		  semail =user.get(SessionManager.KEY_EMAIL); 
		  smobile =user.get(SessionManager.KEY_MOBILE); 
		  scityid =user.get(SessionManager.KEY_CITYID); 
		  scityname =user.get(SessionManager.KEY_CITYNAME); 
		  scountry =user.get(SessionManager.KEY_COUNTRY); 
		  sgeonotify =user.get(SessionManager.KEY_GEO_NOTIFI);
		  sage =user.get(SessionManager.KEY_AGE);
		  sgender =user.get(SessionManager.KEY_GENDER);
		  soccupy =user.get(SessionManager.KEY_OCCUPY);
		  scate =user.get(SessionManager.KEY_CATEGORYID);
		  scateories =user.get(SessionManager.KEY_CATEGORIES);

        
        listview = (ListView) rootView.findViewById(R.id.NotiDeallist);
        noimage = (ImageView) rootView.findViewById(R.id.noimage);
		adapter=new listviewAdapter();
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {
				Dealid = ((TextView) v.findViewById(R.id.sadmindeal_id))
						.getText().toString();

				// TODO Auto-generated method stub
				Intent ddin = new Intent(getActivity(),
						DisplayDealActivity.class);
				 
				Bundle bundle = new Bundle();
				 
				bundle.putString("DealID", Dealid);
				ddin.putExtras(bundle);
				startActivity(ddin);
			  }

			 

		   });
		
		pdialog=new ProgressDialog(getActivity());
		pdialog.setMessage("Loading Today's Hot Deals...");
		pdialog.show();
		 
	   RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
		/* JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
	                url, null,
	                new Response.Listener<JSONObject>() {*/
        StringRequest postReq = new StringRequest(Request.Method.POST, "http://54.169.81.215/streetsmartadmin4/shopping/pushsuperadmindeal", new Response.Listener<String>() 
		{
			public void onResponse(String response) 
			{
				pdialog.dismiss();
			   parseJSON(response);
				 
			 }
			}, new Response.ErrorListener() 
			  {

				@Override
				public void onErrorResponse(VolleyError error) 
				{
					System.out.println("Error ["+error+"]");
					
				}
			})  
			{

				@Override
				protected Map<String, String> getParams() throws AuthFailureError 
				{
					HashMap<String, String> headers = new HashMap<String, String>();
					//Map<String, String> params = new HashMap<String, String>();
					headers.put("userid", suserid);
					headers.put("cityid", scityid);
					headers.put("categoryid", scateories);
					headers.put("age", sage);
					headers.put("occupy", soccupy);
					headers.put("gender", sgender);
					return headers;
				}
				
			};
			
			//rq.add(postReq);
			
			AppController.getInstance().addToRequestQueue(postReq);
			return rootView;
			
}

	
	
	private void parseJSON(String response)
	{
		//JSONObject jrootobj = new JSONObject(response);
		try{
			JSONArray jsonArray = new JSONArray(response);
			 Log.i("Area Name Sub1", jsonArray.toString());
			for (int i = 0; i < jsonArray.length(); i++)

			{
				JSONArray jArray = (JSONArray) jsonArray.getJSONArray(i);

				for (int j = 0; j < jArray.length(); j++) {
        
					 

					JSONObject json = (JSONObject) jArray.get(j);
           
                    
                  //  Toast.makeText(getApplicationContext(), item.toString(),Toast.LENGTH_LONG).show();
                    ItemModel nm = new ItemModel();
                    nm.setDeal_id(json.optString("deal_id"));
                    nm.setRetailer_name(json.optString("retailer_name"));
                    nm.setOffername(json.optString("offername"));
                    nm.setPromotext(json.optString("promotext"));
                    nm.setEdate(json.optString("edate"));
                    nm.setPrice(json.optString("price"));
                    nm.setPercentage(json.optString("percentage"));
                    nm.setRetailer_logo(json.optString("retailer_logo"));
                    //nm.setPubDate(item.optString("pubDate"));
                   modelList.add(nm);
                   
                   int count = modelList.size();
   				String count1 = String.valueOf(count);  
   				if(count1.equalsIgnoreCase("0"))
   				{	  
   					modelList.clear();	 
   					noimage.setImageResource(R.drawable.noimg);
   					
   					
   				}else
   				{
   					noimage.setImageResource(0);
   					 
   				}
                     
            }
        }
		}
        catch(Exception e){
            e.printStackTrace();
        }
		adapter.notifyDataSetChanged();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	 

	private void dissmissDialog() 
	{
		// TODO Auto-generated method stub
		if(pdialog !=null)
		{
			if(pdialog.isShowing())
			{
				pdialog.dismiss();
			}
			pdialog=null;
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private class listviewAdapter extends BaseAdapter
	{

		private LayoutInflater inflater;
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return modelList.size();
		}

		@Override
		public Object getItem(int arg0) 
		{
			// TODO Auto-generated method stub
			return modelList.get(arg0);
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
				inflater=(LayoutInflater) getActivity().getLayoutInflater();
			if(view==null)
				view=inflater.inflate(R.layout.superadmindeallist, null);
			if(imageLoader == null)
				 imageLoader = AppController.getInstance().getImageLoader();
			
			
			NetworkImageView imageview=(NetworkImageView)view.findViewById(R.id.sadminretailerlogo);
			
			TextView retailername=(TextView)view.findViewById(R.id.sadminretailername);
			TextView deal_id=(TextView)view.findViewById(R.id.sadmindeal_id);
			TextView dealoffer=(TextView)view.findViewById(R.id.sadmindealoffer);
			TextView dealprice=(TextView)view.findViewById(R.id.sadmindealprice);
			TextView dealpromo=(TextView)view.findViewById(R.id.sadmindealpromo);
			TextView expeirdate=(TextView)view.findViewById(R.id.expeirdate);
			 
			
			ItemModel itemmodel=modelList.get(position);
			
			imageview.setImageUrl(itemmodel.getRetailer_logo(), imageLoader);
			retailername.setText(itemmodel.getRetailer_name());
			dealoffer.setText(itemmodel.getOffername());
			dealprice.setText("Rs." +itemmodel.getPrice()+ "/-  OR   "+itemmodel.getPercentage()+"% OFF");
			dealpromo.setText(itemmodel.getPromotext());
			 
			deal_id.setText(itemmodel.getDeal_id());
			
			String newdate = itemmodel.getEdate().toString();
			String inputPattern = "yyyy-MM-dd HH:mm:ss";
			String outputPattern = "MMM dd";
			SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
			SimpleDateFormat  outputFormat = new SimpleDateFormat(outputPattern);

			 Date date1 = null;
			  String str = null;

			    try {
			        date1 = inputFormat.parse(newdate);
			        str = outputFormat.format(date1);
			    } catch (ParseException e) 
			    {
			        e.printStackTrace();
			    } catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    expeirdate.setText(str);
			 
			 
			
			
			return view;
		}
		
	}
	
	
	
	private class ItemModel
	{
		String deal_id;
		public String getDeal_id() {
			return deal_id;
		}
		public void setDeal_id(String deal_id) {
			this.deal_id = deal_id;
		}
		public String getRetailer_id() {
			return retailer_id;
		}
		public void setRetailer_id(String retailer_id) {
			this.retailer_id = retailer_id;
		}
		public String getRetailer_name() {
			return retailer_name;
		}
		public void setRetailer_name(String retailer_name) {
			this.retailer_name = retailer_name;
		}
		public String getRetailer_logo() {
			return retailer_logo;
		}
		public void setRetailer_logo(String retailer_logo) {
			this.retailer_logo = retailer_logo;
		}
		public String getEdate() {
			return edate;
		}
		public void setEdate(String edate) {
			this.edate = edate;
		}
		public String getOffername() {
			return offername;
		}
		public void setOffername(String offername) {
			this.offername = offername;
		}
		public String getPromotext() {
			return promotext;
		}
		public void setPromotext(String promotext) {
			this.promotext = promotext;
		}
		public String getOfferimage() {
			return offerimage;
		}
		public void setOfferimage(String offerimage) {
			this.offerimage = offerimage;
		}
		public String getOfferthumbnails() {
			return offerthumbnails;
		}
		public void setOfferthumbnails(String offerthumbnails) {
			this.offerthumbnails = offerthumbnails;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getPercentage() {
			return percentage;
		}
		public void setPercentage(String percentage) {
			this.percentage = percentage;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		String retailer_id;
		String retailer_name; 
		String retailer_logo;
		String edate;
		String offername;
		String promotext;
		String offerimage;	
		String offerthumbnails;
		String price;
		String percentage;
		String gender;
		
	}
	
	
	@Override
    public void onDestroy() 
	{
        super.onDestroy();
        dissmissDialog();
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



	public static boolean onBackPressed2() {
		// TODO Auto-generated method stub
		int c;
		  if (rootView.getVisibility() == View.VISIBLE) {
			  
			  c=1;
			  
	        	rootView.setVisibility(View.GONE);
	        } else {
	            //getActivity().finish();
	        	c=0;
	        }
		  
		  if(c==1){
	        	 return true;
	        }else{
	        	return false;
	        }
	}
}
