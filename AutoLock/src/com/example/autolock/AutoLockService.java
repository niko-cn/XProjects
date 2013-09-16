package com.example.autolock;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class AutoLockService extends Service {

	private MonitorThread monitorThread = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.v("test",
				"in autolockservice on create ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("test",
				"in autolockservice onstartcommand~~~~~~~~~~~~~~~~~~~~~~~");
		if (monitorThread == null) {

			float defValue = 0;
			SharedPreferences settingsPreferences = getSharedPreferences(
					Constant.PREFS_NAME, 0);
			float lockAxisX = settingsPreferences.getFloat(Constant.LOCKAXISX,
					defValue);
			float lockAxisY = settingsPreferences.getFloat(Constant.LOCKAXISY,
					defValue);
			float lockAxisZ = settingsPreferences.getFloat(Constant.LOCKAXISZ,
					defValue);
			float unLockAxisX = settingsPreferences.getFloat(
					Constant.UNLOCKAXISX, defValue);
			float unLockAxisY = settingsPreferences.getFloat(
					Constant.UNLOCKAXISY, defValue);
			float unLockAxisZ = settingsPreferences.getFloat(
					Constant.UNLOCKAXISZ, defValue);
			Constant.timeIntervals = 500 / settingsPreferences.getInt(
					Constant.LOCKFREQUENCY, 3);
			Constant.variation = 4 * settingsPreferences.getInt(
					Constant.LOCKFREQUENCY, 5) / 10;

			Constant.lockAxisX_max = lockAxisX + Constant.variation / 2;
			Constant.lockAxisX_min = lockAxisX - Constant.variation / 2;
			Constant.lockAxisY_max = lockAxisY + Constant.variation / 2;
			Constant.lockAxisY_min = lockAxisY - Constant.variation / 2;
			Constant.lockAxisZ_max = lockAxisZ + Constant.variation / 2;
			Constant.lockAxisZ_min = lockAxisZ - Constant.variation / 2;

			Constant.unlockAxisX_max = unLockAxisX + Constant.variation / 2;
			Constant.unlockAxisX_min = unLockAxisX - Constant.variation / 2;
			Constant.unlockAxisY_max = unLockAxisY + Constant.variation / 2;
			Constant.unlockAxisY_min = unLockAxisY - Constant.variation / 2;
			Constant.unlockAxisZ_max = unLockAxisZ + Constant.variation / 2;
			Constant.unlockAxisZ_min = unLockAxisZ - Constant.variation / 2;

			monitorThread = new MonitorThread(this);
			monitorThread.setFlag(true);
			monitorThread.setMode(Constant.MODE_ONSERVICE);
			monitorThread.start();
		}
		return START_STICKY;
	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public void onDestroy() {
		Log.v("onDestroy", "+++++++++++ on Destroy +++++++++++++++");
		Constant.isServiceOn = false;
		monitorThread.stopSensor();
		monitorThread.setFlag(false);
		monitorThread.interrupt();
		monitorThread = null;
		super.onDestroy();

	}

}
