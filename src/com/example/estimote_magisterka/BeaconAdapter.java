package com.example.estimote_magisterka;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

public class BeaconAdapter extends BaseAdapter {

	int layoutResourceId;
	private LayoutInflater inflater;
	private ArrayList<Beacon> beacons;
	private double distance;

	public BeaconAdapter(Context context, int layoutResourceId) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.beacons = new ArrayList<Beacon>();
		this.layoutResourceId = layoutResourceId;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(layoutResourceId, null);
			convertView.setTag(new RowHolder(convertView));
		}
		RowHolder holder = (RowHolder) convertView.getTag();
		Beacon beacon = (Beacon) getItem(position);
		holder.macTextView.setText(String.format("MAC: %s",
				beacon.getMacAddress()));
		distance = Utils.computeAccuracy(beacon);
		holder.distanceTextView.setText(String.format("%.2fm", distance));
		holder.rssiTextView.setText(String.format("%ddBm", beacon.getRssi()));
		holder.measuredPowerTextView.setText(String.format("%ddBm",
				beacon.getMeasuredPower()));
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beacons.size();
	}

	@Override
	public Beacon getItem(int arg0) {
		// TODO Auto-generated method stub
		return beacons.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void replaceWith(Collection<Beacon> newBeacons) {
		this.beacons.clear();
		this.beacons.addAll(newBeacons);
		notifyDataSetChanged();
	}

	static class RowHolder {
		final TextView macTextView;
		final TextView distanceTextView;
		final TextView rssiTextView;
		final TextView measuredPowerTextView;

		public RowHolder(View view) {
			// TODO Auto-generated constructor stub
			macTextView = (TextView) view.findViewWithTag("mac");
			distanceTextView = (TextView) view.findViewWithTag("distance");
			rssiTextView = (TextView) view.findViewWithTag("rssi");
			measuredPowerTextView = (TextView) view
					.findViewWithTag("measured_power");
		}
	}
}
