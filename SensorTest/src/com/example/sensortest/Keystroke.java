package com.example.sensortest;

import java.util.LinkedList;

public class Keystroke {
	private LinkedList<SensorReading> [] acc;
	private LinkedList<SensorReading> [] gy;
	
	public Keystroke(LinkedList<SensorReading> [] acc, LinkedList<SensorReading> [] gy) {
		this.acc = acc;
		this.gy = gy;
	}
	
	public LinkedList<SensorReading>[] getAcc() {
		return acc;
	}

	public void setAcc(LinkedList<SensorReading>[] acc) {
		this.acc = acc;
	}

	public LinkedList<SensorReading>[] getGy() {
		return gy;
	}

	public void setGy(LinkedList<SensorReading>[] gy) {
		this.gy = gy;
	}

}
