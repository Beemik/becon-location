package com.example.estimote_magisterka;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.connection.BeaconConnection;
import com.estimote.sdk.connection.BeaconConnection.BeaconCharacteristics;

public class BeaconCharacteristicsActivity extends Activity {

	private Beacon beacon;
	private BeaconConnection beaconConnection;
	private View connectedView1;
	private View connectedView2;
	private TextView statusTextView;
	private TextView nameTextView;
	private TextView uuidTextView;
	private TextView macAdressTextView;
	private TextView rssiTextView;
	private TextView intervalTextView;
	private TextView batteryTextView;
	private TextView powerTextView;
	private TextView hardwareVersionTextView;
	private TextView softwareVersionTextView;

	private Button minimumPower;
	private Button averagePower;
	private Button maximumPower;
	private Button minimumInterval;
	private Button averageInterval;
	private Button maximumInterval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_characteristics);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		beacon = getIntent().getParcelableExtra(LocateActivity.CLICKED_BEACON);
		beaconConnection = new BeaconConnection(this, beacon,
				createConnectionCallback());
		connectedView1 = findViewById(R.id.conView1);
		connectedView2 = findViewById(R.id.conView2);
		statusTextView = (TextView) findViewById(R.id.textView1);

		nameTextView = (TextView) findViewById(R.id.textView2);
		uuidTextView = (TextView) findViewById(R.id.textView3);
		macAdressTextView = (TextView) findViewById(R.id.textView4);
		rssiTextView = (TextView) findViewById(R.id.textView5);
		intervalTextView = (TextView) findViewById(R.id.textView6);
		batteryTextView = (TextView) findViewById(R.id.textView7);
		powerTextView = (TextView) findViewById(R.id.textView8);
		hardwareVersionTextView = (TextView) findViewById(R.id.textView9);
		softwareVersionTextView = (TextView) findViewById(R.id.textView10);

		minimumPower = (Button) findViewById(R.id.button1);
		averagePower = (Button) findViewById(R.id.button2);
		maximumPower = (Button) findViewById(R.id.button3);
		minimumInterval = (Button) findViewById(R.id.button4);
		averageInterval = (Button) findViewById(R.id.button5);
		maximumInterval = (Button) findViewById(R.id.button6);

		minimumPower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeBroadcastingPower(-30,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});

		averagePower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeBroadcastingPower(-12,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});

		maximumPower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeBroadcastingPower(4,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Broadcasting power error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});

		minimumInterval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeAdvertisingInterval(1000,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(
												getApplicationContext(),
												"Advertising interval updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Advertising interval error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});

		averageInterval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeAdvertisingInterval(200,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(
												getApplicationContext(),
												"Advertising interval updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Advertising interval error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});

		maximumInterval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beaconConnection.writeAdvertisingInterval(50,
						new BeaconConnection.WriteCallback() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(
												getApplicationContext(),
												"Advertising interval updated.",
												Toast.LENGTH_LONG).show();
									}
								});
							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(getApplicationContext(),
												"Advertising interval error.",
												Toast.LENGTH_LONG).show();
									}
								});
							}
						});
			}
		});
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!beaconConnection.isConnected()) {
			statusTextView.setText("Connecting to beacon.");
			beaconConnection.authenticate();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		beaconConnection.close();
		super.onDestroy();
	}

	private BeaconConnection.ConnectionCallback createConnectionCallback() {
		return new BeaconConnection.ConnectionCallback() {

			@Override
			public void onDisconnected() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						statusTextView.setText("Disconected from beacon.");
					}

				});
			}

			@Override
			public void onAuthenticationError() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						statusTextView.setText("Authentication error.");
					}
				});

			}

			@Override
			public void onAuthenticated(
					final BeaconCharacteristics beaconDetales) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						nameTextView.setText("Name: " + beacon.getName());
						uuidTextView.setText("UUID: "
								+ beacon.getProximityUUID());
						macAdressTextView.setText("Mac adress: "
								+ beacon.getMacAddress());
						rssiTextView.setText("RSSI: " + beacon.getRssi());
						statusTextView.setText("Connected to beacon.");
						intervalTextView.setText("Advertising interval: "
								+ beaconDetales.getAdvertisingIntervalMillis()
								+ "ms");
						batteryTextView.setText("Battery: "
								+ beaconDetales.getBatteryPercent() + "%");
						powerTextView.setText("Broadcast power: "
								+ beaconDetales.getBroadcastingPower() + "dBm");
						hardwareVersionTextView.setText("Hardware version: "
								+ beaconDetales.getHardwareVersion());
						softwareVersionTextView.setText("Software version: "
								+ beaconDetales.getSoftwareVersion());
						connectedView1.setVisibility(View.VISIBLE);
						connectedView2.setVisibility(View.VISIBLE);
					}
				});
			}
		};
	}

}
