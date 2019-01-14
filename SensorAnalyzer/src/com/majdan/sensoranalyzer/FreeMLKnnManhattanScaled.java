package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class FreeMLKnnManhattanScaled extends FreeMLKnn {
	
	private Float[] weights;
	private Map<Integer, Float[]> stdDev = new LinkedHashMap<Integer, Float[]>();
	
	public FreeMLKnnManhattanScaled(int k, Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing, Float[] weights) {
		super(k, training, testing);
		this.weights = weights;
		for(Map.Entry<Integer, Collection<? extends Arrayzable>> e : training.entrySet()) {
			Float[] stdDev = FreeData.computeAbsDev(e.getValue(), true);
			this.stdDev.put(e.getKey(), stdDev);
		}
	}
	
	@Override
	public float computeDistance(Arrayzable fd1, Arrayzable fd2) {
		float distance = fd1.manhattanScaled(fd2, weights, stdDev.get(fd1.getLabel()));
		//System.out.println(distance);
		return distance;
	}

	public static String getNameObjects(boolean weighted) {
		return "Free-text kNN manhattan scaled"+(weighted ? " weighted" : "");
	}

	public static MLCreator getFactory(final Float[] weightsF) {
		return new MLCreator() {
			private boolean weighted; 
			@Override
			public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
				this.weighted = weightsF!=null;
				return new FreeMLKnnManhattanScaled(1, training, testing, weightsF);
			}
			@Override
			public String getName() {
				return getNameObjects(weighted)+" (k="+1+")";
			}
		};
	}
}
