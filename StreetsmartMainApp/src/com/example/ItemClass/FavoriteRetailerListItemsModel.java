package com.example.ItemClass;

public class FavoriteRetailerListItemsModel {

	String retailer_id,retailer_name,logo_img;
	
	


	boolean selected=false;

	
	public FavoriteRetailerListItemsModel(String retailer_id,String retailer_name,String logo_img,boolean selected) {
		// TODO Auto-generated constructor stub
		super();
		this.retailer_id=retailer_id;
		this.retailer_name=retailer_name;
		this.logo_img=logo_img;
	    this.selected=selected;
	}
	
	
	
	
	public FavoriteRetailerListItemsModel() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public String getRetailer_id() {
		return retailer_id;
	}


	public void setRetailer_id(String retailer_id) {
		this.retailer_id = retailer_id;
	}


	public String getRetailer_name() {
		return retailer_name;
	}


	public void setRetailer_name(String retailer_name) {
		this.retailer_name = retailer_name;
	}


	public String getLogo_img() {
		return logo_img;
	}


	public void setLogo_img(String logo_img) {
		this.logo_img = logo_img;
	}


	
	
}
