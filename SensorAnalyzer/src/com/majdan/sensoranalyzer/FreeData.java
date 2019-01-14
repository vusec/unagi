package com.majdan.sensoranalyzer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.KeyChange;
import com.majdan.sensordynamics.KeyDownChange;
import com.majdan.sensordynamics.KeyUpChange;
import com.majdan.sensordynamics.Keystroke;
import com.majdan.sensordynamics.SensorReading;

public class FreeData extends Arrayzable {
	
	private int label = -1;
	
	public Integer getLabel() {
		return this.label;
	}
	
	private static Float[] weights = {44.507704f, 12.332206f, 4.318134f, 14.892757f, 15.194817f, 6.728528f, 4.650002f, 6.578515f, 3.752873f, 8.178100f, 5.110305f, 2.138472f, 5.689948f, 3.706780f, 2.639057f, 8.243513f, 8.009668f, 2.405492f, 0.744299f, 1.244696f, 1.190267f, 5.605970f, 12.561454f, 2.576368f, 8.974026f, 44.251590f, 21.886756f, 13.748566f, 10.388540f, 10.420712f, 32.375873f, 8.001813f, 7.026638f, 9.606736f, 25.629233f, 3.904439f, 59.263356f, 146.139585f, 99.047589f, 9.256923f, 6.873813f, 4.783489f, 5.313691f, 8.068317f, 0.000000f, 0.816645f, 1.054270f, 0.000000f, 4.037190f, 7.013456f, 0.000000f, 3.828402f, 6.670087f, 0.000000f, 17.119187f, 8.993874f, 6.089900f, 14.750620f, 14.911876f, 6.719099f, 3.232515f, 3.881755f, 3.261682f, 3.900223f, 3.113234f, 3.106560f, 3.241674f, 3.837424f, 3.322662f, 3.425071f, 3.049393f, 2.856083f, 3.933661f, 0.324106f};
	public static Float[] getWeights() {
		return weights;
	}
	
