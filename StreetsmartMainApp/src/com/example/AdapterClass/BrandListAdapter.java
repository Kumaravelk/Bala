package com.example.AdapterClass;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import com.example.ItemClass.BrandItem;
//import com.example.searchlistview.branditem;
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

public class BrandListAdapter extends BaseAdapter {

	private Context _context;
	Context mContext;
	LayoutInflater inflater;
	private LayoutInflater mLayoutInflater;
	private List<BrandItem> branditemlist = null;
	//private ArrayList<branditem> arraylist;
	private ArrayList<BrandItem> arrayOfBrand;

	public BrandListAdapter(Context context,List<BrandItem> branditemlist) {
		// TODO Auto-generated constructor stub
		//this.arrayOfBrand = arrayOfBrand;

		//mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		this.branditemlist = branditemlist;
		inflater = LayoutInflater.from(mContext);
		this.arrayOfBrand = new ArrayList<BrandItem>();
		this.arrayOfBrand.addAll(branditemlist);
		//Log.i("inside cons","inside cons");
	}

	public int getCount() {

		return branditemlist.size();

	}

	public Object getItem(int position) {

		return branditemlist.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		BrandlistViewHolder brandholder;

		if (convertView == null) {
			//Log.i("inside view","inside view");
			convertView = inflater.inflate(R.layout.branditem, null);
		//	view = inflater.inflate(R.layout.listview_item, null);
			brandholder = new BrandlistViewHolder();
			brandholder.brandname = (TextView) convertView
					.findViewById(R.id.brandname);
			brandholder.brandid = (TextView) convertView
					.findViewById(R.id.brandid);

			//brandholder.brandicon = (ImageView) convertView.findViewById(R.id.brandicon);

			convertView.setTag(brandholder);
		} else {
			brandholder = (BrandlistViewHolder) convertView.getTag();
		}

		BrandItem branditem = (BrandItem) arrayOfBrand.get(position);
		brandholder.brandname.setText(branditemlist.get(position).getBrandname());
        
		int brand_id = branditemlist.get(position).getBrandid();
		
		brandholder.brandid.setText(""+brand_id);
		//brandholder.brandname.setText(branditem.getBrandname());

		
		return convertView;
	}
	public void filter(String charText) {
		//Log.i("inside filter","inside filter");
		charText = charText.toLowerCase(Locale.getDefault());
		branditemlist.clear();
		if (charText.length() == 0) {
			branditemlist.addAll(arrayOfBrand);
		} 
		else 
		{
			for (BrandItem wp : arrayOfBrand) 
			{
				if (wp.getBrandname().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					//Log.i("Sizzee  of next branditemList", "" + branditemlist.size());
					branditemlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	static class BrandlistViewHolder {
		TextView brandname, brandid;
		ImageView brandicon;
	}
}
