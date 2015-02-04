package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import java.util.ArrayList;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Session.SessionManager;
 
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
 

public class RetailerVolleyActivity extends Activity {
	
	String Retailerid,Retailername = "ALL RETAILERS";
	SessionManager session;
	private String TAG=RetailerVolleyActivity.class.getSimpleName();
	private ListView listview;
	private ProgressDialog pdialog;
	private listviewAdapter adapter;
	private List<ItemModel> modelList=new ArrayList<ItemModel>();
	private String URL= COMM_URL+"/streetsmartadmin4/shopping/listretailer";
	String scityid,sareaid;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_volley);	
		
		 ActionBar actionBar = getActionBar();
		  actionBar.setTitle("");
		  actionBar.hide();
		
		 session = new SessionManager(getApplicationContext());
		 HashMap<String, String> user = session.getUserDetails();
		 Retailerid = user.get(SessionManager. KEY_RETALIERID);
		 
		 
		 
		 scityid =user.get(SessionManager.KEY_CITYID); 
		 sareaid =user.get(SessionManager.KEY_AREAID); 
		 
	
		listview=(ListView)findViewById(R.id.retailerlist);
		adapter=new listviewAdapter();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Retailerid = ((TextView) arg1.findViewById(R.id.retailerid))
						.getText().toString();

				Retailername = ((TextView) arg1.findViewById(R.id.retailername))
						.getText().toString();
				
				//Toast.makeText(getApplicationContext(),Retailerid+""+Retailername , Toast.LENGTH_LONG).show();
				session.createBrand(Retailerid,Retailername);
				Intent intent = new Intent();
				intent.putExtra("Retailerid", Retailerid);
				intent.putExtra("Retailername", Retailername);

				setResult(2, intent);
				
				finish();
			}

		   });
		
		
		pdialog=new ProgressDialog(this);
		pdialog.setMessage("Loading data..");
		pdialog.show();
		RequestQueue rq = Volley.newRequestQueue(this);
		/*
		 * JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
		 * url, null, new Response.Listener<JSONObject>() {
		 */
		StringRequest postReq = new StringRequest(Request.Method.POST,
				"http://54.169.81.215/streetsmartadmin4/shopping/listretailer",
				new Response.Listener<String>() {
					public void onResponse(String response) {
						dissmissDialog();
						parseJSON(response);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println("Error [" + error + "]");

					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				HashMap<String, String> headers = new HashMap<String, String>();
				// Map<String, String> params = new HashMap<String, String>();
				headers.put("city_id", scityid);
				headers.put("area_id", sareaid);

				return headers;
			}

		};

		// rq.add(postReq);

		AppController.getInstance().addToRequestQueue(postReq);

	}

	private void parseJSON(String response) {
		// JSONObject jrootobj = new JSONObject(response);

		try {
			JSONArray jsonArray = new JSONArray(response);

			for (int i = 0; i < jsonArray.length(); i++)

			{
				JSONObject jArray = (JSONObject) jsonArray.getJSONObject(i);

				 

					ItemModel nm = new ItemModel();
					nm.setRetailer_id(jArray.optString("retailer_id"));
					nm.setRetailer_name(jArray.optString("retailer_name"));
					nm.setLogo_image(jArray.optString("logo_image"));
					modelList.add(nm);

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private void dissmissDialog() {
		// TODO Auto-generated method stub
		if (pdialog != null) {
			if (pdialog.isShowing()) {
				pdialog.dismiss();
			}
			pdialog = null;
		}
	}
	
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

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(inflater ==null)
				inflater=(LayoutInflater)getLayoutInflater();
			if(view==null)
				view=inflater.inflate(R.layout.retaileritem, null);
			 
			NetworkImageView imageview=(NetworkImageView)view.findViewById(R.id.retailericon);
			TextView brandname=(TextView)view.findViewById(R.id.retailername);
			TextView brandid=(TextView)view.findViewById(R.id.retailerid);
			
			ItemModel itemmodel=modelList.get(position);
			imageview.setImageUrl(itemmodel.getLogo_image(), imageLoader);
			brandname.setText(itemmodel.getRetailer_name());
			brandid.setText(itemmodel.getRetailer_id());
			return view;
		}
		
	}
	
	
	private class ItemModel
	{
		private String retailer_id,retailer_name,logo_image;

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

		public String getLogo_image() {
			return logo_image;
		}

		public void setLogo_image(String logo_image) 
		{
			this.logo_image = logo_image;
		}

	}
	
	
	@Override
    public void onDestroy() 
	{
        super.onDestroy();
        dissmissDialog();
    }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		  Intent intent=new Intent();
	        intent.putExtra("Retailerid",Retailerid);
	        intent.putExtra("Retailername",Retailername);
	        
	        setResult(2,intent);
	        finish();
	}
	
}
