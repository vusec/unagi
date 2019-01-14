package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.analysis.function.Gaussian;

public abstract class FreeMLNaiveBayes extends FreeML {

	private Map<Integer, Gaussian[]> gaussian = new LinkedHashMap<Integer, Gaussian[]>();
	
	public FreeMLNaiveBayes(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		super(training, testing);
		this.gaussian = computeGaussian(training);
	}
	
	public static float computeDistance(Gaussian g, float v) {
		return Arrayzable.scaleNaiveBayes((float)g.value(v) );
	}
	
	public static Map<Integer, Gaussian[]> computeGaussian(Map<Integer, Collection<? extends Arrayzable>> training) {
		int size = training.values().iterator().next().iterator().next().getArrayData().length;
		
		Map<Integer, Gaussian[]> result = new LinkedHashMap<Integer, Gaussian[]>();
		
		for(Entry<Integer, Collection<? extends Arrayzable>> e : training.entrySet()) {
			float[] newMean = new float[size];
			float[] newVariance = new float[size];
			Gaussian[] newGaussian = new Gaussian[size];
			
			for(int i=0; i<size; i++) {
				newMean[i] = 0f;
				newVariance[i] = 0f;
			}
			
			for(Arrayzable fd : e.getValue()) {
				Float[] arr = fd.getArrayData();
				for(int i=0; i<arr.length; i++) {
					newMean[i] += arr[i];
				}
			}
			for(int i=0; i<newMean.length; i++) {
				newMean[i] /= e.getValue().size();
			}
			
			for(Arrayzable fd : e.getValue()) {
				Float[] arr = fd.getArrayData();
				for(int i=0; i<arr.length; i++) {
					newVariance[i] += (newMean[i]-arr[i])*(newMean[i]-arr[i]);
				}
			}
			for(int i=0; i<newVariance.length; i++) {
//				if(newVariance[i] == 0) {
//					System.err.println("!!!!!!! "+i);
//				}
				newVariance[i] /= e.getValue().size();
				//System.out.print(newVariance[i]+", ");
				
				if(newVariance[i] == 0)
					newVariance[i] = Float.MIN_VALUE;
			}
			//System.out.println();

			for(int i=0; i<newGaussian.length; i++) {
				newGaussian[i] = new Gaussian(newMean[i], Math.sqrt(newVariance[i]));
			}
			
			result.put(e.getKey(), newGaussian);
		}
		
		return result;
	}
	
	abstract float distance(Arrayzable fd, Gaussian[] g);

	@Override
	public ErrorRate getErrRate(float acceptanceThreshold, int userId) {
		int farCount = 0;
		int farCountAll = 0;
		int mrCount = 0;
		int mrCountAll = 0;
		
		Gaussian[] thisGaussian = this.gaussian.get(userId);
		{ // far
			Collection<? extends Arrayzable> tsSet = this.testing.get(userId);
			
			if(tsSet != null) {
				int countGood = 0;
				int countAll = 0;
				for(Arrayzable e : tsSet) {
					float prob = distance(e, thisGaussian);
					if(prob < acceptanceThreshold) {
						countGood++;
					}
					countAll++;
				}
				
				farCount = countAll-countGood;
				farCountAll = countAll;
			}
		}
		
		{ // mr
			int countWronglyAccepted = 0;
			int countAll = 0;
			for(Entry<Integer, Collection<? extends Arrayzable>> e : this.testing.entrySet()) {
				if(e.getKey() != userId) {
					for(Arrayzable fd : e.getValue()) {
						float prob = distance(fd, thisGaussian);
						if(prob < acceptanceThreshold) {
							countWronglyAccepted++;
						}
						countAll++;
					}
				}
			}
			
			mrCount = countWronglyAccepted;
			mrCountAll = countAll;
		}
		
		return new ErrorRate(farCount, farCountAll, mrCount, mrCountAll);
	}
}