	public Float[] floatData = null;
	public String[] floatLabels = null;
	//private int floatDataCounter = 0;
	private LinkedList<Float> floatDataPrepare = new LinkedList<Float>();
	private LinkedList<String> floatDataLabelPrepare = new LinkedList<String>();
	
//	private Map<String, Integer> floatLabels;
//	
//	private static Map<String, Integer> floatLabelsSensors = new LinkedHashMap<String, Integer>();
//	private static Map<String, Integer> floatLabelsKeystroke = new LinkedHashMap<String, Integer>();
//	private static Map<String, Integer> floatLabelsSensorsKeystroke = new LinkedHashMap<String, Integer>();
	
//	private static int floatLabelsLengthSensors = 20*3 + 4;
//	private static int floatLabelsLengthKeystrokePress = 5;
//	private static int floatLabelsLengthKeystrokeRelease = 5;
//	private static int floatLabelsLengthKeystrokeDownDown = 5;
//	private static int floatLabelsLengthKeystrokeUpUp = 5;

//	private static int lengthSensors = 24*3;
//	private static int lengthKeystrokePress = 1;
//	private static int lengthKeystrokeRelease = 1;
//	private static int lengthKeystrokeDownDown = 1;
//	private static int lengthKeystrokeUpUp = 1;
	
	
//	private static String[] floatDataLabelsSensors = {
//		"rootMeanSquareAcc",
//		"rootMeanSquareGy",
//		"minValAcc",
//		"minValGy",
//		"maxValAcc",
//		"maxValGy",
//		"avgDeltaAcc",
//		"avgDeltaGy",
//		"sumPositiveAcc",
//		"sumPositiveGy",
//		"sumNegativeAcc",
//		"sumNegativeGy",
//		"avgValAcc",
//		"avgValGy",
//		"avgKeyUpValAcc",
//		"avgKeyUpValGy",
//		"avgKeyDownValAcc",
//		"avgKeyDownValGy",
//		"standDevAcc",
//		"standDevGy",
//		
//		// integer
//		"minPeaksCountAcc",
//		"minPeaksCountGy",
//		"maxPeaksCountAcc",
//		"maxPeaksCountGy"
//	};
//	private static String[] floatDataLabelsKeystroke = {
//		"avgKeyPress",
//		"rootMeanSquareKeyPress",
//		"minValKeyPress",
//		"maxValKeyPress",
//		"standDevKeyPress",
//		
//		"avgKeyRelease",
//		"rootMeanSquareKeyRelease",
//		"minValKeyRelease",
//		"maxValKeyRelease",
//		"standDevKeyRelease",
//		
//		"avgKeyDownDown",
//		"rootMeanSquareKeyDownDown",
//		"minValKeyDownDown",
//		"maxValKeyDownDown",
//		"standDevKeyDownDown"
//
//		"avgKeyUpUp",
//		"rootMeanSquareKeyUpUp",
//		"minValKeyUpUp",
//		"maxValKeyUpUp",
//		"standDevKeyUpUp"
//	};
	
//	static {
//		floatLabelsLengthSensors = 3*floatDataLabelsSensors.length;
//		floatLabelsLengthKeystroke = floatDataLabelsKeystroke.length;
//		floatLabelsLengthSensorsKeystroke = floatLabelsLengthSensors+floatLabelsLengthKeystroke;
//		
//		int sensorsCounter = 0;
//		int keystrokeCounter = 0;
//		int sensorsKeystrokeCounter = 0;
//		
//		for(String s : floatDataLabelsSensors) {
//			floatLabelsSensors.put(s+"0", sensorsCounter++);
//			floatLabelsSensors.put(s+"1", sensorsCounter++);
//			floatLabelsSensors.put(s+"2", sensorsCounter++);
//
//			floatLabelsSensorsKeystroke.put(s+"0", sensorsKeystrokeCounter++);
//			floatLabelsSensorsKeystroke.put(s+"1", sensorsKeystrokeCounter++);
//			floatLabelsSensorsKeystroke.put(s+"2", sensorsKeystrokeCounter++);
//		}
//	
//		for(String s : floatDataLabelsKeystroke) {
//			floatLabelsKeystroke.put(s, keystrokeCounter++);
//			
//			floatLabelsSensorsKeystroke.put(s, sensorsKeystrokeCounter++);
//		}
//	}
	

//	public int computeLength(DataSettings s) {
//		int length = 0;
//		if(s.isSensors())
//			length += FreeData.lengthSensors;
//		if(s.isKeystrokeKeyPress())
//			length += FreeData.lengthKeystrokePress;
//		if(s.isKeystrokeKeyRelease())
//			length += FreeData.lengthKeystrokeRelease;
//		if(s.isKeystrokeKeyUpUp())
//			length += FreeData.lengthKeystrokeUpUp;
//		if(s.isKeystrokeKeyDownDown())
//			length += FreeData.lengthKeystrokeDownDown;
//		
//		return length;
//	}
	
//	private void setUp(DataSettings settings) {
////		int length = 0;
////		if(settings.isSensors() && keystroke) {
////			length = floatLabelsLengthSensorsKeystroke;
////			this.floatLabels = floatLabelsSensorsKeystroke;
////		} else if(sensors) {
////			length = floatLabelsLengthSensors;
////			this.floatLabels = floatLabelsSensors;
////		} else if(keystroke) {
////			length = floatLabelsLengthKeystroke;
////			this.floatLabels = floatLabelsKeystroke;
////		} else {
////			length = 0;
////			this.floatLabels = null;
////		}
////		
//		int length = computeLength(settings);
//		//System.err.println(length);
//		
//		this.floatData = new Float[length];
//		this.floatLabels = new String[length];
//		for(int i=0; i<floatData.length; i++) {
//			this.floatData[i] = 0f;
//		}
//	}
//	private void checkCorrectness() {
//		if(this.floatDataCounter != this.floatData.length || this.floatDataCounter != this.floatLabels.length)
//			throw new RuntimeException("floatData & floatLabels have not been filled");
//	}

//	public Float getFloat(String label) {
//		return floatData[ floatLabels.get(label) ];
//	}
//	public void setFloat(String label, Float v) {
//		floatData[ floatLabels.get(label) ] = v;
//	}
//	public void setFloatArr(String label, Float[] arr) {
//		for(int i=0; i<arr.length; i++) {
//			floatData[ floatLabels.get(label)+i ] = arr[i];
//		}
//	}
//	public void setFloat(String label, Integer v) {
//		floatData[ floatLabels.get(label) ] = (float)v;
//	}
//	public void setFloatArr(String label, Integer[] arr) {
//		for(int i=0; i<arr.length; i++) {
//			floatData[ floatLabels.get(label)+i ] = (float)arr[i];
//		}
//	}
	
