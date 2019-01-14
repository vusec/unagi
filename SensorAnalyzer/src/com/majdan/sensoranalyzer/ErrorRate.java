package com.majdan.sensoranalyzer;

public class ErrorRate implements Comparable<ErrorRate> {
	private Float far=0f, mr=0f;
	private Integer farCount, farCountAll, mrCount, mrCountAll;
	
	public ErrorRate(Integer farCount, Integer farCountAll, Integer mrCount, Integer mrCountAll) {
		this.farCount = farCount;
		this.farCountAll = farCountAll;
		this.mrCount = mrCount;
		this.mrCountAll = mrCountAll;
		
		if(farCount != null && farCountAll != null && farCountAll != 0)
			this.far = ((float)farCount)/farCountAll;
		if(mrCount != null && mrCountAll != null && mrCountAll != 0)
			this.mr = ((float)mrCount)/mrCountAll;
	}
	
	
	public Integer getFarCount() {
		return farCount;
	}
	public Integer getFarCountAll() {
		return farCountAll;
	}
	public Integer getMrCount() {
		return mrCount;
	}
	public Integer getMrCountAll() {
		return mrCountAll;
	}
	
	
	public Float getMeanErr() {
		if(far!=null && mr!=null)
			return (far+mr)/2;
		else return null;
	}
	
	public Float diff() {
		if(far!=null && mr!=null)
			return Math.abs(far-mr);
		else return null;
	}

	public Float getFar() {
		return far;
	}
	public Float getMr() {
		return mr;
	}
	
	@Override
	public String toString() {
		return "(far:"+far+"["+farCount+"/"+farCountAll+"] mr:"+mr+"["+mrCount+"/"+mrCountAll+"] diff:"+this.diff()+")";
	}

	@Override
	public int compareTo(ErrorRate o) {
		if(this.far==null || this.mr==null || o.far==null || o.mr==null) {
			throw new IllegalArgumentException("FAR or MR is null in one of objects");
		}
		
		Integer farC = new Float(this.far).compareTo(o.far);
		if(farC != 0)
			return farC;
		else
			return new Float(this.mr).compareTo(o.mr);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorRate o = (ErrorRate)obj;
		
		return this.far == o.far && this.mr == o.mr;
	}
	
}
