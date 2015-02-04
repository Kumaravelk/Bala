package com.base2.streetsmart;

import java.util.ArrayList;

public class ListModel {

	String subcat_id,subcat_name;
	ArrayList<String> parent;
	

	

	public ArrayList<String> getParent() {
		return parent;
	}

	public void setParent(ArrayList<String> parent) {
		this.parent = parent;
	}

	public String getSubcat_id() {
		return subcat_id;
	}

	public void setSubcat_id(String subcat_id) {
		this.subcat_id = subcat_id;
	}

	public String getSubcat_name() {
		return subcat_name;
	}

	public void setSubcat_name(String subcat_name) {
		this.subcat_name = subcat_name;
	}

	
	
	
}
