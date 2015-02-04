package com.example.AdapterClass;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.base2.streetsmart.R;
import com.example.ItemClass.CityListGropusModel;
import com.example.ItemClass.CityListItemsModel;

@SuppressLint("DefaultLocale")
public class CityExListAdapter extends BaseExpandableListAdapter {

	private Context context;
	//private ArrayList<Continent> continentList;
	//private ArrayList<Continent> originalList;
	
	ArrayList<CityListGropusModel> grouplist;
	ArrayList<CityListGropusModel> childlist;
	
	public CityExListAdapter(Context context, ArrayList<CityListGropusModel> grouplist) {
		this.context = context;
		this.grouplist = new ArrayList<CityListGropusModel>();
		this.grouplist.addAll(grouplist);
		this.childlist = new ArrayList<CityListGropusModel>();
		this.childlist.addAll(grouplist);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<CityListItemsModel> childList = grouplist.get(groupPosition).getChildlist();//  getCountryList();
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
		
		CityListItemsModel child = (CityListItemsModel) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.city_area_list_item, null);
		}
		
		TextView code = (TextView) convertView.findViewById(R.id.city_area_id);
		TextView name = (TextView) convertView.findViewById(R.id.city_area_name);
	//	TextView population = (TextView) convertView.findViewById(R.id.population);
		code.setText(child.getArea_id().trim());
		name.setText(child.getArea_name().trim());
		//population.setText(NumberFormat.getNumberInstance(Locale.US).format(child.g.getPopulation()));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<CityListItemsModel> child = grouplist.get(groupPosition).getChildlist();// getCountryList();
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
		CityListGropusModel parent1 = (CityListGropusModel) getGroup(groupPosition);
		if(convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.city_area_list_group, null);
		}
		
		TextView heading = (TextView) convertView.findViewById(R.id.tv_newcity_name);
		TextView id=(TextView)convertView.findViewById(R.id.tv_newcity_id);
		
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
	//	Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		
		grouplist.clear();
		
		if(query.isEmpty())
		{
			grouplist.addAll(childlist);
		} else {
			for(CityListGropusModel par: childlist)
			{
				ArrayList<CityListItemsModel> childList = par.getChildlist();//.getCountryList();
				ArrayList<CityListItemsModel> newList = new ArrayList<CityListItemsModel>();
				for(CityListItemsModel child: childList)
				{
					if(child.getArea_id().toLowerCase().contains(query)|| child.getArea_name().toLowerCase().contains(query))
					{
						newList.add(child);
					}
				}
				if(newList.size() > 0)
				{
					CityListGropusModel nContinent = new CityListGropusModel(par.getCity_id(),par.getCity_name(), newList);
					grouplist.add(nContinent);
				}
			}
		}
		
		//Log.v("MyListAdapter", String.valueOf(grouplist.size()));
		notifyDataSetChanged();
	}
}
