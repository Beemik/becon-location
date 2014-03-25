package com.example.estimote_magisterka;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

//main activity class
//display buttons to go on with application
public class MainActivity extends Activity {

	private Button startButton; // button starting ranging
	private Button sendMailButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (Button) findViewById(R.id.button1);
		sendMailButton = (Button) findViewById(R.id.button2);

		// starting activity with beacon list
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						LocateActivity.class);
				startActivity(intent);
			}
		});

		// starting app to send mail
		sendMailButton.setOnClickListener(new OnClickListener() {

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

				// adding file with data
				try {
					File dir = new File(android.os.Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/Documents");
					dir.mkdirs();
					File file = new File(dir, "distance.txt");
					// if file doesn't exists mailapp doesn't start
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

}
