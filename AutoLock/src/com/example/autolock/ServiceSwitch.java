package com.example.autolock;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ServiceSwitch extends Fragment {
	private TextView textview1;
	private ImageButton imageButton;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("test", "creat view 1");

		View view = inflater.inflate(R.layout.fragment_al_view1, container,
				false);
		
		imageButton = (ImageButton) view.findViewById(R.id.imageButton1);
		if (isServiceExisted(Constant.SERVICE_NAME)) {
			imageButton.setBackgroundResource(R.drawable.button_start);
		} else {
			imageButton.setBackgroundResource(R.drawable.button_stop);
		}
		imageButton.setOnTouchListener(new ImageButton.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					if (Constant.isServiceOn) {
						arg0.setBackgroundResource(R.drawable.button_start_pressed);
					} else {
						arg0.setBackgroundResource(R.drawable.button_stop_pressed);
					}
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					if (!Constant.isServiceOn) {
						arg0.setBackgroundResource(R.drawable.button_start);
					} else {
						arg0.setBackgroundResource(R.drawable.button_stop);
					}
				}
				return false;
			}

		});
		imageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				Constant.isServiceOn = !Constant.isServiceOn;
				Intent intent = new Intent(ServiceSwitch.this.getActivity(),
						AutoLockService.class);
				if (Constant.isServiceOn) {

					getActivity().startService(intent);
					Log.v("test",
							"on serviceswitch onClick~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				} else {
					getActivity().stopService(intent);
				}
				Log.v("test",
						"button clicked ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			}
		});

		return view;
	}

	public boolean isServiceExisted(String className) {
		ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if(!(serviceList.size() > 0)){
			return false;
		}
		
		for (int i = 0; i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;
			if (serviceName.getClassName().equals(className)) {
				Constant.isServiceOn = true;
				return true;
			}
		}
		return false;
	}

}
