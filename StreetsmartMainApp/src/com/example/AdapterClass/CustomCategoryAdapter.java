package com.example.AdapterClass;

import java.util.ArrayList;

import org.json.JSONException;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base2.streetsmart.R;
import com.example.ItemClass.CustomCategoryListModel;

public class CustomCategoryAdapter extends BaseAdapter {

	
	ArrayList data;
	LayoutInflater inflater;
	CustomCategoryListModel temp_val=null;
	Context cus_cat_con;
	String categorys;
	Activity activity;
	
	public CustomCategoryAdapter(Activity activity,ArrayList myarr) {
		// TODO Auto-generated constructor stub
	
		this.data=myarr;
		//this.activity=a;
	//	inflater=(LayoutInflater) cus_cat_con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	 public static class ViewHolder{
	    	
	        public TextView cus_cat_id,cus_cat_name;
	        
	        public CheckBox cus_cat_check;
	        

	    }
	
	@SuppressWarnings("static-access")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder ;
		View v=convertView;
		
		
		if(convertView==null){
			
			
		
			
			//LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//convertView = vi.inflate(R.layout.customcatelist, null);
			v=inflater.inflate(R.layout.customcatelist, null);
		
			holder=new ViewHolder();
			
			holder.cus_cat_id=(TextView)v.findViewById(R.id.custextid);
			//holder.cus_cat_name=(TextView)v.findViewById(R.id.cus_cat_name);
			holder.cus_cat_check=(CheckBox)v.findViewById(R.id.cuscheckBox);
			
			v.setTag(holder);
			
			holder.cus_cat_check.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					CheckBox cb = (CheckBox) v;
				//	int i=(Integer) cb.getTag();
					CustomCategoryListModel categoryvalues = (CustomCategoryListModel) cb.getTag();
					categoryvalues.setCus_cat_select(cb.isChecked());
					
					
				}
			});
			
			
			
		}else{
			
			holder=(ViewHolder)v.getTag();
		}
		
		temp_val=null;
		
		
		temp_val=(CustomCategoryListModel)data.get(position);
		
		holder.cus_cat_id.setText(temp_val.getCus_cat_id());
		//holder.cus_cat_name.setText(temp_val.getCus_cat_name());
		
		int flag=(int) temp_val.getFlag();
		
		
		holder.cus_cat_check.setChecked(temp_val.isCus_cat_select());
		holder.cus_cat_check.setText(temp_val.getCus_cat_name());
		holder.cus_cat_check.setTag(temp_val);
		
		
	//	Log.d("Flag Val  : ", ""+flag+"");
		
		if(flag==1){
			
			holder.cus_cat_check.setChecked(true);
			
			CustomCategoryListModel categoryvalues = (CustomCategoryListModel)holder.cus_cat_check.getTag();
			categoryvalues.setCus_cat_select(holder.cus_cat_check.isChecked());
			
		}else{
			
			holder.cus_cat_check.setChecked(false);
		}
		
	//	if(holder.cus_cat_check)
		
		return v;
	}
	
	
	
	//@SuppressWarnings("unchecked")
	public String checkButtonClick() {

		  String re;
			
		StringBuffer responseText = new StringBuffer();

		ArrayList<CustomCategoryListModel> arrayofcategoryList = data;
		for (int i = 0; i < arrayofcategoryList.size(); i++) {
			CustomCategoryListModel categoryvalues = arrayofcategoryList.get(i);
			if (categoryvalues.isCus_cat_select()) {
				responseText.append(categoryvalues.getCus_cat_id() + ",");
			}
		}

		 re = responseText.toString();

		return re;
	}


}
