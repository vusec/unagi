package com.majdan.sensordynamics;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FixedKeystrokeContainer2 implements Serializable {
	private Keystroke keystroke;
	private LinkedList<KeyUpChange> keyUps = new LinkedList<KeyUpChange>();
	private LinkedList<KeyDownChange> keyDowns = new LinkedList<KeyDownChange>();
	private LinkedList<KeyChange> keyDownsUps = null;
	
	private boolean changed = false;
	
	public FixedKeystrokeContainer2(FixedKeystrokeContainer fkc) {
		this.keystroke = fkc.getKeystroke();
		for(KeyChange kc : fkc.getKeyUps()) {
			keyUps.add((KeyUpChange) kc);
		}
		for(KeyChange kc : fkc.getKeyDowns()) {
			keyDowns.add((KeyDownChange) kc);
		}
	}
	
	FixedKeystrokeContainer2(Keystroke k, LinkedList<KeyUpChange> keyUps, LinkedList<KeyDownChange> keyDowns) {
		this.keystroke = k;
		this.keyUps = keyUps;
		this.keyDowns = keyDowns;
	}
	
	FixedKeystrokeContainer2(Keystroke k, LinkedList<KeyChange> keyUpsDowns) {
		this.keystroke = k;
		
		for(KeyChange kc : keyUpsDowns) {
			if(kc instanceof KeyUpChange) {
				keyUps.add((KeyUpChange) kc);
			} else if(kc instanceof KeyDownChange) {
				keyDowns.add((KeyDownChange) kc);
			} else {
				throw new RuntimeException("Unkown subclas of KeyChange");
			}
		}
	}
	
	FixedKeystrokeContainer2() {
		LinkedList<SensorReading> [] acc = new LinkedList [3];
		LinkedList<SensorReading> [] gy = new LinkedList [3];
		acc[0] = new LinkedList<SensorReading>();
		acc[1] = new LinkedList<SensorReading>();
		acc[2] = new LinkedList<SensorReading>();
		gy[0] = new LinkedList<SensorReading>();
		gy[1] = new LinkedList<SensorReading>();
		gy[2] = new LinkedList<SensorReading>();
		
		this.keystroke = new Keystroke(acc, gy);
	}
	
	public void lowerFrequency(int freq) {
		if(freq == 0)
			return;
		
		LinkedList<SensorReading> [] newAcc = new LinkedList[3];
		LinkedList<SensorReading> [] newGy = new LinkedList[3];
		newAcc[0] = new LinkedList<SensorReading>();
		newAcc[1] = new LinkedList<SensorReading>();
		newAcc[2] = new LinkedList<SensorReading>();
		newGy[0] = new LinkedList<SensorReading>();
		newGy[1] = new LinkedList<SensorReading>();
		newGy[2] = new LinkedList<SensorReading>();

		Iterator<SensorReading> acc0It = this.keystroke.getAcc()[0].iterator();
		Iterator<SensorReading> acc1It = this.keystroke.getAcc()[1].iterator();
		Iterator<SensorReading> acc2It = this.keystroke.getAcc()[2].iterator();
		Iterator<SensorReading> gy0It = this.keystroke.getGy()[0].iterator();
		Iterator<SensorReading> gy1It = this.keystroke.getGy()[1].iterator();
		Iterator<SensorReading> gy2It = this.keystroke.getGy()[2].iterator();
		
		int freqAbs = freq > 0 ? freq : -freq;
		
		int i=0;
		while(acc0It.hasNext() || acc1It.hasNext() || acc2It.hasNext() || gy0It.hasNext() || gy1It.hasNext() || gy2It.hasNext()) {
			boolean add = false;
			if(freq > 0) {
				add = (i%freqAbs) != 0;
			} else {
				add = (i%freqAbs) == 0;
			}
			
			if(add) {
				if(acc0It.hasNext())
					newAcc[0].add(acc0It.next());
				if(acc1It.hasNext())
					newAcc[1].add(acc1It.next());
				if(acc2It.hasNext())
					newAcc[2].add(acc2It.next());
				if(gy0It.hasNext())
					newGy[0].add(gy0It.next());
				if(gy1It.hasNext())
					newGy[1].add(gy1It.next());
				if(gy2It.hasNext())
					newGy[2].add(gy2It.next());				
			} else {
				if(acc0It.hasNext())
					acc0It.next();
				if(acc1It.hasNext())
					acc1It.next();
				if(acc2It.hasNext())
					acc2It.next();
				if(gy0It.hasNext())
					gy0It.next();
				if(gy1It.hasNext())
					gy1It.next();
				if(gy2It.hasNext())
					gy2It.next();
			}
			//System.err.println(add+" ");
			i++;
		}

//		System.err.print(this.keystroke.getAcc()[0].size()+"/");
//		System.err.print(this.keystroke.getAcc()[1].size()+"/");
//		System.err.print(this.keystroke.getAcc()[2].size()+"/");
//		System.err.print(this.keystroke.getGy()[0].size()+"/");
//		System.err.print(this.keystroke.getGy()[1].size()+"/");
//		System.err.print(this.keystroke.getGy()[2].size()+"/");
		this.keystroke = new Keystroke(newAcc, newGy);
		//System.err.println(this.keystroke.getAcc()[0].size());
	}
	
	public static void lowerFrequency(Map<Integer, Collection<FixedKeystrokeContainer2>> map, int freq) {
		for(Entry<Integer, Collection<FixedKeystrokeContainer2>> e : map.entrySet()) {
			for(FixedKeystrokeContainer2 fkc2 : e.getValue()) {
				//System.err.print(fkc2.getKeystroke().getAcc()[0].size()+"/");
				fkc2.lowerFrequency(freq);
				//System.err.println(fkc2.getKeystroke().getAcc()[0].size());
			}
		}
	}

	private static boolean equalsKeyChange(KeyChange kc1, KeyChange kc2) {
		return kc1.getTime() == kc2.getTime() && kc1.getKey() == kc2.getKey();
	}
	public FixedKeystrokeContainer2(Iterator<FixedKeystrokeContainer2> it) {
		this();
		
		if(!it.hasNext())
			return;
		
		FixedKeystrokeContainer2 elt = it.next();
		while(elt != null) {
			if(this.keyDowns.size() != 0)
				if(equalsKeyChange(this.keyDowns.getLast(), elt.keyDowns.getFirst()))
					this.keyDowns.removeLast();
			if(this.keyUps.size() != 0)
				if(equalsKeyChange(this.keyUps.getLast(), elt.keyUps.getFirst()))
					this.keyUps.removeLast();
			
			this.keyDowns.addAll(elt.keyDowns);
			this.keyUps.addAll(elt.keyUps);
			
			for(int i=0; i<3; i++) {
				this.keystroke.getAcc()[i].addAll(elt.keystroke.getAcc()[i]);
				this.keystroke.getGy()[i].addAll(elt.keystroke.getGy()[i]);
			}
			
			elt = it.hasNext() ? it.next() : null;
		}
	}
	public FixedKeystrokeContainer2(LinkedList<FixedKeystrokeContainer2> list) {
		this(list.iterator());
	}
	public FixedKeystrokeContainer2(FixedKeystrokeContainer2[] arr) {
		this(new ArrayIterator2<FixedKeystrokeContainer2>(arr));
	}
	
	enum TypeKeystrokeDataAll { KEYUP, KEYDOWN, ACC0, ACC1, ACC2, GY0, GY1, GY2; };
	class KeystrokeDataAll implements Comparable<KeystrokeDataAll> {
		Object o;
		TypeKeystrokeDataAll type;
		Long time;
		KeystrokeDataAll(Object o, TypeKeystrokeDataAll type) {
			this.o = o;
			this.type = type;
			if(o instanceof KeyChange) {
				this.time = ((KeyChange)o).getTime();
			} else if(o instanceof SensorReading)
				this.time = ((SensorReading)o).getTime();
			}
		@Override
		public int compareTo(KeystrokeDataAll another) {
			return this.time.compareTo(another.time);
		}
	}
	public FixedKeystrokeContainer2[] splitKeystrokes() {
		LinkedList<KeyUpChange> ku = this.getKeyUps();
		LinkedList<KeyDownChange> kd = this.getKeyDowns();
		LinkedList<SensorReading> [] acc = this.getKeystroke().getAcc();
		LinkedList<SensorReading> [] gy = this.getKeystroke().getGy();
		
		int length = ku.size() + kd.size() + acc[0].size() + acc[1].size() + acc[2].size() + gy[0].size() + gy[1].size() + gy[2].size();
		
		KeystrokeDataAll[] sorted = new KeystrokeDataAll[length];
		int rI = 0;
		for(KeyChange kc : ku) { sorted[rI++] = new KeystrokeDataAll(kc, TypeKeystrokeDataAll.KEYUP); }
		for(KeyChange kc : kd) { sorted[rI++] = new KeystrokeDataAll(kc, TypeKeystrokeDataAll.KEYDOWN); }
		for(SensorReading sr : acc[0]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.ACC0); }
		for(SensorReading sr : acc[1]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.ACC1); }
		for(SensorReading sr : acc[2]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.ACC2); }
		for(SensorReading sr : gy[0]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.GY0); }
		for(SensorReading sr : gy[1]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.GY1); }
		for(SensorReading sr : gy[2]) { sorted[rI++] = new KeystrokeDataAll(sr, TypeKeystrokeDataAll.GY2); }
		
		assert(rI == length);
		
		Arrays.sort(sorted);
		
		LinkedList<FixedKeystrokeContainer2> result = new LinkedList<FixedKeystrokeContainer2>();
		FixedKeystrokeContainer2 currFkc = new FixedKeystrokeContainer2();
		int i=0;
		while(sorted[i].type != TypeKeystrokeDataAll.KEYDOWN) {
			i++;
		}
		//System.out.print("XXXX-"+i);
		
		currFkc.keyDowns.add((KeyDownChange) sorted[i].o);
		i++;
		for(; i<sorted.length; i++) {
			if(sorted[i].type == TypeKeystrokeDataAll.KEYDOWN) {
				currFkc.keyDowns.add((KeyDownChange)sorted[i].o);
				
				result.add(currFkc);
				currFkc = new FixedKeystrokeContainer2();
				
				currFkc.keyDowns.add((KeyDownChange)sorted[i].o);
				
			} else if(sorted[i].type == TypeKeystrokeDataAll.KEYUP) {
				currFkc.keyUps.add((KeyUpChange)sorted[i].o);
				
				result.add(currFkc);
				currFkc = new FixedKeystrokeContainer2();
				
				currFkc.keyUps.add((KeyUpChange)sorted[i].o);
				
			} else if(sorted[i].type == TypeKeystrokeDataAll.ACC0) {
				currFkc.keystroke.getAcc()[0].add((SensorReading) sorted[i].o);
			} else if(sorted[i].type == TypeKeystrokeDataAll.ACC1) {
				currFkc.keystroke.getAcc()[1].add((SensorReading) sorted[i].o);
			} else if(sorted[i].type == TypeKeystrokeDataAll.ACC2) {
				currFkc.keystroke.getAcc()[2].add((SensorReading) sorted[i].o);
			} else if(sorted[i].type == TypeKeystrokeDataAll.GY0) {
				currFkc.keystroke.getGy()[0].add((SensorReading) sorted[i].o);
			} else if(sorted[i].type == TypeKeystrokeDataAll.GY1) {
				currFkc.keystroke.getGy()[1].add((SensorReading) sorted[i].o);
			} else if(sorted[i].type == TypeKeystrokeDataAll.GY2) {
				currFkc.keystroke.getGy()[2].add((SensorReading) sorted[i].o);
			} else {
				throw new RuntimeException("Wrong TypeKeystrokeDataAll type");
			}
		}
		//System.out.println("-"+i);
		
		return (FixedKeystrokeContainer2[]) result.toArray(new FixedKeystrokeContainer2[0]);
		
