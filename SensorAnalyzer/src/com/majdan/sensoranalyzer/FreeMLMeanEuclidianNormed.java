package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

public class FreeMLMeanEuclidianNormed extends FreeMLMean {
	
	private Float[] weights;
	
	public FreeMLMeanEuclidianNormed(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing, Float[] weights) {
		super(training, testing);
		this.weights = weights;
	}
	
	@Override
	public float computeDistance(Arrayzable fd1, Arrayzable fd2) {
		float distance = fd1.eclideanNormed(fd2, weights);
		//System.out.println(distance);
		return distance;
	}
	
	public static String getNameObjects(boolean weighted) {
		return "Free-text mean euclidean normed"+(weighted ? " weighted" : "");
	}

	public static MLCreator getFactory(final Float[] weightsF) {
		return new MLCreator() {
			boolean weighted;
			@Override
			public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
				this.weighted = weightsF!=null;
				return new FreeMLMeanEuclidianNormed(training, testing, weightsF);
			}
			@Override
			public String getName() {
				return getNameObjects(weighted);
			}
		};
	}
}
