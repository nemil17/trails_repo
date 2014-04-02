package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ShowAll extends BaseActivity {

	Bundle b;
	static int trail_id[];
	int count = 0;
	int[] imagess;
	EditText search;
	ListView listview;
	String[] data;
	String[] listItemss;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.showall);

		b = getIntent().getExtras();
		listItemss = b.getStringArray("list");
		imagess = b.getIntArray("image");
		data = b.getStringArray("data");

		listview = (ListView) findViewById(R.id.listView1);
		listview.setScrollingCacheEnabled(false);
		listview.setDividerHeight(0);
		listview.setCacheColorHint(R.color.blue);
		listview.setFadingEdgeLength(0);

		search = (EditText) findViewById(R.id.search);
		search.setHint("Filter Result");
		search.setSelected(false);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (search.getText().toString().trim().length() == 0) {
					listview.setAdapter(new ImageAdapter(ShowAll.this,
							R.layout.clistview, R.id.text1, R.id.image1, b
									.getStringArray("list"), b
									.getIntArray("image"), b
									.getStringArray("data"), 1));
				} else {
					int newcount = 0;
					for (int i = 0; i < listItemss.length; i++) {
						try {
							String temp = listItemss[i].toLowerCase();
							String tempsearch = search.getText().toString()
									.toLowerCase();
							if (temp.contains(tempsearch)) {
								newcount++;
							}

						} catch (Exception e) {
							System.out.println(e.toString());
						}
					}
					String[] newlist = new String[newcount];
					String[] newdata = new String[newcount];
					newcount = 0;
					for (int i = 0; i < listItemss.length; i++) {
						try {
							String temp = listItemss[i].toLowerCase();
							String tempsearch = search.getText().toString()
									.toLowerCase();
							if (temp.contains(tempsearch)) {
								newlist[newcount] = listItemss[i];
								newdata[newcount] = data[i];
								newcount++;
							}

						} catch (Exception e) {
							System.out.println(e.toString());
						}

					}
					// listview.setAdapter(new ImageAdapter(ShowAll.this,
					// R.layout.clistview, R.id.text1, R.id.image1,newlist
					// ,Arrays.copyOf(imagess, newcount),newdata ,1 ));

				}
			}

		});

		new ProgressTask().execute();

	}

	private class ProgressTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progress = new ProgressDialog(ShowAll.this, 1);

		@Override
		protected void onPreExecute() {
			progress.setMessage("Fetching Trails...");
			progress.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			listview.setAdapter(new ImageAdapter(ShowAll.this,
					R.layout.clistview, R.id.text1, R.id.image1, b
							.getStringArray("list"), b.getIntArray("image"), b
							.getStringArray("data"), 1));
			search.clearFocus();

			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

			} catch (Exception e) {
				System.out.println(e.toString());
				// TODO Auto-generated catch block
			}
			// TODO Auto-generated method stub
			return null;
		}
	}
}
