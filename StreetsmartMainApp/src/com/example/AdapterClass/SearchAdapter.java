package com.example.AdapterClass;

import java.io.IOException;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.base2.streetsmart.*;



//import com.example.categorylist.ImageDownloaderTask;

//import com.example.categorylist.ImageDownloaderTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	
	
	private Context _context;

	private LayoutInflater mLayoutInflater;
      
	private ArrayList<Item> arrayOfList;
	


	public SearchAdapter(Context context, ArrayList arrayOfList) {
		// TODO Auto-generated constructor stub
		this.arrayOfList =arrayOfList ;
		
		mLayoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
	
			return arrayOfList.size();

	}

	public Object getItem(int position) {

		return arrayOfList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		listViewHolder holder;
	

			if (convertView == null) {

				convertView = mLayoutInflater.inflate(R.layout.searchlistitem, null);

				holder = new listViewHolder();
				
				holder.searchoffername = (TextView) convertView.findViewById(R.id.searchdealoffer);
				holder.searchpromotext = (TextView) convertView.findViewById(R.id.searchdealpromo);
				
				holder.searchprice = (TextView) convertView.findViewById(R.id.searchdealprice);
				
				holder.searchdealid = (TextView)convertView.findViewById(R.id.searchdeal_id);
				
				holder.searchretailer = (ImageView)convertView.findViewById(R.id.searchretailer);
				
				holder.searchretailername = (TextView)convertView.findViewById(R.id.searchretailername);
				
				holder.dealtype = (ImageView)convertView.findViewById(R.id.searchdealtype);
				
				holder.creditcard = (ImageView)convertView.findViewById(R.id.searchcreditcard);
				
				holder.searchdate = (TextView)convertView.findViewById(R.id.searchdate);

				convertView.setTag(holder);
			} 
			else 
			{
				holder = (listViewHolder) convertView.getTag();
			}

			Item newsItem = (Item) arrayOfList.get(position);
	 
			holder.searchdealid.setText(newsItem.getSearchdealid());
			holder.searchoffername.setText(newsItem.getSearchoffer());
			holder.searchpromotext.setText(newsItem.getSearchpromo());
			holder.searchprice.setText("Rs."+newsItem.getSearchprice()+"/-  OR   "+newsItem.getSearchpercentage()+"% OFF");
			holder.searchretailername.setText(newsItem.getSearchretailer());
			
			
			String searcdate = newsItem.getSearchdate().toString();
			
			
			String inputPattern = "yyyy-MM-dd HH:mm:ss";
			String outputPattern = "MMM dd";
			SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
			SimpleDateFormat  outputFormat = new SimpleDateFormat(outputPattern);

			 Date date = null;
			  String str = null;

			    try {
			        date = inputFormat.parse(searcdate);
			        str = outputFormat.format(date);
			    } catch (ParseException e) 
			    {
			        e.printStackTrace();
			    } catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			holder.searchdate.setText(str);
			
               String search_dealtype = newsItem.getSearchdealtype().toString();
			
			String search_creditcard = newsItem.getSearchcreditcard().toString();
			
			if(search_dealtype.equalsIgnoreCase("4"))
			{
				holder.dealtype.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.dealtype.setVisibility(View.INVISIBLE);
			}
			
			if(search_creditcard.equalsIgnoreCase("1"))
			{
			      holder.creditcard.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.creditcard.setVisibility(View.INVISIBLE);
			}
			
                  if (newsItem.getSearch_retailerimage()!= null) {
				
				
				
			         holder.searchretailer.setImageBitmap(newsItem.getSearch_retailerimage());
			} else {
				
				new ImageDownloaderTaskDeal().execute(position);

			}
		
						return convertView;
		}

		
		
	

	static class listViewHolder {
		TextView searchoffername,searchpromotext,searchprice,searchpercentage,searchdealid,searchretailername,searchdate;
		ImageView searchretailer,dealtype,creditcard;
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
	image = downloadBitmap(arrayOfList.get(pos).getSearchretailerlogo());
	
	
	arrayOfList.get(pos).setSearch_retailerimage(image);

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
	