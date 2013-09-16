package com.example.autolock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutView extends Fragment {
	
	private TextView textView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("test", "creat view 3==============================");
		View view = inflater.inflate(R.layout.fragment_al_view3, container, false);
		
		textView = (TextView) view.findViewById(R.id.textView1);

		
		return view;
	}
}

