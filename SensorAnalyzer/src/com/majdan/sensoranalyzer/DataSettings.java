package com.majdan.sensoranalyzer;

public class DataSettings {
	private boolean sensors;
	private boolean keystrokeKeyPress;
	private boolean keystrokeKeyRelease;
	private boolean keystrokeKeyUpUp;
	private boolean keystrokeKeyDownDown;
	private boolean allKeystrokes;
	
	public DataSettings(boolean sensors, boolean keystrokeKeyPress, boolean keystrokeKeyRelease, boolean keystrokeKeyUpUp, boolean keystrokeKeyDownDown, boolean allKeystrokes) {
		this.sensors = sensors;
		this.keystrokeKeyPress = keystrokeKeyPress;
		this.keystrokeKeyRelease = keystrokeKeyRelease;
		this.keystrokeKeyUpUp = keystrokeKeyUpUp;
		this.keystrokeKeyDownDown = keystrokeKeyDownDown;
		this.allKeystrokes = allKeystrokes;
	}
	
	public boolean isSensors() {
		return sensors;
	}
	public boolean isKeystrokeKeyPress() {
		return keystrokeKeyPress;
	}
	public boolean isKeystrokeKeyRelease() {
		return keystrokeKeyRelease;
	}
	public boolean isKeystrokeKeyUpUp() {
		return keystrokeKeyUpUp;
	}
	public boolean isKeystrokeKeyDownDown() {
		return keystrokeKeyDownDown;
	}
	public boolean isAllKeystrokes() {
		return allKeystrokes;
	}
	
	
}
