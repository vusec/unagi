package com.majdan.sensoranalyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class StatisticalAttack {
	
	public static LinkedHashMap<String, LinkedList<LinkedHashMap<Float, Integer>>> binnedInputs;
	public static LinkedHashMap<String, Integer> positionToServeFromBins;
	public static Float[] weights;
	public static int h = 3; //hardcoded, to be changed
	public static int currentTry = 0;
	public static LinkedHashMap<String, Float> metricsWeights;
	
	public static void createFakeInputs(Map<Integer, Collection<? extends Arrayzable>> map, Float[] passedweights) {
		
		//see which are the weights, to know how to deal the positions to serve from bins
		weights = passedweights;
		
		currentTry = 0;
		
		//generate statistical values based on inputs
		//binnedInputs will be sorted by value (the most frequent one will be at the beginning)
		binnedInputs = StatisticalAttack.binInputs(map);
		
		
		//here we have pairs metric - weight, sorted ascending by weigth
		metricsWeights = StatisticalAttack.linkMetricsToWeights(weights, map);
		
		
		//for keeping the same order
		positionToServeFromBins = StatisticalAttack.initBinPositions(metricsWeights);
		
		
		//replace the values with statistical values
		for (Map.Entry<Integer, Collection<? extends Arrayzable>> entry : map.entrySet()) {
			Integer currentKey = entry.getKey();
			Collection<? extends Arrayzable> samplesForCurrentParticipant = entry.getValue();
			StatisticalAttack.replaceInputValues(samplesForCurrentParticipant);
		}
		
		
	}
	
	public static void replaceInputValues(Collection<? extends Arrayzable> samples) {
		for (Arrayzable currentSample : samples) {
			FreeData elt = (FreeData) currentSample;
			StatisticalAttack.setNextSetOfValues(elt);
		}
	}
	
	
	public static LinkedHashMap<String, LinkedList<LinkedHashMap<Float, Integer>>> binInputs(Map<Integer, Collection<? extends Arrayzable>> map) {
		LinkedHashMap<String, LinkedList<LinkedHashMap<Float, Integer>>> binnedInputs = new LinkedHashMap<String, LinkedList<LinkedHashMap<Float, Integer>>>();
		LinkedHashMap<String, LinkedList<Float>> rawInputs = new LinkedHashMap<String, LinkedList<Float>>();
		
		
		for (Map.Entry<Integer, Collection<? extends Arrayzable>> entry : map.entrySet()) {
			Collection<? extends Arrayzable> samplesForCurrentParticipant = entry.getValue();
			for (Arrayzable currentSample : samplesForCurrentParticipant) {
				FreeData elt = (FreeData) currentSample;
				
				for (int i = 0; i < elt.floatLabels.length; i++) {
					if (!rawInputs.containsKey(elt.floatLabels[i])) {
						rawInputs.put(elt.floatLabels[i], new LinkedList<Float>());
					}
					rawInputs.get(elt.floatLabels[i]).add(elt.floatData[i]);
					//System.out.print(elt.floatData[i] + " ");
					
					
					//also add in binnedInputs
					if (!binnedInputs.containsKey(elt.floatLabels[i])) {
						binnedInputs.put(elt.floatLabels[i], new LinkedList<LinkedHashMap<Float, Integer>>());
					}
				}
			}
		}
		
		//now, in rawInputs, we have all the values read for all the metrics, in all participants
		//start the binning process
		for (Map.Entry<String, LinkedList<Float>> entry : rawInputs.entrySet()) {
			Collections.sort(entry.getValue());
			//System.out.println("FIRST: " + entry.getValue().get(0) + ", LAST: " + entry.getValue().get(20) + ", SIZE: " + entry.getValue().size());
		}
		
		
		
		Integer numberOfBins = 100;
		
		for (Map.Entry<String, LinkedList<Float>> entry : rawInputs.entrySet()) {
			Float minValueInList = entry.getValue().getFirst();
			Float maxValueInList = entry.getValue().getLast();
			String metricName = entry.getKey();
			//System.out.println(metricName + ":");
			//System.out.println("minValueInList: " + minValueInList + ", maxValueInList: " + maxValueInList + ", length: " + entry.getValue().size());
			
			//we take the min and max value, and make equal splits in the inputs for this metric
			for (int i = 0; i < numberOfBins; i++) {
				//min of interval, max of interval, and middle of interval (for the bin value)
				Float currentBinMin = minValueInList + ((maxValueInList - minValueInList)/numberOfBins) * i;
				Float currentBinMax = minValueInList + ((maxValueInList - minValueInList)/numberOfBins) * (i + 1);
				Float currentBinValue = (currentBinMin + currentBinMax) / 2;
				
				
				//count number of values in current bin
				int count = 0;
				for (Float f : entry.getValue()) {
					if (f >= currentBinMin && f < currentBinMax) {
						count++;
					}
				}
				
				
				//if it's the last bin, we add 1, to include the last value
				if (i == numberOfBins - 1) {
					count++;
				}
				
				//System.out.println("binMin: " + currentBinMin + ", binMax: " + currentBinMax + ", binValue: " + currentBinValue + ", count: " + count);
				
				
				//add bin to the binnedInputs, together with computed count
				LinkedHashMap<Float, Integer> currentBinValuePair = new LinkedHashMap<Float, Integer>();
				currentBinValuePair.put(currentBinValue, count);
				binnedInputs.get(metricName).add(currentBinValuePair);
			}
			
			//sort binned inputs
			Collections.sort(binnedInputs.get(metricName), new Comparator<LinkedHashMap<Float, Integer>>() {
				public int compare(LinkedHashMap<Float, Integer> bin1, LinkedHashMap<Float, Integer> bin2) {
					Integer binCount1 = new Integer(0);
					for (Map.Entry<Float, Integer> entry : bin1.entrySet()) {
						Float binValue1 = entry.getKey();
						binCount1 = entry.getValue();
						break;
					}
					
					Integer binCount2 = new Integer(0);
					for (Map.Entry<Float, Integer> entry : bin2.entrySet()) {
						Float binValue2 = entry.getKey();
						binCount2 = entry.getValue();
						break;
					}
					
					if (binCount1 == binCount2) {
						return 0;
					}
					else {
						if (binCount1 > binCount2) {
							return 1;
						}
						else {
							return -1;
						}
					}
               }
			});
		}
		
		
		return binnedInputs;
	}
	
	
	public static void setNextSetOfValues(FreeData elt) {
		//System.out.print("VALUES SET: ");
		for (int i = 0; i < elt.floatLabels.length; i++) {
			LinkedList<LinkedHashMap<Float, Integer>> arrayOfValuesForCurrentLabel = binnedInputs.get(elt.floatLabels[i]);
			LinkedHashMap<Float, Integer> valueToSet = arrayOfValuesForCurrentLabel.get(positionToServeFromBins.get(elt.floatLabels[i]));
			
			//System.out.print(elt.floatLabels[i] + ":");
			//using LinkedHashMap only as a Pair
			for(Float f : valueToSet.keySet()) {
				elt.floatData[i] = f;
				//System.out.print(elt.floatData[i] + " ");
				break;
			}
		}
		//System.out.println();
		
		StatisticalAttack.calculateNextSetOfValues();
	}
	
	public static void calculateNextSetOfValues() {
		//here we manipulate the positionToServeFromBins array
		int currentValue = 0;
		
		currentTry++;
		//System.out.println(currentTry);
		currentValue = currentTry;
		for (Map.Entry<String, Integer> entry : positionToServeFromBins.entrySet()) {
			entry.setValue(currentValue % h);
			currentValue = currentValue / h;
		}
	}
	
	
	public static LinkedHashMap<String, Integer> initBinPositions(LinkedHashMap<String, Float> bins) {
		LinkedHashMap<String, Integer> positions = new LinkedHashMap<String, Integer>();
		for (String s : bins.keySet()) {
			//for each metric, we initiate the bin position with 0 (the most probably, statistically speaking, mixture)
			positions.put(s, 0);
		}
		return positions;
	}
	
	
	public static LinkedHashMap<String, Float> linkMetricsToWeights(Float[] weights, Map<Integer, Collection<? extends Arrayzable>> map) {
		LinkedHashMap<String, Float> links = new LinkedHashMap<String, Float>();
		
		for (Map.Entry<Integer, Collection<? extends Arrayzable>> entry : map.entrySet()) {
			for (Arrayzable currentSample : entry.getValue()) {
				FreeData elt = (FreeData) currentSample;
				for (int i = 0; i < elt.floatLabels.length; i++) {
					links.put(elt.floatLabels[i], weights[i]);
				}
				break;
			}
			break;
		}
		
		
		//sort by value
		links = StatisticalAttack.sortByValues(links);
		
		return links;
	}
	
	private static LinkedHashMap sortByValues(LinkedHashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       LinkedHashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
}








