package com.example.autolock;

public class Constant {

	public static boolean isServiceOn = false;
	public static boolean isLock = false;
	public static int timeIntervals = 200;
	public static int CHECK_COUNT = 2;
	public static int setTimeIntervals = 50;
	public static int SET_CHECK_COUNT = 20;
	
	public static float variation = 2;

	public static float lockAxisX_max = 0;
	public static float lockAxisX_min = 0;
	public static float lockAxisY_max = 0;
	public static float lockAxisY_min = 0;
	public static float lockAxisZ_max = -7;
	public static float lockAxisZ_min = 0;

	public static float unlockAxisX_max = 1;
	public static float unlockAxisX_min = 0;
	public static float unlockAxisY_max = 4;
	public static float unlockAxisY_min = 3;
	public static float unlockAxisZ_max = 100;
	public static float unlockAxisZ_min = 8;

	public static float curAxisX = 0;
	public static float curAxisY = 0;
	public static float curAxisZ = 0;

	public static long[] pattern = { 0, 600, 0 };
	public static int repeat = -1;

	public static String explanation = "allow the permission";
	public static final String PREFS_NAME = "AutoLockPrefsFile";
	public static final String SERVICE_NAME = "com.example.autolock.AutoLockService";
	
	public static final String LOCKAXISX = "LOCKAXISX";
	public static final String LOCKAXISY = "LOCKAXISY";
	public static final String LOCKAXISZ = "LOCKAXISZ";
	public static final String UNLOCKAXISX = "UNLOCKAXISX";
	public static final String UNLOCKAXISY = "UNLOCKAXISY";
	public static final String UNLOCKAXISZ = "UNLOCKAXISZ";
	public static final String LOCKFREQUENCY = "LOCKFREQUENCY";
	
	
	public static final int POSITION_LOCK = 0;
	public static final int POSITION_UNLOCK = 1;
	public static final int NONEPOSITION = -1;
	public static final int MODE_SETTING = 1;
	public static final int MODE_ONSERVICE = 0;
	public static final int SET_LOCK = 1;
	public static final int SET_UNLOCK = 0;

}
