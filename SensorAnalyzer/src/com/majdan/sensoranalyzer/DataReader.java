package com.majdan.sensoranalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.majdan.sensordynamics.FixedKeystrokeContainer;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.KeyChange;
import com.majdan.sensordynamics.KeyDownChange;
import com.majdan.sensordynamics.KeyUpChange;
import com.majdan.sensordynamics.Keystroke;
import com.majdan.sensordynamics.SensorReading;

public class DataReader {

	LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> getInputData(String phrase) {
		LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> map = new LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>>();
		String path = "/home/student/workspace/kdroid/results/"+phrase+"/";
		System.out.println(path);
//		LinkedHashSet<String> users = new LinkedHashSet<String>();
//		users.add("dario");
//		users.add("davide1");
//		users.add("davide2");
//		users.add("fedrico");
//		users.add("giuseppe");
//		users.add("kamil");
//		users.add("katia");
//		users.add("mario");
//		users.add("marta");
		
		int i = 1;
		for(File folder : new File(path).listFiles()) {
			LinkedHashSet<FixedKeystrokeContainer2> setX = new LinkedHashSet<FixedKeystrokeContainer2>();
			
			for(File file : folder.listFiles()) {
				if(file.isFile()) {
					setX.add(deserialize(file.getPath()));
				}
			}
			
			map.put(i, setX);
			i++;
		}
		
		leaveOnlyLettersCollection(map);
		trimSensorReadingsAllCollection(map);
		
		return map;
	}

	
	private boolean validKey(int key) {
		return
			(key >= 97 && key <= 122) ||
			(key >= 65 && key <= 90) ||
			(key == 32 || key == 46);
	}
	
	private long min(long x, long y) {
		return x < y ? x : y;
	}
	private long max(long x, long y) {
		return x > y ? x : y;
	}
	
	private long timeMargin = 100;
	
	private LinkedList<SensorReading> trimSensorReadings(LinkedList<SensorReading> list, long firstKey, long lastKey) {
		//System.out.println("----------------------");
		LinkedList<SensorReading> newList = new LinkedList<SensorReading>();
		for(SensorReading sr : list) {
			if(sr.getTime() > firstKey && sr.getTime() < lastKey) {
				newList.add(new SensorReading(sr.getTime()-firstKey, sr.getValue()));
				//System.out.println(sr.getTime()-firstKey);
			}
		}
		return newList;
	}
	
	private LinkedList<? extends KeyChange> trimKeyUpDowns(long firstKey, LinkedList<? extends KeyChange> list) {
		LinkedList<KeyChange> newList = new LinkedList<KeyChange>();
		for(KeyChange kc : list) {
			if(kc instanceof KeyDownChange)
				newList.add(new KeyDownChange(kc.getKey(), kc.getTime()-firstKey, kc.getAcc(), kc.getGy()));
			else if(kc instanceof KeyUpChange)
				newList.add(new KeyUpChange(kc.getKey(), kc.getTime()-firstKey, kc.getAcc(), kc.getGy()));
		}
		
		return newList;
	}
	
	private boolean checkAscending(LinkedList<? extends KeyChange> list) {
		KeyChange prev = null;
		for(KeyChange curr : list) {
			if(prev != null) {
				if(prev.getTime() > curr.getTime())
					return false;
			}
			prev = curr;
		}
		
		return true;
	}
	
