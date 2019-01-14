package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

public class FreeMLKnnManhattan extends FreeMLKnn {
	
	private Float[] weights;
	
	public FreeMLKnnManhattan(int k, Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing, Float[] weights) {
		super(k, training, testing);
		this.weights = weights;
	}
	
	@Override
	public float computeDistance(Arrayzable fd1, Arrayzable fd2) {
		float distance = fd1.manhattan(fd2, weights);
		//System.out.println(distance);
		return distance;
	}
	
	public static String getNameObjects(boolean weighted) {
		return "Free-text kNN manhattan"+(weighted ? " weighted" : "");
	}

	public static MLCreator getFactory(final Float[] weightsF) {
		return new MLCreator() {
			private boolean weighted;
			@Override
			public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
				this.weighted = weightsF!=null;
				return new FreeMLKnnManhattan(1, training, testing, weightsF);
			}
			@Override
			public String getName() {
				return getNameObjects(weighted)+" (k="+1+")";
			}
		};
	}
}
