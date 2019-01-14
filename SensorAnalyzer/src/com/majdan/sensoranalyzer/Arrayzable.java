package com.majdan.sensoranalyzer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.majdan.sensordynamics.FixedKeystrokeContainer;
import com.majdan.sensordynamics.FixedKeystrokeContainer2;


public abstract class Arrayzable implements Serializable{
	public abstract Float[] getArrayData();
	public abstract Integer getLabel();
	
//	public abstract float eclidean(T o, Float[] weights);
//	public abstract float eclideanNormed(T o, Float[] weights);
//	public abstract float manhattan(T o, Float[] weights);
//	public abstract float mahalanobis(T o, RealMatrix covM);
//	public abstract float naiveBayes(Gaussian[] g);
	
//	public abstract float naiveBayesEuclidean(Gaussian[] g, Float[] weights);
//	public abstract float naiveBayesEuclideanNormed(Gaussian[] g, Float[] weights);
//	public abstract float naiveBayesManhattan(Gaussian[] g, Float[] weights);
//	public abstract float naiveBayesMahalanobis(Gaussian[] g, RealMatrix covM);

	@Override
	public String toString() {
		Float[] data = getArrayData();
		
		String result = "";
		for(int i=0; i<data.length; i++) {
			String s;
			if(Float.isNaN(data[i]))
				s = "0";
			else
				s = new DecimalFormat("#.############").format(data[i]).replace(",", ".");
			result += (i+1)+":"+s+" ";
		}

		return result;
	}

	public static Float[] computeStdDev(Collection<? extends Arrayzable> data, boolean filter) {
		int length = data.iterator().next().getArrayData().length;
		Float[] result = new Float[length];
		for(int i=0; i<length; i++) {
			float mean = 0;
			for(Arrayzable a : data) {
				mean += a.getArrayData()[i];
			}
			mean /= data.size();
			
			float sum = 0;
			for(Arrayzable a : data) {
				sum += sqr(mean-a.getArrayData()[i]);
			}
			result[i] = (float) Math.sqrt(sum/data.size());
			if(filter && result[i] < 0.01f)
				result[i] = 0.01f;
		}
		
		return result;
	}
	public static Float[] computeAbsDev(Collection<? extends Arrayzable> data, boolean filter) {
		int length = data.iterator().next().getArrayData().length;
		Float[] result = new Float[length];
		for(int i=0; i<length; i++) {
			float mean = 0;
			for(Arrayzable a : data) {
				mean += a.getArrayData()[i];
			}
			mean /= data.size();
			
			float sum = 0;
			for(Arrayzable a : data) {
				sum += Math.abs( mean-a.getArrayData()[i] );
			}
			result[i] = (float) sum/data.size();
			if(filter && result[i] < 0.01f)
				result[i] = 0.01f;
		}
		
		return result;
	}
	
	public float eclidean(Arrayzable o, Float[] weights) {
		if(this.getArrayData().length != o.getArrayData().length || (weights!=null && this.getArrayData().length != weights.length)) {
			throw new RuntimeException("Different length of vectors: "+this.getArrayData().length+", "+o.getArrayData().length+(weights!=null ? ", "+weights.length : ""));
		}
		float sum = 0;
		for(int i=0; i<this.getArrayData().length; i++) {
			if(weights != null)
				sum += weights[i]*weights[i]*sqr(this.getArrayData()[i]-o.getArrayData()[i]);
			else
				sum += sqr(this.getArrayData()[i]-o.getArrayData()[i]);
		}
		
		float result;
		if(weights != null) {
			float weightsSum = 0;
			for(int i=0; i<weights.length; i++) {
				weightsSum += weights[i]*weights[i];
			}
			
			sum /= weightsSum;
			
			result = (float)Math.sqrt(sum);
			result = scale(sum, 0, 0.7f);
			
		} else {
			result = (float)Math.sqrt(sum);
			result = scale(result, 0, 10);
		}
		
		
		//System.err.println(result);
		return result;
	}

	public float eclideanNormed(Arrayzable o, Float[] weights) {
		float result = this.eclidean(o, weights)/(float)Math.sqrt((sqrSum(this)*sqrSum(o)));
		//System.err.println(scale(result, 0, 0.5f));
		return scale(result, 0, 0.5f);
	}

	public float manhattan(Arrayzable o, Float[] weights) {
		float sum = 0;
		for(int i=0; i<this.getArrayData().length; i++) {
			if(weights != null)
				sum += weights[i]*Math.abs(this.getArrayData()[i]-o.getArrayData()[i]);
			else
				sum += Math.abs(this.getArrayData()[i]-o.getArrayData()[i]);
		}
		sum /= this.getArrayData().length;
		
		float result;
		if(weights != null) {
			float weightsSum = 0;
			for(int i=0; i<weights.length; i++) {
				weightsSum += weights[i];//*weights[i];
			}
			
			sum /= weightsSum;
			
			result = scale(sum, 0, 0.01f);
		} else {
			result = scale(sum, 0, 0.4f);
		}
		
		//System.err.println(result);
		return result;
	}
	public float manhattanScaled(Arrayzable o, Float[] weights, Float[] stdDev) {
		float sum = 0;
		for(int i=0; i<this.getArrayData().length; i++) {
			if(weights != null)
				sum += ( weights[i]*Math.abs(this.getArrayData()[i]-o.getArrayData()[i]) ) / stdDev[i];
			else
				sum += ( Math.abs(this.getArrayData()[i]-o.getArrayData()[i]) ) / stdDev[i];
		}
		sum /= this.getArrayData().length;
		
		float result;
		if(weights != null) {
			float weightsSum = 0;
			for(int i=0; i<weights.length; i++) {
				weightsSum += weights[i];//*weights[i];
			}
			
			sum /= weightsSum;
			
			//result = scale(sum, 0, 0.01f);
			result = sum;
		} else {
			result = scale(sum, 0, 30f);
		}
		
		//System.err.println(result);
		return result;
	}
	
