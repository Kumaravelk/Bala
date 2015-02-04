package com.example.ItemClass;

public class CategoryItem {
	
	
	String catname;
	String caticon;
	String catid;
	int subcateid;
	
	
	public int getSubcateid() {
		return subcateid;
	}
	public void setSubcateid(int subcateid) {
		this.subcateid = subcateid;
	}
	String subcatname;
	String catenameset;
	
	
	public String getCatenameset() {
		return catenameset;
	}
	public void setCatenameset(String catenameset) {
		this.catenameset = catenameset;
	}
	
	public String getSubcatname() {
		return subcatname;
	}
	public void setSubcatname(String subcatname) {
		this.subcatname = subcatname;
	}
	
	
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getCatname() 
	{
		return catname;
	}
	public void setCatname(String catname) {
		this.catname = catname;
	}
	public String getCaticon() {
		return caticon;
	}
	public void setCaticon(String caticon) {
		this.caticon = caticon;
	}
	

}
