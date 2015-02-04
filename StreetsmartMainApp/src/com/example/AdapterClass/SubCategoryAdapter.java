package com.example.AdapterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.ItemClass.CategoryItem;
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

public class SubCategoryAdapter extends BaseAdapter {

	
	private ArrayList arrayOfSubcateList;
	
private ArrayList arrayOfList;
	
	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<CategoryItem> subcatitemlist = null;
	
	//private ArrayList<BrandItem> arrayOfBrand;
	private ArrayList<CategoryItem> subcatlistData;

	private LayoutInflater layoutInflater;

	public SubCategoryAdapter(Context context, List<CategoryItem> subcatitemlist) {
		// TODO Auto-generated constructor stub
		//this.arrayOfSubcateList =arrayOfSubcateList ;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		
		

		
		
		
		mContext = context;
		this.subcatitemlist =subcatitemlist ;
		inflater = LayoutInflater.from(mContext);
		this.subcatlistData = new ArrayList<CategoryItem>();
		this.subcatlistData.addAll(subcatitemlist);
	//	Log.i("inside cons","inside cons");
		
	}

	public int getCount() {
	
			return subcatitemlist.size();

	}

	public Object getItem(int position) {

		return subcatitemlist.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		listViewHolder holder;
	

			if (convertView == null) {
			//	Log.i("inside view","inside view");
				convertView = inflater.inflate(R.layout.subcategorylist, null);
				//convertView = mLayoutInflater.inflate(R.layout.subcategorylist, null);

				holder = new listViewHolder();
				
				holder.subcategory = (TextView) convertView.findViewById(R.id.subcatename);
				
				holder.subcatid = (TextView)convertView.findViewById(R.id.subcateid);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (listViewHolder) convertView.getTag();
			}

			CategoryItem newsItem = (CategoryItem) subcatitemlist.get(position);
			int subcateid = newsItem.getSubcateid();
			String subcid = String.valueOf(subcateid);		
			
holder.subcategory.setText(subcatitemlist.get(position).getSubcatname());
			
			holder.subcatid.setText(subcid);
			
			
			/*holder.subcategory.setText(newsItem.getSubcatname());
			holder.subcatid.setText(subcid);*/
			return convertView;
		}
	
	public void filter(String charText) {
	//	Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		subcatitemlist.clear();
		if (charText.length() == 0) {
			subcatitemlist.addAll(subcatlistData);
		} 
		else 
		{
			for (CategoryItem wp : subcatlistData) 
			{
				if (wp.getSubcatname().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
				//	Log.i("Sizzee  of next branditemList", "" +subcatitemlist.size());
					subcatitemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
		

	static class listViewHolder {
		TextView subcategory,subcatid;
		ImageView icon;
	}
}	