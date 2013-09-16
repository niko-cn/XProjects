package com.example.autolock;

import java.util.LinkedList;
import java.util.List;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class MonitorThread extends Thread {

	private SensorManager sensorManager;
	private Sensor sensor;
	private SensorEventListener sensorEventListener;
	private DevicePolicyManager devicePolicyManager;
	private KeyguardManager keyguardManager;
	private Context mContext;
	private PositionCheckClass positionCheckClass = new PositionCheckClass();
	private boolean ISSERVICEON = false;
	private int onMode = 0;
	private boolean flag_set = true;
	private int isLock = 0;
	private List<CurAxis> queue = new LinkedList<CurAxis>();
	private CurAxis setAxis = new CurAxis();
	private Handler mHandler = new Handler();
	private int continualCount = 0;

	public MonitorThread(Context context) {
		mContext = context;
	}

	public void run() {
		Log.v("test", "~~~~~~~~~in thread run ~~~~~~~~~~~~");
		devicePolicyManager = (DevicePolicyManager) mContext
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		new ComponentName(mContext, ALDeviceAdminReceiver.class);

		sensorManager = (SensorManager) mContext
				.getSystemService(Service.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorEventListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
					Constant.curAxisX = event.values[0];
					Constant.curAxisY = event.values[1];
					Constant.curAxisZ = event.values[2];
					Log.v("SensorData", "on sensorChanged x =="
							+ Constant.curAxisX + "|||||||||||y =="
							+ Constant.curAxisY + "||||||||||z == "
							+ Constant.curAxisZ);
				} else {
					Log.v("SensorData", "on sensorChanged  ==  this is not sensor.TYPE_ACCELEROMETER");
				}
			}

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		};
		sensorManager.registerListener(sensorEventListener, sensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		switch (onMode) {
		case Constant.MODE_ONSERVICE:
			onServiceMode();
			break;
		case Constant.MODE_SETTING:
			onSettingMode();
			break;

		default:
			break;
		}

	}

	public void stopSensor() {
		sensorManager.unregisterListener(sensorEventListener, sensor);
	}

	public void onServiceMode() {
		while (ISSERVICEON) {
			Log.v("MonitorThread",
					"~~~~~~~~~~~~~thread is running~~~~~~~~~~~on serviceon Mode");
			setLock();
			try {
				Thread.sleep(Constant.timeIntervals);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void onSettingMode() {
		while (flag_set) {
			Log.v("MonitorThread",
					"~~~~!!!!!!!!!!!!! thread is running on settingmode");

			CurAxis curAxis = new CurAxis(Constant.curAxisX, Constant.curAxisY,
					Constant.curAxisZ);

			if (queue.size() < Constant.SET_CHECK_COUNT) {
				((LinkedList<CurAxis>) queue).addLast(curAxis);
				Log.v("MonitorThread",
						" on Setting Mode queue size is ============ "
								+ queue.size());

			} else {
				((LinkedList<CurAxis>) queue).addLast(curAxis);
				((LinkedList<CurAxis>) queue).removeFirst();
				Log.v("MonitorThread",
						" on Setting Mode queue size should be  ============ "
								+ queue.size());
				setSetting(queue);
			}
			try {
				Thread.sleep(Constant.setTimeIntervals);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setSetting(List<CurAxis> q) {
		Log.v("MonitorThread", "queue.size =====================" + q.size());
		boolean ishold = positionCheckClass.isHold(q);
		Log.v("MonitorThread", "is hold ========================" + ishold);
		Log.v("MonitorThread", "queue.size =====================" + q.size());
		if (ishold) {
			Message message = new Message();
			setAxis = positionCheckClass.getPostureAxis(queue);
			if (saveSetting()) {
				message.what = 1;
			} else {
				message.what = 0;
			}
			mHandler.sendMessage(message);
		}
	}

	public boolean saveSetting() {
		Log.v("monitorThread",
				"---------------in savesetting ---------------------------");

		SharedPreferences settings = mContext.getSharedPreferences(
				Constant.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		switch (isLock) {
		case Constant.SET_LOCK:
			editor.putFloat(Constant.LOCKAXISX, setAxis.getAxisX());
			editor.putFloat(Constant.LOCKAXISY, setAxis.getAxisY());
			editor.putFloat(Constant.LOCKAXISZ, setAxis.getAxisZ());
			editor.commit();
			Log.v("MonitorThread",
					"---------------in savesetting -------------------------set lock successed--");
			break;
		case Constant.SET_UNLOCK:
			editor.putFloat(Constant.UNLOCKAXISX, setAxis.getAxisX());
			editor.putFloat(Constant.UNLOCKAXISY, setAxis.getAxisY());
			editor.putFloat(Constant.UNLOCKAXISZ, setAxis.getAxisZ());
			editor.commit();
			Log.v("MonitorThread",
					"---------------in savesetting ---------------------------set unlock successed--");
			break;

		default:
			break;
		}
		return true;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void setFlag(boolean flag) {
		ISSERVICEON = flag;
	}

	public void setSetFlag(boolean flag) {
		flag_set = flag;
	}

	public void setLockorUnlock(int islock) {
		isLock = islock;
	}

	public void setMode(int mode) {
		onMode = mode;
	}

	public boolean isLockScreen() {
		keyguardManager = (KeyguardManager) mContext
				.getSystemService(Context.KEYGUARD_SERVICE);
		if (keyguardManager.inKeyguardRestrictedInputMode()) {
			Constant.isLock = true;
		} else {

			Constant.isLock = false;
		}
		Log.v("MonitorThread", "lock status is ===================="
				+ Constant.isLock);
		return Constant.isLock;
	}

	public void setLock() {
		int posture = positionCheckClass.positionCheck(Constant.curAxisX,
				Constant.curAxisY, Constant.curAxisZ);
		Log.v("MonitorThread",
				"~~~~~~~~~~~~~~in set lock posture================" + posture);
		if (posture != Constant.NONEPOSITION) {
			continualCount++;
			Log.v("MonitorThread", "~~~~~~~~~~~~~continualcount ========= "
					+ continualCount);
			if (continualCount == Constant.CHECK_COUNT) {
				Log.v("MonitorThread",
						"~~~~~~~~~~~~~~~~~~~~~in setlock continualCount == Check_count~~~~~~~~~~posture=="
								+ posture);
				switch (posture) {
				case Constant.POSITION_LOCK:
					lockScreen();
					break;
				case Constant.POSITION_UNLOCK:
					unlockScreen();
					break;
				default:
					break;
				}
			}
		} else {
			continualCount = 0;
		}
	}

	public void lockScreen() {
		Log.v("MonitorThread", "in monitorthread lock screen~~~~~~~~~~~~~~~");

		PowerManager powerManager = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "lock");
		wakeLock.acquire();

		Log.v("MonitorThread",
				"lock Screen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		 devicePolicyManager.lockNow();
	}

	@SuppressWarnings("deprecation")
	public void unlockScreen() {
		Log.v("MonitorThread", "in monitorthread unlock screen~~~~~~~~~~~~~~~");
		PowerManager powerManager = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP, "unlock");
		// if (isLockScreen()) {
		wakeLock.acquire();
		wakeLock.release();

		// KeyguardLock keyguardLock =
		// keyguardManager.newKeyguardLock("unlock");
		// keyguardLock.disableKeyguard();
		//
		// keyguardLock.reenableKeyguard();

		Log.v("MonitorThread",
				"unlock Screen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// }
		// else {
		// Log.v("MonitorThread",
		// "is on unlock Screen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// }

	}

}
