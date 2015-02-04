package com.example.AdapterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.ItemClass.BrandItem;
import com.example.ItemClass.RetailerItem;
import com.base2.streetsmart.*;

 

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class RetailerListAdapter extends BaseAdapter {
	
	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<RetailerItem> retaileritemlist = null;
	//private ArrayList<branditem> arraylist;
	//private ArrayList<BrandItem> arrayOfBrand;
	

	//private LayoutInflater mLayoutInflater;
      
	private ArrayList <RetailerItem>arrayOfRetailer;
	


	public RetailerListAdapter(Context context, List<RetailerItem> retaileritemlist) {
		// TODO Auto-generated constructor stub
		this. retaileritemlist = retaileritemlist;
		
		//mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		//this.branditemlist = branditemlist;
		inflater = LayoutInflater.from(mContext);
		this.arrayOfRetailer= new ArrayList<RetailerItem>();
		this.arrayOfRetailer.addAll(retaileritemlist);
		//Log.i("inside cons","inside cons");
	}

	public int getCount() {
	
			return retaileritemlist.size();

	}

	public Object getItem(int position) {

		return retaileritemlist.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		RetailerlistViewHolder retailerholder;
	

			if (convertView == null) {
			//	Log.i("inside view","inside view");
				convertView = inflater.inflate(R.layout.retaileritem, null);
			//	convertView = mLayoutInflater.inflate(R.layout.retaileritem, null);

				retailerholder = new RetailerlistViewHolder();
				retailerholder.retailername = (TextView) convertView.findViewById(R.id.retailername);
				retailerholder.retailerid = (TextView) convertView.findViewById(R.id.retailerid);
				
				//retailerholder.retailericon = (ImageView)convertView.findViewById(R.id.retailericon);
				
				convertView.setTag(retailerholder);
			} 
			else 
			{
				retailerholder = (RetailerlistViewHolder) convertView.getTag();
			}

			RetailerItem retaileritem = (RetailerItem) arrayOfRetailer.get(position);
			retailerholder.retailername.setText(retaileritemlist.get(position).getRetailername());
			retailerholder.retailerid.setText(retaileritemlist.get(position).getRetailerid());
			//retailerholder.retailername.setText(retaileritem.getRetailername());
			
			

			

			/*if (retailerholder.retailericon != null) 
			{
				//Log.i("checking1","checking1");
				new ImageDownloaderTask(retailerholder.retailericon).execute(retaileritem.getRetailericon());
				//Log.i("checking2","checking2");
			}*/
			
	 
			return convertView;
		}

		
	public void filter(String charText) {
	//	Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		retaileritemlist.clear();
		if (charText.length() == 0) {
			retaileritemlist.addAll(arrayOfRetailer);
		} 
		else 
		{
			for (RetailerItem wp : arrayOfRetailer) 
			{
				if (wp.getRetailername().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
				//	Log.i("Sizzee  of next branditemList", "" + retaileritemlist.size());
			retaileritemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	

	static class RetailerlistViewHolder {
		TextView retailername,retailerid;
		ImageView retailericon;
	}
}
