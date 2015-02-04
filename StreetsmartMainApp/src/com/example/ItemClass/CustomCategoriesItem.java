package com.example.ItemClass;

public class CustomCategoriesItem {
	
	
	String cat_id,cat_name;
	String code = null;
	String name = null;
	 boolean selected = false;
	 
	public CustomCategoriesItem(String code, String name, boolean selected) {
	 super();
	 this.code = code;
	 this.name = name;
	 this.selected = selected;
	}
	
	
	public CustomCategoriesItem() {
		// TODO Auto-generated constructor stub
	}


	public boolean isSelected() {
		  return selected;
		 }
		 public void setSelected(boolean selected) {
		  this.selected = selected;
		 }
		  

	
	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

}