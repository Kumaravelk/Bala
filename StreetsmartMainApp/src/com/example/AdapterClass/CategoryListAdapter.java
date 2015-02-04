package com.example.AdapterClass;

import java.util.ArrayList;

import com.example.ItemClass.CategoryItem;
import com.base2.streetsmart.*;




import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {
	
	
	private Context _context;

	private LayoutInflater mLayoutInflater;
      
	private ArrayList arrayOfList;
	


	public CategoryListAdapter(Context context, ArrayList arrayOfList) {
		// TODO Auto-generated constructor stub
		this.arrayOfList =arrayOfList ;
		
		mLayoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
	
			return arrayOfList.size();

	}

	public Object getItem(int position) {

		return arrayOfList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		listViewHolder holder;
	

			if (convertView == null) {

				convertView = mLayoutInflater.inflate(R.layout.categorylist, null);

				holder = new listViewHolder();
				
				holder.category = (TextView) convertView.findViewById(R.id.catename);
				
				
				
				holder.catid = (TextView) convertView.findViewById(R.id.catid);
				
				//holder.icon = (ImageView)convertView.findViewById(R.id.icon);
				
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (listViewHolder) convertView.getTag();
			}

			CategoryItem newsItem = (CategoryItem) arrayOfList.get(position);
	 
			//holder.catid.setText(newsItem.getCategoryid());
			
			
			
			holder.category.setText(newsItem.getCatname());
			
			holder.catid.setText(newsItem.getCatid());
			
			
			
			//Log.i("Image URL ",""+newsItem.getCaticon());

			

			/*if (holder.icon != null) 
			{
				//Log.i("checking1","checking1");
				new ImageDownloaderTask(holder.icon).execute(newsItem.getCaticon());
				//Log.i("checking2","checking2");
			}*/
			
	 
			return convertView;
		}

		
		
	

	static class listViewHolder {
		TextView category,catid;
		ImageView icon;
	}
}	