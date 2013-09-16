package com.example.autolock;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingsView extends Fragment {

	private SeekBar seekBar;
	private TextView editText;
	private Button setLockButton;
	private Button setUnlockButton;
	private Handler mHandler;
	private MonitorThread monitorThread = null;
	
	private static final int SAVESUCCESS = 1;
	private static final int SAVEUNSUCESS = 0;

	private Vibrator vibrator;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("test", "creat view 2");
		View view = inflater.inflate(R.layout.fragment_al_view2, container,
				false);
		SharedPreferences settingsPreferences = getActivity().getSharedPreferences(
				Constant.PREFS_NAME, 0);
		int settingsFrequency = settingsPreferences.getInt(Constant.LOCKFREQUENCY, 5);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
		editText = (TextView) view.findViewById(R.id.textView4);
		editText.setText(String.valueOf(settingsFrequency));
		seekBar.setProgress(settingsFrequency);
		setLockButton = (Button) view.findViewById(R.id.button1);
		setUnlockButton = (Button) view.findViewById(R.id.button2);
		vibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
	
		
		mHandler = new Handler() {
			public void handleMessage(Message msg){
				switch (msg.what) {
				case SettingsView.SAVESUCCESS:
					stopThread();
					onVibrate();
					Toast.makeText(getActivity(), R.string.setting_successed, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SettingsView.this.getActivity(), AutoLockService.class);
					getActivity().startService(intent);
					Log.v("SettingsView", "set successfully~~~~~~~~~~~~~~~~~~~~");
					break;
				case SettingsView.SAVEUNSUCESS:
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		setLockButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("vibrator",
						"in settingview oncreateview setlockbutton on click~~~~~~~~~~~~~~~~~~~~~~~~~~");
				Intent intent = new Intent(SettingsView.this.getActivity(), AutoLockService.class);
				getActivity().stopService(intent);
				
				if (monitorThread == null) {
					monitorThread = new MonitorThread(getActivity());
					monitorThread.setSetFlag(true);
					monitorThread.setMode(Constant.MODE_SETTING);
					monitorThread.setHandler(mHandler);
					monitorThread.setLockorUnlock(Constant.SET_LOCK);
					monitorThread.start();
				}
			}
		});

		setUnlockButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsView.this.getActivity(), AutoLockService.class);
				getActivity().stopService(intent);
				
				if (monitorThread == null) {
					monitorThread = new MonitorThread(getActivity());
					monitorThread.setSetFlag(true);
					monitorThread.setMode(Constant.MODE_SETTING);
					monitorThread.setHandler(mHandler);
					monitorThread.setLockorUnlock(Constant.SET_UNLOCK);
					monitorThread.start();
				}
			}
		});

		seekBar.setMax(9);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				int viewNum = progress + 1;
				editText.setText("" + viewNum);
				SharedPreferences settingsPreferences = getActivity().getSharedPreferences(Constant.PREFS_NAME, 0);
				Editor editor = settingsPreferences.edit();
				editor.putInt(Constant.LOCKFREQUENCY, viewNum);
				editor.commit();
			}
		});

		return view;
	}
	
	public void stopThread(){
		monitorThread.setSetFlag(false);
		monitorThread.interrupt();
		monitorThread = null;
	}
	
	public void onVibrate(){
		vibrator.vibrate(Constant.pattern, Constant.repeat);
	}

	public void onStop() {
		super.onStop();
		vibrator.cancel();
	}

}
