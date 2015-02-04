package com.example.ItemClass;

import java.util.ArrayList;

public class MallListGropus {

	
	String city_id,city_name;
	ArrayList<MallListItems> childlist=new ArrayList<MallListItems>();
	
	
	public MallListGropus(String city_id,String city_name,ArrayList<MallListItems> childlist) {
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

	public ArrayList<MallListItems> getChildlist() {
		return childlist;
	}

	public void setChildlist(ArrayList<MallListItems> childlist) {
		this.childlist = childlist;
	}
	
}