	private void trimSensorReadingsAll(LinkedHashMap<? extends Object, FixedKeystrokeContainer2> map) {
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : map.entrySet()) {
			LinkedHashSet<FixedKeystrokeContainer2> set = new LinkedHashSet<FixedKeystrokeContainer2>();
			set.add(e.getValue());
			
			LinkedHashMap<Object, Collection<FixedKeystrokeContainer2>> mapIn = new LinkedHashMap<Object, Collection<FixedKeystrokeContainer2>>();
			mapIn.put(e.getKey(), set);
			
			trimSensorReadingsAllCollection(mapIn);
		}
	}
	
	private void trimSensorReadingsAllCollection(Map<? extends Object, Collection<FixedKeystrokeContainer2>> map) {
		for(Collection<FixedKeystrokeContainer2> ce : map.values()) {
			for(FixedKeystrokeContainer2 e : ce) {
				Keystroke k = e.getKeystroke();
				LinkedList<KeyDownChange> kd = e.getKeyDowns();
				LinkedList<KeyUpChange> ku = e.getKeyUps();
				
				if(!checkAscending(ku) || !checkAscending(kd))
					throw new RuntimeException("List is not ascending");
				
				long firstKey = min(kd.getFirst().getTime(), ku.getFirst().getTime());
				long lastKey = max(kd.getLast().getTime(), ku.getLast().getTime());
				firstKey -= timeMargin;
				lastKey += timeMargin;
				
				//System.out.println("========== firstKey: "+firstKey);
				
				e.setKeyDowns((LinkedList<KeyDownChange>) trimKeyUpDowns(firstKey, kd));
				e.setKeyUps((LinkedList<KeyUpChange>) trimKeyUpDowns(firstKey, ku));
				
				k.getAcc()[0] = trimSensorReadings(k.getAcc()[0], firstKey, lastKey);
				k.getAcc()[1] = trimSensorReadings(k.getAcc()[1], firstKey, lastKey);
				k.getAcc()[2] = trimSensorReadings(k.getAcc()[2], firstKey, lastKey);
				
				k.getGy()[0] = trimSensorReadings(k.getGy()[0], firstKey, lastKey);
				k.getGy()[1] = trimSensorReadings(k.getGy()[1], firstKey, lastKey);
				k.getGy()[2] = trimSensorReadings(k.getGy()[2], firstKey, lastKey);
				
				
				//
	
				long firstKeyX = min(e.getKeyDowns().getFirst().getTime(), e.getKeyUps().getFirst().getTime());
				long lastKeyX = max(e.getKeyDowns().getLast().getTime(), e.getKeyUps().getLast().getTime());
				long accXx = k.getAcc()[0].getFirst().getTime();
				long accYx = k.getAcc()[1].getFirst().getTime();
				long accZx = k.getAcc()[2].getFirst().getTime();
				long gyXx = k.getGy()[0].getFirst().getTime();
				long gyYx = k.getGy()[1].getFirst().getTime();
				long gyZx = k.getGy()[2].getFirst().getTime();
				
				//System.out.println(firstKeyX +", "+ lastKeyX +", "+ accXx +", "+ accYx +", "+ accZx +", "+ gyXx +", "+ gyYx +", "+ gyZx);
				
			}
		}
	}
	
	private void leaveOnlyLetters(LinkedHashMap<? extends Object, FixedKeystrokeContainer2> map) {
		for(Entry<? extends Object, FixedKeystrokeContainer2> e : map.entrySet()) {
			LinkedHashSet<FixedKeystrokeContainer2> set = new LinkedHashSet<FixedKeystrokeContainer2>();
			set.add(e.getValue());
			
			LinkedHashMap<Object, Collection<FixedKeystrokeContainer2>> mapIn = new LinkedHashMap<Object, Collection<FixedKeystrokeContainer2>>();
			mapIn.put((Object)e.getKey(), set);
			
			leaveOnlyLettersCollection(mapIn);
		}
	}
	
	private void leaveOnlyLettersCollection(Map<?, Collection<FixedKeystrokeContainer2>> map) {
		for(Collection<FixedKeystrokeContainer2> ce : map.values()) {
			for(FixedKeystrokeContainer2 e : ce) {
				{
					LinkedList<KeyUpChange> keyUps = e.getKeyUps();
					LinkedList<KeyUpChange> newKeyUps = new LinkedList<KeyUpChange>();
					for(KeyUpChange kc : keyUps) {
						if(validKey(kc.getKey()))
							newKeyUps.add(kc);
					}
					e.setKeyUps(newKeyUps);
				} {
					LinkedList<KeyDownChange> keyDowns = e.getKeyDowns();
					LinkedList<KeyDownChange> newkeyDowns = new LinkedList<KeyDownChange>();
					for(KeyDownChange kc : keyDowns) {
						if(validKey(kc.getKey()))
							newkeyDowns.add(kc);
					}
					e.setKeyDowns(newkeyDowns);
				}
			}
		}
	}

	FixedKeystrokeContainer2 deserialize(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			FixedKeystrokeContainer myDeserializedObject = (FixedKeystrokeContainer)ois.readObject();
			
			ois.close();

			return new FixedKeystrokeContainer2(myDeserializedObject);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
