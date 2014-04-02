package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class GMapActivity extends BaseActivity implements LocationListener {

	public static double latitude = 33.82;
	public static double longitude = -111.7;

	static int trail_id[];
	int count = 0;
	boolean flag = true, startflag = true;
	int[] imagess;
	ListView listview;
	Button show;
	LocationManager locationManager;
	ImageView imageView;
	Location location;
	AlertDialog dialog;

	String[] data;
	String[] listItemss;
	TextView customTitleText;
	MyLocationListener mylocationlistener;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mylocationlistener = new MyLocationListener();
		setContentView(R.layout.main);
		final String regions[] = { "Western Arizona: Yuma and Sonoran Desert",
				"Greater Phoenix", "Eastern Arizona: Safford and Mogollon Rim",
				"South Central Arizona: Tucson and Saguaro National Park",
				"Northern Arizona: Flagstaff and Kaibab National Forest" };

		try {

			Button bmap = (Button) findViewById(R.id.bmap);
			bmap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntentA1A2 = new Intent(GMapActivity.this,
							MapPage.class);
					startActivity(myIntentA1A2);

				}

			});

			Button bregion = (Button) findViewById(R.id.bregion);

			bregion.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							GMapActivity.this);
					builder.setTitle("Select a Region");
					builder.setItems(regions,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int position) {

									String srestroom = "", sparking = "", scampground = "", sfree = "", swater = "";

									Bundle b1 = new Bundle();

									b1.putString("restroom", srestroom);
									b1.putString("parking", sparking);
									b1.putString("water", swater);
									b1.putString("fee", sfree);
									b1.putString("campground", scampground);
									b1.putString("Search", "yes");
									b1.putString("keyword", "");

									b1.putString("latitude", String
											.valueOf(location.getLatitude()));
									b1.putString("longitude", String
											.valueOf(location.getLongitude()));
									System.out.println("3");

									switch (position) {
									case 0:
										b1.putString("region", "Western Region");
										break;
									case 1:
										b1.putString("region",
												"Greater Phoenix");
										break;

									case 2:
										b1.putString("region", "Eastern Region");
										break;

									case 3:
										b1.putString("region", "South Central");
										break;

									case 4:
										b1.putString("region",
												"Northern Region");
										break;

									}
									System.out.println("4");

									Intent myIntentA1A2 = new Intent(
											GMapActivity.this,
											TrailWithRegions.class);
									myIntentA1A2.putExtras(b1);
									startActivity(myIntentA1A2);
									dialog.dismiss();
								}

							});
					dialog = builder.create();
					dialog.show();

				}

			});

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						GMapActivity.this);
				builder.setTitle("Location Manager");
				builder.setMessage("Would you like to enable GPS?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(i, 33);
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								GMapActivity.this.finish();
							}
						});
				builder.create().show();
			} else {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1, 1000,
						mylocationlistener);

				location = locationManager
						.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				locationManager.removeUpdates(mylocationlistener);

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		listview = (ListView) findViewById(R.id.listView1);
		listview.setScrollingCacheEnabled(false);
		listview.setDivider(null);
		listview.setCacheColorHint(R.color.blue);
		listview.setFadingEdgeLength(0);

		show = (Button) findViewById(R.id.show);
		show.setBackgroundDrawable(listview.getBackground());
		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GMapActivity.this, ShowAll.class);
				intent.putExtra("list", listItemss);
				intent.putExtra("data", data);
				intent.putExtra("image", imagess);
				startActivity(intent);

			}

		});
		new ProgressTask().execute();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 33) {
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Location Manager");
				builder.setMessage("Would you like to enable GPS?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(i, 33);
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								GMapActivity.this.finish();
							}
						});
				builder.create().show();
			} else {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1, 1000,
						mylocationlistener);
			}
			location = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			locationManager.removeUpdates(mylocationlistener);
			// GMapActivity.this.finish();
		}
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {
		}

	}

	@Override
	public void onLocationChanged(Location loc) {
		loc.getLatitude();

		loc.getLongitude();

		latitude = loc.getLatitude();

		longitude = loc.getLongitude();

	}

	@Override
	public void onProviderDisabled(String provider) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	private class ProgressTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progress = new ProgressDialog(GMapActivity.this, 1);

		@Override
		protected void onPreExecute() {
			progress.setMessage("Fetching Trails...");
			progress.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			show.setText("Show " + (listItemss.length - 3) + " more trails");
			listview.setAdapter(new ImageAdapter(GMapActivity.this,
					R.layout.clistview, R.id.text1, R.id.image1, (new String[] {
							listItemss[0], listItemss[1], listItemss[2] }),
					(new int[] { imagess[0], imagess[3], imagess[2] }),
					new String[] { data[0], data[1], data[2] }, 1));
			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<String> listItems = new ArrayList<String>();

			try {

				String l = Functions.TrailNearMe(latitude, longitude);

				JSONArray ja = new JSONArray(l);

				trail_id = new int[ja.length()];
				listItemss = new String[ja.length()];
				imagess = new int[ja.length()];
				data = new String[ja.length()];

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems.add(jo.getString("name"));
					listItemss[i] = jo.getString("name");
					imagess[i] = R.drawable.arrow;
					data[i] = jo.toString();
					trail_id[count] = jo.getInt("id");
					count++;
				}
				if (listItems.isEmpty()) {
					listItems.add("No Trails near your current position");
					listview.setClickable(false);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				// TODO Auto-generated catch block
			}
			// TODO Auto-generated method stub
			return null;
		}
	}

}