package com.example.AdapterClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class JsonParse {
     double current_latitude,current_longitude;
     public JsonParse(){}
     public JsonParse(double current_latitude,double current_longitude){
         this.current_latitude=current_latitude;
         this.current_longitude=current_longitude;
        
     }
     public List<SuggestGetSet> getParseJsonWCF(String sName,String City)
        {
    	 String page;
         List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
         HttpClient httpclient = new DefaultHttpClient();
  		HttpPost httppost = new HttpPost(
  				"http://54.169.81.215/streetsmartadmin4/shopping/searchenginesuggested1");

         try {
        	// Log.i("inside","inside");
           // String temp=sName.replace(" ", "%20");
           
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
 			nameValuePairs.add(new BasicNameValuePair("word", sName));
 			nameValuePairs.add(new BasicNameValuePair("city_id", City));
 			
 			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 		
 			HttpResponse response = httpclient.execute(httppost);

 			page = EntityUtils.toString(response.getEntity());

 			//Log.i("Page", page);
            JSONObject jsonResponse = new JSONObject(page);
            JSONArray jsonArray = jsonResponse.getJSONArray("result");
            for(int i = 0; i < jsonArray.length(); i++){
              //  JSONObject r = jsonArray.getJSONObject(i);
                ListData.add(new SuggestGetSet(jsonArray.getString(i)));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
         
         return ListData;
 
        }
 
     
     
     
     
}