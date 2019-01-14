package com.majdan.sensordynamics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorEventListener;

@SuppressLint("NewApi")
public abstract class MyActivity extends Activity implements SensorEventListener, KeyListener {

	public void alert(String s) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(s);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   @Override
		public void onClick(DialogInterface dialog, int which) {}
		});
		alertDialog.show();
	}
}
