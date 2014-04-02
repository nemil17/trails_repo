package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ActivityGroup;
import android.content.Context;
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
public class FilterPage extends BaseActivity implements LocationListener {
	public static double latitude = 33;
	public static double longitude = -111;
	LocationManager locationManager;
	ToggleButton easy,medium,difficult,dall,tall,t02,t24,t5;
	Location location;
	String srestroom = "", sparking = "", scampground = "", sfree = "",
			swater = "", sregions = "", sdifficulty = "",time_to_travel_lte="",time_to_travel_gte="";
	Bundle b1;
	TextView difficultymessage;
	SeekBar difficulty;
	MyLocationListener mylocationlistener;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onCreate(Bundle savedInstanceState) {
		try {
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

			final Spinner regions = (Spinner) findViewById(R.id.spinner1);

			final EditText keyword = (EditText) findViewById(R.id.input);

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
						sdifficulty="easy";
						medium.setChecked(false);
						dall.setChecked(false);
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
						sdifficulty="medium";
						easy.setChecked(false);
						dall.setChecked(false);
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
						sdifficulty="hard";
						dall.setChecked(false);
						medium.setChecked(false);
						easy.setChecked(false);
					}
				}
				
			});

			dall.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1)
					{
						sdifficulty="";
						difficult.setChecked(false);
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
					if (campground.isChecked())
						scampground = "Yes";

					b1.putString("latitude", String.valueOf(latitude));

					b1.putString("longitude", String.valueOf(longitude));
					b1.putString("difficulty", sdifficulty);
					b1.putString("restroom", srestroom);
					b1.putString("parking", sparking);
					b1.putString("water", swater);
					b1.putString("fee", sfree);
					b1.putString("campground", scampground);
					b1.putString("keyword", keyword.getText().toString());
					b1.putString("Search", "yes");
					b1.putString("time_to_travel_lte", time_to_travel_lte);
					b1.putString("time_to_travel_gte", time_to_travel_gte);

					b1.putString("region", regions.getSelectedItem().toString());
					System.out.println("kiren");
					Intent myIntentA1A2 = new Intent(FilterPage.this,
							SearchResults.class);
					myIntentA1A2.putExtras(b1);
					myIntentA1A2.putExtras(getIntent().getExtras());

					startActivity(myIntentA1A2);
				}
			});
			regions.setAdapter(new ArrayAdapter(this,
					android.R.layout.simple_dropdown_item_1line, listItems));

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