	public void setFloat(String label, Float v) {
//		int pos = this.floatDataCounter++;
//		floatData[pos] = v;
//		floatLabels[pos] = label;
		this.floatDataLabelPrepare.add(label);
		this.floatDataPrepare.add(v);
	}
	public void setFloatArr(String label, Float[] arr) {
		for(int i=0; i<arr.length; i++) {
//			int pos = this.floatDataCounter++;
//			floatData[pos] = arr[i];
//			floatLabels[pos] = label+i;
			this.floatDataLabelPrepare.add(label+i);
			this.floatDataPrepare.add(arr[i]);
		}
	}
	public void setFloat(String label, Integer v) {
//		int pos = this.floatDataCounter++;
//		floatData[pos] = (float)v;
//		floatLabels[pos] = label;
		
		this.floatDataLabelPrepare.add(label);
		this.floatDataPrepare.add((float)v);
	}
	public void setFloatArr(String label, Integer[] arr) {
		for(int i=0; i<arr.length; i++) {
//			int pos = this.floatDataCounter++;
//			floatData[pos] = (float)arr[i];
//			floatLabels[pos] = label;
			this.floatDataLabelPrepare.add(label+i);
			this.floatDataPrepare.add((float)arr[i]);
		}
	}
	
	

//	private void addArr(Float[] arr1, Float[] arr2) {
//		for(int i=0; i<arr1.length; i++) {
//			arr1[i] += arr2[i];
//		}
//	}
//	private void addArr(Integer[] arr1, Integer[] arr2) {
//		for(int i=0; i<arr1.length; i++) {
//			arr1[i] += arr2[i];
//		}
//	}
//	private void divArr(Float[] arr1, int s) {
//		for(int i=0; i<arr1.length; i++) {
//			arr1[i] /= s;
//		}
//	}
//	private void divArr(Integer[] arr1, int s) {
//		for(int i=0; i<arr1.length; i++) {
//			arr1[i] /= s;
//		}
//	}
	public FreeData(int label, Collection<? extends Arrayzable> collection) {
		this.label = label;
		
		int length = collection.iterator().next().getArrayData().length;
		this.floatData = new Float[length];
		for(int i=0; i<length; i++) {
			this.floatData[i] = 0f;
			for(Arrayzable fd : collection) {
				this.floatData[i] += fd.getArrayData()[i]; 
			}
			this.floatData[i] /= collection.size();
		}
	}
	
	public static FreeData computeMean(int label, Collection<FreeData> c) {
		return new FreeData(label, c);
	}
	
	public static String freeDataToString(FreeData fkc, int label) {
		return label+" "+(fkc.toString())+"\r\n";
	}
	
	public static String setToString(Collection<? extends Arrayzable> c, int label) {
		String result = "";
		for(Arrayzable fkc : c) {
			result += freeDataToString(fkc, label);
		}
		
		return result;
	}
	

