package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.javacodegeeks.android.googlemaps.AllTrailDetails.ImageAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.ActivityGroup;
import android.app.AlertDialog;

public class TrailDetails extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miscdetails);

		ListView listview = (ListView) findViewById(R.id.geodetails);
		listview.setDividerHeight(2);
		listview.setCacheColorHint(R.color.blue);
		listview.setFadingEdgeLength(0);

		try {
			Bundle b = getIntent().getExtras();
			String data = b.getString("data");
			JSONObject ja = new JSONObject(data);

			String[] listItems = new String[17];
			int[] listImages = new int[17];

			listImages[0] = -9999;
			listItems[0] = ja.getString("name");

			listImages[1] = 0;
			listItems[1] = "Distance : "
					+ String.valueOf(Double.parseDouble(new DecimalFormat(
							"#.##").format(ja.getDouble("distance"))))
					+ " miles";

			listImages[2] = 0;
			listItems[2] = "Difficulty : " + (String) ja.get("difficulty");

			listImages[3] = 0;
			listItems[3] = "Hike Time : "
					+ String.valueOf(Double.parseDouble(new DecimalFormat(
							"#.##").format(ja.getDouble("time")))) + " hours";

			listImages[4] = -9999;
			listItems[4] = "Trail Details";

			listItems[5] = "Entry fee : " + (String) ja.get("fees");
			if (ja.getString("fees").trim().equalsIgnoreCase("yes")) {
				listImages[5] = R.drawable.fee_yes;
			} else if (ja.getString("fees").trim().equalsIgnoreCase("no")) {
				listImages[5] = R.drawable.fee_no;
			}

			listItems[6] = "Restrooms  : " + (String) ja.get("restrooms");
			if (ja.getString("restrooms").trim().equalsIgnoreCase("yes")) {
				listImages[6] = R.drawable.restroom_yes;
			} else if (ja.getString("restrooms").trim().equalsIgnoreCase("no")) {
				listImages[6] = R.drawable.restroom_yes;
			}

			listItems[7] = "Parking details : " + (String) ja.get("parking");
			if (ja.getString("parking").trim().equalsIgnoreCase("yes")) {
				listImages[7] = R.drawable.parking_yes;
			} else if (ja.getString("parking").trim().equalsIgnoreCase("no")) {
				listImages[7] = R.drawable.parking_no;
			}

			listItems[8] = "Drinking water  : " + (String) ja.get("drnkwater");
			if (ja.getString("drnkwater").trim().equalsIgnoreCase("yes")) {
				listImages[8] = R.drawable.water_yes;
			} else if (ja.getString("drnkwater").trim().equalsIgnoreCase("no")) {
				listImages[8] = R.drawable.water_no;
			}

			listItems[9] = "Horse Stage : " + (String) ja.get("horsestage");
			if (ja.getString("horsestage").trim().equalsIgnoreCase("yes")) {
				listImages[9] = R.drawable.horse_yes;
			} else if (ja.getString("horsestage").trim().equalsIgnoreCase("no")) {
				listImages[9] = R.drawable.horse_no;
			}

			listItems[10] = "Camping allowed : "
					+ (String) ja.get("campground");
			if (ja.getString("campground").trim().equalsIgnoreCase("yes")) {
				listImages[10] = R.drawable.camping_yes;
			} else if (ja.getString("campground").trim().equalsIgnoreCase("no")) {
				listImages[10] = R.drawable.camping_no;
			}

			listImages[11] = -9999;
			listItems[11] = "Geographical Details";

			listImages[12] = 0;
			listImages[13] = 0;
			listImages[14] = 0;
			listImages[15] = 0;
			listImages[16] = 0;

			listItems[12] = "Elevation Change: " + ja.getDouble("elevft");
			listItems[13] = "Latitude: "
					+ Double.parseDouble(new DecimalFormat("#.##").format(ja
							.getDouble("lat")));
			listItems[14] = "Longitude: "
					+ Double.parseDouble(new DecimalFormat("#.##").format(ja
							.getDouble("lon")));
			listItems[15] = "Management Agency: "
					+ (String) ja.get("mngagency");
			listItems[16] = "Management Unit: " + (String) ja.get("mngunit");

			listview.setAdapter(new ImageAdapter(this, R.layout.gclistview,
					R.id.text1, R.id.image1, listItems, (new int[17]),
					listImages, 0));
		} catch (Exception e) {
			System.out.println("hi" + e.toString());
			// TODO Auto-generated catch block
		}
	}

	public class ImageAdapter extends ArrayAdapter {
		SherlockActivity context;
		String[] items;
		int[] arrows;
		int layoutId;
		int textId;
		int imageId;
		int[] icons;
		int clickable;

		@SuppressWarnings("unchecked")
		public ImageAdapter(SherlockActivity context, int layoutId, int textId,
				int imageId, String[] items, int[] arrows, int[] icons,
				int clickable) {
			super(context, layoutId, items);

			this.context = context;
			this.items = items;
			this.arrows = arrows;
			this.layoutId = layoutId;
			this.textId = textId;
			this.imageId = imageId;
			this.clickable = clickable;
			this.icons = icons;
		}

		public View getView(int pos, View convertView, ViewGroup parent) {
			try {
				LayoutInflater inflater = context.getLayoutInflater();
				View row = inflater.inflate(layoutId, null);

				ImageView icon = (ImageView) row.findViewById(imageId);
				icon.setVisibility(View.GONE);
					
				TextView label = (TextView) row.findViewById(textId);
				label.setGravity(Gravity.CENTER_VERTICAL);
				Typeface typeface = Typeface.createFromAsset(
						context.getAssets(), "Roboto-Regular.ttf");
				label.setTypeface(typeface);
				label.setPadding(10, 0, 0, 0);

				label.setText(items[pos]);
				ImageView iicons = (ImageView) row.findViewById(R.id.icon);
				label.setGravity(Gravity.CENTER_VERTICAL);

				if (icons[pos] != 0 && icons[pos]!=-9999) {
					iicons.setImageResource(icons[pos]);
				} else {
					iicons.setVisibility(View.GONE);
					icon.setVisibility(View.GONE);
				}

				row.setBackgroundResource(R.drawable.white_corner_round);

				if (icons[pos] == -9999) {
					iicons.setVisibility(View.GONE);
					icon.setVisibility(View.GONE);
					Typeface typeface1 = Typeface.createFromAsset(
							context.getAssets(), "Roboto-Regular.ttf");
					
					label.setTypeface(typeface1, typeface1.BOLD_ITALIC);
					label.setGravity(Gravity.CENTER);
					new Color();
					row.setBackgroundColor(Color.parseColor("#fff6e5"));
				}

				return (row);
			} catch (Exception e) {
				System.out.print(e.toString());
				return parent;

			}
		}
	}

}