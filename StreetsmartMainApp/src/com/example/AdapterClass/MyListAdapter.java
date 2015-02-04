package com.example.AdapterClass;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.base2.streetsmart.R;
import com.example.ItemClass.ListGropus;
import com.example.ItemClass.ListItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class MyListAdapter extends BaseExpandableListAdapter {

	private Context context;
	//private ArrayList<Continent> continentList;
	//private ArrayList<Continent> originalList;
	
	ArrayList<ListGropus> grouplist;
	ArrayList<ListGropus> childlist;
	
	public MyListAdapter(Context context, ArrayList<ListGropus> grouplist) {
		this.context = context;
		this.grouplist = new ArrayList<ListGropus>();
		this.grouplist.addAll(grouplist);
		this.childlist = new ArrayList<ListGropus>();
		this.childlist.addAll(grouplist);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<ListItems> childList = grouplist.get(groupPosition).getChildlist();//  getCountryList();
		return childList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ListItems child = (ListItems) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.child_list_item, null);
		}
		
		TextView code = (TextView) convertView.findViewById(R.id.tv_sub_cat_id);
		TextView name = (TextView) convertView.findViewById(R.id.tv_sub_cat_name);
	//	TextView population = (TextView) convertView.findViewById(R.id.population);
		code.setText(child.getSub_cat_id().trim());
		name.setText(child.getSub_cat_name().trim());
		//population.setText(NumberFormat.getNumberInstance(Locale.US).format(child.g.getPopulation()));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<ListItems> child = grouplist.get(groupPosition).getChildlist();// getCountryList();
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
		ListGropus parent1 = (ListGropus) getGroup(groupPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.list_group, null);
		}
		
		TextView id = (TextView) convertView.findViewById(R.id.tv_c_id);
		TextView heading=(TextView)convertView.findViewById(R.id.tv_c_name);
	
		
		id.setText(parent1.getCategory_id().trim());
		heading.setText(parent1.getCategory_name().trim());
		
		return convertView;
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
			for(ListGropus par: childlist)
			{
				ArrayList<ListItems> childList = par.getChildlist();//.getCountryList();
				ArrayList<ListItems> newList = new ArrayList<ListItems>();
				for(ListItems child: childList)
				{
					if(child.getSub_cat_id().toLowerCase().contains(query)|| child.getSub_cat_name().toLowerCase().contains(query))
					{
						newList.add(child);
					}
				}
				if(newList.size() > 0)
				{
					ListGropus nContinent = new ListGropus(par.getCategory_id(),par.getCategory_name(), newList);
					grouplist.add(nContinent);
				}
			}
		}
		
//		Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		notifyDataSetChanged();
	}
}
