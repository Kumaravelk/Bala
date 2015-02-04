package com.example.AdapterClass;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.base2.streetsmart.R;
import com.example.ItemClass.MallListGropus;
import com.example.ItemClass.MallListItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class NewMAllAdapter extends BaseExpandableListAdapter {

	private Context context;
	//private ArrayList<Continent> continentList;
	//private ArrayList<Continent> originalList;
	
	ArrayList<MallListGropus> grouplist;
	ArrayList<MallListGropus> childlist;
	
	public NewMAllAdapter(Context context, ArrayList<MallListGropus> grouplist) {
		this.context = context;
		this.grouplist = new ArrayList<MallListGropus>();
		this.grouplist.addAll(grouplist);
		this.childlist = new ArrayList<MallListGropus>();
		this.childlist.addAll(grouplist);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<MallListItems> childList = grouplist.get(groupPosition).getChildlist();//  getCountryList();
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
		
		MallListItems child = (MallListItems) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.mall_list_item, null);
		}
		
		TextView code = (TextView) convertView.findViewById(R.id.tv_mall_id);
		TextView name = (TextView) convertView.findViewById(R.id.tv_mall_name);
	//	TextView population = (TextView) convertView.findViewById(R.id.population);
		code.setText(child.getMall_id().trim());
		name.setText(child.getMall_name().trim());
		//population.setText(NumberFormat.getNumberInstance(Locale.US).format(child.g.getPopulation()));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<MallListItems> child = grouplist.get(groupPosition).getChildlist();// getCountryList();
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
		MallListGropus parent1 = (MallListGropus) getGroup(groupPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.mall_list_group, null);
		}
		
		TextView heading = (TextView) convertView.findViewById(R.id.tv_mallcity_name);
		TextView id=(TextView)convertView.findViewById(R.id.tv_mallcity_id);
		
		id.setText(parent1.getCity_id().trim());
		heading.setText(parent1.getCity_name().trim());
		
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
		Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		
		grouplist.clear();
		
		if(query.isEmpty())
		{
			grouplist.addAll(childlist);
		} else {
			for(MallListGropus par: childlist)
			{
				ArrayList<MallListItems> childList = par.getChildlist();//.getCountryList();
				ArrayList<MallListItems> newList = new ArrayList<MallListItems>();
				for(MallListItems child: childList)
				{
					if(child.getMall_id().toLowerCase().contains(query)|| child.getMall_name().toLowerCase().contains(query))
					{
						newList.add(child);
					}
				}
				if(newList.size() > 0)
				{
					MallListGropus nContinent = new MallListGropus(par.getCity_id(),par.getCity_name(), newList);
					grouplist.add(nContinent);
				}
			}
		}
		
		Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		notifyDataSetChanged();
	}
}