	static LinkedHashMap<Integer, Collection<FreeData>> map2FreeDataMap(LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> map, DataSettings settings) {
		LinkedHashMap<Integer, Collection<FreeData>> map2 = new LinkedHashMap<Integer, Collection<FreeData>>();
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : map.entrySet()) {
			LinkedHashSet<FreeData> set = new LinkedHashSet<FreeData>();
			for(FixedKeystrokeContainer2 fkc : e.getValue()) {
				set.add(new FreeData(e.getKey(), fkc, settings));
			}
			map2.put(e.getKey(), set);
		}
		return map2;
	}

	public static void writeToFile(String file, Map<Integer, Collection<? extends Arrayzable> > data) throws IOException {
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		for(Entry<Integer, Collection<? extends Arrayzable> > e : data.entrySet()) {
			writer.write(FreeData.setToString(e.getValue(), e.getKey() ));
		}
		writer.close();
	}
	public static void splitTo2Files(String file1, String file2, Map<Integer, Collection<FixedKeystrokeContainer2>> data, DataSettings settings) throws IOException {
		PrintWriter[] writers = new PrintWriter[2];
		writers[0] = new PrintWriter(file1, "UTF-8");
		writers[1] = new PrintWriter(file2, "UTF-8");
		int i = 0;
		for(Entry<Integer, Collection<FixedKeystrokeContainer2> > e : data.entrySet()) {
			for(FixedKeystrokeContainer2 fkc : e.getValue()) {
				writers[i].write(freeDataToString(new FreeData(e.getKey(), fkc, settings), e.getKey()));
				
				i = (i+1)%2;
			}
		}
		writers[0].close();
		writers[1].close();
	}
	
	private Float[] createFloatArr(int size) {
		Float[] arr = new Float[size];
		for(int i=0; i<size ; i++) {
			arr[i] = 0f;
		}
		return arr;
	}
	
	private Integer[] createIntegerArr(int size) {
		Integer[] arr = new Integer[size];
		for(int i=0; i<size ; i++) {
			arr[i] = 0;
		}
		return arr;
	}
	
	private String arrToString(Object[] arr) {
		String result = "[";
		for(Object elt : arr) {
			result += elt+", ";
		}
		result += "]";
		
		return result;
	}
	
