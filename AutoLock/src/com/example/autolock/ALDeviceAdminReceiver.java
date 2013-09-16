package com.example.autolock;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ALDeviceAdminReceiver extends DeviceAdminReceiver {

	public static ComponentName getCn(Context context) {
		return new ComponentName(context, ALDeviceAdminReceiver.class);
	}

	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
	}

	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
	}
}
