package com.majdan.sensoranalyzer;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import com.majdan.sensoranalyzer.outputgraphs.ChartValues;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;
import com.majdan.sensordynamics.KeyChange;
import com.majdan.sensordynamics.KeyDownChange;
import com.majdan.sensordynamics.KeyUpChange;
import com.majdan.sensordynamics.Keystroke;
import com.majdan.sensordynamics.SensorReading;

public class FixedAnalyzer {
	
	private static String filesPath = "/home/student/workspace/kdroid/charts/";
	private static String[] words = {"internet01", "satellite01"};
	private static int[] graphTypes = {0,1,2,3,4,5,6,7,8}; //{0, 1, 2, 3, 4, 5, 6, 7};//, -2, -1, 0, 1, 2, 3, 4};
	private static int[] steps = {1};
	/*
	//int[] steps = {2};
//	Float[][] weightsTmp = {
//			{88.140755f, 12.054371f, 12.872952f, 11.700265f, 5.726603f, 3.894022f, 4.426091f, 4.589742f, 7.084529f, 6.007397f, 3.689195f, 3.578147f, 3.11268f, 6.514543f, 2.705846f, 1.888624f, 1.940606f, 1.567612f, 1.074718f, 1.100753f, 2.869284f, 2.777533f, 0.990346f, 0.978623f, 1.666718f, 1.488973f, 1.22414f, 1.212867f, 2.721236f, 2.406157f, 1.387371f, 7.207508f, 1.288997f, 6.379587f, 1.583523f, 1.174779f, 7.923297f, 14.785127f, 16.593481f, 7.797235f, 22.459332f, 12.021213f, 13.035656f, 14.360211f, 8.646094f, 6.801717f, 8.286773f, 3.262661f, 170.52603f, 7.51737f, 117.50634f, 5.878504f, 60.666847f, 4.035341f, 7.878047f, 5.400694f, 0.0f, 0.565746f, 0.624027f, 0.0f, 4.641537f, 3.670118f, 0.0f, 4.279137f, 5.212426f, 0.0f, 9.678506f, 11.98477f, 8.217385f, 11.698734f, 4.698755f, 4.003163f, 1.871984f, 1.257571f},
//			{48.36854f, 5.499368f, 8.055094f, 1.417446f, 1.271161f, 1.414884f, 2.906707f, 2.084405f, 2.381197f, 2.817218f, 2.267189f, 2.014136f, 0.813637f, 0.482802f, 1.146283f, 0.93475f, 0.292697f, 1.907809f, 1.788371f, 1.757004f, 1.167161f, 1.157266f, 1.668221f, 1.671583f, 0.618533f, 0.637094f, 1.513097f, 1.482513f, 1.503229f, 1.620017f, 0.312533f, 2.995331f, 1.288057f, 0.495015f, 0.659137f, 0.679203f, 3.903046f, 13.485789f, 5.88894f, 5.125483f, 27.582012f, 15.032578f, 1.461477f, 7.24612f, 16.511683f, 6.414768f, 4.635527f, 1.298782f, 52.38473f, 1.851786f, 162.93387f, 5.554475f, 49.494987f, 4.932514f, 4.027417f, 6.378601f, 0.0f, 0.242494f, 1.327501f, 0.0f, 4.072417f, 3.853018f, 0.0f, 0.227985f, 0.46106f, 0.0f, 2.928267f, 5.475318f, 4.07027f, 1.163795f, 1.15522f, 1.33962f, 2.182626f, 0.900582f}
//	};
	//int[] freqs = {-15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
	//int[] freqs = {-500, -200, -100, -50, -20, -10, -5, -4, -3, -2, 3, 4, 5, 10, 20, 50, 100, 200, 500, 0};
	//int[] freqs = {2, 3, 4, 5, 10};
	//int[] freqs = {0};
	 */
	private static int[] freqs = {0};
	private static String[] actualLetters = {"internet", "satellite"};//, "satellite"};
	private static boolean eer = true; // EER || false-alarm zero-miss 
	
