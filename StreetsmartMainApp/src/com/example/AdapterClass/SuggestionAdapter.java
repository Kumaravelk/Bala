package com.example.AdapterClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.example.Session.SessionManager;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

 
public class SuggestionAdapter extends ArrayAdapter<String> {
	
	SessionManager session;
	
	String cityid;
    protected static final String TAG = "SuggestionAdapter";
    
    HashMap<String, String> user;
    private List<String> suggestions;
    
    public SuggestionAdapter(Activity context, String nameFilter) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        session = new SessionManager(context);
			 
			user = session.getUserDetails();
        suggestions = new ArrayList<String>();
    }
 
    @Override
    public int getCount() {
        return suggestions.size();
    }
 
    @Override
    public String getItem(int index) {
        return suggestions.get(index);
    }
 
    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
               
                cityid =user.get(SessionManager.KEY_CITYID);
                
           //     Log.i("CityID", cityid);
                
                JsonParse jp=new JsonParse();
                if (constraint != null) {
                    
                    List<SuggestGetSet> new_suggestions =jp.getParseJsonWCF(constraint.toString(),cityid);
                    suggestions.clear();
                    for (int i=0;i<new_suggestions.size();i++) {
                        suggestions.add(new_suggestions.get(i).getName());
                    }
 
                   
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    
                    
                    
                   
                }
                return filterResults;
            }
 
            @Override
            protected void publishResults(CharSequence contraint,
                    FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
 
}
