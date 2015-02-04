package com.example.ItemClass;

import java.util.ArrayList;

public class FavoriteRetailerListGropusModel {

	
	String category_id,category_name,category_icon;
	
	

	ArrayList<FavoriteRetailerListItemsModel> childlist=new ArrayList<FavoriteRetailerListItemsModel>();
	
	
	public FavoriteRetailerListGropusModel(String category_id,String category_name,String category_icon, ArrayList<FavoriteRetailerListItemsModel> childlist) {
		// TODO Auto-generated constructor stub
		
		this.category_id=category_id;
		this.category_name=category_name;
		this.childlist=childlist;
		this.category_icon=category_icon;
	}
	
	

	public ArrayList<FavoriteRetailerListItemsModel> getChildlist() {
		return childlist;
	}

	public void setChildlist(ArrayList<FavoriteRetailerListItemsModel> childlist) {
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

	
	public String getCategory_icon() {
		return category_icon;
	}



	public void setCategory_icon(String category_icon) {
		this.category_icon = category_icon;
	}


	
	
}
