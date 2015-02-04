package com.example.AdapterClass;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.example.ItemClass.NearMeItem;
import com.base2.streetsmart.*;

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

public class NearMeAdapter extends BaseAdapter {

	private Context _context;

	private LayoutInflater mLayoutInflater;

	private ArrayList<NearMeItem> arrayOfnearDeal;

	public NearMeAdapter(Context context, ArrayList arrayOfnearDeal) {
		// TODO Auto-generated constructor stub
		this.arrayOfnearDeal = arrayOfnearDeal;

		mLayoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {

		return arrayOfnearDeal.size();

	}

	public Object getItem(int position) {

		return arrayOfnearDeal.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		listViewHolder holder;

		if (convertView == null) {

			convertView = mLayoutInflater.inflate(R.layout.nearmeitem, null);

			holder = new listViewHolder();

			holder.dealoffer = (TextView) convertView
					.findViewById(R.id.neardealoffer);

			holder.dealpromo = (TextView) convertView
					.findViewById(R.id.neardealpromo);

			holder.price = (TextView) convertView
					.findViewById(R.id.neardealprice);

			holder.neardate = (TextView) convertView
					.findViewById(R.id.neardate);

			holder.nearretailer = (TextView) convertView
					.findViewById(R.id.nearretailername);

			holder.dealimage = (ImageView) convertView
					.findViewById(R.id.nearretailerlogo);
			// holder.dealthump =
			// (ImageView)convertView.findViewById(R.id.neardealthump);

			convertView.setTag(holder);
		} else {
			holder = (listViewHolder) convertView.getTag();
		}

		NearMeItem nearItem = (NearMeItem) arrayOfnearDeal.get(position);

		// holder.catid.setText(newsItem.getCategoryid());

		// Log.i("namecate",""+ newsItem.getCatname());

		holder.nearretailer.setText(nearItem.getNearretailer_name());
		holder.dealoffer.setText(nearItem.getNearoffername());
		holder.dealpromo.setText(nearItem.getNearpromotext());
		holder.price.setText("Rs." + nearItem.getNearprice() + "/-  OR   "
				+ nearItem.getNearpercentage() + "% OFF");

		String neardates = nearItem.getNeardate().toString();

	//	Log.i("Date", neardates);

		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "MMM dd";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(neardates);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.neardate.setText(str);

		if (nearItem.getNearlist_image() != null) {

			holder.dealimage.setImageBitmap(nearItem.getNearlist_image());
		} else {
			new ImageDownloaderTaskDeal().execute(position);
		}

		/*
		 * if (holder.dealimage != null) {
		 * 
		 * Log.i("checking1","checking1"); new
		 * ImageDownloaderTask(holder.dealimage
		 * ).execute(nearItem.getNearofferthumbnails());
		 * //Log.i("checking2","checking2"); } if (holder.dealthump != null) {
		 * Log.i("checking1111","checking1"); new
		 * ImageDownloaderTask(holder.dealthump
		 * ).execute(nearItem.getNearofferimage());
		 * //Log.i("checking2","checking2"); }
		 */

		return convertView;
	}

	static class listViewHolder {
		TextView dealoffer, dealpromo, price, neardate, nearretailer;
		ImageView dealimage, dealthump;
	}

	public class ImageDownloaderTaskDeal extends
			AsyncTask<Integer, Void, Bitmap> {

		Bitmap image;

		@Override
		// Actual download method, run in the task thread
		protected Bitmap doInBackground(Integer... params) {
			// params comes from the execute() call: params[0] is the url.

			int pos = params[0];

	//		Log.i("arrayOfDeals.get(pos).getOfferimage()", pos
	//				+ "arrayOfDeals.get(pos).getOfferimage(): "
	//				+ arrayOfnearDeal.get(pos).getNearofferimage());
		
			image = downloadBitmap(arrayOfnearDeal.get(pos).getNearofferimage());

			arrayOfnearDeal.get(pos).setNearlist_image(image);

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
		//			Log.w("ImageDownloader", "Error " + statusCode
		//					+ " while retrieving bitmap from " + url);
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
	//			Log.w("ImageDownloader", "Error while retrieving bitmap from "
	//					+ url);
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
			// notifyDataSetInvalidated();
		}
	}

}