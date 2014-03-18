package com.example.estimote_magisterka;

public class AveragesDistances {

	private Double[] sumDistance;
	private int[] count;

	public AveragesDistances(int beaconCount) {
		super();
		this.sumDistance = new Double[beaconCount];
		this.count = new int[beaconCount];
		for (int i = 0; i < this.sumDistance.length; i++) {
			this.sumDistance[i] = (double) 0;
			this.count[i] = 0;
		}
	}

	public void incrementCount(int i) {
		this.count[i]++;
	}

	public Double getSumDistance(int i) {
		return this.sumDistance[i];
	}

	public void setSumDistance(int i, Double distance) {
		this.sumDistance[i] += distance;
	}

	public int getCount(int i) {
		return count[i];
	}

	public void setCount(int i, int count) {
		this.count[i] = count;
	}

}
