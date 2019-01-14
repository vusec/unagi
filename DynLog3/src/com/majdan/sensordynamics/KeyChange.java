package com.majdan.sensordynamics;

import java.io.Serializable;

public abstract class KeyChange implements Serializable, Cloneable {
	
	private Integer key;
	private Long time;
	
	private float[] acc;
	private float[] gy;
	

	public KeyChange(int key, long t, float[] acc, float[] gy) {
		this.key = key;
		this.time = t;
		this.acc = acc.clone();
		this.gy = gy.clone();
	}
	public KeyChange(KeyChange other) {
		this.key = other.key;
		this.time = other.time;
		this.acc = other.acc.clone();
		this.gy = other.gy.clone();
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}
	public float[] getAcc() {
		return acc;
	}

	public void setAcc(float[] acc) {
		this.acc = acc;
	}

	public float[] getGy() {
		return gy;
	}

	public void setGy(float[] gy) {
		this.gy = gy;
	}
	
	
}