//	public String printString() {
//		String result = "";
//		for(Entry<String, Integer> e : floatLabels.entrySet()) {
//			result += e.getKey()+": "+floatData[e.getValue()]+"\r\n";
//		}
//		
//		return result+">";
//	}
	
	private void setLong(String label, List<Long> list) {
		int i=0;
		for(Long v : list) {
			this.setFloat(label+"Keystroke"+i, (float)v);
			i++;
		}
	}
	
	private void setLongMetrics(String label, List<Long> list) {
		if(list == null || list.size() == 0) {
			this.setFloat(label+"Avg", 0f);
			return;
		}
		
		long avg = 0;
		long rootMeanSquare = 0;
		long minVal = Long.MAX_VALUE;
		long maxVal = Long.MIN_VALUE;
		
		for(Long v : list) {
			avg += v;
			rootMeanSquare += v*v;
			if(v < minVal)
				minVal = v;
			if(v > maxVal)
				maxVal = v;
		}
		
		avg /= list.size();
		rootMeanSquare /= list.size();
		
		long standDev = 0;
		for(Long v : list) {
			standDev += (avg-v)*(avg-v);
		}
		standDev /= list.size();
		
		this.setFloat(label+"Avg", (float)avg);
		this.setFloat(label+"RootMeanSquare", (float)Math.sqrt(rootMeanSquare));
		this.setFloat(label+"MinVal", (float)minVal);
		this.setFloat(label+"MaxVal", (float)maxVal);
		this.setFloat(label+"StandDev", (float)standDev);
	}
	
	public FreeData(int label, FixedKeystrokeContainer2 fkc, DataSettings settings) {
		//setUp(settings);
		
		this.label = label;
		
		LinkedList<KeyUpChange> keyUps = fkc.getKeyUps();
		LinkedList<KeyDownChange> keyDowns = fkc.getKeyDowns();
		
		Keystroke k = fkc.getKeystroke();
		LinkedList<SensorReading>[] acc = k.getAcc();
		LinkedList<SensorReading>[] gy = k.getGy();

		if(acc[0].size() != acc[1].size() || acc[1].size() != acc[2].size())
			throw new RuntimeException("acc[] lists are not the same size");
		if(gy[0].size() != gy[1].size() || gy[1].size() != gy[2].size())
			throw new RuntimeException("gy[] lists are not the same size");
		int accLength = acc[0].size();
		int gyLength = gy[0].size();
		
		if(settings.isSensors()) {
			Float[] avgValAcc = new Float[3];
			Float[] avgValGy = new Float[3];
			for(Integer i=0; i<3; i++) {
				Float sum = 0f;
				for(SensorReading sr : acc[i]) {
					sum += sr.getValue() * sr.getValue();
				}
				sum /= acc[i].size();
				sum = new Double(Math.sqrt(sum.doubleValue())).floatValue();
				
				this.setFloat("rootMeanSquareAcc"+i, sum);
				
				sum = 0f;
				for(SensorReading sr : gy[i]) {
					sum += sr.getValue() * sr.getValue();
				}
				sum /= gy[i].size();
				sum = new Double(Math.sqrt(sum.doubleValue())).floatValue();
				
				this.setFloat("rootMeanSquareGy"+i, sum);
			}
			for(Integer i=0; i<3; i++) {
				Float min = Float.MAX_VALUE;
				Float max = Float.MIN_VALUE;
				for(SensorReading sr : acc[i]) {
					if(sr.getValue() < min)
						min = sr.getValue();
					if(sr.getValue() > max)
						max = sr.getValue();
				}
				this.setFloat("minValAcc"+i, min);
				this.setFloat("maxValAcc"+i, max);
				
				min = Float.MAX_VALUE;
				max = Float.MIN_VALUE;
				for(SensorReading sr : gy[i]) {
					if(sr.getValue() < min)
						min = sr.getValue();
					if(sr.getValue() > max)
						max = sr.getValue();
				}
				this.setFloat("minValGy"+i, min);
				this.setFloat("maxValGy"+i, max);
			}
			for(Integer i=0; i<3; i++) {
				if(accLength >= 2) {
					Iterator<SensorReading> it = acc[i].iterator();
					Integer minPeaksCountX = 0;
					Integer maxPeaksCountX = 0;
					Float prev1 = it.next().getValue();
					Float prev2 = it.next().getValue();
					while(it.hasNext()) {
						Float curr = it.next().getValue();
						if(prev2 < prev1 && prev2 < curr)
							minPeaksCountX++;
						if(prev2 > prev1 && prev2 > curr)
							maxPeaksCountX++;
						prev1 = prev2;
						prev2 = curr;
					}
					this.setFloat("minPeaksCountAcc"+i, minPeaksCountX);
					this.setFloat("maxPeaksCountAcc"+i, maxPeaksCountX);
				} else {
					this.setFloat("minPeaksCountAcc"+i, 0f);
					this.setFloat("maxPeaksCountAcc"+i, 0f);
				}
				
				if(gyLength >= 2) {
					Iterator<SensorReading> it = gy[i].iterator();
					Integer minPeaksCountX = 0;
					Integer maxPeaksCountX = 0;
					Float prev1 = it.next().getValue();
					Float prev2 = it.next().getValue();
					while(it.hasNext()) {
						Float curr = it.next().getValue();
						if(prev2 < prev1 && prev2 < curr)
							minPeaksCountX++;
						if(prev2 > prev1 && prev2 > curr)
							maxPeaksCountX++;
						prev1 = prev2;
						prev2 = curr;
					}
					this.setFloat("minPeaksCountGy"+i, minPeaksCountX);
					this.setFloat("maxPeaksCountGy"+i, maxPeaksCountX);
				} else {
					this.setFloat("minPeaksCountGy"+i, 0f);
					this.setFloat("maxPeaksCountGy"+i, 0f);
				}
			}
			for(Integer i=0; i<3; i++) {
				if(accLength > 0) {
					Iterator<SensorReading> it = acc[i].iterator();
					Float sumDeltas = 0f;
					Float prev = it.next().getValue();
					while(it.hasNext()) {
						sumDeltas += Math.abs(prev - it.next().getValue()); 
					}
					this.setFloat("avgDeltaAcc"+i, sumDeltas/acc[i].size());
				} else {
					this.setFloat("avgDeltaAcc"+i, 0f);
				}
				
				if(gyLength > 0) {
					Iterator<SensorReading> it = gy[i].iterator();
					Float sumDeltas = 0f;
					Float prev = it.next().getValue();
					while(it.hasNext()) {
						sumDeltas += Math.abs(prev - it.next().getValue()); 
					}
					this.setFloat("avgDeltaGy"+i, sumDeltas/gy[i].size());
				} else {
					this.setFloat("avgDeltaGy"+i, 0f);
				}
			}
			for(Integer i=0; i<3; i++) {
				Float sumPositive = 0f;
				Float sumNegative = 0f;
				for(SensorReading sr : acc[i]) {
					if(sr.getValue() > 0)
						sumPositive += sr.getValue();
					else
						sumNegative += sr.getValue();
				}
				this.setFloat("sumPositiveAcc"+i, sumPositive);
				this.setFloat("sumNegativeAcc"+i, sumNegative);
	
				sumPositive = 0f;
				sumNegative = 0f;
				for(SensorReading sr : gy[i]) {
					if(sr.getValue() > 0)
						sumPositive += sr.getValue();
					else
						sumNegative += sr.getValue();
				}
				this.setFloat("sumPositiveGy"+i, sumPositive);
				this.setFloat("sumNegativeGy"+i, sumNegative);
			}
			for(Integer i=0; i<3; i++) {
				Float sum = 0f;
				for(SensorReading sr : acc[i]) {
					sum += sr.getValue();
				}
				avgValAcc[i] = sum/acc[i].size();
				this.setFloat("avgValAcc"+i, avgValAcc[i]);
	
				sum = 0f;
				for(SensorReading sr : gy[i]) {
					sum += sr.getValue();
				}
				avgValGy[i] = sum/gy[i].size();
				this.setFloat("avgValGy"+i, avgValGy[i]);
			}
			{
				Float[] sumKeyUpsAcc = createFloatArr(3);
				Float[] sumKeyUpsGy = createFloatArr(3);
				for(KeyChange kc : keyUps) {
					sumKeyUpsAcc[0] += kc.getAcc()[0];
					sumKeyUpsAcc[1] += kc.getAcc()[1];
					sumKeyUpsAcc[1] += kc.getAcc()[2];
					sumKeyUpsGy[0] += kc.getGy()[0];
					sumKeyUpsGy[1] += kc.getGy()[1];
					sumKeyUpsGy[1] += kc.getGy()[2];
				}
				this.setFloat("avgKeyUpValAcc0", sumKeyUpsAcc[0]/keyUps.size());
				this.setFloat("avgKeyUpValAcc1", sumKeyUpsAcc[1]/keyUps.size());
				this.setFloat("avgKeyUpValAcc2", sumKeyUpsAcc[2]/keyUps.size());
				this.setFloat("avgKeyUpValGy0", sumKeyUpsGy[0]/keyUps.size());
				this.setFloat("avgKeyUpValGy1", sumKeyUpsGy[1]/keyUps.size());
				this.setFloat("avgKeyUpValGy2", sumKeyUpsGy[2]/keyUps.size());
		
				
				Float[] sumKeyDownsAcc = createFloatArr(3);
				Float[] sumKeyDownsGy = createFloatArr(3);
				for(KeyChange kc : keyDowns) {
					sumKeyDownsAcc[0] += kc.getAcc()[0];
					sumKeyDownsAcc[1] += kc.getAcc()[1];
					sumKeyDownsAcc[1] += kc.getAcc()[2];
					sumKeyDownsGy[0] += kc.getGy()[0];
					sumKeyDownsGy[1] += kc.getGy()[1];
					sumKeyDownsGy[1] += kc.getGy()[2];
				}
				this.setFloat("avgKeyDownValAcc0", sumKeyDownsAcc[0]/keyDowns.size());
				this.setFloat("avgKeyDownValAcc1", sumKeyDownsAcc[1]/keyDowns.size());
				this.setFloat("avgKeyDownValAcc2", sumKeyDownsAcc[2]/keyDowns.size());
				this.setFloat("avgKeyDownValGy0", sumKeyDownsGy[0]/keyDowns.size());
				this.setFloat("avgKeyDownValGy1", sumKeyDownsGy[1]/keyDowns.size());
				this.setFloat("avgKeyDownValGy2", sumKeyDownsGy[2]/keyDowns.size());
			}
			for(Integer i=0; i<3; i++) {
				float sum = 0;
				for(SensorReading sr : acc[i]) {
					sum += sqr(sr.getValue()-avgValAcc[i]);
				}
				sum /= acc[i].size();
				this.setFloat("standDevAcc"+i, (float)Math.sqrt(sum));
	
				sum = 0;
				for(SensorReading sr : gy[i]) {
					sum += sqr(sr.getValue()-avgValGy[i]);
				}
				sum /= gy[i].size();
				this.setFloat("standDevGy"+i, (float)Math.sqrt(sum));
			}
		}
		
		
		
		if(settings.isKeystrokeKeyDownDown() || settings.isKeystrokeKeyUpUp() || settings.isKeystrokeKeyPress() || settings.isKeystrokeKeyRelease()) {
			if(fkc.getKeyDownsUps().size() == 0) {
//				if(settings.isKeystrokeKeyPress())
//					this.setLongList("keyPresses", null);
//				if(settings.isKeystrokeKeyRelease())
//					this.setLongList("keyReleases", null);
//				if(settings.isKeystrokeKeyDownDown())
//					this.setLongList("keyDownDowns", null);
//				if(settings.isKeystrokeKeyUpUp())
//					this.setLongList("keyUpsUps", null);
			} else {
				
				LinkedList<Long> keyPresses = new LinkedList<Long>();
				LinkedList<Long> keyReleases = new LinkedList<Long>();
				
				LinkedList<Long> keyPressesReleases = new LinkedList<Long>();
				LinkedList<Long> keyDownDowns = new LinkedList<Long>();
				LinkedList<Long> keyUpsUps = new LinkedList<Long>();
				
				LinkedList<KeyChange> allKeyChanges = fkc.getKeyDownsUps();
				Iterator<KeyChange> it = allKeyChanges.iterator();
				KeyChange kc = it.next();
				while(it.hasNext()) {
					KeyChange newKc = it.next();
					long v = newKc.getTime()-kc.getTime();
					if(kc instanceof KeyUpChange) {
						keyPresses.add(v);
						keyPressesReleases.add(v);
					} else if(kc instanceof KeyDownChange) {
						keyReleases.add(v);
						keyPressesReleases.add(v);
					}
					
					kc = newKc;
				}
				
				if(settings.isAllKeystrokes())
					setLong("allKeystrokes", keyPressesReleases);
				
				if(settings.isKeystrokeKeyPress())
					setLong("keyPresses", keyPresses);
				
				if(settings.isKeystrokeKeyRelease())
					setLong("keyReleases", keyReleases);
	
				if(settings.isKeystrokeKeyDownDown()) {
					it = allKeyChanges.iterator();
					while(it.hasNext()) {
						KeyChange one = it.next();
						if(it.hasNext()) {
							KeyChange two = it.next();
							keyDownDowns.add(one.getTime()+two.getTime());
						}
					}
					
					setLong("keyDownDowns", keyDownDowns);
				}
				
				if(settings.isKeystrokeKeyUpUp()) {
					it = allKeyChanges.iterator();
					it.next();
					while(it.hasNext()) {
						KeyChange one = it.next();
						if(it.hasNext()) {
							KeyChange two = it.next();
							keyUpsUps.add(one.getTime()+two.getTime());
						}
					}
					
					setLong("keyUpsUps", keyUpsUps);
				}
				
	//			if(settings.isKeystrokeAddAllData()){
	//				int newLength = this.floatData.length;
	//				if(settings.isKeystrokeKeyPress())
	//					newLength += keyPresses.size();
	//				if(settings.isKeystrokeKeyRelease())
	//					newLength += keyReleases.size();
	//				if(settings.isKeystrokeKeyDownDown())
	//					newLength += keyDownDowns.size();
	//				if(settings.isKeystrokeKeyUpUp())
	//					newLength += keyUpsUps.size();
	//				
	//				Float[] newFloatData = new Float[newLength];
	//				
	//				int i = 0;
	//				for(Float v : this.floatData) {
	//					newFloatData[i++] = v;
	//				}
	//				if(settings.isKeystrokeKeyPress()) {
	//					for(long v : keyPresses) {
	//						newFloatData[i++] = (float)v;
	//					}
	//				}
	//				if(settings.isKeystrokeKeyRelease()) {
	//					for(long v : keyReleases) {
	//						newFloatData[i++] = (float)v;
	//					}
	//				}
	//				if(settings.isKeystrokeKeyDownDown()) {
	//					for(long v : keyDownDowns) {
	//						newFloatData[i++] = (float)v;
	//					}
	//				}
	//				if(settings.isKeystrokeKeyUpUp()) {
	//					for(long v : keyUpsUps) {
	//						newFloatData[i++] = (float)v;
	//					}
	//				}
	//				
	//				this.floatData = newFloatData;
	//			}
			}
		}
		
		
		setData();
		//checkCorrectness();
	}
	

	private static float sqr(float v) {
		return v*v;
	}
