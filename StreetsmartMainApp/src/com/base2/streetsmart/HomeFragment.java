package com.base2.streetsmart;

import static com.base2.streetsmart.MainActivity1.COMM_URL;

import com.base2.streetsmart.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

	Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		
		 
		/*Intent intent = new Intent(getActivity(), HomeTabActivity.class);

		// intent.putExtra(....); // put your data

		getActivity().startActivity(intent);*/
		
	

		return rootView;
	}
}
