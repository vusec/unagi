package com.example.sensortest;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private SensorManager sensorManager;
	private long lastUpdate;
	private int current = -1;
	private int currentPrevious = -1;
	private Random rnd = new Random();
	private boolean registerSensorData = false;
	
	private LinkedList<SensorReading> [] acc = (LinkedList<SensorReading>[]) new LinkedList [3];
	private LinkedList<SensorReading> [] gy = (LinkedList<SensorReading>[]) new LinkedList [3];
	
	
	{
		resetAccGy();
	}
	
	public void alert(String s) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Title");
		alertDialog.setMessage("Message");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {}
		});
		alertDialog.show();
	}

	private void resetAccGy() {
		acc[0] = new LinkedList<SensorReading>();
		acc[1] = new LinkedList<SensorReading>();
		acc[2] = new LinkedList<SensorReading>();
		
		gy[0] = new LinkedList<SensorReading>();
		gy[1] = new LinkedList<SensorReading>();
		gy[2] = new LinkedList<SensorReading>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();
		
		final MainActivity ma = this;
		
		final EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            //alert("dupa");
	        	new Thread() {
	        		@Override
					public
	        		void run() {
	        			getKeystrokes();
	        		}
	        	}.run();
	        }

	    });
	}

	private Keystroke getKeystroke(int i) {
		
		final Thread thread = Thread.currentThread();
		
		final EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				editText.setText("");
				thread.interrupt();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		TextView text = (TextView) findViewById(R.id.command_text);
		text.setText("Press "+i);
		
		try {
			Thread.sleep(Long.MAX_VALUE);
			return null;
		} catch (InterruptedException e) {
			Keystroke k = new Keystroke(acc, gy);
			resetAccGy();
			return k;
		}
	}

	private int getRandom() {
		return rnd.nextInt(10);
	}
	private Map<Integer, Keystroke> [] getKeystrokes() {
		LinkedHashMap<Integer, Keystroke> [] keystrokes = (LinkedHashMap<Integer, Keystroke>[]) new LinkedHashMap [10];
		LinkedHashSet<Integer>[] left = (LinkedHashSet<Integer>[]) new LinkedHashSet[10];
		for(int i=0; i<10; i++) {
			keystrokes[i] = new LinkedHashMap<Integer, Keystroke>();
			left[i] = new LinkedHashSet<Integer>();
			for(int j=0; j<10; j++) {
				left[i].add(j);
			}
		}
		
		this.current = getRandom();
		registerSensorData = true;
		Keystroke tmp = getKeystroke(this.currentPrevious);

		int leftCounter = 100;
		while(leftCounter > 0) {
			this.currentPrevious = this.current;
			while(true) {
				this.current = getRandom();
				if(!left[current].contains(currentPrevious)) {
					continue;
				} else {
					left[current].remove(currentPrevious);
					Keystroke k = getKeystroke(current);
					keystrokes[current].put(currentPrevious, k);
					break;
				}
			}

			leftCounter--;
		}
		registerSensorData = false;
		
		return keystrokes;
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!registerSensorData)
			return;
		
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

			getAccelerometer(event);
		} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

			getGyroscope(event);
		}
	}

	private void getAccelerometer(SensorEvent event) {
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];

		long time = System.currentTimeMillis();

		//TextView text = (TextView) findViewById(R.id.sensor_data_a_text);
		//TextView textTime = (TextView) findViewById(R.id.sensor_time_text);
		//text.setText("A "+actualTime+":\r\n"+x+",\r\n"+y+",\r\n"+z);
		//textTime.setText("T "+(actualTime-lastUpdate));
		
		acc[0].add(new SensorReading(time, x));
		acc[1].add(new SensorReading(time, y));
		acc[2].add(new SensorReading(time, z));

		lastUpdate = time;
	}

	private void getGyroscope(SensorEvent event) {
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];

		long time = System.currentTimeMillis();

		//TextView text = (TextView) findViewById(R.id.sensor_data_g_text);
		//TextView textTime = (TextView) findViewById(R.id.sensor_time_text);
		//text.setText("G "+actualTime+":\r\n"+x+",\r\n"+y+",\r\n"+z);
		//textTime.setText("T "+(actualTime-lastUpdate));
		
		gy[0].add(new SensorReading(time, x));
		gy[1].add(new SensorReading(time, y));
		gy[2].add(new SensorReading(time, z));

		lastUpdate = time;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}


	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
}
