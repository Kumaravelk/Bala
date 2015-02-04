package com.base2.streetsmart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;


import android.app.ActionBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class Direction extends FragmentActivity {
 
    // Google Map
	private static LatLng USERLOCATION;
	private static LatLng RETAILERLOCATION;
	private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

	GoogleMap googleMap;
	final String TAG = "Direction";
	
	String userlat,userlng,retailerlat,retailerlong;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapslayout);
        
        
        
        
		 Bundle loca_bundle = getIntent().getExtras();
		 
		 userlat = loca_bundle.getString("userlat");  
		 userlng = loca_bundle.getString("userlong");
		 
		 
		 retailerlat = loca_bundle.getString("retailerlat");  
		 retailerlong = loca_bundle.getString("retailerlong");
		 
		 
	
		 
		 double ulat = Double.parseDouble(userlat);
		 
		 double ulong = Double.parseDouble(userlng);
		 
        double rlat = Double.parseDouble(retailerlat);
		 
		 double rlong = Double.parseDouble(retailerlong);
        
		 
		 
		  ActionBar actionbar = getActionBar();
	        
	        actionbar.hide();
		 
		 USERLOCATION = new LatLng(ulat, ulong);
		 RETAILERLOCATION = new LatLng(rlat, rlong);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		googleMap = fm.getMap();

		MarkerOptions options = new MarkerOptions();
		options.position(USERLOCATION);
		options.position(RETAILERLOCATION);
		//options.position(WALL_STREET);
		googleMap.addMarker(options);
		String url = getMapsApiDirectionsUrl();
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USERLOCATION,
				13));
		addMarkers();

	}

	private String getMapsApiDirectionsUrl() {
		String waypoints = "waypoints=optimize:true|"
				+ USERLOCATION.latitude + "," + USERLOCATION.longitude
				+ "|" + "|" + RETAILERLOCATION.latitude + ","
				+ RETAILERLOCATION.longitude;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	private void addMarkers() {
		if (googleMap != null) {
			googleMap.addMarker(new MarkerOptions().position(RETAILERLOCATION)
					.title("First Point"));
			googleMap.addMarker(new MarkerOptions().position(USERLOCATION)
					.title("Second Point"));
			/*googleMap.addMarker(new MarkerOptions().position(WALL_STREET)
					.title("Third Point"));*/
		}
	}

	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			// traversing through routes
			for (int i = 0; i < routes.size(); i++) {
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);

				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				polyLineOptions.addAll(points);
				polyLineOptions.width(4);
				polyLineOptions.color(Color.BLUE);
			}

			googleMap.addPolyline(polyLineOptions);
		}
	}
}