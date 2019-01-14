package com.android.inputmethoddl.latin;

import android.annotation.SuppressLint;
import android.inputmethodservice.KeyboardView;
import android.util.Log;

@SuppressLint("NewApi")
public class MyOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener
{
	private static final String TAG = "MyOnKeyboardActionListener";
	private static final String keylogFileName = "keystrokedynamics.txt";
	
	private KeyStrokeDiagram currentDiagram = null;
	
	
	public void enableSensors()
	{
		
	}
	
	public void disableSensors()
	{
		
	}
	
	
	public MyOnKeyboardActionListener()
	{
		Log.d(TAG, "MyOnKeyboardActionListener()");
	}
	
	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		// TODO Majdan
		Log.v("MAJDAN", "MyOnKeyboardActionListener.onKey");
	}

	@Override
	public void onPress(int primaryCode) {
		// TODO Majdan
		Log.v("MAJDAN", "MyOnKeyboardActionListener.onPress");
	}

	@Override
	public void onRelease(int primaryCode) {
		Log.d(TAG, "onRelease()");
		
	}

	@Override
	public void onText(CharSequence text) {
		// TODO Auto-generated method stub
	}

	@Override
	public void swipeDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeUp() {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
    /**
     * Data structure used to save keystroke dynamics data
     * 
     * @author marthijn
     *
     */
    private class KeyStrokeDiagram
    {
    	public int keyCode1 = 0;
    	public int keyCode2 = 0;
    	public char keyLabel1 = ' ';
    	public char keyLabel2 = ' ';
    	
    	public long keyCode1TimeStamp = 0; // keydown of the first key
    	public long keyCode2TimeStamp = 0; // keydown of the successive key
    	
    	public long keyCode1KeyUpTimeStamp = 0; // keyup of the first key
    	
    	public float acceleration; // acceleration
    	public float accelerationGravity; // acceleration including gravity
    	
    	private int latestBatteryStatus;
    	
    	private double latitude;
    	private double longitude;
    	
    	public String programName;
    	
    	public float accel_x, accel_y, accel_z;
        public float mag_x, mag_y, mag_z;
        public float ori_x, ori_y, ori_z;
    }

}