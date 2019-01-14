package com.majdan.sensordynamics;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.android.inputmethoddl.latin.LatinIME;
import com.android.inputmethoddl.latin.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FixedActivity extends MyActivity {

	private static FixedActivity instance;
	public final static String EXTRA_MESSAGE = "com.majdan.sensordynamics.MESSAGE";
	
	private SensorManager sensorManager;
	private boolean registerSensorData = false;
	
	private Lock accLock = new ReentrantLock();
	private Lock gyLock = new ReentrantLock();
	private LinkedList<SensorReading> [] acc = new LinkedList [3];
	private LinkedList<SensorReading> [] gy = new LinkedList [3];
	
	private float[] lastAcc = new float[3];
	private float[] lastGy = new float[3];
	
	private long firstReadAcc = 0;
	private long countReadAcc = 0;
	private long firstReadGy = 0;
	private long countReadGy = 0;
	
	private LinkedList<KeyChange> keyUps = new LinkedList<KeyChange>();
	private LinkedList<KeyChange> keyDowns = new LinkedList<KeyChange>();
	
	ProgressDialog dialog = null;
	
	int counter = 0;
	
	LinkedHashMap<String, FixedKeystrokeContainer> dataToSend = new LinkedHashMap<String, FixedKeystrokeContainer>(); 

	//"the brown fox is very quick and lives in a cave. he jumps over a dog and a zebra";
	private Collection<String> phrases = new LinkedHashSet<String>();
	
	
	{
		resetData(false);
		phrases.add("satellite");
		phrases.add("internet");
	}
	
	synchronized public void resetCounter(View view) {
		this.counter = 0;
		refreshCounter();
	}
	
	private void incCounter() {
		this.counter++;
	}
	
	public FixedActivity() {
		FixedActivity.instance = this;
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	public static FixedActivity getInstance() {
		return FixedActivity.instance;
	}
	
	private boolean phraseStartsWith(String userInput) {
		for(String s : this.phrases) {
			if(s.startsWith(userInput))
				return true;
		}
		return false;
	}
	private boolean phraseEquals(String userInput) {
		for(String s : this.phrases) {
			if(s.equals(userInput.toLowerCase()))
				return true;
		}
		return false;
	}
	
	synchronized private void refreshCounter() {
		TextView text = (TextView) findViewById(R.id.counter);
		text.setText(""+counter+" / "+dataToSend.size());
	}
	
	synchronized private void resetData(boolean setText) {
		
		synchronized(accLock) { synchronized(gyLock) {
			acc = new LinkedList [3];
			acc[0] = new LinkedList<SensorReading>();
			acc[1] = new LinkedList<SensorReading>();
			acc[2] = new LinkedList<SensorReading>();
			
			gy = new LinkedList [3];
			gy[0] = new LinkedList<SensorReading>();
			gy[1] = new LinkedList<SensorReading>();
			gy[2] = new LinkedList<SensorReading>();
			
			keyUps = new LinkedList<KeyChange>();
			keyDowns = new LinkedList<KeyChange>();
			
			registerSensorData = true;
			
			if(setText) {
				EditText text = (EditText) findViewById(R.id.edit_message);
				if(text != null)
					text.setText("");
			}
		} }
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		System.currentTimeMillis();
		
		refreshCounter();
		
		final EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	setUp();
	        }

	    });
		
		LatinIME.registerKeyListener(this);
		
		//save();
	}
	
	
	synchronized private void setUp() {
		//
		resetData(true);
		
		//
		registerSensorData = true;
		
	}
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!registerSensorData)
			return;
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			synchronized(accLock) {
				if(firstReadAcc == 0) {
					firstReadAcc = System.currentTimeMillis();
				}
				countReadAcc++;
				
				getAccelerometer(event);
			}
			
		} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			synchronized(gyLock) {
				if(firstReadGy== 0) {
					firstReadGy = System.currentTimeMillis();
				}
				countReadGy++;
	
				getGyroscope(event);
			}
		}
	}

	private void getAccelerometer(SensorEvent event) {
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];

		long time = System.currentTimeMillis();

		TextView text = (TextView) findViewById(R.id.sensor_data_a_text);
		TextView textTime = (TextView) findViewById(R.id.sensor_time_text);
		text.setText("A: "+x+",\r\n"+y+",\r\n"+z);
		if(countReadAcc!=0 && countReadGy!=0)
			textTime.setText("T "+time+"  acc:"+(time-firstReadAcc)/countReadAcc+" gy:"+(time-firstReadGy)/countReadGy);
		if(countReadAcc > 500 || countReadGy > 500) {
			firstReadAcc = time;
			countReadAcc = 0;
			firstReadGy = time;
			countReadGy = 0;
		}
		
		acc[0].add(new SensorReading(time, x));
		acc[1].add(new SensorReading(time, y));
		acc[2].add(new SensorReading(time, z));
		
		lastAcc = values.clone();
	}

	private void getGyroscope(SensorEvent event) {
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];

		long time = System.currentTimeMillis();

		TextView text = (TextView) findViewById(R.id.sensor_data_g_text);
		TextView textTime = (TextView) findViewById(R.id.sensor_time_text);
		text.setText("G: "+x+",\r\n"+y+",\r\n"+z);
		if(countReadAcc!=0 && countReadGy!=0)
			textTime.setText("T "+time+"  acc:"+(time-firstReadAcc)/countReadAcc+" gy:"+(time-firstReadGy)/countReadGy);
		if(countReadAcc > 500 || countReadGy > 500) {
			firstReadAcc = time;
			countReadAcc = 0;
			firstReadGy = time;
			countReadGy = 0;
		}
		
		gy[0].add(new SensorReading(time, x));
		gy[1].add(new SensorReading(time, y));
		gy[2].add(new SensorReading(time, z));
		
		lastGy = values.clone();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}


	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void userMadeMistake() {
		alert("You made a mistake, sorry...");
		resetData(true);
	}

	synchronized public void sendMessage(View view) {
//		Intent intent = new Intent(this, DisplayMessageActivity.class);
//		EditText editText = (EditText) findViewById(R.id.edit_message);
//		String message = editText.getText().toString();
//		intent.putExtra(EXTRA_MESSAGE, message);
//		startActivity(intent);
		
		String phraseX = ((EditText) findViewById(R.id.edit_message)).getText().toString();
		if(!phraseEquals(phraseX)) {
			userMadeMistake();
			return;
		}
		
		final EditText text = (EditText) findViewById(R.id.name_message);
		registerSensorData = false;
		synchronized(accLock) { synchronized(gyLock) {
			registerSensorData = false;
			dataToSend.put(FileUploader.getFileName(text.getText().toString()+"__"+phraseX), new FixedKeystrokeContainer(new Keystroke(acc, gy), keyUps, keyDowns));
			incCounter();
			refreshCounter();
			resetData(true);
		} }
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {}
	}
	synchronized public void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.edit_message)).getWindowToken(), 0);
	}
	synchronized public void resetMessage(View view) {
		resetData(true);
	}
	synchronized public void resetDataToSend(View view) {
		resetDataToSend();
		refreshCounter();
	}
	synchronized public void sendAll(View view) {
		int count = FileUploader.save(this, dataToSend);
		resetDataToSend();
		alert("Sent: "+count);
	}
	synchronized private void resetDataToSend() {
		resetCounter(null);
		dataToSend = new LinkedHashMap<String, FixedKeystrokeContainer>();
		refreshCounter();
	}

	@Override
	synchronized public void onMyKeyDown(int keyCode) {
		this.keyDowns.add(new KeyDownChange(keyCode, System.currentTimeMillis(), lastAcc, lastGy));
		Log.v("MX", "keyDOWN");
	}
	@Override
	synchronized public void onMyKeyUp(int keyCode) {
		String text = ((EditText) findViewById(R.id.edit_message)).getText().toString();
		if(!text.equals("") && (keyCode == -5 || !phraseStartsWith(text))) {
			userMadeMistake();
			return;
		}
		
		this.keyUps.add(new KeyUpChange(keyCode, System.currentTimeMillis(), lastAcc, lastGy));
		Log.v("MX", "keyUP");
	}
}






