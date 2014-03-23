package com.example.estimote_magisterka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

public class LocateActivity extends Activity {

	public static final String CLICKED_BEACON = "clickedBeacon";
	private static final int REQUEST_ENABLE_BT = 0;
	private Region beaconsRegion = new Region("regionId", null, null, null);
	private BeaconManager beaconManager;
	private BeaconAdapter beaconAdapter;
	private Button getResultsButton;
	private ListView averageDistanceListView;
	private TextView averageDistanceTextView;

	private Button readFile;
	private boolean getResult;
	private AveragesDistances[] sumDistance;
	private ArrayList<String> macAddress;
	private ArrayAdapter<String> adapter;
	private int all = 0;
	private ArrayList<String> tmpArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getResult = false;
		averageDistanceListView = (ListView) findViewById(R.id.listView2);
		averageDistanceTextView = (TextView) findViewById(R.id.textView1);
		readFile = (Button) findViewById(R.id.button2);

		beaconAdapter = new BeaconAdapter(this, R.layout.beacon);
		final ListView beaconList = (ListView) findViewById(R.id.listView1);
		beaconList.setAdapter(beaconAdapter);
		beaconList.setOnItemClickListener(createOnItemClickListener());
		getResultsButton = (Button) findViewById(R.id.button1);

		readFile.setVisibility(View.VISIBLE);

		readFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> tmp = readFromFile();
				adapter = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.distance_row, tmp);
				averageDistanceListView.setAdapter(adapter);
			}
		});

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

						final int N = 5;
						int beaconCount = beaconAdapter.getCount();
						boolean error = false;

						if (getResult == true) {
							for (int i = 0; i < beaconCount; i++) {
								if (error) {
									getResult = false;
									break;
								}
								if (beaconCount != beaconAdapter.getCount()) {
									error = true;
									getResult = false;
									averageDistanceTextView.setText("Error.");
									break;
								}
								for (int j = 0; j < beaconCount; j++) {
									if (beaconAdapter.getCount() == 0) {
										averageDistanceTextView
												.setText("There are no beacons in range.");
										error = true;
										getResult = false;
										break;
									}
									if (sumDistance[j].getCount() == N
											&& sumDistance[j].getEnd() == false) {
										tmpArrayList.add(String
												.format("%d  values; %s : %.2fm; RSSI: %ddBm",
														sumDistance[j]
																.getCount(),
														macAddress.get(j),
														sumDistance[j]
																.getSumDistance()
																/ N,
														sumDistance[j]
																.getSumRSSI()
																/ N));
										sumDistance[j].setEnd(true);
										all++;
									}

									else if (sumDistance[j].getCount() != N
											&& macAddress.get(j).equals(
													beaconAdapter.getItem(i)
															.getMacAddress())) {
										sumDistance[j].setSumDistance(Utils
												.computeAccuracy(beaconAdapter
														.getItem(i)));
										sumDistance[j].setSumRSSI(beaconAdapter.getItem(i).getRssi());
										sumDistance[j].incrementCount();
									}
									if (all == beaconCount) {
										getResult = false;
									} else
										averageDistanceTextView.setText(String
												.format("wait.. %d",
														sumDistance[j]
																.getCount()));
								}
							}
						} else if (all > 0) {
							saveToFile(tmpArrayList);
							for (int i = 0; i < beaconCount; i++) {
								if (sumDistance[i].getEnd() == true) {
									sumDistance[i].setCount(0);
								}

							}
							all = 0;
							adapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.distance_row, tmpArrayList);

							averageDistanceTextView.setText("Results:");
							averageDistanceListView.setAdapter(adapter);
							macAddress.clear();
						}
					}
				});
			}
		});

		getResultsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tmpArrayList = new ArrayList<String>();
				if (getResult == false && beaconAdapter.getCount() != 0) {
					getResult = true;
					macAddress = new ArrayList<String>();
					sumDistance = new AveragesDistances[beaconAdapter
							.getCount()];
					for (int position = 0; position < beaconAdapter.getCount(); position++) {
						macAddress.add(beaconAdapter.getItem(position)
								.getMacAddress());
						sumDistance[position] = new AveragesDistances();
					}
				}
			}
		});
	}

	private void saveToFile(ArrayList<String> data) {
		File dir = new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
		dir.mkdirs();
		File file = new File(dir, "distance.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					file, true));
			for (int i = 0; i < data.size(); i++) {
				bufferedWriter.append(data.get(i) + "\n");
			}
			bufferedWriter.close();
			Toast.makeText(getApplicationContext(), "File saved.",
					Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), "File not found.",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Cannot save file.",
					Toast.LENGTH_LONG).show();
		}
	}

	private ArrayList<String> readFromFile() {
		ArrayList<String> readDistance = new ArrayList<String>();
		File dir = new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
		dir.mkdirs();
		File file = new File(dir, "distance.txt");
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			DataInputStream dataInputStream = new DataInputStream(
					fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream));
			String tmp = "";
			while ((tmp = bufferedReader.readLine()) != null) {
				readDistance.add(tmp);
			}
			dataInputStream.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), "File not found.",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Cannot read file.",
					Toast.LENGTH_LONG).show();
		}
		return readDistance;
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