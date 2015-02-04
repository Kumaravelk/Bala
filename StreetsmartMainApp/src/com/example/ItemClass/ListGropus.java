package com.example.ItemClass;

import java.util.ArrayList;

public class ListGropus {

	
	String category_id,category_name;
	
	
	ArrayList<ListItems> childlist=new ArrayList<ListItems>();
	
	
	public ListGropus(String category_id,String category_name,ArrayList<ListItems> childlist) {
		// TODO Auto-generated constructor stub
		
		this.category_id=category_id;
		this.category_name=category_name;
		this.childlist=childlist;
	}
	
	

	public ArrayList<ListItems> getChildlist() {
		return childlist;
	}

	public void setChildlist(ArrayList<ListItems> childlist) {
		this.childlist = childlist;
	}
	
	public String getCategory_id() {
		return category_id;
	}



	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}



	public String getCategory_name() {
		return category_name;
	}



	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	
	
}
