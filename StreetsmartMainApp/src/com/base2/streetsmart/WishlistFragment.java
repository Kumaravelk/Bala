package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
 
import com.example.Session.SessionManager;
import com.google.analytics.tracking.android.EasyTracker;
 

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WishlistFragment extends Fragment {
	
	private String TAG=WishlistFragment.class.getSimpleName();
	private ListView listview;
	private ProgressDialog pdialog;
	private listviewAdapter adapter;
	private List<ItemModel> modelList=new ArrayList<ItemModel>();
	private String URL="http://54.169.81.215/streetsmartadmin4/shopping/displaywishlist/3";
	
	
	 
	
	String Dealid ;

	Context context;
	String str, page, name;
	 
	ProgressDialog pDialog;
	static View rootView;
    ImageView noimage;
	 
    SessionManager session;
	String suserid,sname,semail,smobile,scityid,scityname,scountry,sgeonotify,spushnotify,semailnotify;
	 
	public WishlistFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.wishlist_fragment, container, false);
        noimage = (ImageView) rootView.findViewById(R.id.noimage);

        session = new SessionManager(getActivity().getApplicationContext());
        
        getActivity().onBackPressed();
        
        
        
       /* FragmentManager fm = getFragmentManager();
        
        fm.addOnBackStackChangedListener(new OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Back", Toast.LENGTH_LONG).show();
			}
		});
		  */
		  HashMap<String, String> user = session.getUserDetails();
	        
	        suserid = user.get(SessionManager. KEY_USERID);
	        sname = user.get(SessionManager.KEY_NAME);
	        semail = user.get(SessionManager.KEY_EMAIL);
	        smobile = user.get(SessionManager.KEY_MOBILE);
	        scityid = user.get(SessionManager.KEY_CITYID);
	        scityname = user.get(SessionManager.KEY_CITYNAME);
	        scountry = user.get(SessionManager.KEY_COUNTRY);
	        sgeonotify = user.get(SessionManager.KEY_GEO_NOTIFI);
	        spushnotify = user.get(SessionManager.KEY_PUSH_NOTIFI);
	        semailnotify = user.get(SessionManager.KEY_EMAIL_NOTIFI);
        
	
        listview=(ListView) rootView.findViewById(R.id.wishlist);
        adapter=new listviewAdapter();
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {
				Dealid = ((TextView) v.findViewById(R.id.wishdealid))
						.getText().toString();
				
				//Toast.makeText(getActivity(), Dealid, Toast.LENGTH_SHORT).show();

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
		pdialog.setMessage("Loading your Wishlist");
		pdialog.show();
		JsonArrayRequest arrayReq=new JsonArrayRequest("http://54.169.81.215/streetsmartadmin4/shopping/displaywishlist/"+suserid, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				// TODO Auto-generated method stub
				dissmissDialog();
				for(int i=0;i<response.length();i++){
					try {
						JSONObject obj=response.getJSONObject(i);
						ItemModel model=new ItemModel();
						model.setWish_image(obj.getString("deal_images"));
						model.setWish_name(obj.getString("deal_name"));
						model.setWish_updatedate(obj.getString("updatedate"));
						model.setWish_id(obj.getString("deal_id"));
						modelList.add(model);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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

				adapter.notifyDataSetChanged();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				VolleyLog.d(TAG, "ERROR"+error.getMessage());
			}
		});
		 AppController.getInstance().addToRequestQueue(arrayReq);
		 
		 
		 
	
        return rootView;
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
	private class listviewAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return modelList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return modelList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(inflater ==null)
				inflater=(LayoutInflater) getActivity().getLayoutInflater();
			if(view==null)
				view=inflater.inflate(R.layout.wishlist, null);
			if(imageLoader == null)
				 imageLoader = AppController.getInstance().getImageLoader();
			NetworkImageView imageview=(NetworkImageView)  view.findViewById(R.id.wishimage);
			TextView dealname=(TextView)view.findViewById(R.id.wishname);
			TextView date=(TextView)view.findViewById(R.id.date);
			TextView deal_id=(TextView)view.findViewById(R.id.wishdealid);
			
			
			
			ItemModel itemmodel=modelList.get(position);
			imageview.setImageUrl(itemmodel.getWish_image(), imageLoader);
			dealname.setText(itemmodel.getWish_name());
			deal_id.setText(itemmodel.getWish_id());
		
			String newdate = itemmodel.getWish_updatedate().toString();
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
			    date.setText(str);
			return view;
		}
		
	}
	private class ItemModel{
		private String wish_id,wish_name,wish_image,wish_updatedate;

		public String getWish_id() {
			return wish_id;
		}

		public void setWish_id(String wish_id) {
			this.wish_id = wish_id;
		}

		public String getWish_name() {
			return wish_name;
		}

		public void setWish_name(String wish_name) {
			this.wish_name = wish_name;
		}

		public String getWish_image() {
			return wish_image;
		}

		public void setWish_image(String wish_image) {
			this.wish_image = wish_image;
		}

		public String getWish_updatedate() {
			return wish_updatedate;
		}

		public void setWish_updatedate(String wish_updatedate) {
			this.wish_updatedate = wish_updatedate;
		}
		
	}

	@Override
    public void onDestroy() {
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

	 
	  
	  public static boolean onBackPressed4() {
		  
		  int c;
		  
	        if (rootView.getVisibility() == View.VISIBLE) {
	        	
	        	c=1;
	        	rootView.setVisibility(View.GONE);
	        } else {
	         
	        	c=0;
	        	//getActivity().finish();
	        }
	 
		//  Toast.makeText(, text, duration)
	        if(c==1){
	        	 return true;
	        }else{
	        	return false;
	        }
	       
	    }
	 
	  
	  
	  
}

	
	
	

