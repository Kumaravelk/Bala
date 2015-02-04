package com.example.AdapterClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.base2.streetsmart.R;
import com.example.ItemClass.CustomRetailerListGropusModel;
import com.example.ItemClass.CustomRetailerListItemsModel;

@SuppressLint("DefaultLocale")
public class CustomRetailerExListAdapter extends BaseExpandableListAdapter {

	private Context context;
	//private ArrayList<Continent> continentList;
	//private ArrayList<Continent> originalList;
	
	String re;
	
	ArrayList<CustomRetailerListGropusModel> grouplist;
	ArrayList<CustomRetailerListGropusModel> childlist;
	
	CustomRetailerListGropusModel parent_arr;
	CustomRetailerListItemsModel arr_parent;
	
	ArrayList<CustomRetailerListItemsModel> arr_child=new ArrayList<CustomRetailerListItemsModel>();
	
	//ArrayList<ListItems>  arr_list_parent=new ArrayList<ListItems>();
 	
	ArrayList<String> isCheckedStatus = new ArrayList<String>();
	
	//HashMap<String, String> mCheckBoxData = new HashMap<String, String>();
	
//	ArrayList<String> fav_sub_cat_id=new java.util.ArrayList<String>();
	
	public CustomRetailerExListAdapter(Context context, ArrayList<CustomRetailerListGropusModel> grouplist) {
		this.context = context;
		this.grouplist = new ArrayList<CustomRetailerListGropusModel>();
		this.grouplist.addAll(grouplist);
		this.childlist = new ArrayList<CustomRetailerListGropusModel>();
		this.childlist.addAll(grouplist);
		
		for (int i = 0; i < childlist.size(); i++) {
            isCheckedStatus.add("false");
        }
	}
	
	
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<CustomRetailerListItemsModel> childList = grouplist.get(groupPosition).getChildlist();//  getCountryList();
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
		
		final CustomRetailerListItemsModel child = (CustomRetailerListItemsModel) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			
		
		
		
			
			holder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.custom_retailer_list_item, null);
			
			holder.check_catid=(CheckBox)convertView.findViewById(R.id.cus_ret_check);
			holder.r_id=(TextView) convertView.findViewById(R.id.cus_ret_id);
			holder.r_name= (TextView) convertView.findViewById(R.id.cus_ret_name);
			convertView.setTag(holder);
			
			
			
			holder.check_catid.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					//mCheckBoxData=new HashMap<String, String>();
					
					CheckBox cb = (CheckBox) v;
					CustomRetailerListItemsModel categoryvalues = (CustomRetailerListItemsModel) cb.getTag();
					categoryvalues.setSelected(cb.isChecked());
					//mCheckBoxData.put("", child.getSub_cat_id());
					
					
				}
			});
		}else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String no_re=child.getCus_retailer_id().toString();
		
		
		
		if (no_re == "" + 0 + "") {

			holder.check_catid.setVisibility(View.INVISIBLE);
		} else {
			holder.check_catid.setVisibility(View.VISIBLE);
		
		}
		
		holder.r_id.setText(child.getCus_retailer_id().trim());
		holder.r_name.setText(child.getCus_retailer_name().trim());
		holder.check_catid.setChecked(child.isSelected());
		//holder.check_catid.setText(child.getSub_cat_id());
		holder.check_catid.setTag(child);
		
	//	holder.bitmap=getBitmapFromUrl(child.getLogo_img());
     //   holder.icon.setImageBitmap(holder.bitmap);
		
	   int s_flag=child.getFlag();
		
	//	Log.d("Flag No :", "___________________________________");
		
	//	Log.d("Flag No :", ""+s_flag+"");
		
		if(s_flag==1){
			holder.check_catid.setChecked(true);
			CustomRetailerListItemsModel categoryvalues = (CustomRetailerListItemsModel)holder.check_catid.getTag();
			categoryvalues.setSelected(holder.check_catid.isChecked());
			
		}else{
			
			holder.check_catid.setChecked(false);
		}
		
		
		
		
		return convertView;
	}

	
	
	
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<CustomRetailerListItemsModel> child = grouplist.get(groupPosition).getChildlist();// getCountryList();
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
		CustomRetailerListGropusModel parent1 = (CustomRetailerListGropusModel) getGroup(groupPosition);
		
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.custom_retailer_list_group, null);
		}
		
		TextView id = (TextView) convertView.findViewById(R.id.cus_ret_cat_id);
		TextView heading=(TextView)convertView.findViewById(R.id.cus_ret_cat_name);
	//	ImageView cat_icon=(ImageView)convertView.findViewById(R.id.img_cat);
		
		
	//	bitmap = getBitmapFromUrl(parent1.getCategory_icon());
		
		
		id.setText(parent1.getCus_category_id().trim());
		heading.setText(parent1.getCus_category_name().trim());
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
			for(CustomRetailerListGropusModel par: childlist)
			{
				ArrayList<CustomRetailerListItemsModel> childList = par.getChildlist();//.getCountryList();
				ArrayList<CustomRetailerListItemsModel> newList = new ArrayList<CustomRetailerListItemsModel>();
				for(CustomRetailerListItemsModel child: childList)
				{
					if(child.getCus_retailer_id().toLowerCase().contains(query)|| child.getCus_retailer_name().toLowerCase().contains(query))
					{
						newList.add(child);
					}
				}
				if(newList.size() > 0)
				{
					CustomRetailerListGropusModel nContinent = new CustomRetailerListGropusModel(par.getCus_category_id(),par.getCus_category_name(),par.getCus_category_icon(), newList);
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
		
		for(CustomRetailerListGropusModel par: childlist)
		{
			ArrayList<CustomRetailerListItemsModel> childList = par.getChildlist();//.getCountryList();
			ArrayList<CustomRetailerListItemsModel> newList = new ArrayList<CustomRetailerListItemsModel>();
			for(CustomRetailerListItemsModel child: childList)
			{
				if (child.isSelected())
				{
					responseText.append(child.getCus_retailer_id() + ",");
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
