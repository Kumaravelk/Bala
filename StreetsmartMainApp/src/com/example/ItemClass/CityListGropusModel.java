package com.example.ItemClass;

import java.util.ArrayList;

public class CityListGropusModel {

	
	String city_id,city_name;
	ArrayList<CityListItemsModel> childlist=new ArrayList<CityListItemsModel>();
	
	
	public CityListGropusModel(String city_id,String city_name,ArrayList<CityListItemsModel> childlist) {
		// TODO Auto-generated constructor stub
		
		this.city_id=city_id;
		this.city_name=city_name;
		this.childlist=childlist;
	}
	
	
	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public ArrayList<CityListItemsModel> getChildlist() {
		return childlist;
	}

	public void setChildlist(ArrayList<CityListItemsModel> childlist) {
		this.childlist = childlist;
	}
	
}
