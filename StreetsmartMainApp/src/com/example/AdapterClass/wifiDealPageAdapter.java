package com.example.AdapterClass;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ItemClass.wifiDealList;
import com.base2.streetsmart.*;
public class wifiDealPageAdapter extends BaseAdapter
{

private Context _context;

private LayoutInflater mLayoutInflater;
  
private ArrayList<wifiDealList> wifiOfDeals;

public wifiDealPageAdapter(Context context, ArrayList wifiOfDeals) {
	// TODO Auto-generated constructor stub
	this.wifiOfDeals =wifiOfDeals ;
	
	mLayoutInflater = LayoutInflater.from(context);
}

public int getCount() {

		return wifiOfDeals.size();

}

public Object getItem(int position) {

	return wifiOfDeals.get(position);
}

public long getItemId(int position) {

	return position;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	
	DeallistViewHolder Dealholder;


		if (convertView == null) {

			convertView = mLayoutInflater.inflate(R.layout.wifi_lsit, null);

			Dealholder = new DeallistViewHolder();
		
			Dealholder.offername = (TextView) convertView.findViewById(R.id.wifidealoffer);
			Dealholder.promotext = (TextView) convertView.findViewById(R.id.wifidealpromo);
			
			Dealholder.price = (TextView) convertView.findViewById(R.id.wifidealprice);
			
			Dealholder.dealid = (TextView)convertView.findViewById(R.id.wifideal_id);
			
			Dealholder.dealthumps = (ImageView)convertView.findViewById(R.id.wifiretailerlogo);
			
			Dealholder.retailername = (TextView)convertView.findViewById(R.id.wifiretailername);
			
			Dealholder.dealtype = (ImageView)convertView.findViewById(R.id.wifidealtype);
			
			//Dealholder.creditcard = (ImageView)convertView.findViewById(R.id.wificreditcard);
			
			Dealholder.wifidate = (TextView)convertView.findViewById(R.id.wifidate);
			
			//Dealholder.geopercentage = (TextView)convertView.findViewById(R.id.geoper);
			
			
			convertView.setTag(Dealholder);
		} 
		else 
		{
			Dealholder = (DeallistViewHolder) convertView.getTag();
		}

		wifiDealList dealitem = (wifiDealList) wifiOfDeals.get(position);
 
		Dealholder.offername.setText(dealitem.getWifidealoffername());
		Dealholder.promotext.setText(dealitem.getWifidealpromo());
		Dealholder.retailername.setText(dealitem.getWifidealretailername());
		Dealholder.price.setText("Rs."+dealitem.getWifidealPrices()+"/-  OR   "+dealitem.getWifipercentage()+"% OFF");
		Dealholder.dealid.setText(dealitem.getWifidealdealid());
		Dealholder.wifidate.setText(dealitem.getWifiDate());
		//Dealholder.geopercentage.setText(dealitem.getGeopercentage()+"% OFF");
		
		String newdate = dealitem.getWifiDate().toString();
		
	
		
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "MMM dd";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat  outputFormat = new SimpleDateFormat(outputPattern);

		 Date date = null;
		  String str = null;

		    try {
		        date = inputFormat.parse(newdate);
		        str = outputFormat.format(date);
		    } catch (ParseException e) 
		    {
		        e.printStackTrace();
		    } catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		   
		
		/* SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd HH:MM:SS");
	        
		 Date testDate = null;
	        
	        try {
	            testDate = sdf.parse(newdate);
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
	      
	        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
	       
	        String newFormat = formatter.format(testDate);*/
	        
	        Dealholder.wifidate.setText(str);
		
		String geo_dealtype = dealitem.getWifidealType().toString();
		
		//String geo_creditcard = dealitem.getGeodealcredit().toString();
		
		if(geo_dealtype.equalsIgnoreCase("4"))
		{
			Dealholder.dealtype.setVisibility(View.VISIBLE);
		}
		else
		{
			Dealholder.dealtype.setVisibility(View.INVISIBLE);
		}
		
		/*if(geo_creditcard.equalsIgnoreCase("1"))
		{
			Dealholder.creditcard.setVisibility(View.VISIBLE);
		}
		else
		{
			Dealholder.creditcard.setVisibility(View.INVISIBLE);
		}*/
		
		//Log.i("Image URL ",""+dealitem.getOfferimage());

		if (dealitem.getWifidealre_listimage()!= null) {
			
			
			
			Dealholder.dealthumps.setImageBitmap(dealitem.getWifidealre_listimage());
		} else {
			
			new ImageDownloaderTaskDeal().execute(position);

		}

		return convertView;
	}

public class DeallistViewHolder {
	TextView offername,promotext,price,percentage,gender,dealid,retailername,wifidate,wifipercentage;
	ImageView dealthumps,dealtype,creditcard;
}


public class ImageDownloaderTaskDeal extends
		AsyncTask<Integer, Void, Bitmap> {

	Bitmap image;

	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(Integer... params) {
		// params comes from the execute() call: params[0] is the url.

		int pos = params[0];
		
		/*Log.i("arrayOfDeals.get(pos).getOfferimage()", pos
				+ "arrayOfDeals.get(pos).getOfferimage(): "
				+ arrayOfDeals.get(pos).getGeodealofferimage());*/
		image = downloadBitmap(wifiOfDeals.get(pos).getWifidealofferimage());
		
		
		wifiOfDeals.get(pos).setWifidealre_listimage(image);

		return image;
	}

	Bitmap downloadBitmap(String url) {

		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);

					return bitmap;
				}

				finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException
			// or
			// IllegalStateException
			getRequest.abort();
			
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) {
		notifyDataSetChanged();
		//notifyDataSetInvalidated();
	}
}


}
