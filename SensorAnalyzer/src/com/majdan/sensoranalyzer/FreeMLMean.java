package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class FreeMLMean extends FreeML {
	
	FreeMLMean(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		super(training, testing);
	}

	public abstract float computeDistance(Arrayzable fd1, Arrayzable fd2);
	
//	@Override
//	public ErrorRate getErrRate(float acceptanceThreshold) {
//		int farIn, farOut, mrIn, mrOut;
//		LinkedHashMap<Integer, FreeData> trainingMap = new LinkedHashMap<Integer, FreeData>();
//		for(Map.Entry<Integer, Collection<FreeData>> e : training.entrySet()) {
//			trainingMap.put(e.getKey(), new FreeData(e.getKey(), e.getValue()));
//		}
//		
//		{ // far
//			int countGood = 0;
//			int countAll = 0;
//			for(Entry<Integer, Collection<FreeData>> tsSet : testing.entrySet()) {
//				for(FreeData ts : tsSet.getValue()) {
//					FreeData trX = trainingMap.get(ts.getLabel());
//					float distance = this.computeDistance(trX, ts);
//					
//					if(distance < acceptanceThreshold) {
//						countGood++;
//					}
//					countAll++;
//				}
//			}
//			farIn = countAll-countGood;
//			farOut = countAll;
//		}
//		{ // mr
//			int countWronglyAccepted = 0;
//			int countAll = 0;
//			for(Entry<Integer, Collection<FreeData>> tsSet : testing.entrySet()) {
//				for(FreeData ts : tsSet.getValue()) {
//					for(Map.Entry<Integer, FreeData> tr : trainingMap.entrySet()) {
//						if(ts.getLabel() != tr.getKey()) {
//							float distance = this.computeDistance(tr.getValue(), ts);
//							if(distance < acceptanceThreshold) {
//								countWronglyAccepted++;
//							}
//							countAll++;
//						}
//					}
//				}
//			}
//			
//			mrIn = countWronglyAccepted;
//			mrOut = countAll;
//		}
//		
//		return new ErrorRate(farIn, farOut, mrIn, mrOut);
//	}
	@Override
	public ErrorRate getErrRate(float acceptanceThreshold, int userId) {
		int farIn=0, farOut=0, mrIn=0, mrOut=0;

		FreeData trainingObj = new FreeData(userId, training.get(userId));
		
		{ // far
			Collection<? extends Arrayzable> tsSet = testing.get(userId);
			
			if(tsSet != null) {
				int countGood = 0;
				int countAll = 0;
				for(Arrayzable ts : tsSet) {
					float distance = this.computeDistance(trainingObj, ts);
					
					if(distance < acceptanceThreshold) {
						countGood++;
					}
					countAll++;
				}
				farIn = countAll-countGood;
				farOut = countAll;
			}
		}
		{ // mr
			int countWronglyAccepted = 0;
			int countAll = 0;
			for(Entry<Integer, Collection<? extends Arrayzable>> tsSet : testing.entrySet()) {
				if(tsSet.getKey() != userId) {
					for(Arrayzable ts : tsSet.getValue()) {
						float distance = this.computeDistance(trainingObj, ts);
						if(distance < acceptanceThreshold) {
							countWronglyAccepted++;
						}
						countAll++;
					}
				}
			}
			
			mrIn = countWronglyAccepted;
			mrOut = countAll;
		}
		
		//System.err.println(new ErrorRate(farIn, farOut, mrIn, mrOut));
		return new ErrorRate(farIn, farOut, mrIn, mrOut);
	}
}
