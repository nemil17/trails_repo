package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class OfflineAllTrailDetails extends BaseMapActivity {

	GridView gdetails;
	List<Overlay> overlays;
	JSONObject ja;
	Bundle b;
	String[] weather;
	ImageView mv;
	String hikingcount;
	Bitmap bmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offlinealltraildetails);
		b = getIntent().getExtras();
		String[] sdata = new String[4];
		String data = b.getString("data");
		try {
			ja = new JSONObject(data);
			TextView TrailName = (TextView) findViewById(R.id.trailname);
			TrailName.setText(ja.getString("name"));
			sdata[0] = String.format("%.2f", ja.getDouble("distance"))
					+ " Miles";
			sdata[1] = ja.getString("difficulty");

			sdata[2] = String.format("%.2f", ja.getDouble("time")) + " Hours";
			weather = Functions.readFile(
					(Functions.getCachePath() + "/Trails_Downloaded/"
							+ b.getString("trailname") + "/Weather"))
					.split("#");

			sdata[3] = weather[2] + " ¡F";

			gdetails = (GridView) findViewById(R.id.gdetails);
			String[] head = { "TRAIL DISTANCE", "DIFFICULTY", "ESTIMATED TIME",
					"TEMPERATUE" };

			mv = (ImageView) findViewById(R.id.MapView1);
			mv.setImageURI(Uri.parse(b.getString("mapurl")));
			bmap = BitmapFactory.decodeFile(b.getString("mapurl"));
			ListView lv = (ListView) findViewById(R.id.moredetails);
			lv.setScrollingCacheEnabled(false);
			lv.setDivider(null);
			lv.setCacheColorHint(R.color.blue);
			lv.setFadingEdgeLength(0);
			hikingcount = Functions.readFile(
					Functions.getCachePath() + "/Trails_Downloaded/"
							+ b.getString("trailname") + "/Count").toString();
			int[] listImages = { R.drawable.global_raquo,
					R.drawable.global_raquo, R.drawable.global_raquo,
					R.drawable.global_raquo, R.drawable.global_raquo };
			String[] listItems = { "More Trail Details",
					"More Trail Conditions", "Trail Map", "Snap Post Card",
					"Trails Completed " + hikingcount + " times" };

			int[] icons = { R.drawable.trailpagetraildetails,
					R.drawable.trailpageconditions,
					R.drawable.trailpagetraildetails,
					R.drawable.trailpagepostcard,
					R.drawable.trailpagecompletedtrail };
			lv.setAdapter(new ImageAdapter(this, R.layout.gclistview,
					R.id.text1, R.id.image1, listItems, listImages, icons, 0));

			gdetails.setAdapter(new ButtonAdapter(this, head, sdata));

			Button update = (Button) findViewById(R.id.update);
			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Functions.browserInternet()
							&& Functions
									.isNetworkAvailable(OfflineAllTrailDetails.this)) {
						try {
							try {
								JSONObject j = new JSONObject(b
										.getString("data"));

								String l = Functions.GetStaticMap(j
										.getInt("id"));
								String name = j.getString("name");
								final JSONObject jo = new JSONObject(l);

								String lon = jo.getString("ll_long");
								String lat = jo.getString("ll_lat");

								File sdTargetDirectory = new File(Functions
										.getCachePath()
										+ "/Trails_Downloaded/"
										+ name
										+ "+"
										+ jo.getInt("google_zl")
										+ "+" + lat + "+" + lon);
								if (!sdTargetDirectory.exists())
									sdTargetDirectory.mkdirs();

								FileOutputStream out = new FileOutputStream(
										sdTargetDirectory.toString()
												+ "/mapicon.jpg");
								bmap.compress(Bitmap.CompressFormat.JPEG, 90,
										out);
								out.close();
								boolean saveflag = Functions.downloadTrail(b
										.getString("data"));
								Functions.writeFile(
										sdTargetDirectory.toString() + "/Count",
										hikingcount);
								AlertDialog.Builder alertbox = new AlertDialog.Builder(
										OfflineAllTrailDetails.this);

								if (saveflag == true) {
									alertbox.setMessage("Trail updated");
								} else {
									alertbox.setMessage("Error downloading trail");
								}

								alertbox.setNeutralButton("Ok",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface arg0,
													int arg1) {
												Intent i = new Intent(
														OfflineAllTrailDetails.this,
														OfflineAllTrailDetails.class)
														.putExtras(b);
												startActivity(i);
												finish();
											}
										});
								alertbox.show();
							} catch (Exception e) {
								e.printStackTrace();
							}

						} catch (Exception e) {
							TextView tx = (TextView) findViewById(R.id.textView1);
							tx.setText("Sorry, update failed.");
							System.out.println("dload" + e.toString());
						}

					}
				}
			});

			Button golive = (Button) findViewById(R.id.golive);
			golive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Functions.browserInternet()
							&& Functions
									.isNetworkAvailable(OfflineAllTrailDetails.this)) {
						try {
							Bundle myData = new Bundle();
							myData.putString("id",
									String.valueOf(ja.getInt("id")));
							myData.putString("name",
									String.valueOf(ja.getString("name")));
							myData.putString("zip",
									String.valueOf(ja.getString("zip")));
							myData.putString("lat",
									String.valueOf(ja.getDouble("lat")));
							myData.putString("lon",
									String.valueOf(ja.getDouble("lon")));
							myData.putString("data", ja.toString());

							Intent live = new Intent(
									OfflineAllTrailDetails.this,
									AllTrailDetails.class).putExtras(myData);
							startActivity(live);
						} catch (Exception e) {
							System.out.println(e.toString());
						}
					} else {
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								OfflineAllTrailDetails.this);

						alertbox.setMessage("No Internet connection found. Cannot go live.");
						alertbox.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								});
						alertbox.show();

					}
				}

			});

		} catch (Exception e) {

			System.out.println(e.toString());
		}

	}

	private class ButtonAdapter extends BaseAdapter {
		Context c;
		String[] shead, sdata;

		ButtonAdapter(SherlockMapActivity c, String[] shead, String[] sdata) {
			this.c = c;
			this.shead = shead;
			this.sdata = sdata;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shead.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View row = convertView;
			if (convertView == null) {
				try {
					System.out.println("1");
					LayoutInflater inflater = getLayoutInflater();
					System.out.println("2");
					row = inflater.inflate(R.layout.gridlist, null);
					System.out.println("3");
					TextView head = (TextView) row.findViewById(R.id.head);
					System.out.println("4");
					TextView data = (TextView) row.findViewById(R.id.data);
					System.out.println("5");
					head.setGravity(Gravity.CENTER_HORIZONTAL);
					data.setGravity(Gravity.CENTER_HORIZONTAL);
					head.setText(shead[pos].toUpperCase());
					data.setText(sdata[pos].toUpperCase());
					System.out.println("6");
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
			return row;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class ImageAdapter extends ArrayAdapter {
		SherlockMapActivity context;
		String[] items;
		int[] arrows;
		int layoutId;
		int textId;
		int imageId;
		int[] icons;
		int clickable;
		TextView tcount;

		@SuppressWarnings("unchecked")
		public ImageAdapter(SherlockMapActivity context, int layoutId,
				int textId, int imageId, String[] items, int[] arrows,
				int[] icons, int clickable) {
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
				final int temppos = pos;
				LayoutInflater inflater = context.getLayoutInflater();
				View row = inflater.inflate(layoutId, null);

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
				ImageView iicons = (ImageView) row.findViewById(R.id.icon);
				iicons.setImageResource(icons[pos]);

				row.setPadding(3, 15, 0, 15);

				row.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent i;
							switch (temppos) {
							case 0:
								i = new Intent(OfflineAllTrailDetails.this,
										TrailDetails.class);
								i.putExtras(b);
								startActivity(i);
								break;

							case 2:
								i = new Intent(OfflineAllTrailDetails.this,
										TrackTrail.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 1:
								i = new Intent(OfflineAllTrailDetails.this,
										Weather.class);
								try {
									System.out.println(Functions.getWeather(ja
											.getString("zip"))[0]);
									i.putExtra("weather", Functions
											.getWeather(ja.getString("zip")));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								i.putExtras(b);
								startActivity(i);
								break;
							case 3:
								i = new Intent(OfflineAllTrailDetails.this,
										Test2.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 4:
								LayoutInflater alinflater = context
										.getLayoutInflater();
								View alrow = alinflater.inflate(
										R.layout.numberpicker_page, null);
								tcount = (TextView) alrow
										.findViewById(R.id.hikingcount);
								tcount.setText(Functions
										.readFile(
												(Functions.getCachePath()
														+ "/Trails_Downloaded/"
														+ b.getString("trailname") + "/Count"))
										.trim());
								
								final int tempcount=Integer.parseInt(Functions.readFile((Functions.getCachePath()
														+ "/Trails_Downloaded/"
														+ b.getString("trailname") + "/Count")).trim());
								AlertDialog.Builder builder = new AlertDialog.Builder(
										OfflineAllTrailDetails.this);
								builder.setView(alrow);
								builder.setTitle("How many times have u finished this trail?");

								builder.setPositiveButton("Change",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												Functions.writeFile(
														(Functions
																.getCachePath()
																+ "/Trails_Downloaded/"
																+ b.getString("trailname") + "/Count"),
														tcount.getText()
																.toString()
																.trim());
												int finalcount=Integer.parseInt(tcount.getText().toString().trim());
												
												try {
													double totaldistance=ja.getDouble("distance");
													Functions.writeDistance((totaldistance)*(finalcount-tempcount));
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
												Intent i = new Intent(
														OfflineAllTrailDetails.this,
														OfflineAllTrailDetails.class)
														.putExtras(b);
												startActivity(i);
												context.finish();
											}
										});
								AlertDialog d = builder.create();
								// s
								// d.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
								// LayoutParams.WRAP_CONTENT);
								d.show();

								break;

							}
						} catch (Exception e) {

						}

					}
				});

				return (row);

			} catch (Exception e) {
				System.out.print(e.toString());
				return parent;

			}
		}
	}

	Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

}
