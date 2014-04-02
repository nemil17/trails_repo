package com.javacodegeeks.android.googlemaps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class AllTrailDetails extends BaseMapActivity {

	GridView gdetails;
	List<Overlay> overlays;
	JSONObject ja;
	Bundle b;
	MapView mv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_trail_details);
		b = getIntent().getExtras();
		System.out.println("1");
		String[] sdata = new String[4];
		System.out.println("1");

		String data = b.getString("data");

		try {
			ja = new JSONObject(data);

			System.out.println("2");

			TextView TrailName = (TextView) findViewById(R.id.trailname);
			TrailName.setText(ja.getString("name"));
			sdata[0] = String.format("%.2f", ja.getDouble("length_of_hike"))
					+ " Miles";
			sdata[1] = ja.getString("difficulty");

			sdata[2] = String.format("%.2f", ja.getDouble("time_to_travel"))
					+ " Hours";

			sdata[3] = Functions.getWeather(ja.getString("zip"))[2] + " ¡F";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gdetails = (GridView) findViewById(R.id.gdetails);
		String[] head = { "TRAIL DISTANCE", "DIFFICULTY", "ESTIMATED TIME",
				"TEMPERATUE" };
		System.out.println("3");

		gdetails.setAdapter(new ButtonAdapter(this, head, sdata));
		mv = (MapView) findViewById(R.id.MapView1);
		overlays = mv.getOverlays();
		overlays.clear();
		MapController mc = mv.getController();
		mc.setZoom(mv.getMaxZoomLevel() - 2);
		mv.setClickable(false);
		System.out.println("4");

		ListView lv = (ListView) findViewById(R.id.moredetails);
		lv.setScrollingCacheEnabled(false);
		lv.setDivider(null);
		lv.setCacheColorHint(R.color.blue);
		lv.setFadingEdgeLength(0);
		System.out.println("5");

		int[] listImages = { R.drawable.global_raquo, R.drawable.global_raquo,
				0, R.drawable.global_raquo, R.drawable.global_raquo,
				R.drawable.global_raquo, R.drawable.global_raquo,
				R.drawable.global_raquo };
		String[] listItems = {
				"More Trail Details",
				"More Trail Conditions",
				"Save to My Trails and Check conditions befor leaving internet connection",
				"Driving Directions", "Trail Map", "Snap Post Card", "Photos",
				"Save to My Trail" };

		int[] icons = { R.drawable.trailpagetraildetails,
				R.drawable.trailpageconditions, 0,
				R.drawable.traildetails_driving,
				R.drawable.trailpagetraildetails, R.drawable.trailpagepostcard,
				R.drawable.trailpagephotos, R.drawable.trailpagesavetomytrails };
		lv.setAdapter(new ImageAdapter(this, R.layout.gclistview, R.id.text1,
				R.id.image1, listItems, listImages, icons, 0));

		try {
			mc.setCenter(new GeoPoint((int) (ja.getDouble("lat") * 1E6),
					(int) (ja.getDouble("lon") * 1E6)));
			overlays.add(new MyOverlay(ja.getDouble("lat"), ja.getDouble("lon")));

		} catch (Exception e) {
			e.printStackTrace();
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

			System.out.println(pos);
			final int temppos = pos;
			View row = convertView;
			try {
				if (convertView == null) {

					LayoutInflater inflater = getLayoutInflater();
					row = inflater.inflate(R.layout.gridlist, null);
					row.setClickable(false);
					TextView head = (TextView) row.findViewById(R.id.head);
					TextView data = (TextView) row.findViewById(R.id.data);
					head.setGravity(Gravity.CENTER_HORIZONTAL);
					data.setGravity(Gravity.CENTER_HORIZONTAL);
					head.setText(shead[pos].toUpperCase());
					data.setText(sdata[pos].toUpperCase());

					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (temppos == 3) {
								Intent i = new Intent(AllTrailDetails.this,
										Weather.class);
								try {
									i.putExtra("weather", Functions
											.getWeather(ja.getString("zip")));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								i.putExtras(b);
								startActivity(i);
							} else if (temppos == 1 || temppos == 0) {
								Intent i = new Intent(AllTrailDetails.this,
										TrailDetails.class);
								i.putExtras(b);
								startActivity(i);
							}

						}

					});

				}
				// TODO Auto-generated method stub
			} catch (Exception e) {

			}
			return row;
		}

	}

	private class MyOverlay extends com.google.android.maps.Overlay {
		double llatitude, llongitude;

		MyOverlay(double llatitude, double llongitude) {
			this.llongitude = llongitude;
			this.llatitude = llatitude;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			Point point = new Point();
			GeoPoint geoPoint = new GeoPoint((int) (llatitude * 1E6),
					(int) (llongitude * 1E6));
			mapView.getProjection().toPixels(geoPoint, point);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.map_marker_blue);

			int x = point.x - bmp.getWidth() / 2;

			int y = point.y - bmp.getHeight();

			canvas.drawBitmap(bmp, x, y, null);
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
				label.setGravity(Gravity.CENTER_VERTICAL);
				Typeface typeface = Typeface.createFromAsset(
						context.getAssets(), "Roboto-Regular.ttf");
				label.setTypeface(typeface);
				label.setPadding(10, 0, 0, 0);

				label.setText(items[pos]);
				row.setClickable(true);
				ImageView iicons = (ImageView) row.findViewById(R.id.icon);
				iicons.setImageResource(icons[pos]);
				// row.setBackgroundColor(R.drawable.white_corner_round);
				row.setBackgroundResource(R.drawable.white_corner_round);
				if (icons[pos] == 0) {
					row.setClickable(false);
					row.setEnabled(false);
					row.setBackgroundColor(context.getResources().getColor(
							R.color.dull_orange));
					iicons.setVisibility(View.GONE);
					icon.setVisibility(View.GONE);

				}
				row.setPadding(3, 15, 0, 15);

				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							Intent i;
							switch (temppos) {
							case 0:

								i = new Intent(AllTrailDetails.this,
										TrailDetails.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 1:

								i = new Intent(AllTrailDetails.this,
										Weather.class);
								try {
									i.putExtra("weather", Functions
											.getWeather(ja.getString("zip")));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								i.putExtras(b);
								startActivity(i);
								break;
							case 4:
								i = new Intent(AllTrailDetails.this, Test.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 6:
								i = new Intent(AllTrailDetails.this,
										SlideShow.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 5:
								i = new Intent(AllTrailDetails.this,
										Test2.class);
								i.putExtras(b);
								startActivity(i);
								break;
							case 3:
								String url = "https://maps.google.com/maps?f=d&hl=en&geocode=&saddr="
										+ Functions.getLocations(context)[0]
										+ ","
										+ Functions.getLocations(context)[1]
										+ "&daddr="
										+ b.getString("lat")
										+ ","
										+ b.getString("lon") + "&dirflg=w";
								Uri uri = Uri.parse("geo:40.763500,-73.979305");

								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW, Uri
												.parse(url));
								intent.setClassName(
										"com.google.android.apps.maps",
										"com.google.android.maps.MapsActivity");
								startActivity(intent);
								break;
							case 7:
								try {
									mv.setDrawingCacheEnabled(true);
									mv.buildDrawingCache();
									Bitmap bm = Bitmap.createBitmap(
											mv.getWidth(), mv.getHeight(),
											Bitmap.Config.ARGB_8888);
									Canvas canvas = new Canvas(bm);
									mv.preLoad();
									mv.draw(canvas);

									try {
										JSONObject j = new JSONObject(b
												.getString("data"));

										String l = Functions.GetStaticMap(j
												.getInt("id"));
										String name = j.getString("name");
										final JSONObject jo = new JSONObject(l);

										String serverUrl = "http://129.219.171.240/~trails/wsgi";

										String lon = jo.getString("ll_long");
										String lat = jo.getString("ll_lat");

										File sdTargetDirectory = new File(
												Functions.getCachePath()
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
										bm.compress(Bitmap.CompressFormat.JPEG,
												90, out);
										out.close();
									} catch (Exception e) {
										e.printStackTrace();
									}

									boolean saveflag = Functions
											.downloadTrail(b.getString("data"));
									AlertDialog.Builder alertbox = new AlertDialog.Builder(
											AllTrailDetails.this);

									if (saveflag == true) {
										alertbox.setMessage("Trail downloaded");
									} else {
										alertbox.setMessage("Error downloading trail");
									}

									alertbox.setNeutralButton(
											"Ok",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface arg0,
														int arg1) {
												}
											});
									alertbox.show();
								} catch (Exception e) {
									TextView tx = (TextView) findViewById(R.id.textView1);
									tx.setText("Sorry, no map was found for this trail.");

									System.out.println("dload" + e.toString());
								}
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
