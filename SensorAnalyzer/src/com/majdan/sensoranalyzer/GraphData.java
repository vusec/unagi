package com.majdan.sensoranalyzer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.majdan.sensordynamics.FixedKeystrokeContainer;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.KeyChange;
import com.majdan.sensordynamics.KeyDownChange;
import com.majdan.sensordynamics.KeyUpChange;
import com.majdan.sensordynamics.Keystroke;
import com.majdan.sensordynamics.SensorReading;

public class GraphData extends Arrayzable {

	int graphType;
	int label;
	Float[] floatData;
	
	public GraphData(int graphType, int label, List<? extends Arrayzable> l) {
		this.graphType = graphType;
		this.label = label;

		int length = 0;
		for(Arrayzable fd : l) {
			length += fd.getArrayData().length;
		}
		
		this.floatData = new Float[length];

		int i=0;
		for(Arrayzable fd : l) {
			Float[] arr = fd.getArrayData();
			for(int j=0; j<arr.length; j++) {
				this.floatData[i++] = arr[j];
			}
		}
	}
	
	public GraphData(int graphType, int step, boolean splitPressRelease, int label, FixedKeystrokeContainer2 fkc, DataSettings settings) {
		this.graphType = graphType;
		this.label = label;
		
		int graphTypeAbs = ( graphType > 0 ? graphType : -graphType);
		
		FixedKeystrokeContainer2[] parts = fkc.splitKeystrokes();
		if(graphTypeAbs > parts.length)
			throw new RuntimeException("keystroke parts is to short for graphType="+graphType);
		
		FixedKeystrokeContainer2[] data = generateGraphs(graphType, step, splitPressRelease, parts);
//		System.out.println("######## "+data.length);
//		for(FixedKeystrokeContainer2 d : data) {
//			System.out.println("---------- "+(d.getKeyDownsUps().size()-1));
//			System.out.println(d.printKeystrokes());
//			//System.out.println("\r\n");
//		}
		FreeData[] freeData = new FreeData[data.length];
		int floatDataSize = 0;
		for(int i=0; i<data.length; i++) {
			freeData[i] = new FreeData(label, data[i], settings);
			floatDataSize += freeData[i].getArrayData().length;
		}
		
		this.floatData = new Float[floatDataSize];
		int i = 0;
		for(FreeData fd : freeData) {
			for(Float v : fd.getArrayData()) {
				this.floatData[i++] = v;
			}
		}
	}
	
	private FixedKeystrokeContainer2[] generateGraphs(int graphType, int step, boolean splitPressRelease, FixedKeystrokeContainer2[] data) {
		if(graphType == 0) {
			return data;
		}
		
		FixedKeystrokeContainer2[][] graphsChopped = generateGraphs(graphType, step, data);
		
		if(!splitPressRelease) {
			FixedKeystrokeContainer2[] result = new FixedKeystrokeContainer2[graphsChopped.length];
			for(int i=0; i<result.length; i++) {
				result[i] = new FixedKeystrokeContainer2(graphsChopped[i]);
			}
			return result;
			
		} else {
			LinkedList<FixedKeystrokeContainer2> result = new LinkedList<FixedKeystrokeContainer2>();
			for(int i=0; i<graphsChopped.length; i++) {
				LinkedList<FixedKeystrokeContainer2> pressesOrReleasesA = new LinkedList<FixedKeystrokeContainer2>();
				LinkedList<FixedKeystrokeContainer2> pressesOrReleasesB = new LinkedList<FixedKeystrokeContainer2>();
				for(int j=0; j<graphsChopped[i].length; j++) {
					if(j%2 == 0) {
						pressesOrReleasesA.add(graphsChopped[i][j]);
					} else {
						pressesOrReleasesB.add(graphsChopped[i][j]);
					}
				}
				
				result.add(new FixedKeystrokeContainer2(pressesOrReleasesA));
				result.add(new FixedKeystrokeContainer2(pressesOrReleasesB));
			}
			
			return result.toArray(new FixedKeystrokeContainer2[0]);
		}
	}
	private FixedKeystrokeContainer2[][] generateGraphs(int graphType, int step, FixedKeystrokeContainer2[] data) {
		if(graphType == 0) {
			throw new RuntimeException("assert(graphType!=0)");
		}
		
		boolean forwardKeystroke = graphType >= 0;
		int graphTypeLength = 1 + ( graphType > 0 ? graphType : -graphType);
		
		LinkedList<FixedKeystrokeContainer2[]> result = new LinkedList<FixedKeystrokeContainer2[]>();
		for(int i=forwardKeystroke?0:1 ; i+graphTypeLength<=data.length ; i+=step) {
			FixedKeystrokeContainer2[] graph = new FixedKeystrokeContainer2[graphTypeLength];
			for(int j=0; j<graphTypeLength; j++) {
				graph[j] = data[i+j];
			}
			result.add(graph);
		}
		
		return result.toArray(new FixedKeystrokeContainer2[0][]);
	}
	
	@Override
	public Float[] getArrayData() {
		return floatData;
	}
	@Override
	public Integer getLabel() {
		return this.label; 
	}
}