	public static void main(String[] args) throws IOException {
		//mainTester(args);
		//mainSvmExportTrainTest("internet01", "D:\\Programowanie\\kdroid\\trunk_kamil\\svm\\my4\\internet01.");
		//mainSvmExportTrainTest("satellite01", "D:\\Programowanie\\kdroid\\trunk_kamil\\svm\\my4\\satellite01.");
		
		//mainDist(new DataSettings(false, true, true, true, true, true));
		
		//mainDistOld(new DataSettings(true, true, true, false, false, false));
		//mainDist(new DataSettings(true, true, true, false, false, false));
		//mainDist(new DataSettings(false, true, true, true, true, true));
		
		//mainDist(new DataSettings(true, false, false, false, false, false));
		
		
		
		//mainDist(new DataSettings(false, false, false, true, true, true));
		
		//old
		//mainStatistic(new DataSettings(true, false, false, true, true, true));
		
		//only keystrokes
		//mainStatistic(new DataSettings(false, true, true, false, false, false));
		mainDist(new DataSettings(true, true, true, false, false, false));
		
		
		
		//mainDist(new DataSettings(true, false, false, true, true, true));
		
		//mainCountFreq();
	}
	
	public static void mainCountFreq() {
		Map<String, Collection<ChartValues>> charts = new LinkedHashMap<String, Collection<ChartValues>>();
		
		for(int w=0; w<words.length; w++) {
			String word = words[w];
			Map<Integer, Collection<FixedKeystrokeContainer2>> data = new DataReader().getInputData(word);
			filterKeystroke(data, actualLetters[w]);
			
			for(Map.Entry<Integer, Collection<FixedKeystrokeContainer2>> fkcc : data.entrySet()) {
				
				int sum = 0;
				for(FixedKeystrokeContainer2 fkc : fkcc.getValue()) {
					int sensCount = fkc.getKeystroke().getAcc()[0].size() +
							fkc.getKeystroke().getAcc()[1].size() +
							fkc.getKeystroke().getAcc()[2].size() +
							fkc.getKeystroke().getGy()[0].size() +
							fkc.getKeystroke().getGy()[1].size() +
							fkc.getKeystroke().getGy()[2].size();
					int sensCountAvg = sensCount/6;

					long sensBeg = fkc.getKeystroke().getAcc()[0].getFirst().getTime() +
							fkc.getKeystroke().getAcc()[1].getFirst().getTime() +
							fkc.getKeystroke().getAcc()[2].getFirst().getTime() +
							fkc.getKeystroke().getGy()[0].getFirst().getTime() +
							fkc.getKeystroke().getGy()[1].getFirst().getTime() +
							fkc.getKeystroke().getGy()[2].getFirst().getTime();
					long sensBegAvg = sensBeg/6;

					long sensEnd = fkc.getKeystroke().getAcc()[0].getLast().getTime() +
							fkc.getKeystroke().getAcc()[1].getLast().getTime() +
							fkc.getKeystroke().getAcc()[2].getLast().getTime() +
							fkc.getKeystroke().getGy()[0].getLast().getTime() +
							fkc.getKeystroke().getGy()[1].getLast().getTime() +
							fkc.getKeystroke().getGy()[2].getLast().getTime();
					long sensEndAvg = sensEnd/6;
					
					int timeInterval = (int)(sensEnd-sensBeg);
					
					sum += timeInterval/sensCountAvg;
				}
				
				int result = sum/fkcc.getValue().size();
				
				System.out.println(fkcc.getKey()+": "+result);
			}
		}
	}
	/*
//	public static void mainTester(String[] args) {
//		String[] words = {"internet01", "satellite01"};
//		String[] actualLetters = {"interne", "satellit"};
//		for(int i=0; i<words.length; i++) {
//			System.out.println("===== WORD: "+words[i]+" =====");
//			
//			LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> map = new DataReader().getInputData(words[i]);
//			filterKeystroke(map, actualLetters[i]);
//			
//			for(Entry<Integer, Collection<FixedKeystrokeContainer2>> user : map.entrySet()) {
//				
//				System.out.println("== USER: "+user.getKey()+" ==");
//				for(FixedKeystrokeContainer2 myDeserializedObject : user.getValue()) {
//
//					LinkedList<KeyChange> l = myDeserializedObject.getKeyDownsUps();
//					String s = "["+l.size()+"] "+"kU:"+myDeserializedObject.getKeyUps().size()+", kD:"+myDeserializedObject.getKeyDowns().size()+"::   ";
//					for(KeyChange e : l) {
//						if(e instanceof KeyDownChange) {
//							KeyDownChange eX = (KeyDownChange)e;
//							//s += "D";
//						} else if(e instanceof KeyUpChange) {
//							KeyUpChange eX = (KeyUpChange)e;
//							//s += "U";
//						}
//						//s += e.getTime()+" "+Character.toString((char)(int)e.getKey())+", ";
//						s += Character.toString((char)(int)e.getKey());
//					}
//					System.out.println(s);
//				}
//			}
//		}
//	}
	*/
	
