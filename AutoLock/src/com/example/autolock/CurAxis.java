package com.example.autolock;

public  class CurAxis {
	private float curAxisX = 0;
	private float curAxisY = 0;
	private float curAxisZ = 0;
	
	
	public CurAxis() {
		// TODO Auto-generated constructor stub
	}
	
	public CurAxis(float x, float y, float z) {
		curAxisX = x;
		curAxisY = y;
		curAxisZ = z;
	}

	public void setAxisX(float x) {
		curAxisX = x;
	}

	public void setAxisY(float y) {
		curAxisY = y;
	}

	public void setAxisZ(float z) {
		curAxisZ = z;
	}

	public float getAxisX() {
		return curAxisX;
	}

	public float getAxisY() {
		return curAxisY;
	}

	public float getAxisZ() {
		return curAxisZ;
	}

}
