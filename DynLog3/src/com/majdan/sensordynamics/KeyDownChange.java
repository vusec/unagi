package com.majdan.sensordynamics;

public class KeyDownChange extends KeyChange {
	public KeyDownChange(int key, long t, float[] acc, float[] gy) {
		super(key, t, acc, gy);
	}
	public KeyDownChange(KeyDownChange other) {
		super(other);
	}
}
