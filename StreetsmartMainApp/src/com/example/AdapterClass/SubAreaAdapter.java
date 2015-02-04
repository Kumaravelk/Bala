package com.example.AdapterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.AdapterClass.SubCategoryAdapter.listViewHolder;
import com.example.ItemClass.CategoryItem;
import com.example.ItemClass.CityItem;
import com.example.ItemClass.SubCityItem;
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

public class SubAreaAdapter extends BaseAdapter {

		private ArrayList arrayOfSubareaList;
	

	
	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<SubCityItem> subitemlist = null;
	
	//private ArrayList<BrandItem> arrayOfBrand;
	private ArrayList<SubCityItem> subcitylistData;

	private LayoutInflater layoutInflater;

	public SubAreaAdapter(Context context,  List<SubCityItem> subitemlist) {
		
		
		// TODO Auto-generated constructor stub
		this.arrayOfSubareaList =arrayOfSubareaList ;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		
		
		mContext = context;
		this.subitemlist = subitemlist;
		inflater = LayoutInflater.from(mContext);
		this.subcitylistData = new ArrayList<SubCityItem>();
		this.subcitylistData.addAll(subitemlist);
	//	Log.i("inside cons","inside cons");
	
	}

	public int getCount() {
	
			return subitemlist.size();

	}

	public Object getItem(int position) {

		return subitemlist.get(position);
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
				convertView = inflater.inflate(R.layout.subarealist, null);
			//	convertView = mLayoutInflater.inflate(R.layout.subarealist, null);

				holder = new listViewHolder();
				
				holder.sub_areaname = (TextView) convertView.findViewById(R.id.subareaname);
				
				holder.sub_areaid = (TextView)convertView.findViewById(R.id.subareaid);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (listViewHolder) convertView.getTag();
			}

			SubCityItem newsItem = (SubCityItem) subcitylistData.get(position);
			holder.sub_areaname.setText(subitemlist.get(position).getArea_name());
			//holder.sub_areaname.setText(newsItem.getArea_name());
			holder.sub_areaid.setText(subitemlist.get(position).getArea_id());
			
			
			return convertView;
		}

	public void filter(String charText) {
	//	Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		subitemlist.clear();
		if (charText.length() == 0) {
			subitemlist.addAll(subcitylistData);
		} 
		else 
		{
			for (SubCityItem wp : subcitylistData) 
			{
				if (wp.getArea_name().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
			//		Log.i("Sizzee  of next branditemList", "" + subitemlist.size());
					subitemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
		
	

	static class listViewHolder {
		TextView sub_areaname,sub_areaid;
		
	}
}	