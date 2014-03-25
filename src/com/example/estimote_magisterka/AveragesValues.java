package com.example.estimote_magisterka;

//class to measure average distance and RSSI
public class AveragesValues {

	private Double sumDistance;
	private int sumRSSI;
	private int count;
	private boolean end;

	public AveragesValues() {
		super();
		this.end = false;
		this.sumDistance = (double) 0;
		this.sumRSSI = 0;
		this.count = 0;
	}

	public boolean getEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public void incrementCount() {
		this.count++;
	}

	public Double getSumDistance() {
		return this.sumDistance;
	}

	// add distance value to variable
	public void setSumDistance(Double distance) {
		this.sumDistance += distance;
	}

	public int getSumRSSI() {
		return this.sumRSSI;
	}

	// add RSSI value to variable
	public void setSumRSSI(int rssi) {
		this.sumRSSI += rssi;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
