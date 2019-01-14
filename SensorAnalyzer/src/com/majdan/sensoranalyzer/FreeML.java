package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.majdan.sensordynamics.FixedKeystrokeContainer;

public abstract class FreeML extends ML {
	
	protected Map<Integer, Collection<? extends Arrayzable>> training = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>(); 
	protected Map<Integer, Collection<? extends Arrayzable>> testing = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
	protected static MLCreator factory;
	
	FreeML(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		this.training = training;
		this.testing = testing;
	}
	
	private static int partsNr = 5;
	private static float thresold = 0.001f;
	private static int nrLoopThresold = 10;
	
	private ErrorRate getErrRateMy(Float acceptanceThreshold, Boolean eer) {
		int farCount = 0;
		int farCountAll = 0;
		int mrCount = 0;
		int mrCountAll = 0;
		
		Collection<Integer> userIds = training.keySet();
		for(Integer userId : userIds) {
			ErrorRate err;
			if(acceptanceThreshold != null) {
				err = getErrRate(acceptanceThreshold, userId);
			} else {
				if(eer)
					err = getEqRate(userId);
				else
					err = getZeroMissRate(userId);
			}
				
			//System.out.println("user "+userId+": "err);

			farCount += err.getFarCount();
			farCountAll += err.getFarCountAll();
			mrCount += err.getMrCount();
			mrCountAll += err.getMrCountAll();
		}
		
		ErrorRate err = new ErrorRate(farCount, farCountAll, mrCount, mrCountAll);
		return err;
	}
	
	@Override
	public ErrorRate getErrRate(float acceptanceThreshold) {
		return getErrRateMy(acceptanceThreshold, true);
	}
	@Override
	public ErrorRate getEqRate() {
		return getEqRate(null);
	}
	@Override
	public ErrorRate getEqRateUsersSplit() {
		return getErrRateMy(null, true);
	}
	@Override
	public ErrorRate getZeroMissRate() {
		return getZeroMissRate(null);
	}
	@Override
	public ErrorRate getZeroMissRateUserSplit() {
		return getErrRateMy(null, false);
	}
	
	private ErrorRate getEqRate(Integer userId) {
		float left = 0;
		float right = 1;
		int nrPrevErrSame = 0;
		ErrorRate prevErr = null;
		ErrorRate bestErr = null;
		while(nrPrevErrSame < nrLoopThresold) {
			float[] parts = new float[partsNr];
			ErrorRate[] partsValues = new ErrorRate[partsNr];
			float step = (right-left)/(partsNr-1);
			if(step == 0f || left+step==left) return bestErr;
			float sum = left;
			float min = Float.MAX_VALUE;
			int minNr = -1;
			for(int i=0; i<partsNr; i++) {
				parts[i] = sum;
				
				if(userId == null)
					partsValues[i] = getErrRate(sum);
				else
					partsValues[i] = getErrRate(sum, userId);
				//System.out.println("TH: "+sum+" | ERR: "+partsValues[i]);
				
				if(partsValues[i].diff() < min) {
					min = partsValues[i].diff();
					minNr = i;
				}
				
				sum += step;
			}
			if(bestErr == null) {
				bestErr = partsValues[minNr];
			} else if(partsValues[minNr].diff() < bestErr.diff()) {
				bestErr = partsValues[minNr];
			}
			
			if(partsValues[minNr].diff() < thresold) {
				return partsValues[minNr];
			}
			
			if(minNr == 0) {
				//left = left;
				right = parts[1];
			} else if(minNr == partsNr-1) {
				left = parts[partsNr-2];
				//right = right;
			} else {
				left = parts[minNr-1];
				right = parts[minNr+1];
			}
			
			if(partsValues[minNr].equals(prevErr)) {
				nrPrevErrSame++;
			} else {
				nrPrevErrSame = 0;
				prevErr = partsValues[minNr];
			}
		}
		
		return bestErr;
	}

	ErrorRate getZeroMissRate(Integer userId) {
		float left = 0;
		float right = 1;
		int nrPrevErrSame = 0;
		ErrorRate prevErr = null;
		ErrorRate bestErr = null;
		while(nrPrevErrSame < nrLoopThresold) {
			float[] parts = new float[partsNr];
			ErrorRate[] partsValues = new ErrorRate[partsNr];
			float step = (right-left)/(partsNr-1);
			if(step == 0f || left+step==left) return bestErr;
			float sum = left;
			float minTh = Float.POSITIVE_INFINITY;
			int minThNr = -1;
			float maxTh = Float.NEGATIVE_INFINITY;
			int maxThNr = -1;
			for(int i=0; i<partsNr; i++) {
				parts[i] = sum;
				
				if(userId == null)
					partsValues[i] = getErrRate(sum);
				else
					partsValues[i] = getErrRate(sum, userId);
				//System.out.println("   "+parts[i]+" * "+partsValues[i]);
				if(partsValues[i].getMrCount() == 0 && parts[i] > maxTh) {
					maxTh = parts[i];
					maxThNr = i;
				}
				if(partsValues[i].getMrCount() != 0 && parts[i] < minTh) {
					minTh = parts[i];
					minThNr = i;
				}
				
				sum += step;
			}
			bestErr = partsValues[maxThNr];
			//System.out.println(partsValues[maxThNr]);
			//System.out.print(minTh+"("+minThNr+")/"+maxTh+"("+maxThNr+")");
			//System.out.println("  -- "+partsValues[minThNr]+"("+parts[minThNr]+") - "+partsValues[maxThNr]+"("+parts[maxThNr]+")");

			left = parts[minThNr];
			right = parts[maxThNr];
			
			if(bestErr.equals(prevErr)) {
				nrPrevErrSame++;
			} else {
				nrPrevErrSame = 0;
				prevErr = bestErr;
			}
		}
		
		return bestErr;
	}
}