//		
//		LinkedList<FixedKeystrokeContainer2> result = new LinkedList<FixedKeystrokeContainer2>();
//		FixedKeystrokeContainer2 curr = new FixedKeystrokeContainer2();
//		
//		Iterator<KeyChange> kcIt = kud.iterator();
//		Iterator<SensorReading> acc0It = acc[0].iterator();
//		Iterator<SensorReading> acc1It = acc[1].iterator();
//		Iterator<SensorReading> acc2It = acc[2].iterator();
//		Iterator<SensorReading> gy0It = gy[0].iterator();
//		Iterator<SensorReading> gy1It = gy[1].iterator();
//		Iterator<SensorReading> gy2It = gy[2].iterator();
//		
//		KeyChange kc = kcIt.next();
//		SensorReading acc0 = acc0It.next();
//		SensorReading acc1 = acc1It.next();
//		SensorReading acc2 = acc2It.next();
//		SensorReading gy0 = gy0It.next();
//		SensorReading gy1 = gy1It.next();
//		SensorReading gy2 = gy2It.next();
//		
//		while(kcIt.hasNext() ||
//			acc0It.hasNext() || acc1It.hasNext() || acc2It.hasNext() ||
//			gy0It.hasNext() || gy1It.hasNext() || gy2It.hasNext()
//		) {
//			
//		}
	}

	public Keystroke getKeystroke() {
		return keystroke;
	}

	public void setKeystroke(Keystroke keystroke) {
		this.keystroke = keystroke;
	}

	public LinkedList<KeyUpChange> getKeyUps() {
		return keyUps;
	}

	public void setKeyUps(LinkedList<KeyUpChange> keyUps) {
		this.keyUps = keyUps;
		changed = true;
	}

	public LinkedList<KeyDownChange> getKeyDowns() {
		return keyDowns;
	}

	public void setKeyDowns(LinkedList<KeyDownChange> keyDowns) {
		this.keyDowns = keyDowns;
		changed = true;
	}
	
	public LinkedList<KeyChange> getKeyDownsUps() {
		if(keyDownsUps != null && !changed) {
			return keyDownsUps;
		} else {
			if(keyUps.size() == 0) {
				keyDownsUps = new LinkedList<KeyChange>(keyDowns);
				return keyDownsUps;
			} else if(keyDowns.size() == 0) {
				keyDownsUps = new LinkedList<KeyChange>(keyUps);
				return keyDownsUps;
			}
			
			//System.out.print("~@~~~~~~~~~~~~~~~~ "+keyUps.size()+"-"+keyDowns.size()+": ");
			LinkedList<KeyChange> keyDownsUps = new LinkedList<KeyChange>(); 
			
			Iterator<KeyUpChange> kuIt = keyUps.iterator();
			Iterator<KeyDownChange> kdIt = keyDowns.iterator();
			KeyChange ku = kuIt.next();
			KeyChange kd = kdIt.next();
			
			while(ku!=null && kd!=null) {
				if(ku.getTime() < kd.getTime()) {
					keyDownsUps.add(ku);
					ku = kuIt.hasNext() ? kuIt.next() : null; 
				} else {
					keyDownsUps.add(kd);
					kd = kdIt.hasNext() ? kdIt.next() : null;
				}
			}
			while(ku!=null) {
				keyDownsUps.add(ku);
				ku = kuIt.hasNext() ? kuIt.next() : null;
			}
			while(kd!=null) {
				keyDownsUps.add(kd);
				kd = kdIt.hasNext() ? kdIt.next() : null;
			}
			
			this.keyDownsUps = keyDownsUps;
			
			//System.out.println(keyDownsUps.size());
			return keyDownsUps;
		}
	}

	public String printKeystrokes() {
		String s = "";
		for(KeyChange kc : this.getKeyDownsUps()) {
			if(kc instanceof KeyUpChange) {
				s += "-"+((char)(int)((KeyUpChange)kc).getKey());
			} else if(kc instanceof KeyDownChange) {
				s += "  "+((char)(int)((KeyDownChange)kc).getKey());
			} else {
				throw new RuntimeException();
			}
		}
		
		return s;
	}
}
