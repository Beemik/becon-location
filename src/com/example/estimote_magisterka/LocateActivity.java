package com.example.estimote_magisterka;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class LocateActivity extends Activity {

	public static final String CLICKED_BEACON = "clickedBeacon";
	private static final int REQUEST_ENABLE_BT = 0;
	private Region beaconsRegion = new Region("regionId", null, null, null);
	private BeaconManager beaconManager;
	private BeaconAdapter beaconAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		beaconAdapter = new BeaconAdapter(this, R.layout.beacon);
		ListView beaconList = (ListView) findViewById(R.id.listView1);
		beaconList.setAdapter(beaconAdapter);
		beaconList.setOnItemClickListener(createOnItemClickListener());

		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region arg0, final List<Beacon> arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						beaconAdapter.replaceWith(arg1);
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.scan, menu);
		MenuItem refresh = menu.findItem(R.id.item1);
		refresh.setActionView(R.layout.progress_layout);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void connectWithBeacon() {
		beaconAdapter.replaceWith(Collections.<Beacon> emptyList());
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

			@Override
			public void onServiceReady() {
				// TODO Auto-generated method stub
				try {
					beaconManager.startRanging(beaconsRegion);
				} catch (RemoteException e) {
					Toast.makeText(LocateActivity.this,
							"Cannot start ranging, something wrong.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK)
				connectWithBeacon();
			else
				Toast.makeText(this, "Bluetooth not enabled.",
						Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// if device cannot support BT Low Energy
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device doesn't support Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}
		// if BT isn't enabled, enable it
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectWithBeacon();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		try {
			beaconManager.stopRanging(beaconsRegion);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Cannot stop ranging.", Toast.LENGTH_LONG)
					.show();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		beaconManager.disconnect();
		super.onDestroy();
	}

	private AdapterView.OnItemClickListener createOnItemClickListener() {
		return new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						BeaconCharacteristicsActivity.class);
				intent.putExtra(CLICKED_BEACON, beaconAdapter.getItem(position));
				startActivity(intent);
			}

		};
	}
}