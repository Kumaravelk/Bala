package com.example.ItemClass;

public class CustomRetailerListItemsModel {

	String cus_retailer_id,cus_retailer_name,cus_logo_img;
	
	

	int flag;




	boolean selected=false;

	
	public CustomRetailerListItemsModel(String retailer_id,String retailer_name,int flag,boolean selected) {
		// TODO Auto-generated constructor stub
		super();
		this.cus_retailer_id=retailer_id;
		this.cus_retailer_name=retailer_name;
	//	this.cus_logo_img=logo_img;
	    this.selected=selected;
	    this.flag=flag;
	}
	
	
	
	public int getFlag() {
		return flag;
	}




	public void setFlag(int flag) {
		this.flag = flag;
	}


	
	public CustomRetailerListItemsModel() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}



	public String getCus_retailer_id() {
		return cus_retailer_id;
	}




	public void setCus_retailer_id(String cus_retailer_id) {
		this.cus_retailer_id = cus_retailer_id;
	}




	public String getCus_retailer_name() {
		return cus_retailer_name;
	}




	public void setCus_retailer_name(String cus_retailer_name) {
		this.cus_retailer_name = cus_retailer_name;
	}




	public String getCus_logo_img() {
		return cus_logo_img;
	}




	public void setCus_logo_img(String cus_logo_img) {
		this.cus_logo_img = cus_logo_img;
	}


	
	
}