//	private static float sqr(Float[] v) {
//		return sqr(v[0])+sqr(v[1])+sqr(v[2]);
//	}
//	private static float sqr(Integer[] v) {
//		return sqr(v[0])+sqr(v[1])+sqr(v[2]);
//	}
	private static float sqr(int v) {
		return (float)v*v;
	}
//	private static float sqr3(Float[] a, Float[] b) {
//		return	sqr(a[0]-b[0]) +
//				sqr(a[1]-b[1]) +
//				sqr(a[2]-b[2]);
//	}
//	private static float sqr3(Integer[] a, Integer[] b) {
//		return	sqr(a[0]-b[0]) +
//				sqr(a[1]-b[1]) +
//				sqr(a[2]-b[2]);
//	}
//	private static float abs3(Float[] a, Float[] b) {
//		return Math.abs(a[0]-b[0])+Math.abs(a[1]-b[1])+Math.abs(a[2]-b[2]);
//	}
//	private static float abs3(Integer[] a, Integer[] b) {
//		return (float)Math.abs(a[0]-b[0])+Math.abs(a[1]-b[1])+Math.abs(a[2]-b[2]);
//	}
//	private static float sqrSum(FreeData o) {
//		float sum = 0;
//		for(float v : o.floatData) {
//			sum += sqr(v);
//		}
//		return (float)Math.sqrt(sum);
//	}
	
	public static void normalize(Collection<? extends Arrayzable> arrC) {
		int count = arrC.iterator().next().getArrayData().length;
		for(int i=0; i<count; i++) {
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			for(Arrayzable arrO : arrC) {
				Float[] arr = arrO.getArrayData();
				Float v = arr[i];
				if(v < min)
					min = v;
				if(v > max)
					max = v;
			}
			for(Arrayzable arrO : arrC) {
				Float[] arr = arrO.getArrayData();
				arr[i] = (arr[i]-min)/(max-min);
			}
		}
	}
	@Override
	public Float[] getArrayData() {
		if(this.floatData == null) {
			setData();
		}
		return this.floatData;
	}
	
	private void setData() {
		if(this.floatData == null) {
			if(this.floatDataPrepare.size() != this.floatDataLabelPrepare.size())
				throw new RuntimeException("this.floatDataPrepare.size() != this.floatDataLabelPrepare.size()");
			
			Iterator<Float> dIt = this.floatDataPrepare.iterator();
			Iterator<String> lIt = this.floatDataLabelPrepare.iterator();
			
			this.floatData = new Float[ this.floatDataPrepare.size() ];
			this.floatLabels = new String[ floatData.length ];
			
			for(int i=0; i<floatData.length; i++) {
				this.floatData[i] = dIt.next();
				this.floatLabels[i] = lIt.next();
			}
		}
	}
	
	public String[] getFloatLabels() {
		return this.floatLabels;
	}
	
}







