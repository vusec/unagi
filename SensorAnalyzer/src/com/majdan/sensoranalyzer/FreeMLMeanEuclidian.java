package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

public class FreeMLMeanEuclidian extends FreeMLMean {
	
	private Float[] weights;
	
	public FreeMLMeanEuclidian(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing, Float[] weights) {
		super(training, testing);
		this.weights = weights;
	}
	
	@Override
	public float computeDistance(Arrayzable fd1, Arrayzable fd2) {
		float distance = fd1.eclidean(fd2, weights);
		//System.out.println(distance);
		return distance;
	}
	
	public static String getNameObjects(boolean weighted) {
		return "Free-text mean euclidean"+(weighted ? " weighted" : "");
	}

	public static MLCreator getFactory(final Float[] weightsF) {
		return new MLCreator() {
			private boolean weighted;
			@Override
			public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
				this.weighted = weightsF!=null;
				return new FreeMLMeanEuclidian(training, testing, weightsF);
			}
			@Override
			public String getName() {
				return getNameObjects(weighted);
			}
		};
	}
}
