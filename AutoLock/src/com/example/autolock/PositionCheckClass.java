package com.example.autolock;

import java.util.List;

import android.util.Log;

public class PositionCheckClass {

	public int positionCheck(float x, float y, float z) {

		Log.v("SensorData", "in position function X ==" + x + "||||||||y=" + y
				+ "||||||||||z=" + z);

		if (isLockPosition(x, y, z)) {
			Log.v("PositionCheck",
					"~~~~~~~~return position_lock~~~~~~~~~~~~~~~~~~~~~~");
			return Constant.POSITION_LOCK;
		}
		if (isUnlockPosition(x, y, z)) {
			Log.v("PositionCheck",
					"~~~~~~~~return position_unlock~~~~~~~~~~~~~~~~~~~~~~");
			return Constant.POSITION_UNLOCK;
		}
		Log.v("PositionCheck",
				"~~~~~~~~~~return position_NONEPOSITION~~~~~~~~~~~~~~~~~~~~");
		return Constant.NONEPOSITION;
	}

	public boolean isLockPosition(float x, float y, float z) {
		float axisX = x;
		float axisY = y;
		float axisZ = z;

		if (axisX < Constant.lockAxisX_max && axisX > Constant.lockAxisX_min
				&& axisY < Constant.lockAxisY_max
				&& axisY > Constant.lockAxisY_min
				&& axisZ < Constant.lockAxisZ_max
				&& axisZ > Constant.lockAxisZ_min) {
			Log.v("autolock",
					"the device is in unlock posture~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return true;
		} else {
			return false;
		}
	}

	public boolean isUnlockPosition(float x, float y, float z) {

		float axisX = x;
		float axisY = y;
		float axisZ = z;

		if (axisX < Constant.unlockAxisX_max
				&& axisX > Constant.unlockAxisX_min
				&& axisY < Constant.unlockAxisY_max
				&& axisY > Constant.unlockAxisY_min
				&& axisZ < Constant.unlockAxisZ_max
				&& axisZ > Constant.unlockAxisZ_min) {
			Log.v("autolock",
					"the device is in unlock posture~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			return true;
		} else {
			return false;
		}

	}

	public boolean isHold(List<CurAxis> q) {
		// /检测20组数据是否变化不大，变化不大，求平均值保存，返回True

		float axisX = 0;
		float axisY = 0;
		float axisZ = 0;

		float xMax = 0;
		float xMin = 0;
		float yMax = 0;
		float yMin = 0;
		float zMax = 0;
		float zMin = 0;

		Log.v("PositionCheck", "in is hold function queue's size is =========="
				+ q.size());
		for (int i = 0; i < q.size(); i++) {
			CurAxis curAxis = new CurAxis();
			curAxis = q.get(i);
			axisX = curAxis.getAxisX();
			axisY = curAxis.getAxisY();
			axisZ = curAxis.getAxisZ();
			if (i == 0) {
				xMax = axisX;
				xMin = axisX;
				yMax = axisY;
				yMin = axisY;
				zMax = axisZ;
				zMin = axisZ;
			}

			xMax = xMax > axisX ? xMax : axisX;
			xMin = xMin > axisX ? axisX : xMin;

			yMax = yMax > axisY ? yMax : axisY;
			yMin = yMin > axisY ? axisY : yMin;

			zMax = zMax > axisZ ? zMax : axisZ;
			zMin = zMin > axisZ ? axisZ : zMin;

			Log.v("PositionCheck",
					"in is hold function queue's poll function , i =========="
							+ i);

			Log.v("PositionCheck", "xMax ============" + xMax);
			Log.v("PositionCheck", "xMin ============" + xMin);
			Log.v("PositionCheck", "yMax ============" + yMax);
			Log.v("PositionCheck", "yMin ============" + yMin);
			Log.v("PositionCheck", "zMax ============" + zMax);
			Log.v("PositionCheck", "zMin ============" + zMin);
		}

		if ((xMax - xMin) < 1 && (yMax - yMin) < 1 && (zMax - zMin) < 1) {
			return true;
		}

		return false;
	}

	public CurAxis getPostureAxis(List<CurAxis> queue) {
		CurAxis curAxis = new CurAxis();
		float xSum = 0;
		float ySum = 0;
		float zSum = 0;

		for (int i = 0; i < queue.size(); i++) {
			curAxis = queue.get(i);
			xSum += curAxis.getAxisX();
			ySum += curAxis.getAxisY();
			zSum += curAxis.getAxisZ();
		}

		curAxis.setAxisX(xSum / queue.size());
		curAxis.setAxisY(ySum / queue.size());
		curAxis.setAxisZ(zSum / queue.size());

		return curAxis;
	}

}
