package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

public class FreeMLKnnEuclidianNormed extends FreeMLKnn {
	
	private Float[] weights;
	
	public FreeMLKnnEuclidianNormed(int k, Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing, Float[] weights) {
		super(k, training, testing);
		this.weights = weights;
	}
	
	@Override
	public float computeDistance(Arrayzable fd1, Arrayzable fd2) {
		float distance = fd1.eclideanNormed(fd2, weights);
		//System.out.println(distance);
		return distance;
	}
	
	public static String getNameObjects(boolean weighted) {
		return "Free-text kNN euclidean normed"+(weighted ? " weighted" : "");
	}

	public static MLCreator getFactory(final Float[] weightsF) {
		return new MLCreator() {
			private boolean weighted; 
			@Override
			public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
				this.weighted = weightsF!=null;
				return new FreeMLKnnEuclidianNormed(1, training, testing, weightsF);
			}
			@Override
			public String getName() {
				return getNameObjects(this.weighted)+" (k="+1+")";
			}
		};
	}
}
