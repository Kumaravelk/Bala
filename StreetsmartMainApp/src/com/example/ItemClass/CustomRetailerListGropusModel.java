package com.example.ItemClass;
import java.util.ArrayList;

public class CustomRetailerListGropusModel {

	
	String cus_category_id,cus_category_name,cus_category_icon;
	
	
	ArrayList<CustomRetailerListItemsModel> childlist=new ArrayList<CustomRetailerListItemsModel>();
	
	
	public CustomRetailerListGropusModel(String category_id,String category_name,String category_icon, ArrayList<CustomRetailerListItemsModel> childlist) {
		// TODO Auto-generated constructor stub
		
		this.cus_category_id=category_id;
		this.cus_category_name=category_name;
		this.childlist=childlist;
		this.cus_category_icon=category_icon;
	}
	
	

	public String getCus_category_id() {
		return cus_category_id;
	}



	public void setCus_category_id(String cus_category_id) {
		this.cus_category_id = cus_category_id;
	}



	public String getCus_category_name() {
		return cus_category_name;
	}



	public void setCus_category_name(String cus_category_name) {
		this.cus_category_name = cus_category_name;
	}



	public String getCus_category_icon() {
		return cus_category_icon;
	}



	public void setCus_category_icon(String cus_category_icon) {
		this.cus_category_icon = cus_category_icon;
	}

	
	public ArrayList<CustomRetailerListItemsModel> getChildlist() {
		return childlist;
	}

	public void setChildlist(ArrayList<CustomRetailerListItemsModel> childlist) {
		this.childlist = childlist;
	}
	
	
	
	
}
