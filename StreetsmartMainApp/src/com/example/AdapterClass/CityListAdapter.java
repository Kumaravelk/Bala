package com.example.AdapterClass;

 
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.example.ItemClass.BrandItem;
import com.example.ItemClass.CityItem;
import com.base2.streetsmart.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class CityListAdapter extends BaseAdapter {
	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<CityItem> cityitemlist = null;
	
	//private ArrayList<BrandItem> arrayOfBrand;
	private ArrayList<CityItem> CitylistData;

	private LayoutInflater layoutInflater;

	public CityListAdapter(Context context,List<CityItem> cityitemlist) {
		this.CitylistData = CitylistData;
		layoutInflater = LayoutInflater.from(context);
		
		
		mContext = context;
		this.cityitemlist = cityitemlist;
		inflater = LayoutInflater.from(mContext);
		this.CitylistData = new ArrayList<CityItem>();
		this.CitylistData.addAll(cityitemlist);
		//Log.i("inside cons","inside cons");
	}

	@Override
	public int getCount() {
		return cityitemlist.size();
	}

	@Override
	public Object getItem(int position) {
		return cityitemlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		listViewHolder holder;
		if (convertView == null) {
		//	Log.i("inside view","inside view");
			convertView = inflater.inflate(R.layout.arealist, null);
			//convertView = layoutInflater.inflate(R.layout.arealist, null);
			holder = new listViewHolder();
			 
			holder.cityname = (TextView) convertView.findViewById(R.id.cityname);
			holder.cityid  = (TextView) convertView.findViewById(R.id.cityid);
			convertView.setTag(holder);
		} else {
			holder = (listViewHolder) convertView.getTag();
		}

		CityItem bigDealListItem = (CityItem) CitylistData.get(position);
	//	holder.cityname.setText(bigDealListItem.getCityname());
		holder.cityid.setText(cityitemlist.get(position).getCityid());
		holder.cityname.setText(cityitemlist.get(position).getCityname());
		return convertView;
	}
	public void filter(String charText) {
	//	Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		cityitemlist.clear();
		if (charText.length() == 0) {
			cityitemlist.addAll(CitylistData);
		} 
		else 
		{
			for (CityItem wp : CitylistData) 
			{
				if (wp.getCityname().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
				//	Log.i("Sizzee  of next branditemList", "" + cityitemlist.size());
					cityitemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	

	public class listViewHolder 
	{
		TextView cityname, cityid, countryid;
	}

}