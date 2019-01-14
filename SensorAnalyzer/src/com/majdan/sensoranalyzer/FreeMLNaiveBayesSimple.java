package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.math3.analysis.function.Gaussian;

public class FreeMLNaiveBayesSimple extends FreeMLNaiveBayes {
	
	public FreeMLNaiveBayesSimple(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		super(training, testing);
	}
	
	@Override
	float distance(Arrayzable fd, Gaussian[] g) {
		float distance = fd.naiveBayes(g);
		//System.out.println(distance);
		return distance;
	}
	
	public static String getNameObjects() {
		return "Free-text naive bayes simple";
	}

	private static MLCreator factory = new MLCreator() {
		@Override
		public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
			return new FreeMLNaiveBayesSimple(training, testing);
		}
		@Override
		public String getName() {
			return getNameObjects();
		}
	};
	public static MLCreator getFactory() {
		return factory;
	}
}