	public float outlier(Arrayzable o, Float[] weights, Float[] stdDev) {
		for(int i=0; i<this.getArrayData().length; i++) {
			Float v = Math.abs( this.getArrayData()[i]-o.getArrayData()[i] ) / stdDev[i];
			System.out.print(""+v+", ");
		}
		System.out.println();
		return manhattan(o, weights);
	}

	public float mahalanobis(Arrayzable o, RealMatrix covM) {
		double[] subArr = new double[this.getArrayData().length];
		for(int i=0; i<subArr.length; i++) {
			subArr[i] = this.getArrayData()[i]-o.getArrayData()[i];
		}
		
		RealMatrix right = MatrixUtils.createColumnRealMatrix(subArr);
		RealMatrix left = right.transpose();
		
		RealMatrix mid = left.multiply(covM);
		
		RealMatrix result = mid.multiply(right);
		
		int rowDim = result.getRowDimension();
		int colDim = result.getColumnDimension();
		if(rowDim != 1 || colDim != 1)
			throw new RuntimeException("Result matrix is not a 1x1 matrix");
		
		return (float) Math.sqrt( result.getEntry(0, 0) );
	}
	
	
	public float naiveBayes(Gaussian[] g) {
		Float[] arr = this.getArrayData();
		float prob = 1f;
		
		for(int i=0; i<arr.length; i++) {
			prob *= g[i].value(arr[i]);
		}
		
		return scaleNaiveBayes(prob);
	}
	
	
	public static RealMatrix computeInvCovMatrix(Collection<Float[]> vectors) {
		int n = vectors.size();
		int vecSize = -1;
		LinkedHashSet<RealVector> vectorsR = new LinkedHashSet<RealVector>();
		LinkedHashSet< double [] > vectorsD = new LinkedHashSet< double [] >();
		for(Float[] v : vectors) {
			double[] vD = new double[v.length];
			for(int j=0; j<v.length; j++) {
				vD[j] = v[j];
			}
			vecSize = v.length;
			
			vectorsR.add( MatrixUtils.createRealVector(vD) );
		}
		
		RealVector xP = new ArrayRealVector(vecSize);
		for(RealVector v : vectorsR) {
			xP.add(v);
		}
		xP.mapDivideToSelf((double)n);
		
		RealMatrix sumM = MatrixUtils.createRealMatrix(xP.getDimension(), xP.getDimension());
		for(RealVector v : vectorsR) {
			RealVector v1 = v.copy();
			RealVector v2 = v.copy();
			
			v1 = v1.subtract(xP);
			v2 = v2.subtract(xP);
			
			RealMatrix outProduct = v1.outerProduct(v2);
			sumM = sumM.add(outProduct);
		}
		sumM = sumM.scalarMultiply(1/((double)n));
		
		System.out.println(sumM.getColumnDimension()+", "+sumM.getRowDimension());
		System.out.println(sumM);
		
		return new LUDecomposition(sumM).getSolver().getInverse();
	}
	
	public static float scale(float v, float min, float max) {
		return (v-min)/(max-min);
	}
	public static float scaleNaiveBayes(float prob) {
		return (0.5f-prob)*2;
	}
	
	
	

	private static float sqr(float v) {
		return v*v;
	}
	private float sqrSum(Arrayzable o) {
		float sum = 0;
		for(float v : o.getArrayData()) {
			sum += sqr(v);
		}
		return (float)Math.sqrt(sum);
	}
	
	

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
	public static void writeToFile(String file, Map<Integer, Collection<? extends Arrayzable> > data) throws IOException {
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		for(Entry<Integer, Collection<? extends Arrayzable> > e : data.entrySet()) {
			writer.write(Arrayzable.setToString(e.getValue(), e.getKey() ));
		}
		writer.close();
	}
	public static String setToString(Collection<? extends Arrayzable> c, int label) {
		String result = "";
		for(Arrayzable fkc : c) {
			result += freeDataToString(fkc, label);
		}
		
		return result;
	}
	public static String freeDataToString(Arrayzable fkc, int label) {
		return label+" "+(fkc.toString())+"\r\n";
	}
	static LinkedHashMap<Integer, Collection<? extends Arrayzable>> map2ArrayzableMap(LinkedHashMap<Integer, Collection<FixedKeystrokeContainer2>> map, DataSettings settings) {
		LinkedHashMap<Integer, Collection<? extends Arrayzable>> map2 = new LinkedHashMap<Integer, Collection<? extends Arrayzable>>();
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : map.entrySet()) {
			LinkedHashSet<FreeData> set = new LinkedHashSet<FreeData>();
			for(FixedKeystrokeContainer2 fkc : e.getValue()) {
				set.add(new FreeData(e.getKey(), fkc, settings));
			}
			map2.put(e.getKey(), set);
		}
		return map2;
	}
}
