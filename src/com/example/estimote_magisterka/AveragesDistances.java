package com.example.estimote_magisterka;

public class AveragesDistances {

	private Double sumDistance;
	private int count;
	private boolean end;

	public AveragesDistances() {
		super();
		this.end = false;
		this.sumDistance = (double) 0;
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

	public void setSumDistance(Double distance) {
		this.sumDistance += distance;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
