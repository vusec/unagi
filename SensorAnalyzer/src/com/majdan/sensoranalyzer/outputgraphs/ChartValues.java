package com.majdan.sensoranalyzer.outputgraphs;

import com.majdan.sensoranalyzer.ErrorRate;

public class ChartValues {
	private String series;
	private String cattegory;
	private ErrorRate err;
	
	public ChartValues(String cattegory, String series, ErrorRate err) {
		this.series = series;
		this.cattegory = cattegory;
		this.err = err;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getCattegory() {
		return cattegory;
	}

	public void setCattegory(String cattegory) {
		this.cattegory = cattegory;
	}

	public ErrorRate getErr() {
		return err;
	}

	public void setErr(ErrorRate err) {
		this.err = err;
	}

}
