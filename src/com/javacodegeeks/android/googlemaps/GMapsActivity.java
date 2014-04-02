package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.io.IOException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

public class GMapsActivity extends SherlockActivity {

	public static double latitude;
	public static double longitude;
	static int trail_id[] = new int[1000];
	int count = 0;
	LocationManager locationManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(0xff669900));
		getSupportActionBar().setTitle("AZ STATE TRAILS");
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTextView = (TextView)findViewById(actionBarTitleId); 
		actionBarTextView.setTextColor(Color.WHITE);
		actionBarTextView.setTypeface(null, Typeface.BOLD);


		try {
			if (!(new File(Functions.getCachePath() + "/Trails_Downloaded/distance").exists())) {
				new File(Functions.getCachePath() + "/Trails_Downloaded/distance")
						.createNewFile();
				Functions
						.writeFile(Functions.getCachePath() + "/Trails_Downloaded/distance", "0");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(Environment.getDownloadCacheDirectory().getPath());

		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Alert");
		alertbox.setCancelable(false);
		alertbox.setMessage("The App will use your current GPS location. Do you want to Continue?");
		alertbox.setIcon(R.drawable.corner_round);
		alertbox.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {

						getSupportActionBar();
						Thread logotimer = new Thread() {
							public void run() {
								try {
									sleep(3500);
									boolean connected = false;
									ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
									if (connectivityManager.getNetworkInfo(
											ConnectivityManager.TYPE_MOBILE)
											.getState() == android.net.NetworkInfo.State.CONNECTED
											|| connectivityManager
													.getNetworkInfo(
															ConnectivityManager.TYPE_WIFI)
													.getState() == android.net.NetworkInfo.State.CONNECTED) {
										// we are connected to a network
										connected = true;
									} else
										connected = false;

									Intent menuIntent = new Intent(
											GMapsActivity.this,
											GMapActivity.class);
									if (connected == false
											|| !Functions.browserInternet()) {
										menuIntent = new Intent(
												GMapsActivity.this,
												Header.class);
									}

									startActivity(menuIntent);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									AlertDialog.Builder alertbox = new AlertDialog.Builder(
											GMapsActivity.this);

									alertbox.setMessage(e.toString());

									// add a neutral button to the alert box and
									// assign a click listener
									alertbox.setNeutralButton(
											"Ok",
											new DialogInterface.OnClickListener() {

												// click listener on the alert
												// box
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// the button was clicked
													Toast.makeText(
															getApplicationContext(),
															"OK button clicked",
															Toast.LENGTH_LONG)
															.show();
												}
											});
									alertbox.show();

								}

								catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									finish();
								}
							}
						};
						logotimer.start();
					}
				});
		alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				GMapsActivity.this.finish();
			}

		});
		alertbox.show();

	}
}