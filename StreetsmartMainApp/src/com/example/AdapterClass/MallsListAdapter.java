package com.example.AdapterClass;

import java.io.IOException;
 

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.example.ItemClass.CityItem;
import com.example.ItemClass.MallsItem;
import com.base2.streetsmart.*;

 

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MallsListAdapter extends BaseAdapter {
	
	
      
	private ArrayList arrayOfList;
	
	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<MallsItem> mallitemlist = null;
	
	//private ArrayList<BrandItem> arrayOfBrand;
	private ArrayList<MallsItem> MallslistData;

	private LayoutInflater layoutInflater;


	public MallsListAdapter(Context context, List<MallsItem> mallitemlist ) {
		// TODO Auto-generated constructor stub
	
		
		mLayoutInflater = LayoutInflater.from(context);
		
		
		
		mContext = context;
		this.mallitemlist =mallitemlist ;
		inflater = LayoutInflater.from(mContext);
		this.MallslistData = new ArrayList<MallsItem>();
		this.MallslistData.addAll(mallitemlist);
	//	Log.i("inside cons","inside cons");
	}

	public int getCount() {
	
			return mallitemlist.size();

	}

	public Object getItem(int position) {

		return mallitemlist.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		listViewHolder holder;
	

			if (convertView == null) {
				
		//		Log.i("inside view","inside view");
				convertView = inflater.inflate(R.layout.mallslist, null);

			//	convertView = mLayoutInflater.inflate(R.layout.mallslist, null);

				holder = new listViewHolder();
				
				holder.category = (TextView) convertView.findViewById(R.id.textmallname);
				holder.catid = (TextView) convertView.findViewById(R.id.textmallid);
				
				//holder.icon = (ImageView)convertView.findViewById(R.id.icon);
				
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (listViewHolder) convertView.getTag();
			}

         MallsItem newsItem = (MallsItem) MallslistData.get(position);
	 
			//holder.category.setText(newsItem.getMall_name());
			holder.catid.setText(mallitemlist.get(position).getMall_id());
			
			holder.category.setText(mallitemlist.get(position).getMall_name());
			
			//holder.category.setVisibility(4);
		
			
	 
			return convertView;
		}

	public void filter(String charText) {
//		Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		mallitemlist.clear();
		if (charText.length() == 0) {
			mallitemlist.addAll(MallslistData);
		} 
		else 
		{
			for (MallsItem wp : MallslistData) 
			{
				if (wp.getMall_name().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
			//		Log.i("Sizzee  of next branditemList", "" +mallitemlist.size());
					mallitemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
		
	

	static class listViewHolder {
		TextView category,catid;
		ImageView icon;
	}
}
	