package com.majdan.sensordynamics;

public class KeyUpChange extends KeyChange {
	public KeyUpChange(int key, long t, float[] acc, float[] gy) {
		super(key, t, acc, gy);
	}
	public KeyUpChange(KeyUpChange other) {
		super(other);
	}
}
