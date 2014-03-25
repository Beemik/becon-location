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

//class which is an adapter for list of beacons, becouse list isn't standard, has a lot of fields in one row
public class BeaconAdapter extends BaseAdapter {

	int layoutResourceId; // sets layout to display
	private LayoutInflater inflater;
	private ArrayList<Beacon> beacons;

	public BeaconAdapter(Context context, int layoutResourceId) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.beacons = new ArrayList<Beacon>();
		this.layoutResourceId = layoutResourceId;
	}

	// reprezentation beacons to display on device
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
		holder.distanceTextView.setText(String.format("%.2fm",
				Utils.computeAccuracy(beacon)));
		holder.rssiTextView.setText(String.format("%ddBm", beacon.getRssi()));
		holder.measuredPowerTextView.setText(String.format("%ddBm",
				beacon.getMeasuredPower()));
		return convertView;
	}

	// get count of beacons on list
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beacons.size();
	}

	// get beacon from list
	@Override
	public Beacon getItem(int position) {
		// TODO Auto-generated method stub
		return beacons.get(position);
	}

	// get beacon id from list
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// refresh beacon list
	public void replaceWith(Collection<Beacon> newBeacons) {
		this.beacons.clear();
		this.beacons.addAll(newBeacons);
		notifyDataSetChanged();
	}

	// reprezentation of row list of beacons
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
