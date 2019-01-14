package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public abstract class FreeMLKnn extends FreeML {

	protected int k;
	
	FreeMLKnn(int k, Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		super(training, testing);
		this.k = k;
	}
	
	public abstract float computeDistance(Arrayzable fd1, Arrayzable fd2);

//	@Override
//	public ErrorRate getErrRate(float acceptanceThreshold) {
//		int farIn, farOut, mrIn, mrOut;
//		
//		{ // far
//			int countGood = 0;
//			int countAll = 0;
//			for(Entry<Integer, Collection<? extends Arrayzable>> tsSet : testing.entrySet()) {
//				for(Arrayzable ts : tsSet.getValue()) {
//					float minDistance = Float.MAX_VALUE;
//					
//					Collection<? extends Arrayzable> tr = training.get(ts.getLabel());
//					for(Arrayzable trFd : tr) {
//						float distance = this.computeDistance(trFd, ts);
//						if(distance < minDistance) {
//							minDistance = distance;
//						}
//					}
//					
//					if(minDistance < acceptanceThreshold) {
//						countGood++;
//					}
//					countAll++;
//				}
//			}
//			
//			farIn = countAll-countGood;
//			farOut = countAll;
//		}
//		{ // mr
//			int countWronglyAccepted = 0;
//			int countAll = 0;
//
//			for(Entry<Integer, Collection<? extends Arrayzable>> tsSet : testing.entrySet()) {
//				for(Arrayzable ts : tsSet.getValue()) {
//					for(Map.Entry<Integer, Collection<? extends Arrayzable>> tr : training.entrySet()) {
//						if(tr.getKey() != ts.getLabel()) {
//							float minDistance = Float.MAX_VALUE;
//							for(Arrayzable trFd : tr.getValue()) {
//								float distance = this.computeDistance(trFd, ts);
//								if(distance < minDistance) {
//									minDistance = distance;
//								}
//							}
//							if(minDistance < acceptanceThreshold) {
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
		
		Collection<? extends Arrayzable> trainingObjs = training.get(userId);
		if(trainingObjs.size() < this.k)
			throw new IllegalArgumentException("Number of training objects for user "+userId+" are less than k in kNN");
		
		{ // far
			Collection<? extends Arrayzable> tsSet = testing.get(userId);
			if(tsSet != null) {
				int countGood = 0;
				int countAll = 0;
				
				for(Arrayzable ts : tsSet) {
					PriorityQueue<Float> closestDistances = new PriorityQueue<Float>(this.k, new Comparator<Float>() {
						@Override public int compare(Float arg0, Float arg1) {
							return arg1.compareTo(arg0);
						}
						
					});
					Iterator<? extends Arrayzable> it = trainingObjs.iterator();
					for(int i=0; i<this.k; i++) {
						float distance = this.computeDistance(it.next(), ts);
						closestDistances.add(distance);
					}
					
					while(it.hasNext()) {
						Arrayzable trFd = it.next();
						float distance = this.computeDistance(trFd, ts);
						
						
						if(distance < closestDistances.peek()) {
							closestDistances.poll();
							closestDistances.add(distance);
						}
					}
					
					float average = 0;
					for(Float v : closestDistances) {
						average += v;
					}
					average /= closestDistances.size();
					
					if(average < acceptanceThreshold) {
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
						float minDistance = Float.MAX_VALUE;
						for(Arrayzable trFd : trainingObjs) {
							float distance = this.computeDistance(trFd, ts);
							if(distance < minDistance) {
								minDistance = distance;
							}
						}
						if(minDistance < acceptanceThreshold) {
							countWronglyAccepted++;
						}
						countAll++;
					}
				}
			}
			
			mrIn = countWronglyAccepted;
			mrOut = countAll;
		}
		
		return new ErrorRate(farIn, farOut, mrIn, mrOut);
	}
	
//
//	@Override
//	public ErrorRate getErrRate(float acceptanceThreshold, int userId) {
//		int farIn, farOut, mrIn, mrOut;
//		
//		Collection<? extends Arrayzable> trainingObjs = training.get(userId);
//		
//		{ // far
//			int countGood = 0;
//			int countAll = 0;
//			
//			Collection<? extends Arrayzable> tsSet = testing.get(userId);
//			for(Arrayzable ts : tsSet) {
//				float minDistance = Float.MAX_VALUE;
//				for(Arrayzable trFd : trainingObjs) {
//					float distance = this.computeDistance(trFd, ts);
//					if(distance < minDistance) {
//						minDistance = distance;
//					}
//				}
//				
//				if(minDistance < acceptanceThreshold) {
//					countGood++;
//				}
//				countAll++;
//			}
//			
//			farIn = countAll-countGood;
//			farOut = countAll;
//		}
//		{ // mr
//			int countWronglyAccepted = 0;
//			int countAll = 0;
//
//			for(Entry<Integer, Collection<? extends Arrayzable>> tsSet : testing.entrySet()) {
//				if(tsSet.getKey() != userId) {
//					for(Arrayzable ts : tsSet.getValue()) {
//						float minDistance = Float.MAX_VALUE;
//						for(Arrayzable trFd : trainingObjs) {
//							float distance = this.computeDistance(trFd, ts);
//							if(distance < minDistance) {
//								minDistance = distance;
//							}
//						}
//						if(minDistance < acceptanceThreshold) {
//							countWronglyAccepted++;
//						}
//						countAll++;
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
}
