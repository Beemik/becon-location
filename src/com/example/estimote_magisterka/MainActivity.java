package com.example.estimote_magisterka;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button startButton;
	private Button calibrateButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (Button) findViewById(R.id.button1);
		calibrateButton = (Button) findViewById(R.id.button2);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						LocateActivity.class);
				startActivity(intent);
			}
		});

		calibrateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "maciek@manczyk.net" });
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"beacon location [auto_generated]");
				try {
					File dir = new File(android.os.Environment
							.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
					dir.mkdirs();
					File file = new File(dir, "distance.txt");
					if (!file.exists())
						Toast.makeText(getApplicationContext(),
								"There is no file to send.", Toast.LENGTH_LONG)
								.show();
					else {
						emailIntent.putExtra(Intent.EXTRA_STREAM,
								Uri.parse("file://" + file.getAbsolutePath()));
						startActivity(Intent.createChooser(emailIntent,
								"Send mail..."));
					}
				} catch (android.content.ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(),
							"There are no email app instaled.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
