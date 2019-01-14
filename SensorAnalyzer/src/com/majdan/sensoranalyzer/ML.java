package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.SerializationUtils;

import com.majdan.sensordynamics.FixedKeystrokeContainer;


public abstract class ML {
	abstract ErrorRate getEqRate();
	abstract ErrorRate getEqRateUsersSplit();
	abstract ErrorRate getZeroMissRate();
	abstract ErrorRate getZeroMissRateUserSplit();
	abstract ErrorRate getErrRate(float acceptanceThreshold);
	abstract ErrorRate getErrRate(float acceptanceThreshold, int userId);
	
	public static ErrorRate crossValidation3(MLCreator factory, Map<Integer, Collection<? extends Arrayzable>> map1, Map<Integer, Collection<? extends Arrayzable>> map2, Map<Integer, Collection<? extends Arrayzable>> map3, boolean userSplit) {
		Map<Integer, Collection<? extends Arrayzable>> map12 = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		map12.putAll(map1);
		map12.putAll(map2);
		
		Map<Integer, Collection<? extends Arrayzable>> map13 = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		map13.putAll(map1);
		map13.putAll(map3);
		
		Map<Integer, Collection<? extends Arrayzable>> map23 = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		map23.putAll(map2);
		map23.putAll(map3);
		
		ErrorRate err1;
		ErrorRate err2;
		ErrorRate err3;
		if(userSplit) {
			err1 = factory.createObject(map1, map23).getEqRateUsersSplit();
			err2 = factory.createObject(map2, map13).getEqRateUsersSplit();
			err3 = factory.createObject(map3, map12).getEqRateUsersSplit();
		} else {
			err1 = factory.createObject(map1, map23).getEqRate();
			err2 = factory.createObject(map2, map13).getEqRate();
			err3 = factory.createObject(map3, map12).getEqRate();
		}
		
		ErrorRate result = new ErrorRate(
			err1.getFarCount() + err2.getFarCount() + err3.getFarCount(),
			err1.getFarCountAll() + err2.getFarCountAll() + err3.getFarCountAll(),
			err1.getMrCount() + err2.getMrCount() + err3.getMrCount(),
			err1.getMrCountAll() + err2.getMrCountAll() + err3.getMrCountAll()
		);
		
		return result;
	}
	
	public static ErrorRate leaveOneOut(MLCreator factory, Map<Integer, Collection<? extends Arrayzable>> map, boolean userSplit, boolean eer) {
		return ML.leaveOneOutAttack(factory, map, userSplit, eer);
	}
	
	
	public static ErrorRate leaveOneOutAttack(MLCreator factory, Map<Integer, Collection<? extends Arrayzable>> map, boolean userSplit, boolean eer) {
		int farCount=0, farCountAll=0, mrCount=0, mrCountAll=0;
		Float[] weights = FixedAnalyzer.computeWeights(map);
		
		for(Entry<Integer, Collection<? extends Arrayzable>> e : map.entrySet()) {
			//for with all the participants
			//"e" corresponds to each participant
			//System.out.println("User "+e.getKey()+" /"+map.size());
			for(Arrayzable a : e.getValue()) {
				//System.out.println("  case ? /"+e.getValue().size());
				// training remainins identical with leaveOneOut
				Map<Integer, Collection<? extends Arrayzable>> trainingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();

				Collection<? extends Arrayzable> trainingSet = new LinkedHashSet<Arrayzable>(e.getValue());
				trainingSet.remove(a);
				
				trainingMap.put(e.getKey(), trainingSet);
				
				
				
				//create deep copy
				Map<Integer, Collection<? extends Arrayzable>> testingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
				for(Entry<Integer, Collection<? extends Arrayzable>> entry : map.entrySet()) {
					Integer key = new Integer(entry.getKey());
					Collection<Arrayzable> value = new LinkedHashSet<Arrayzable>();
					for (Arrayzable az : entry.getValue()) {
						value.add((Arrayzable)SerializationUtils.deserialize(SerializationUtils.serialize(az)));
					}
					testingMap.put(key, value);
				}
				testingMap.remove(e.getKey());
				
				StatisticalAttack.createFakeInputs(testingMap, weights);
				
				
				
				Collection<Arrayzable> testingSetNext = new LinkedHashSet<Arrayzable>();
				testingSetNext.add(a);
				
				testingMap.put(e.getKey(), testingSetNext);
				
				// computing error rate
				ErrorRate err;
				if(eer) {
					if(userSplit)
						err = factory.createObject(trainingMap, testingMap).getEqRateUsersSplit();
					else
						err = factory.createObject(trainingMap, testingMap).getEqRate();
				} else {
					if(userSplit)
						err = factory.createObject(trainingMap, testingMap).getZeroMissRateUserSplit();
					else
						err = factory.createObject(trainingMap, testingMap).getZeroMissRate();
				}
				
				farCount += err.getFarCount();
				farCountAll += err.getFarCountAll();
				
				mrCount += err.getMrCount(); 
				mrCountAll += err.getMrCountAll();
				
				//return new ErrorRate(farCount, farCountAll, mrCount, mrCountAll);
			}
		}
		
		//System.err.println(new ErrorRate(farCount, farCountAll, mrCount, mrCountAll));
		return new ErrorRate(farCount, farCountAll, mrCount, mrCountAll);
	}
	
	
	
	public static ErrorRate leaveOneOutWrong(MLCreator factory, Map<Integer, Collection<? extends Arrayzable>> map, boolean userSplit) {
		int farCount=0, farCountAll=0, mrCount=0, mrCountAll=0;
		{ // far
			for(Entry<Integer, Collection<? extends Arrayzable>> e : map.entrySet()) {
				for(Arrayzable a : e.getValue()) {
					Map<Integer, Collection<? extends Arrayzable>> trainingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
					Map<Integer, Collection<? extends Arrayzable>> testingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();

					Collection<? extends Arrayzable> trainingSet = new LinkedHashSet<Arrayzable>(e.getValue());
					trainingSet.remove(a);
					
					Collection<Arrayzable> testingSet = new LinkedHashSet<Arrayzable>();
					testingSet.add(a);
					
					
					trainingMap.put(e.getKey(), trainingSet);
					testingMap.put(e.getKey(), testingSet);
					
					ErrorRate err;
					if(userSplit)
						err = factory.createObject(trainingMap, testingMap).getEqRateUsersSplit();
					else
						err = factory.createObject(trainingMap, testingMap).getEqRate();
					
					farCount += err.getFarCount();
					farCountAll += err.getFarCountAll();
				}
			}
		}
		{ // mr
			for(Entry<Integer, Collection<? extends Arrayzable>> e : map.entrySet()) {
				Map<Integer, Collection<? extends Arrayzable>> trainingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
				Map<Integer, Collection<? extends Arrayzable>> testingMap = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>(map);
				
				trainingMap.put(e.getKey(), e.getValue());
				testingMap.remove(e.getKey());

				ErrorRate err;
				if(userSplit)
					err = factory.createObject(trainingMap, testingMap).getEqRateUsersSplit();
				else
					err = factory.createObject(trainingMap, testingMap).getEqRate();
				
				mrCount += err.getMrCount();
				mrCountAll += err.getMrCountAll();
			}
		}
		
		Map<Integer, Collection<? extends Arrayzable>> mapCopy = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		
		return new ErrorRate(farCount, farCountAll, mrCount, mrCountAll);
	}
}
