package com.javacodegeeks.android.googlemaps;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Weather extends BaseActivity {
	Bundle b;
	ImageButton hideshow;
	LinearLayout lay1, lay2, lay3, lay4, lay5, lay6;
	TextView details1, details2, details3, details4, details5, details6;
	boolean flag = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.weather);

		b = getIntent().getExtras();
		JSONObject ja;
		try {
			ja = new JSONObject(b.getString("data"));
			String[] weatherarray = b.getStringArray("weather");
			RelativeLayout lay = (RelativeLayout) findViewById(R.id.lay);
			System.out.println(weatherarray[0]);
			int code = Integer.parseInt(weatherarray[0]);
			if (Functions.getHours() < 19) {
				if ((code >= 0 && code <= 4) || (code >= 37 && code <= 39)) {
					lay.setBackgroundResource(R.drawable.day_stormy);
				} else if ((code >= 5 && code <= 18)
						|| (code >= 40 && code <= 43) || code == 35
						|| (code >= 45 && code <= 47)) {
					lay.setBackgroundResource(R.drawable.day_rainy);
				} else if ((code >= 19 && code <= 30) || code == 44) {
					lay.setBackgroundResource(R.drawable.day_rainy);
				} else {
					lay.setBackgroundResource(R.drawable.day_clear);
				}

			} else {
				if ((code >= 0 && code <= 4) || (code >= 37 && code <= 39)) {
					lay.setBackgroundResource(R.drawable.dusk_stormy);
				} else if ((code >= 5 && code <= 18)
						|| (code >= 40 && code <= 43) || code == 35
						|| (code >= 45 && code <= 47)) {
					lay.setBackgroundResource(R.drawable.day_rainy);
				} else if ((code >= 19 && code <= 30) || code == 44) {
					lay.setBackgroundResource(R.drawable.day_rainy);
				} else {
					lay.setBackgroundResource(R.drawable.night_clear);
				}
			}

			lay1 = (LinearLayout) findViewById(R.id.lay1);
			lay1.setBackgroundResource(R.drawable.the_weather_is);
			details1 = (TextView) findViewById(R.id.details1);
			details1.setTextColor(Color.WHITE);
			details1.setText("\t" + weatherarray[1] + ", " + weatherarray[2]
					+ " F");

			lay2 = (LinearLayout) findViewById(R.id.lay2);
			lay2.setBackgroundResource(R.drawable.windspeed);
			details2 = (TextView) findViewById(R.id.details2);
			details2.setTextColor(Color.WHITE);
			details2.setText(weatherarray[9] + "mph");

			lay3 = (LinearLayout) findViewById(R.id.lay3);
			lay3.setBackgroundResource(R.drawable.sunrise_sunset);
			details3 = (TextView) findViewById(R.id.details3);
			details3.setTextColor(Color.WHITE);
			details3.setText("\t" + weatherarray[7] + "\n\t" + weatherarray[8]);

			lay4 = (LinearLayout) findViewById(R.id.lay4);
			lay4.setBackgroundResource(R.drawable.high_elevation);
			details4 = (TextView) findViewById(R.id.details4);
			details4.setTextColor(Color.WHITE);
			details4.setText("1000 ft");

			lay5 = (LinearLayout) findViewById(R.id.lay5);
			lay5.setBackgroundResource(R.drawable.current_elevation);
			details5 = (TextView) findViewById(R.id.details5);
			details5.setTextColor(Color.WHITE);
			details5.setText("1000 ft");

			lay6 = (LinearLayout) findViewById(R.id.lay6);
			lay6.setBackgroundResource(R.drawable.gps);
			details6 = (TextView) findViewById(R.id.details6);
			details6.setTextColor(Color.WHITE);
			details6.setText(String.format("%.2f", ja.getDouble("lat")) + "\n"
					+ String.format("%.2f", ja.getDouble("lon")));

			hideshow = (ImageButton) findViewById(R.id.hideshow);
			hideshow.setBackgroundColor(Color.TRANSPARENT);

			hideshow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (flag == true) {
						lay1.setBackgroundResource(0);
						lay2.setBackgroundResource(0);
						lay3.setBackgroundResource(0);
						lay4.setBackgroundResource(0);
						lay5.setBackgroundResource(0);
						lay6.setBackgroundResource(0);
						flag = false;
						hideshow.setBackgroundColor(Color.TRANSPARENT);
						hideshow.setImageResource(R.drawable.show);
					} else {
						lay1.setBackgroundResource(R.drawable.the_weather_is);
						lay2.setBackgroundResource(R.drawable.windspeed);
						lay3.setBackgroundResource(R.drawable.sunrise_sunset);
						lay4.setBackgroundResource(R.drawable.high_elevation);
						lay5.setBackgroundResource(R.drawable.current_elevation);
						lay6.setBackgroundResource(R.drawable.gps);
						flag = true;
						hideshow.setBackgroundColor(Color.TRANSPARENT);
						hideshow.setImageResource(R.drawable.hide);

					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}