package com.majdan.sensordynamics;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FixedKeystrokeContainer implements Serializable {
	private Keystroke keystroke;
	private LinkedList<KeyChange> keyUps = new LinkedList<KeyChange>();
	private LinkedList<KeyChange> keyDowns = new LinkedList<KeyChange>();
	private LinkedList<KeyChange> keyDownsUps = null;
	
	private boolean changed = false;
	
	FixedKeystrokeContainer(Keystroke k, LinkedList<KeyChange> keyUps, LinkedList<KeyChange> keyDowns) {
		this.keystroke = k;
		this.keyUps = keyUps;
		this.keyDowns = keyDowns;
	}

	public Keystroke getKeystroke() {
		return keystroke;
	}

	public void setKeystroke(Keystroke keystroke) {
		this.keystroke = keystroke;
	}

	public LinkedList<KeyChange> getKeyUps() {
		return keyUps;
	}

	public void setKeyUps(LinkedList<KeyChange> keyUps) {
		this.keyUps = keyUps;
		changed = true;
	}

	public LinkedList<KeyChange> getKeyDowns() {
		return keyDowns;
	}

	public void setKeyDowns(LinkedList<KeyChange> keyDowns) {
		this.keyDowns = keyDowns;
		changed = true;
	}
	
	public LinkedList<KeyChange> getKeyDownsUps() {
		if(keyDownsUps != null && !changed) {
			return keyDownsUps;
		} else {
			LinkedList<KeyChange> keyDownsUps = new LinkedList<KeyChange>(); 
			
			Iterator<KeyChange> kuIt = keyUps.iterator();
			Iterator<KeyChange> kdIt = keyDowns.iterator();
			KeyChange ku = kuIt.next();
			KeyChange kd = kdIt.next();
			
			while(kuIt.hasNext() && kdIt.hasNext()) {
				if(ku.getTime() < kd.getTime()) {
					keyDownsUps.add(ku);
					ku = kuIt.next();
				} else {
					keyDownsUps.add(kd);
					kd = kdIt.next();
				}
			}
			while(kuIt.hasNext()) {
				keyDownsUps.add(ku);
				ku = kuIt.next();
			}
			while(kdIt.hasNext()) {
				keyDownsUps.add(kd);
				kd = kdIt.next();
			}
			
			this.keyDownsUps = keyDownsUps;
			return keyDownsUps;
		}
	}
}
