package com.example.ItemClass;

public class MallListItems {

	String mall_id,mall_name;

	public MallListItems(String mall_id,String mall_name) {
		// TODO Auto-generated constructor stub
		super();
		this.mall_id=mall_id;
		this.mall_name=mall_name;
	
	}
	
	
	public String getMall_id() {
		return mall_id;
	}

	public void setMall_id(String mall_id) {
		this.mall_id = mall_id;
	}

	public String getMall_name() {
		return mall_name;
	}

	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}
	
}
