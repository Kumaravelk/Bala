package com.example.AdapterClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;

import com.base2.streetsmart.R;
import com.example.ItemClass.FavoriteRetailerListGropusModel;
import com.example.ItemClass.FavoriteRetailerListItemsModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class FavoriteRetailerExListAdapter extends BaseExpandableListAdapter {

	private Context context;
	//private ArrayList<Continent> continentList;
	//private ArrayList<Continent> originalList;
	
	String re;
	
	ArrayList<FavoriteRetailerListGropusModel> grouplist;
	ArrayList<FavoriteRetailerListGropusModel> childlist;
	
	FavoriteRetailerListGropusModel parent_arr;
	FavoriteRetailerListItemsModel arr_parent;
	
	ArrayList<FavoriteRetailerListItemsModel> arr_child=new ArrayList<FavoriteRetailerListItemsModel>();
	
	//ArrayList<ListItems>  arr_list_parent=new ArrayList<ListItems>();
 	
	ArrayList<String> isCheckedStatus = new ArrayList<String>();
	
	//HashMap<String, String> mCheckBoxData = new HashMap<String, String>();
	
//	ArrayList<String> fav_sub_cat_id=new java.util.ArrayList<String>();
	
	public FavoriteRetailerExListAdapter(Context context, ArrayList<FavoriteRetailerListGropusModel> grouplist) {
		this.context = context;
		this.grouplist = new ArrayList<FavoriteRetailerListGropusModel>();
		this.grouplist.addAll(grouplist);
		this.childlist = new ArrayList<FavoriteRetailerListGropusModel>();
		this.childlist.addAll(grouplist);
		
		for (int i = 0; i < childlist.size(); i++) {
            isCheckedStatus.add("false");
        }
	}
	
	
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<FavoriteRetailerListItemsModel> childList = grouplist.get(groupPosition).getChildlist();//  getCountryList();
		return childList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	
	
	private class ViewHolder {
		TextView r_id,r_name;
		CheckBox check_catid;
		ImageView icon;
        Bitmap bitmap;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;
		
		final FavoriteRetailerListItemsModel child = (FavoriteRetailerListItemsModel) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			
		
		
		
			
			holder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.favorite_retailer_list_item, null);
			
			holder.check_catid=(CheckBox)convertView.findViewById(R.id.check);
			holder.r_id=(TextView) convertView.findViewById(R.id.fa_sub_cat_id);
			holder.r_name= (TextView) convertView.findViewById(R.id.fa_sub_cat_name);
			convertView.setTag(holder);
			
			String no_re=child.getRetailer_id().toString();

		/*	Log.d("Retailer No", "+_+__+_+_+_+_+_+_+_+_+_+_+_+_");
			Log.d("Retailer No", no_re);*/
			/*
			if (no_re.equalsIgnoreCase("0")) {
				
				Log.d("Retailer No", no_re);
				holder.check_catid.setVisibility(View.INVISIBLE);
				
			} else {
				Log.d("Retailer No1", no_re);
				holder.check_catid.setVisibility(View.VISIBLE);
			
			}*/
			
			holder.check_catid.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					//mCheckBoxData=new HashMap<String, String>();
					
					CheckBox cb = (CheckBox) v;
					FavoriteRetailerListItemsModel categoryvalues = (FavoriteRetailerListItemsModel) cb.getTag();
					categoryvalues.setSelected(cb.isChecked());
					//mCheckBoxData.put("", child.getSub_cat_id());
					
					
				}
			});
		}else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		
		holder.r_id.setText(child.getRetailer_id().trim());
		holder.r_name.setText(child.getRetailer_name().trim());
		holder.check_catid.setChecked(child.isSelected());
		//holder.check_catid.setText(child.getSub_cat_id());
		holder.check_catid.setTag(child);
		
	//	holder.bitmap=getBitmapFromUrl(child.getLogo_img());
     //   holder.icon.setImageBitmap(holder.bitmap);
		
		return convertView;
	}

	
	
	
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<FavoriteRetailerListItemsModel> child = grouplist.get(groupPosition).getChildlist();// getCountryList();
		return child.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return grouplist.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return grouplist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Bitmap bitmap;
		FavoriteRetailerListGropusModel parent1 = (FavoriteRetailerListGropusModel) getGroup(groupPosition);
		
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.favorite_retailer_list_group, null);
		}
		
		TextView id = (TextView) convertView.findViewById(R.id.fa_c_id);
		TextView heading=(TextView)convertView.findViewById(R.id.fa_c_name);
	//	ImageView cat_icon=(ImageView)convertView.findViewById(R.id.img_cat);
		
		
	//	bitmap = getBitmapFromUrl(parent1.getCategory_icon());
		
		
		id.setText(parent1.getCategory_id().trim());
		heading.setText(parent1.getCategory_name().trim());
	//	cat_icon.setImageBitmap(bitmap);
		
		return convertView;
	}

	
	public Bitmap getBitmapFromUrl(String src) {
		
		try {
			URL url=new URL(src);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setDoInput(true);
			con.connect();
			InputStream input=con.getInputStream();
			Bitmap bitmap=BitmapFactory.decodeStream(input);
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
				
	}
	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressLint("DefaultLocale")
	public void filterData(String query)
	{
		query = query.toLowerCase();
	//	Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		
		grouplist.clear();
		
		if(query.isEmpty())
		{
			grouplist.addAll(childlist);
		} else {
			for(FavoriteRetailerListGropusModel par: childlist)
			{
				ArrayList<FavoriteRetailerListItemsModel> childList = par.getChildlist();//.getCountryList();
				ArrayList<FavoriteRetailerListItemsModel> newList = new ArrayList<FavoriteRetailerListItemsModel>();
				for(FavoriteRetailerListItemsModel child: childList)
				{
					if(child.getRetailer_id().toLowerCase().contains(query)|| child.getRetailer_name().toLowerCase().contains(query))
					{
						newList.add(child);
					}
				}
				if(newList.size() > 0)
				{
					FavoriteRetailerListGropusModel nContinent = new FavoriteRetailerListGropusModel(par.getCategory_id(),par.getCategory_name(),par.getCategory_icon(), newList);
					grouplist.add(nContinent);
				}
			}
		}
		
	//	Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		notifyDataSetChanged();
	}
	
	
	
	public String checkVal() {
		
    String re;
		StringBuffer responseText = new StringBuffer();
		
		
	//	ArrayList<ListItems> arrayofcategoryList = childlist;
		
		for(FavoriteRetailerListGropusModel par: childlist)
		{
			ArrayList<FavoriteRetailerListItemsModel> childList = par.getChildlist();//.getCountryList();
			ArrayList<FavoriteRetailerListItemsModel> newList = new ArrayList<FavoriteRetailerListItemsModel>();
			for(FavoriteRetailerListItemsModel child: childList)
			{
				if (child.isSelected())
				{
					responseText.append(child.getRetailer_id() + ",");
				//	Log.d("Get Sub Cat ID ","_____________________________________________");
				//	Log.d("Get Sub Cat ID ", responseText.toString());
				}
			}
			//ListItems categoryvalues = arraylist1.get(i);
			
		}
		
		re=responseText.toString();
		return re;
	}
}
