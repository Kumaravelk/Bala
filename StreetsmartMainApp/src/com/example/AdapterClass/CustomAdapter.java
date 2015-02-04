package com.example.AdapterClass;

import java.util.ArrayList;

import com.example.ItemClass.CountryItem;
import com.base2.streetsmart.*;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	private ArrayList data;
	public Resources res;
	CountryItem tempValues = null;
	LayoutInflater inflater;

	public CustomAdapter(RegistrationActivity activity2,
			int textViewResourceId, ArrayList objects, Resources resLocal) {
		super(activity2, textViewResourceId, objects);

		activity = activity2;
		data = objects;
		res = resLocal;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public CustomAdapter(DetailedRegistrationActivity activity,
			int spinnerRows, ArrayList objects, Resources res2) {

		super(activity, spinnerRows, objects);

		activity = activity;
		data = objects;
		res = res2;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// TODO Auto-generated constructor stub
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(int position, View convertView, ViewGroup parent) {

		View row = inflater.inflate(R.layout.spinner_rows, parent, false);

		tempValues = null;
		tempValues = (CountryItem) data.get(position);

		TextView label = (TextView) row.findViewById(R.id.company);
		TextView sub = (TextView) row.findViewById(R.id.sub);
		// ImageView companyLogo = (ImageView)row.findViewById(R.id.image);

		label.setVisibility(4);

		label.setText(tempValues.getRank());
		sub.setText(tempValues.getCountry());

		return row;
	}
}
