package com.example.estimote_magisterka;

import java.util.ArrayList;

//class to measure average distance and RSSI
public class AveragesValues {

	private Double sumDistance;
	private int sumRSSI;
	private int count;
	private boolean end;
	private ArrayList<Double> tab1;
	private ArrayList<Integer> tab2;

	public AveragesValues() {
		super();
		this.end = false;
		this.sumDistance = (double) 0;
		this.sumRSSI = 0;
		this.count = 0;
		this.tab1 = new ArrayList<Double>();
		this.tab2 = new ArrayList<Integer>();
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
		tab1.add(distance);
		this.sumDistance += distance;
	}

	public int getSumRSSI() {
		return this.sumRSSI;
	}

	// add RSSI value to variable
	public void setSumRSSI(int rssi) {
		tab2.add(rssi);
		this.sumRSSI += rssi;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<String> getStringTab1() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (Double value : tab1) {
			tmp.add(String.format("%f", value));
		}
		return tmp;
	}

	public ArrayList<String> getStringTab2() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (int value : tab2) {
			tmp.add(String.format("%d", value));
		}
		return tmp;
	}

	public void reset() {
		sumDistance = (double) 0;
		sumRSSI = 0;
		tab1.clear();
		tab2.clear();
	}

}
