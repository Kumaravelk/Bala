package com.base2.streetsmart;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;


import com.base2.streetsmart.R;
import com.example.ItemClass.MallDealList;
 

import android.content.Context;
import android.database.DataSetObserver;
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
import android.widget.ListAdapter;
import android.widget.TextView;

public class MallsAdapter extends BaseAdapter {
	
	
	private Context _context;

	private LayoutInflater mLayoutInflater;
      
	private ArrayList<MallDealList> arrayOfmallDeals;
	
	public MallsAdapter(Context context, ArrayList arrayOfDeals) {
		// TODO Auto-generated constructor stub
		this.arrayOfmallDeals =arrayOfDeals ;
		
		mLayoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
	
			return arrayOfmallDeals.size();

	}

	public Object getItem(int position) {

		return arrayOfmallDeals.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		DeallistViewHolder Dealholder;
	

			if (convertView == null) {

				convertView = mLayoutInflater.inflate(R.layout.mall_list, null);

				Dealholder = new DeallistViewHolder();
				Dealholder.malloffername = (TextView) convertView.findViewById(R.id.malldealoffer);
				
				Dealholder.mallpromotext = (TextView) convertView.findViewById(R.id.malldealpromo);
			
				Dealholder.mallretailername = (TextView) convertView.findViewById(R.id.mallretailername);
		
				Dealholder.mallprice = (TextView) convertView.findViewById(R.id.malldealprice);
				
				Dealholder.malldealid = (TextView)convertView.findViewById(R.id.malldeal_id);
				
				Dealholder.mallretailerlogo = (ImageView)convertView.findViewById(R.id.mallretailerlogo);
				
                Dealholder.dealtype = (ImageView)convertView.findViewById(R.id.malldealtype);
				
				Dealholder.creditcard = (ImageView)convertView.findViewById(R.id.mallcreditcard);
				
				Dealholder.malldate = (TextView)convertView.findViewById(R.id.malldate);
				
				//Dealholder.mallpercentage = (TextView)convertView.findViewById(R.id.mallper);
				
				convertView.setTag(Dealholder);
			} 
			else 
			{
				Dealholder = (DeallistViewHolder) convertView.getTag();
			}

			MallDealList malldealitem = (MallDealList) arrayOfmallDeals.get(position);
	 
			Dealholder.malloffername.setText(malldealitem.getMalldealoffername());
			Dealholder.mallpromotext.setText(malldealitem.getMalldealpromo());
			Dealholder.mallretailername.setText(malldealitem.getMalldealretailername());
			Dealholder.mallprice.setText("Rs. "+malldealitem.getMalldealPrices()+"/-  OR   "+malldealitem.getMallpercentage()+"% OFF");
			Dealholder.malldealid.setText(malldealitem.getMalldealid());
			//Dealholder.mallpercentage.setText(malldealitem.getMallpercentage()+"% OFF");
			
			
            String newdate = malldealitem.getMalldate().toString();
			
		
			
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
			    
			    Dealholder.malldate.setText(str);
			
           String mall_dealtype = malldealitem.getMalldealtype().toString();
			
			String mall_creditcard = malldealitem.getMallcredit().toString();
			
			if(mall_dealtype.equalsIgnoreCase("4"))
			{
				Dealholder.dealtype.setVisibility(View.VISIBLE);
			}
			else
			{
				Dealholder.dealtype.setVisibility(View.INVISIBLE);
			}
			
			if(mall_creditcard.equalsIgnoreCase("1"))
			{
				Dealholder.creditcard.setVisibility(View.VISIBLE);
			}
			else
			{
				Dealholder.creditcard.setVisibility(View.INVISIBLE);
			}
			
			
			//Log.i("Image URL ",""+dealitem.getOfferimage());


			if (malldealitem.getMalldealre_listimage() != null) {
						
				Dealholder.mallretailerlogo.setImageBitmap(malldealitem.getMalldealre_listimage());
					} else {
						new ImageDownloaderTaskDeal().execute(position);
			
					}

			
			
						
	 
			return convertView;
		}

	public class DeallistViewHolder {
		TextView malloffername,mallpromotext,mallprice,mallpercentage,mallretailername,malldealid,malldate;
		ImageView mallretailerlogo,malldealthumps,dealtype,creditcard;
	}
	
	
	public class ImageDownloaderTaskDeal extends
	AsyncTask<Integer, Void, Bitmap> {

Bitmap image;

@Override
// Actual download method, run in the task thread
protected Bitmap doInBackground(Integer... params) {
	// params comes from the execute() call: params[0] is the url.

	int pos = params[0];
	
	
	image = downloadBitmap(arrayOfmallDeals.get(pos).getMalldealofferimage());
	
	
	arrayOfmallDeals.get(pos).setMalldealre_listimage(image);

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
		/*	Log.w("ImageDownloader", "Error " + statusCode
					+ " while retrieving bitmap from " +url);*/
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
