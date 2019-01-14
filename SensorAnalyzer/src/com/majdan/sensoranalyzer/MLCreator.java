package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Map;

public interface MLCreator {
	ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing);
	String getName();
}
