package com.majdan.sensoranalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class FreeMLSvm extends FreeML {
	
	private static float rhoLowerBound = 0;
	private static float rhoUpperBound = 10;
	
	private static String svmTrainPath = "D:\\Programowanie\\libsvm\\windows\\svm-train.exe";
	private static String svmPredictPath = "D:\\Programowanie\\libsvm\\windows\\svm-predict.exe";
	
	private String filesPath = System.getProperty("java.io.tmpdir")+
		new SimpleDateFormat("yyyy_MM_dd__H_m_s__S__").format(new Date())+UUID.randomUUID().toString()+"___";
	
	private String traingingFile = filesPath+"training_";
	private String testingFileFar = filesPath+"testingFar";
	private String testingFileMr = filesPath+"testingMr";
	private String modelFile = filesPath+"model";
	private String outputFile = filesPath+"output";
	
	public FreeMLSvm(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
		super(training, testing);
	}

	private void createTrainingFile(int userId) {
		if(!new File(traingingFile+userId).exists()) {
			Map<Integer, Collection<? extends Arrayzable>> trainingDataUser = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
			trainingDataUser.put(1, training.get(userId));
			try {
				Arrayzable.writeToFile(traingingFile+userId, trainingDataUser);
				new File(traingingFile).deleteOnExit();
			} catch (IOException e) { throw new RuntimeException(e); }
		}
	}
	
	@Override
	public ErrorRate getErrRate(float acceptanceThreshold, int userId) {
		int farIn = 0;
		int farOut = 0;
		int mrIn = 0;
		int mrOut = 0;
		
		createTrainingFile(userId);
		
		Map<Integer, Collection<? extends Arrayzable>> newTesting = renameKeysMap(userId, testing);
		
		// far
		Map<Integer, Collection<? extends Arrayzable>> testingFar = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		testingFar.put(1, newTesting.get(1));
		try {
			Arrayzable.writeToFile(testingFileFar, testingFar);
		} catch (IOException e) { throw new RuntimeException(e); }
	
		// mr
		Map<Integer, Collection<? extends Arrayzable>> testingMr = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		testingMr.put(-1, newTesting.get(-1));
		try {
			Arrayzable.writeToFile(testingFileMr, testingMr);
		} catch (IOException e) { throw new RuntimeException(e); }
		
		String[] trainingCmd = {FreeMLSvm.svmTrainPath, "-s", "2", traingingFile+userId, modelFile};
		String[] testingTestingFarCmd = {FreeMLSvm.svmPredictPath, testingFileFar, modelFile, outputFile};
		String[] testingTestingMrCmd = {FreeMLSvm.svmPredictPath, testingFileMr, modelFile, outputFile};
		
		try {
			// training
			
			Process trainProc = Runtime.getRuntime().exec(trainingCmd);
			
			try {
				trainProc.waitFor();
			} catch (InterruptedException e) {}
			
			// setting the rho
			setRho(acceptanceThreshold);
			
			//testing
			Process testFarProc = Runtime.getRuntime().exec(testingTestingFarCmd);
			Process testMrProc = Runtime.getRuntime().exec(testingTestingMrCmd);
			
			BufferedReader testFarStd = new BufferedReader(new InputStreamReader(testFarProc.getInputStream()));
			BufferedReader testMrStd = new BufferedReader(new InputStreamReader(testMrProc.getInputStream()));
			
			String line;
			// far
			while ((line = testFarStd.readLine()) != null) {
				if(line.length() > 8 && line.startsWith("Accuracy")) {
					AccuracyResult farAcc = readAccuracy(line);
					farIn += farAcc.getIn();
					farOut += farAcc.getOut();
				}
			}
			// mr
			while ((line = testMrStd.readLine()) != null) {
				if(line.length() > 8 && line.startsWith("Accuracy")) {
					AccuracyResult mrAcc = readAccuracy(line);
					mrIn += mrAcc.getIn();
					mrOut += mrAcc.getOut();
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { new File(testingFileFar).delete(); } catch(SecurityException  e) {}
			try { new File(testingFileMr).delete(); } catch(SecurityException  e) {}
			try { new File(modelFile).delete(); } catch(SecurityException  e) {}
			try { new File(outputFile).delete(); } catch(SecurityException  e) {}
		}
		
		
		ErrorRate err = new ErrorRate(farOut-farIn, farOut, mrOut-mrIn, mrOut);
		//System.out.println(err);
		return err;
	}

	private void setRho(float acceptanceThreshold) throws IOException {
		float rho = rhoLowerBound + acceptanceThreshold*(rhoUpperBound-rhoLowerBound);
		
		LinkedList<String> list = new LinkedList<String>();
		BufferedReader f = new BufferedReader(new FileReader(modelFile));
		String line;
		while ((line = f.readLine()) != null) {
			if(line.startsWith("rho")) {
				list.add("rho "+rho);
			} else {
				list.add(line);
			}
		}
		f.close();
		
		new File(modelFile).delete();
		
		PrintWriter w = new PrintWriter(modelFile, "UTF-8");
		for(String l : list) {
			w.write(l);
			w.write("\r\n");
		}
		w.close();
	}
	
	private class AccuracyResult {
		int in;
		int out;
		public AccuracyResult(int in, int out) {
			this.in = in;
			this.out = out;
		}
		int getIn() { return in; }
		int getOut() { return out; }
		@Override public String toString() {
			return "in:"+in+", out:"+out;
		}
	}
	
	private AccuracyResult readAccuracy(String line) {
		int openBracketPos = line.indexOf("(");
		int slashPos = line.indexOf("/");
		int closeBracketPos = line.indexOf(")");
		
		int in = Integer.parseInt( line.substring(openBracketPos+1, slashPos) );
		int out = Integer.parseInt( line.substring(slashPos+1, closeBracketPos) );
		
		return new AccuracyResult(in, out);
	}

	public static String getNameObjects() {
		return "Free-text SVM";
	}
	
	@Override
	public ErrorRate getEqRate() {
		return super.getEqRate();
	}
	
	// changes all keys to values 1 or -1
	// if(key==oneClassNr) then key:=1; else key:=-1 
	Map<Integer, Collection<? extends Arrayzable>> renameKeysMap(Integer oneClassNr, Map<Integer, Collection<? extends Arrayzable>> map) {
		
		LinkedHashSet<Arrayzable> ones = new LinkedHashSet<Arrayzable>();
		LinkedHashSet<Arrayzable> minusOnes = new LinkedHashSet<Arrayzable>();
		
		for(Entry<Integer, Collection<? extends Arrayzable>> e : map.entrySet()) {
			if(e.getKey() == oneClassNr)
				ones.addAll(e.getValue());
			else
				minusOnes.addAll(e.getValue());
		}
		
		Map<Integer, Collection<? extends Arrayzable>> result = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		result.put(1, ones);
		result.put(-1, minusOnes);
		
		return result;
	}

	private static MLCreator factory = new MLCreator() {
		@Override
		public ML createObject(Map<Integer, Collection<? extends Arrayzable>> training, Map<Integer, Collection<? extends Arrayzable>> testing) {
			return new FreeMLSvm(training, testing);
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
