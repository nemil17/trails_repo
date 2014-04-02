package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrailWithRegions extends BaseActivity {

	static int trail_id[] = new int[1000];
	int count = 0;
	String srestroom = "", sparking = "", scampground = "", sfree = "",
			swater = "", sregions = "", sname = "";
	String queryURL = "";
	EditText search;
	String listItemss[];
	int imagess[];
	String data[];
	ArrayList<String> listItems;
	ListView listview;
	Bundle b;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showall);
		System.out.println("Testdatda");
		listview = (ListView) findViewById(R.id.listView1);
		listview.setScrollingCacheEnabled(false);
		listview.setFastScrollEnabled(true);
		listview.setDivider(null);
		listview.setCacheColorHint(R.color.blue);
		listview.setFadingEdgeLength(0);
		search = (EditText) findViewById(R.id.search);
		search.setHint("Filter Result");
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
					listview.setAdapter(new ImageAdapter(TrailWithRegions.this,
							R.layout.clistview, R.id.text1, R.id.image1,
							listItemss, imagess, data, 1));
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
					listview.setAdapter(new ImageAdapter(TrailWithRegions.this,
							R.layout.clistview, R.id.text1, R.id.image1,
							newlist, Arrays.copyOf(imagess, newcount), newdata,
							1));

				}
			}

		});

		listItems = new ArrayList<String>();
		b = getIntent().getExtras();
		new ProgressTask().execute();
	}

	private class ProgressTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progress = new ProgressDialog(TrailWithRegions.this, 1);

		@Override
		protected void onPreExecute() {
			progress.setMessage("Fetching Trails...");
			progress.show();
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void onPostExecute(Void result) {
			if (listItemss.length != 0) {
				listview.setAdapter(new ImageAdapter(TrailWithRegions.this,
						R.layout.clistview, R.id.text1, R.id.image1,
						listItemss, imagess, data, 1));
				search.clearFocus();
			} else {
				listItems.add("Sorry, no trails matches the given criteria");
				listview.setAdapter(new ArrayAdapter(TrailWithRegions.this,
						android.R.layout.simple_list_item_1, listItems));
				search.setVisibility(View.GONE);
			}
			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				queryURL = "?";
				if (b.getString("campground") != "")
					queryURL = queryURL + "&camp=" + b.getString("campground");
				if (b.getString("campground") != "")
					queryURL = queryURL + "&water=" + b.getString("water");
				if (b.getString("campground") != "")
					queryURL = queryURL + "&bath=" + b.getString("restroom");
				if (b.getString("campground") != "")
					queryURL = queryURL + "&fee=" + b.getString("fee");
				if (b.getString("campground") != "")
					queryURL = queryURL + "&parking=" + b.getString("parking");

				if (b.getString("region") != "") {
					queryURL = queryURL + "&region=" + b.getString("region");
				}
				if (b.getString("name") != "") {
					queryURL = queryURL + "&name=" + b.getString("keyword");
				}
				queryURL = queryURL + "&lat=" + b.getString("latitude")
						+ "&lon=" + b.getString("longitude");

				queryURL = queryURL.replace(" ", "%20");
				System.out.println(queryURL);
				String line = Functions.TrailsSearch(queryURL
						+ "&distance=1000000");
				JSONArray ja = new JSONArray(line);

				trail_id = new int[ja.length()];
				listItemss = new String[ja.length()];
				imagess = new int[ja.length()];
				data = new String[ja.length()];

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItemss[i] = jo.getString("name");
					imagess[i] = R.drawable.arrow;
					data[i] = jo.toString();
					trail_id[count] = jo.getInt("id");
					count++;
				}

			} catch (Exception e) {
				System.out.println("podapo: " + e.toString());
			}
			return null;
		}
	}

	private class ImageAdapter extends ArrayAdapter {
		SherlockActivity context;
		String[] items;
		int[] arrows;
		int layoutId;
		int textId;
		int imageId;
		String[] data;
		int clickable;

		@SuppressWarnings("unchecked")
		public ImageAdapter(SherlockActivity context, int layoutId, int textId,
				int imageId, String[] items, int[] arrows, String[] data,
				int clickable) {
			super(context, layoutId, items);

			this.context = context;
			this.items = items;
			this.arrows = arrows;
			this.layoutId = layoutId;
			this.textId = textId;
			this.imageId = imageId;
			this.clickable = clickable;
			this.data = data;
		}

		public View getView(int pos, View convertView, ViewGroup parent) {
			try {
				View row=null;
				if(pos==0)
				{
					LayoutInflater inflater = context.getLayoutInflater();
					row = inflater.inflate(R.layout.listimagefirstrow, null);
					System.out.println("4");

					ImageView icon = (ImageView) row.findViewById(R.id.region);
					Bundle b=getIntent().getExtras();
					String reg=b.getString("region");
					System.out.println("5");

					if(reg.equals("Western Region"))
					{
						icon.setImageResource(R.drawable.western_region);
					} else if(reg.equals("Eastern Region"))
					{
						icon.setImageResource(R.drawable.eastern_region);
					} else if(reg.equals("South Central"))
					{
						icon.setImageResource(R.drawable.south_central);
					} else if(reg.equals("Northern Region"))
					{
						icon.setImageResource(R.drawable.northern_region);
					} else if(reg.equals("Greater Phoenix"))
					{
						icon.setImageResource(R.drawable.central_phoenix);
					}
				}
				else
				{
				final int temppos = pos;
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(layoutId, null);

				ImageView icon = (ImageView) row.findViewById(imageId);
				if (arrows[pos] != 0) {
					icon.setImageResource(arrows[pos]);
				} else {
					icon.setVisibility(View.GONE);
				}
				TextView label = (TextView) row.findViewById(textId);
				Typeface typeface = Typeface.createFromAsset(
						context.getAssets(), "Roboto-Regular.ttf");
				label.setTypeface(typeface);
				label.setPadding(10, 0, 0, 0);

				label.setText(items[pos]);
				row.setClickable(true);
				TextView magency = (TextView) row.findViewById(R.id.magency);
				magency.setPadding(10, 10, 0, 10);
				if (clickable != 0) {
					Double d = new JSONObject(data[temppos])
							.getDouble("distance");
					magency.setText(String.valueOf(d).substring(0, 5)
							+ " miles from your current location");
				} else {
					magency.setVisibility(View.GONE);
				}
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							if (clickable != 0) {
								JSONObject jo = new JSONObject(data[temppos]);
								String l = Functions
										.readURL("http://129.219.171.240/~trails/wsgi/trail_details/?id="
												+ jo.getInt("id"));

								System.out.println(l);

								JSONObject ja = new JSONObject(l.trim());
								System.out.println("test:" + l);
								Intent myIntentA1A2 = new Intent(context,
										AllTrailDetails.class);

								Bundle myData = new Bundle();
								myData.putString("id",
										String.valueOf(jo.getInt("id")));
								myData.putString("name",
										String.valueOf(ja.getString("name")));
								myData.putString("zip",
										String.valueOf(ja.getString("zip")));
								myData.putString("lat",
										String.valueOf(ja.getDouble("lat")));
								myData.putString("lon",
										String.valueOf(ja.getDouble("lon")));
								myData.putString("data", ja.toString());
								myIntentA1A2.putExtras(myData);

								context.startActivity(myIntentA1A2);
							}
						}

						catch (Exception e) {
							System.out.println(e.toString());
						}
					}

				});
				}
				return (row);
				

			} catch (Exception e) {
				System.out.print(e.toString());
				return parent;

			}
		}
	}

}
