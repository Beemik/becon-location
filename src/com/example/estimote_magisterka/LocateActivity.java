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
import android.view.WindowManager;
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

//class with list of beacons
//can get averarage distance and RSSI from each beacon and then save them to file in available for user storage
public class LocateActivity extends Activity {

	// holds position of clicked beacon for another class
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
	private AveragesValues[] averageValues;
	private ArrayList<String> macAddress;
	private ArrayAdapter<String> adapter;
	// determine how much beacons ends average
	private int all = 0;
	// list of beacons that are ended calculate average distance and RSSI
	private ArrayList<String> tmpArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// keep screen on when this activity is on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		getResult = false;
		averageDistanceListView = (ListView) findViewById(R.id.listView2);
		averageDistanceTextView = (TextView) findViewById(R.id.textView1);
		readFile = (Button) findViewById(R.id.button2);

		// adapter for list of beacons
		beaconAdapter = new BeaconAdapter(this, R.layout.beacon);

		// list of available beacons
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

		// method to which discovered beacons in range
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {

			// when discovered beacon
			@Override
			public void onBeaconsDiscovered(Region arg0, final List<Beacon> arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						// seting list of beacons
						beaconAdapter.replaceWith(arg1);

						// number of distance and RSSI values to average
						final int N = 5;
						int beaconCount = beaconAdapter.getCount();
						// to know that is an error when average distance and
						// RSSI
						boolean error = false;

						// if button to start average is pressed
						if (getResult == true) {
							for (int i = 0; i < beaconCount; i++) {
								// if error stop average
								if (error) {
									getResult = false;
									break;
								}
								// if number of beacons is change stop average
								if (beaconCount != beaconAdapter.getCount()) {
									error = true;
									getResult = false;
									averageDistanceTextView.setText("Error.");
									break;
								}
								for (int j = 0; j < beaconCount; j++) {
									// if there are no beacons stop average
									if (beaconAdapter.getCount() == 0) {
										averageDistanceTextView
												.setText("There are no beacons in range.");
										error = true;
										getResult = false;
										break;
									}
									// if some of beacons ended average, they
									// are excluded from further calculations in
									// this course
									if (averageValues[j].getCount() == N
											&& averageValues[j].getEnd() == false) {
										// add beacon to list of correctly
										// average beacons
										tmpArrayList.add(String
												.format("%d  values; %s : %.2fm; RSSI: %ddBm",
														averageValues[j]
																.getCount(),
														macAddress.get(j),
														averageValues[j]
																.getSumDistance()
																/ N,
														averageValues[j]
																.getSumRSSI()
																/ N));
										// sets beacons like excluded from
										// further calculations in
										// this course
										averageValues[j].setEnd(true);
										all++;
									}

									// calculate average distance and RSSI
									else if (averageValues[j].getCount() != N
											&& macAddress.get(j).equals(
													beaconAdapter.getItem(i)
															.getMacAddress())) {
										averageValues[j].setSumDistance(Utils
												.computeAccuracy(beaconAdapter
														.getItem(i)));
										averageValues[j]
												.setSumRSSI(beaconAdapter
														.getItem(i).getRssi());
										// increment number of averaging
										// distance and RSSI
										averageValues[j].incrementCount();
									}
									// if all beacons ended, course is ended
									if (all == beaconCount) {
										getResult = false;
									}
									// draw counter number
									else
										averageDistanceTextView.setText(String
												.format("wait.. %d",
														averageValues[j]
																.getCount()));
								}
							}
						}
						// if course ended and there are beacons that ends save
						// them to file and reset all variables
						else if (all > 0) {
							saveToFile(tmpArrayList);
							for (int i = 0; i < beaconCount; i++) {
								if (averageValues[i].getEnd() == true) {
									averageValues[i].setCount(0);
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

		// sets all needed variables to start average distance and RSSI
		getResultsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// if there are good conditions to start average sets variables
				if (getResult == false && beaconAdapter.getCount() != 0) {
					// initialize list with ended beacons
					tmpArrayList = new ArrayList<String>();
					// button is pressed
					getResult = true;
					// initialize list with available beacons mac address
					macAddress = new ArrayList<String>();
					// list of average distance is set with number of beacons in
					// range
					averageValues = new AveragesValues[beaconAdapter.getCount()];
					// getting mac address and putting it to list
					for (int position = 0; position < beaconAdapter.getCount(); position++) {
						macAddress.add(beaconAdapter.getItem(position)
								.getMacAddress());
						averageValues[position] = new AveragesValues();
					}
				}
			}
		});
	}

	// save to file list of string
	private void saveToFile(ArrayList<String> data) {
		// specify path
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

	// read file from memory
	private ArrayList<String> readFromFile() {
		// specify path
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

	// adding possibility to step backward in action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.scan, menu);
		MenuItem refresh = menu.findItem(R.id.item1);
		refresh.setActionView(R.layout.progress_layout);
		return true;
	}

	// user can step backward in action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// connect to BeaconService, start ranging and monitoring
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

	// when restarting this activity
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

	// when activity starts app check if user has possibility to use this app
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

	// stop ranging beacons
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

	// stop ranging and monitoring beacons
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		beaconManager.disconnect();
		super.onDestroy();
	}

	// when user clicked on beacon
	private AdapterView.OnItemClickListener createOnItemClickListener() {
		return new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						BeaconCharacteristicsActivity.class);
				// puts beacon position
				intent.putExtra(CLICKED_BEACON, beaconAdapter.getItem(position));
				// starting activity with beacon characteristics
				startActivity(intent);
			}

		};
	}
}