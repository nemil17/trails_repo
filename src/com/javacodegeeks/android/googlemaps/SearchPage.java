package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SearchPage extends BaseActivity implements LocationListener {
	public static double latitude = 33;
	public static double longitude = -111;
	LocationManager locationManager;
	String bregion="";
	ToggleButton easy,medium,difficult,dall,tall,t02,t24,t5,all,pavement,natural;
	Location location;
	String srestroom = "", sparking = "", scampground = "", sfree = "",shandicap="",shorsestage="",sskiing="",spets="",spaddle="",
			swater = "", sregions = "", sdifficulty = "",time_to_travel_lte="",time_to_travel_gte="",sort="Nearest",surface="";
	Bundle b1;
	TextView difficultymessage;
	SeekBar difficulty;
	MyLocationListener mylocationlistener;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onCreate(Bundle savedInstanceState) {
		try {
			final String regions[] = { "","Western Arizona: Yuma and Sonoran Desert", "Greater Phoenix",
					"Eastern Arizona: Safford and Mogollon Rim", "South Central Arizona: Tucson and Saguaro National Park", "Northern Arizona: Flagstaff and Kaibab National Forest" };

			super.onCreate(savedInstanceState);
			setContentView(R.layout.searchpage);
			mylocationlistener = new MyLocationListener();
			b1 = new Bundle();
			easy=(ToggleButton)findViewById(R.id.easy);
			medium=(ToggleButton)findViewById(R.id.medium);
			difficult=(ToggleButton)findViewById(R.id.difficult);

			tall=(ToggleButton)findViewById(R.id.tall);

			t02=(ToggleButton)findViewById(R.id.t02);

			t24=(ToggleButton)findViewById(R.id.t24);

			t5=(ToggleButton)findViewById(R.id.t5);
			
			all=(ToggleButton)findViewById(R.id.all);

			natural=(ToggleButton)findViewById(R.id.natural);
			pavement=(ToggleButton)findViewById(R.id.pavement);

			
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1, 1000, mylocationlistener);

			location = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			locationManager.removeUpdates(mylocationlistener);

			final CheckBox restroom = (CheckBox) findViewById(R.id.restroom);

			final CheckBox parking = (CheckBox) findViewById(R.id.parking);

			final CheckBox campground = (CheckBox) findViewById(R.id.campground);

			final CheckBox free = (CheckBox) findViewById(R.id.Free);

			final CheckBox water = (CheckBox) findViewById(R.id.water);

			final CheckBox paddle = (CheckBox) findViewById(R.id.paddle);

			final CheckBox handicap = (CheckBox) findViewById(R.id.handicap);

			final CheckBox skiing = (CheckBox) findViewById(R.id.skiing);

			final CheckBox horsestage = (CheckBox) findViewById(R.id.horsestage);

			final CheckBox pets = (CheckBox) findViewById(R.id.pets);


			final Button region = (Button) findViewById(R.id.spinner1);

			final EditText keyword = (EditText) findViewById(R.id.input);

			all.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						surface="";
						pavement.setChecked(false);
						natural.setChecked(false);
					}
				}
				
			});

			pavement.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						surface="pavement";
						all.setChecked(false);
						natural.setChecked(false);
					}
				}
				
			});

			natural.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						surface="natural";
						all.setChecked(false);
						pavement.setChecked(false);
					}
				}
				
			});


			
			region.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchPage.this);
					builder.setTitle("Select a Region");
					builder.setItems(regions,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int position) {

									switch (position) {
									case 0:
										bregion="";
										break;
									case 1:
										bregion="Western Region";
										break;
									case 2:
										bregion="Greater Phoenix";
										break;

									case 3:
										bregion="Eastern Region";
										break;
									case 4:
										bregion="South Central";
										break;

									case 5:
										bregion="Northern Region";
										break;

									}
									System.out.println("4");
									if(bregion.trim().length()!=0)
									{
									region.setText(bregion);
									}
									else
									{
										region.setText("Select a region");	
									}
									dialog.dismiss();
								}

							});
					AlertDialog dialog = builder.create();
					dialog.show();

				}

			});


			
			tall.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						time_to_travel_lte="";
						time_to_travel_gte="";
						t02.setChecked(false);
						t24.setChecked(false);
						t5.setChecked(false);
					}
				}
				
			});

			t02.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						time_to_travel_lte="0";
						time_to_travel_gte="2";
						tall.setChecked(false);
						t24.setChecked(false);
						t5.setChecked(false);
					}
				}
				
			});

			t24.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						time_to_travel_lte="2";
						time_to_travel_gte="4";
						tall.setChecked(false);
						t02.setChecked(false);
						t5.setChecked(false);
					}
				}
				
			});
			
			t5.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						time_to_travel_lte="5";
						time_to_travel_gte="";
						tall.setChecked(false);
						t02.setChecked(false);
						t24.setChecked(false);
					}
				}
				
			});
			easy.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						sort="Nearest";
						medium.setChecked(false);
						difficult.setChecked(false);
					}
				}
				
			});

			medium.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						sort="Difficulty";
						easy.setChecked(false);
						difficult.setChecked(false);
					}
				}
				
			});

			difficult.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						sort="Alpha";
						medium.setChecked(false);
						easy.setChecked(false);
					}
				}
				
			});



			ArrayList<String> listItems = new ArrayList<String>();
			listItems.add("");
			listItems.add("Eastern Region");
			listItems.add("Northern Region");
			listItems.add("Western Region");
			listItems.add("Greater Phoenix");
			listItems.add("South Central");

			Button search = (Button) findViewById(R.id.button1);

			search.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					if (restroom.isChecked())
						srestroom = "Yes";
					if (parking.isChecked())
						sparking = "Yes";
					if (water.isChecked())
						swater = "Yes";
					if (free.isChecked())
						sfree = "Yes";
					if (skiing.isChecked())
						sskiing = "Yes";
					if (horsestage.isChecked())
						shorsestage = "Yes";
					if (paddle.isChecked())
						spaddle = "Yes";
					if (pets.isChecked())
						spets = "Yes";
					if (handicap.isChecked())
						shandicap = "Yes";
					if (campground.isChecked())
						scampground = "Yes";

					b1.putString("latitude", String.valueOf(latitude));

					b1.putString("longitude", String.valueOf(longitude));
					b1.putString("sort", sort);
					b1.putString("restroom", srestroom);
					b1.putString("parking", sparking);
					b1.putString("water", swater);
					b1.putString("fee", sfree);
					b1.putString("handicap", shandicap);
					b1.putString("horsestage", shorsestage);
					b1.putString("paddle", spaddle);
					b1.putString("skiing", sskiing);
					b1.putString("pets", spets);
					b1.putString("surface", surface);

					b1.putString("campground", scampground);
					b1.putString("keyword", keyword.getText().toString());
					b1.putString("Search", "yes");
					b1.putString("time_to_travel_lte", time_to_travel_lte);
					b1.putString("time_to_travel_gte", time_to_travel_gte);

					b1.putString("region",bregion);
					System.out.println("kiren");
					Intent myIntentA1A2 = new Intent(SearchPage.this,
							SearchResults.class);
					myIntentA1A2.putExtras(b1);
					startActivity(myIntentA1A2);
				}
			});

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {

			loc.getLatitude();

			loc.getLongitude();

			latitude = loc.getLatitude();

			longitude = loc.getLongitude();
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
}
