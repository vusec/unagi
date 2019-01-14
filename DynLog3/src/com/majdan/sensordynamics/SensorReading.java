package com.majdan.sensordynamics;

import java.io.Serializable;

public class SensorReading implements Serializable {
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	private long time;
	private float value;
	
	public SensorReading(long time, float value) {
		this.time = time;
		this.value = value;
	}
}