	public static void filterKeystroke(Map<Integer, Collection<FixedKeystrokeContainer2>> map, String actualLetters) {
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> user : map.entrySet()) {
			LinkedHashSet<FixedKeystrokeContainer2> toRemove = new LinkedHashSet<FixedKeystrokeContainer2>();
			
			for(FixedKeystrokeContainer2 fkc : user.getValue()) {
				boolean remove = false;
				
				LinkedList<KeyChange> l = fkc.getKeyDownsUps();
				//String s = "["+l.size()+"] "+"kU:"+myDeserializedObject.getKeyUps().size()+", kD:"+myDeserializedObject.getKeyDowns().size()+"::   ";
				int i = 0;
				
				if(l.size() != 2*actualLetters.length()) {
					remove = true;
					
				} else {
					for(KeyChange e : l) {
						int pos = i/2;
						boolean isKeyDown = i%2 == 0;
						
						if(pos >= actualLetters.length() ||
							actualLetters.charAt(pos) != (char)(int)e.getKey() ||
							isKeyDown && !(e instanceof KeyDownChange) ||
							!isKeyDown && !(e instanceof KeyUpChange)
						) {
							remove = true;
							continue;
						}
						
						i++;
					}
				}
				
				if(remove) {
					//System.err.println("============== REMOVED");
					toRemove.add(fkc);
				}
			}
			
			int before = user.getValue().size();
			user.getValue().removeAll(toRemove);
			//System.err.println(before+" -- "+user.getValue().size());
		}
	}

	public static void mainSvmExport(String phrase, String outPath, DataSettings settings) throws IOException {
		
		LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> map = new DataReader().getInputData(phrase);
		
		LinkedHashMap<Integer, Collection<? extends Arrayzable>> map2 = Arrayzable.map2ArrayzableMap(map, settings);

		Collection<Arrayzable> normalize = new LinkedHashSet<Arrayzable>();
		for(Entry<Integer, Collection<? extends Arrayzable>> e : map2.entrySet()) {
			normalize.addAll(e.getValue());
		}
		Arrayzable.normalize(normalize);
		
		Arrayzable.writeToFile(outPath, map2);
		
		System.out.println("DONE2");
		
	}
	
	// assume that data in map is normalized
	public static Float[] computeWeights(Map<Integer, Collection<? extends Arrayzable>> map) {
		String fselectScriptPath = "/home/student/workspace/kdroid/libs/libsvm-3.20/tools/fselect.py";
		
		String tmpDir = System.getProperty("java.io.tmpdir")+"/"+
			new SimpleDateFormat("yyyy_MM_dd__H_m_s__S__").format(new Date())+UUID.randomUUID().toString()+
			"/";
		
		String trainingFile = "training";
		//String testingFile = "testing";
		
		String trainingPath = tmpDir+trainingFile;
		//String testingPath = tmpDir+testingFile;
		
		String fscoreFilePath = tmpDir+trainingFile+".fscore";
		
		String[] rankingCmd = {"python", fselectScriptPath, trainingPath};//, testingPath};
		
		/*
//		LinkedHashMap<Integer, Collection<? extends Arrayzable>> mapA = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
//		LinkedHashMap<Integer, Collection<? extends Arrayzable>> mapB = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
//		
//		for(Entry<Integer, Collection<? extends Arrayzable>> e : map.entrySet()) {
//			int key = e.getKey();
//			Collection<? extends Arrayzable> set = e.getValue();
//			
//			LinkedHashSet<Arrayzable> setA = new LinkedHashSet<Arrayzable>();
//			LinkedHashSet<Arrayzable> setB = new LinkedHashSet<Arrayzable>();
//			
//			int i=0;
//			for(Arrayzable ar : set){
//				if(i%2 == 0) 
//					setA.add(ar);
//				else
//					setB.add(ar);
//				i++;
//			}
//			
//			mapA.put(key, setA);
//			mapB.put(key, setB);
//		}
		*/
		
		//
		if(!new File(tmpDir).mkdirs())
			throw new RuntimeException("Cannot create tmp folder '"+tmpDir+"'");
		
		try {
			Arrayzable.writeToFile(trainingPath, map);
//			Arrayzable.writeToFile(trainingPath, mapA);
//			Arrayzable.writeToFile(testingPath, mapB);
			//System.out.println("Inainte de trainproc");
			Process trainProc = Runtime.getRuntime().exec(rankingCmd, null, new File(tmpDir));
			//System.out.println("Dupa trainproc");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(trainProc.getInputStream()));
			while ((reader.readLine()) != null) {}
			
			try {
				trainProc.waitFor();
			} catch (InterruptedException e) {}
			//System.out.println(fscoreFilePath);
			LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fscoreFilePath)));
			lnr.skip(Long.MAX_VALUE);
			int linesCount = lnr.getLineNumber();
			
			Float[] result = new Float[linesCount];
			
			BufferedReader br = new BufferedReader(new FileReader(fscoreFilePath));
			String line;
			while ((line = br.readLine()) != null) {
				if(line.length() > 3) {
					String[] parts = line.split(":");
					parts[0] = parts[0].replaceAll("\\s","");
					parts[1] = parts[1].replaceAll("\\s","");
					
					int nr = Integer.parseInt(parts[0]);
					float v = Float.parseFloat(parts[1]);
					
					result[nr-1] = v;
				}
			}
			br.close();
			
			return result;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { FileUtils.deleteDirectory(new File(tmpDir)); } catch (IOException e1) {}
		}
	}
	
	
	public static void mainStatistic(DataSettings settings) throws IOException {
		String word = words[0];
		
		Map<Integer, Collection<FixedKeystrokeContainer2>> data = new DataReader().getInputData(word);
		filterKeystroke(data, actualLetters[0]);
		
		//System.out.println(actualLetters[0]);
		System.out.println(data.size());
		
		
		
		//keyPresses[0] = [val(I), val(N), .... ]
		//keyPresses[1] = [val(I), val(N), .... ]
		LinkedList<LinkedList<Float>> keyPresses = new LinkedList<LinkedList<Float>>();
		LinkedList<LinkedList<Float>> keyReleases = new LinkedList<LinkedList<Float>>();
		
		//keyPressesPerPosition[0] = [val(I), val(I), .... ]
		//keyPressesPerPosition[1] = [val(N), val(N), .... ]
		LinkedList<LinkedList<Float>> keyPressesPerPosition = new LinkedList<LinkedList<Float>>();
		LinkedList<LinkedList<Float>> keyReleasesPerPosition = new LinkedList<LinkedList<Float>>();
		
		
		//there are more keyReleases
		for (int i = 0; i < word.length()-1; i++) {
			keyPressesPerPosition.add(new LinkedList<Float>());
			keyReleasesPerPosition.add(new LinkedList<Float>());
		}
		keyReleasesPerPosition.add(new LinkedList<Float>());
		
		
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : data.entrySet()) {
			//for with all the participants
			Collection<Arrayzable> sAll = new LinkedHashSet<Arrayzable>();
			System.out.println("NEW");
			
			//accelerometer stuff
			/*
			for(FixedKeystrokeContainer2 fkc : e.getValue()) {
				//for with the samples for the participants
				
				//System.out.println(fkc.printKeystrokes());
				
				FreeData elt = new FreeData(e.getKey(), fkc, settings);
				int indexOfAccY = 0;
				int indexOfAccX = 0;
				for (int i = 0; i < elt.floatLabels.length; i++) {
					if (elt.floatLabels[i].equals("avgValAcc1")) {
						indexOfAccY = i;
					}
					if (elt.floatLabels[i].equals("avgValAcc0")) {
						indexOfAccX = i;
					}
				}
				
				System.out.println(elt.floatData[indexOfAccY] + " " + elt.floatData[indexOfAccX]);
				sAll.add(elt);
			}
			*/
			
			
			
			LinkedList<Float> currentKeyPresses = new LinkedList<Float>();
			LinkedList<Float> currentKeyReleases = new LinkedList<Float>();
			//for keystrokes
			for(FixedKeystrokeContainer2 fkc : e.getValue()) {
				//for with the samples for each participant
				
				//System.out.println(fkc.printKeystrokes());
				
				FreeData elt = new FreeData(e.getKey(), fkc, settings);
				int positionForCurrentKeyPress = 0;
				int positionForCurrentKeyRelease = 0;
				
				
				//print key presses
				System.out.print("KIT: ");
				for (int i = 0; i < elt.floatLabels.length; i++) {
					if (elt.floatLabels[i].contains("keyPresses")) {
						//System.out.print(elt.floatLabels[i]+":");
						System.out.print(elt.floatData[i] + " ");
						currentKeyPresses.add(elt.floatData[i]);
						keyPressesPerPosition.get(positionForCurrentKeyPress).add(elt.floatData[i]);
						positionForCurrentKeyPress++;
					}
				}
				
				
				//print key releases
				System.out.println();
				System.out.print("KHT: ");
				for (int i = 0; i < elt.floatLabels.length; i++) {
					if (elt.floatLabels[i].contains("keyReleases")) {
						//System.out.print(elt.floatLabels[i]+":");
						System.out.print(elt.floatData[i] + " ");
						currentKeyReleases.add(elt.floatData[i]);
						keyReleasesPerPosition.get(positionForCurrentKeyRelease).add(elt.floatData[i]);
						positionForCurrentKeyRelease++;
					}
				}
				System.out.println();
				
				//System.out.println(elt.floatData[indexOfAccY] + " " + elt.floatData[indexOfAccX]);
				sAll.add(elt);
			}
			System.out.println();
			System.out.println();
			
			
			keyPresses.add(currentKeyPresses);
			keyReleases.add(currentKeyReleases);
			
			//mapAll.put(e.getKey(), sAll);
		}
		
		
		
		//here we already have all KHT and KHI
		//we do, for every position in that array, a sorting, then we perform binning
		
		
		//sorting the values, to see how the bins look like
		for(int i = 0; i < keyPressesPerPosition.size(); i++) {
			LinkedList<Float> currentList = keyPressesPerPosition.get(i);
			Collections.sort(currentList);
		}
		
		for(int i = 0; i < keyReleasesPerPosition.size(); i++) {
			LinkedList<Float> currentList = keyReleasesPerPosition.get(i);
			Collections.sort(currentList);
		}
		
		
		
		//10ms bins
		LinkedList<HashMap<Integer, Integer>> binListPressesPerPosition = new LinkedList<HashMap<Integer, Integer>>();
		for(int i = 0; i < keyPressesPerPosition.size(); i++) {
			binListPressesPerPosition.add(new HashMap<Integer, Integer>());
			
			
			LinkedList<Float> currentList = keyPressesPerPosition.get(i);
			HashMap<Integer, Integer> currentHashMap = binListPressesPerPosition.get(i);
			
			for (int j = 0; j < currentList.size(); j++) {
				Float currentValue = currentList.get(j);
				Integer currentIntValue = currentValue.intValue();
				Integer currentBinValue = currentIntValue / 10;
				
				//it the key is not in hash, we add it
				if(!currentHashMap.containsKey(currentBinValue)) {
					currentHashMap.put(currentBinValue, new Integer(1));
				}
				else {
					currentHashMap.put(currentBinValue, currentHashMap.get(currentBinValue) + 1);
				}
			}
		}
		
		
		
		System.out.println("BIN VALUES");
		for (int i = 0; i < binListPressesPerPosition.size(); i++) {
			//hashmap for position 0, 1, etc.
			System.out.print("Position " + i + ": ");
			HashMap<Integer, Integer> currentBin = binListPressesPerPosition.get(i);
			Iterator it = currentBin.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				System.out.print(pair.getKey() + "0ms:" + pair.getValue() + ", ");
				it.remove();
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("All KITs:");
		for(int i = 0; i < keyPressesPerPosition.size(); i++) {
			System.out.print("Position " + i + ": ");
			for (int j = 0; j < keyPressesPerPosition.get(i).size(); j++) {
				System.out.print(keyPressesPerPosition.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("All KHTs:");
		for(int i = 0; i < keyReleasesPerPosition.size(); i++) {
			System.out.print("Position " + i + ": ");
			for (int j = 0; j < keyReleasesPerPosition.get(i).size(); j++) {
				System.out.print(keyReleasesPerPosition.get(i).get(j) + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		
		
		
		
		
		
		
	}
	

	public static void mainDist(DataSettings settings) throws IOException {
		int filesCounter = 0;
		Map<String, Collection<ChartValues>> charts = new LinkedHashMap<String, Collection<ChartValues>>();
		
		System.out.println("########################################################################");
		
		for(int w=0; w<words.length; w++) {
			for(int freq : freqs) {
			String word = words[w];
			
			Map<Integer, Collection<FixedKeystrokeContainer2>> data = new DataReader().getInputData(word);
			filterKeystroke(data, actualLetters[w]);
			FixedKeystrokeContainer2.lowerFrequency(data, freq);
			
			for(int graphType : graphTypes) {
					int graphTypeAbs = graphType > 0 ? graphType : -graphType;
					int realGraphType;
					if(graphType == 0)
						realGraphType = 0;
					else if(graphType > 0)
						realGraphType = graphType+1;
					else // graphType < 0
						realGraphType = graphType-1;
					
					for(int step : steps) {
						if(step <= graphType || graphType == 0) {
		//					if(graphType == 0 && splitPressRelease)
		//						continue;
							//System.out.println(word+" graphType="+graphType+" splitPressRelease="+splitPressRelease);
							System.out.println("%%%%%%%%%%%% "+word+" graphType="+realGraphType+" step="+step+" freq="+freq+" %%%%%%%%%%%%");
							Map<Integer, Collection<? extends Arrayzable>> mapAll = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
							
							for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : data.entrySet()) {
								Collection<Arrayzable> sAll = new LinkedHashSet<Arrayzable>();
								for(FixedKeystrokeContainer2 fkc : e.getValue()) {
									Arrayzable elt;
									if(graphType != 0)
										elt = new GraphData(graphType, step, false, e.getKey(), fkc, settings);
									else
										elt = new FreeData(e.getKey(), fkc, settings);
									sAll.add(elt);
								}
								
								mapAll.put(e.getKey(), sAll);
							}
							//if(1!=2) throw new RuntimeException();
							
							Collection<Arrayzable> normalize = new LinkedHashSet<Arrayzable>();
							for(Entry<Integer, Collection<? extends Arrayzable>> e : mapAll.entrySet()) {
								normalize.addAll(e.getValue());
							}
							
							
							Arrayzable.normalize(normalize);
							
							//Collection<ChartValues> freeText = new LinkedHashSet<ChartValues>();
							Collection<ChartValues> freeTextUserSplit = new LinkedHashSet<ChartValues>();
							Collection<ChartValues> freeTextOther = new LinkedHashSet<ChartValues>();
							
							final Float[] weights = computeWeights(mapAll);
							//final Float[] weights = FreeData.getWeights();
							//final Float[] weights = weightsTmp[w];
				//			System.err.println("WEIGHTS: "+Arrays.toString(weights));
				//			
				//			Float[] tmpWeights = weights.clone();
				//			Arrays.sort(tmpWeights);
				//			float inValue = tmpWeights[(int)(tmpWeights.length*0.5)];
				//			float max = Float.MIN_VALUE;
				//			for(int i=0; i<weights.length; i++) {
				//				if(weights[i] > max)
				//					max = weights[i];
				//			}
				//			System.err.println(inValue+"-"+max);
				//			
				//			for(int i=0; i<weights.length; i++) {
				//				if(weights[i] < inValue) {
				//					weights[i] = 0f;
				//				} else {
				//					weights[i] = 1f;//Arrayzable.scale(weights[i], inValue, max)*10+5;
				//				}
				//			}
							//System.err.println(count+", "+weights.length);
				
				//			freeText.add(new ChartValues("mean", "euclidean", ML.leaveOneOut(FreeMLMeanEuclidian.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "euclidean weighted", ML.leaveOneOut(FreeMLMeanEuclidian.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "euclidean normed", ML.leaveOneOut(FreeMLMeanEuclidianNormed.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "euclidean normed weighted", ML.leaveOneOut(FreeMLMeanEuclidianNormed.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "manhattan", ML.leaveOneOut(FreeMLMeanManhattan.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "manhattan weighted", ML.leaveOneOut(FreeMLMeanManhattan.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "manhattan scaled", ML.leaveOneOut(FreeMLMeanManhattanScaled.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("mean", "manhattan scaled weighted", ML.leaveOneOut(FreeMLMeanManhattanScaled.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "euclidean", ML.leaveOneOut(FreeMLKnnEuclidian.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "euclidean weighted", ML.leaveOneOut(FreeMLKnnEuclidian.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "euclidean normed", ML.leaveOneOut(FreeMLKnnEuclidianNormed.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "euclidean normed weighted", ML.leaveOneOut(FreeMLKnnEuclidianNormed.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "manhattan", ML.leaveOneOut(FreeMLKnnManhattan.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "manhattan weighted", ML.leaveOneOut(FreeMLKnnManhattan.getFactory(weights), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "manhattan scaled", ML.leaveOneOut(FreeMLKnnManhattanScaled.getFactory(null), mapAll, false)));
				//			freeText.add(new ChartValues("kNN (k=1)", "manhattan scaled weighted", ML.leaveOneOut(FreeMLKnnManhattanScaled.getFactory(weights), mapAll, false)));
				//			freeTextOther.add(new ChartValues("naive bayes", "simple algoritm", ML.leaveOneOut(FreeMLNaiveBayesSimple.getFactory(), mapAll, false)));
				//			//freeTextOther.add(new ChartValues("SVM one-class", "simple algoritm", ML.leaveOneOut(FreeMLSvm.getFactory(), mapAll, false)));
				
							System.out.print("A."); freeTextUserSplit.add(new ChartValues("mean", "euclidean", ML.leaveOneOut(FreeMLMeanEuclidian.getFactory(null), mapAll, true, eer)));
		System.out.print("1.");					freeTextUserSplit.add(new ChartValues("mean", "euclidean weighted", ML.leaveOneOut(FreeMLMeanEuclidian.getFactory(weights), mapAll, true, eer)));
							System.out.print("B."); freeTextUserSplit.add(new ChartValues("mean", "euclidean normed", ML.leaveOneOut(FreeMLMeanEuclidianNormed.getFactory(null), mapAll, true, eer)));
		System.out.print("2.");					freeTextUserSplit.add(new ChartValues("mean", "euclidean normed weighted", ML.leaveOneOut(FreeMLMeanEuclidianNormed.getFactory(weights), mapAll, true, eer)));
							System.out.print("C."); freeTextUserSplit.add(new ChartValues("mean", "manhattan", ML.leaveOneOut(FreeMLMeanManhattan.getFactory(null), mapAll, true, eer)));
		System.out.print("3.");					freeTextUserSplit.add(new ChartValues("mean", "manhattan weighted", ML.leaveOneOut(FreeMLMeanManhattan.getFactory(weights), mapAll, true, eer)));
		System.out.print("4.");					freeTextUserSplit.add(new ChartValues("mean", "manhattan scaled", ML.leaveOneOut(FreeMLMeanManhattanScaled.getFactory(null), mapAll, true, eer)));
		System.out.print("5.");					freeTextUserSplit.add(new ChartValues("mean", "manhattan scaled weighted", ML.leaveOneOut(FreeMLMeanManhattanScaled.getFactory(weights), mapAll, true, eer)));
							System.out.print("D."); freeTextUserSplit.add(new ChartValues("kNN (k=1)", "euclidean", ML.leaveOneOut(FreeMLKnnEuclidian.getFactory(null), mapAll, true, eer)));
		System.out.print("6.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "euclidean weighted", ML.leaveOneOut(FreeMLKnnEuclidian.getFactory(weights), mapAll, true, eer)));
		System.out.print("7.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "euclidean normed", ML.leaveOneOut(FreeMLKnnEuclidianNormed.getFactory(null), mapAll, true, eer)));
		System.out.print("8.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "euclidean normed weighted", ML.leaveOneOut(FreeMLKnnEuclidianNormed.getFactory(weights), mapAll, true, eer)));
							System.out.print("E."); freeTextUserSplit.add(new ChartValues("kNN (k=1)", "manhattan", ML.leaveOneOut(FreeMLKnnManhattan.getFactory(null), mapAll, true, eer)));
		System.out.print("9.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "manhattan weighted", ML.leaveOneOut(FreeMLKnnManhattan.getFactory(weights), mapAll, true, eer)));
		System.out.print("10.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "manhattan scaled", ML.leaveOneOut(FreeMLKnnManhattanScaled.getFactory(null), mapAll, true, eer)));
		System.out.print("11.");					freeTextUserSplit.add(new ChartValues("kNN (k=1)", "manhattan scaled weighted", ML.leaveOneOut(FreeMLKnnManhattanScaled.getFactory(weights), mapAll, true, eer)));
	//						System.out.print("X."); freeTextOther.add(new ChartValues("naive bayes", "user-split threshold", ML.leaveOneOut(FreeMLNaiveBayesSimple.getFactory(), mapAll, true, eer)));
							//System.out.print("Y."); freeTextOther.add(new ChartValues("SVM one-class", "user-split threshold", ML.leaveOneOut(FreeMLSvm.getFactory(), mapAll, eer)));
							
							System.out.println();
							
							String mainName = "";
							
							if(settings.isSensors())
								mainName += " & sensors";
							if(settings.isKeystrokeKeyPress())
								mainName += " & keyDownUp";
							if(settings.isKeystrokeKeyRelease())
								mainName += " & keyUpDown";
							if(settings.isKeystrokeKeyDownDown())
								mainName += " & keyDownDown";
							if(settings.isKeystrokeKeyUpUp())
								mainName += " & keyUpUp";
							if(settings.isAllKeystrokes())
								mainName += " & allKeystrokes";
							
							mainName = mainName.substring(2);
							
							if(graphType == 0)
								mainName += " & wholeWord";
							else
								mainName += " & graphType="+realGraphType;
							
							if(freq == 0)
								mainName += " & freq=full";
							else
								mainName += " & freq="+freq;
							
							 String name;
							 Collection<ChartValues> chartValuesC;
							
							//charts.put(mainName+" analysis for word "+word+"\r\n(mean & kNN algorithms), standard algorithm", freeText);
							name = mainName+" analysis for word "+actualLetters[w]+"\n(mean & kNN algorithms), user-split threshold";
							chartValuesC = freeTextUserSplit;
							charts.put(name, chartValuesC);
							printResults(name, chartValuesC);
							
							name = mainName+" analysis for word "+actualLetters[w]+"\n(SVM & naive bayes)";
							chartValuesC = freeTextOther;
							charts.put(name, chartValuesC);
							printResults(name, chartValuesC);
							
							System.out.println("\r\n \r\n \r\n");
						}
					}
				}
			}
		}
		
		for(Entry<String, Collection<ChartValues>> chart : charts.entrySet()) {
			
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			for(ChartValues values : chart.getValue()) {
				ErrorRate err = values.getErr();
				dataSet.addValue(err.getMeanErr(), values.getSeries(), values.getCattegory());
			}
			
			JFreeChart c = createChart(dataSet, chart.getKey());
			
			ChartUtilities.saveChartAsPNG(new File(filesPath+(filesCounter++)+".png"), c, 500, 300);
		}
	}
	private static void printResults(String name, Collection<ChartValues> chartValuesC) {
		System.out.println("\r\n=== "+name+" ===");
		for(ChartValues values : chartValuesC) {
			System.out.println(values.getCattegory()+", "+values.getSeries()+": "+values.getErr().getMeanErr()*100+"%"+"    "+values.getErr()); //+", diff: "+values.getErr().diff()*100+"%");
		}
	}

	
	private static void printKeyUpDowns(Map<Integer, Collection<FixedKeystrokeContainer2>> map) {
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : map.entrySet()) {
			Integer userId = e.getKey();
			Collection<FixedKeystrokeContainer2> keystrokes = e.getValue();
			
			for(FixedKeystrokeContainer2 k : keystrokes) {
				System.out.print("---- USER "+userId+": \t");
				for(KeyChange kdu : k.getKeyDownsUps()) {
					System.out.print((char)(int)kdu.getKey());
					if(kdu instanceof KeyUpChange) {
						System.out.print("' ");
					} else {
						System.out.print(", ");
					}
				}
				System.out.println();
			}
		}
	}
	
    private static JFreeChart createChart(CategoryDataset dataset, String title) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
        	title,         // chart title
            "Variants",               // domain axis label
            "Equal error rate",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setTitle(
		   new org.jfree.chart.title.TextTitle(title,
		       new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14)
		   )
		);

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        DecimalFormat chartDecimalFormat = new DecimalFormat("0%");
        rangeAxis.setNumberFormatOverride(chartDecimalFormat);
        rangeAxis.setRange(0.0, 0.4);
        rangeAxis.setTickUnit(new NumberTickUnit(0.1));

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
//        GradientPaint gp0 = new GradientPaint(
//            0.0f, 0.0f, Color.blue, 
//            0.0f, 0.0f, new Color(0, 0, 64)
//        );
//        GradientPaint gp1 = new GradientPaint(
//            0.0f, 0.0f, Color.green, 
//            0.0f, 0.0f, new Color(0, 64, 0)
//        );
//        GradientPaint gp2 = new GradientPaint(
//            0.0f, 0.0f, Color.red, 
//            0.0f, 0.0f, new Color(64, 0, 0)
//        );
//        renderer.setSeriesPaint(0, gp0);
//        renderer.setSeriesPaint(1, gp1);
//        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }

}