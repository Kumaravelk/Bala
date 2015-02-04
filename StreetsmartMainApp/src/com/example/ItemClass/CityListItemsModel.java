package com.example.ItemClass;

public class CityListItemsModel {

	String area_id,area_name;



	public CityListItemsModel(String area_id,String area_name) {
		// TODO Auto-generated constructor stub
		super();
		this.area_id=area_id;
		this.area_name=area_name;
	
	}
	
	
	
	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
}
