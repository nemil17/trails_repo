package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LoadTrails extends BaseActivity {
	ListView pics, list2;
	File[] filenames;
	String names[] = new String[1000];
	ArrayList<String> listItems = new ArrayList<String>();
	int[] listImages;
	int count = 0;

	public void onCreate(Bundle savedInstanceState) {
		try {
			System.gc();
			super.onCreate(savedInstanceState);
			setContentView(R.layout.loadtrails);

			TextView distancehiked = (TextView) findViewById(R.id.distancehiked);
			distancehiked.setText("You have hiked "
					+ Functions.readFile(
							Functions.getCachePath()
									+ "/Trails_Downloaded/distance").trim()
					+ " miles in AZ state trails");

			pics = (ListView) findViewById(R.id.listView1);
			list2 = (ListView) findViewById(R.id.listView2);

			File fileList = new File(Functions.getCachePath()
					+ "/Trails_Downloaded/");

			count = 0;
			if (fileList != null) {

				filenames = fileList.listFiles();

				for (File tmpf : filenames) {
					if (tmpf.isDirectory()
							&& Functions
									.readFile(
											Functions.getCachePath()
													+ "/Trails_Downloaded/"
													+ tmpf.getName() + "/Count")
									.trim().equalsIgnoreCase("0")) {
						String temp[] = tmpf.getName().split("\\+");
						listItems.add(temp[0]);
						count++;
						names[count] = tmpf.getName();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			listItems.add("No Trails downloaded.");
		} finally {

			if (listItems.isEmpty()) {
				listItems.add("No Trails downloaded.");
				pics.setEnabled(false);
			}
		}

		String[] mStringArray = new String[listItems.size()];
		mStringArray = listItems.toArray(mStringArray);
		pics.setAdapter(new ImageAdapter(this, R.layout.loadtraillistview,
				R.id.text1, R.id.image1, mStringArray, names,
				(new int[mStringArray.length]), listImages, 0));

		try {
			count = 0;
			names = new String[1000];
			listItems.clear();
			for (File tmpf : filenames) {
				if (tmpf.isDirectory()
						&& !Functions
								.readFile(
										Functions.getCachePath()
												+ "/Trails_Downloaded/"
												+ tmpf.getName() + "/Count")
								.trim().equalsIgnoreCase("0")) {
					String temp[] = tmpf.getName().split("\\+");
					listItems.add(temp[0]);
					count++;
					names[count] = tmpf.getName();
				}
			}

		} catch (Exception e) {
			listItems.add("No Trails downloaded.");
		} finally {

			if (listItems.isEmpty()) {
				listItems.add("No Trails downloaded.");
				list2.setEnabled(false);
			}
		}

		mStringArray = new String[listItems.size()];
		mStringArray = listItems.toArray(mStringArray);
		list2.setAdapter(new ImageAdapter(this, R.layout.loadtraillistview,
				R.id.text1, R.id.image1, mStringArray, names,
				(new int[mStringArray.length]), listImages, 0));

		listImages = new int[listItems.size()];
		pics.setDividerHeight(0);
		pics.setCacheColorHint(R.color.blue);
		pics.setFadingEdgeLength(0);

		pics.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final String temp = names[position];
				if (listImages[position] != -9999) {
					try {
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								LoadTrails.this);
						alertbox.setCustomTitle(null);

						alertbox.setMessage("Load the trail or delete?");
						alertbox.setIcon(R.drawable.corner_round);
						alertbox.setPositiveButton("Load",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										// the button was clicked
										Bundle b = new Bundle();
										b.putString("trailname", temp);
										b.putString("data", Functions
												.readFile(Functions
														.getCachePath()
														+ "/Trails_Downloaded/"
														+ temp + "/Data"));
										b.putString("mapurl",
												Functions.getCachePath()
														+ "/Trails_Downloaded/"
														+ temp + "/mapicon.jpg");

										startActivity(new Intent(
												LoadTrails.this,
												OfflineAllTrailDetails.class)
												.putExtras(b));

									}
								});

						alertbox.setNegativeButton("Delete",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										// the button was clicked
										File dfile = new File(Functions
												.getCachePath()
												+ "/Trails_Downloaded/" + temp);
										if (dfile.isDirectory()) {
											for (File child : dfile.listFiles()) {
												child.delete();

											}

											dfile.delete();
										}
										startActivity(new Intent(
												LoadTrails.this,
												LoadTrails.class));
										finish();
									}
								});
						alertbox.show();

					} catch (Exception e) {

					}
				}
			}
		});

		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final String temp = names[position];
				if (listImages[position] != -9999) {
					try {
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								LoadTrails.this);
						alertbox.setCustomTitle(null);

						alertbox.setMessage("Load the trail or delete?");
						alertbox.setIcon(R.drawable.corner_round);
						alertbox.setPositiveButton("Load",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										// the button was clicked
										Bundle b = new Bundle();
										b.putString("trailname", temp);
										b.putString("data", Functions
												.readFile(Functions
														.getCachePath()
														+ "/Trails_Downloaded/"
														+ temp + "/Data"));
										b.putString("mapurl",
												Functions.getCachePath()
														+ "/Trails_Downloaded/"
														+ temp + "/mapicon.jpg");

										startActivity(new Intent(
												LoadTrails.this,
												OfflineAllTrailDetails.class)
												.putExtras(b));

									}
								});

						alertbox.setNegativeButton("Delete",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										// the button was clicked
										File dfile = new File(Functions
												.getCachePath()
												+ "/Trails_Downloaded/" + temp);
										if (dfile.isDirectory()) {
											for (File child : dfile.listFiles()) {
												child.delete();

											}

											dfile.delete();
										}
										startActivity(new Intent(
												LoadTrails.this,
												LoadTrails.class));
										finish();
									}
								});
						alertbox.show();

					} catch (Exception e) {

					}
				}
			}
		});

	}

	public class ImageAdapter extends ArrayAdapter {
		SherlockActivity context;
		String[] items, names;
		int[] arrows;
		int layoutId;
		int textId;
		int imageId;
		int[] icons;
		int clickable;

		@SuppressWarnings("unchecked")
		public ImageAdapter(SherlockActivity context, int layoutId, int textId,
				int imageId, String[] items, String[] names, int[] arrows,
				int[] icons, int clickable) {
			super(context, layoutId, items);
			this.names = names;
			this.context = context;
			this.items = items;
			this.arrows = arrows;
			this.layoutId = layoutId;
			this.textId = textId;
			this.imageId = imageId;
			this.clickable = clickable;
			this.icons = icons;
		}

		@SuppressWarnings("deprecation")
		public View getView(int pos, View convertView, ViewGroup parent) {
			try {
				LayoutInflater inflater = context.getLayoutInflater();
				View row = inflater.inflate(layoutId, null);
				// TextView magency = (TextView) row.findViewById(R.id.magency);
				File dfile = new File(Functions.getCachePath()
						+ "/Trails_Downloaded/" + names[pos] + "/ddate");
				ImageView icon = (ImageView) row.findViewById(imageId);
				icon.setVisibility(View.GONE);

				TextView label = (TextView) row.findViewById(textId);
				TextView tdate = (TextView) row.findViewById(R.id.tdate);
				Date lastModified = new Date(dfile.lastModified());
				tdate.setText("Updated on:" + String.valueOf(lastModified));

				label.setGravity(Gravity.CENTER_VERTICAL);
				Typeface typeface = Typeface.createFromAsset(
						context.getAssets(), "Roboto-Regular.ttf");
				label.setTypeface(typeface);
				label.setPadding(10, 0, 0, 0);

				label.setText(items[pos]);
				ImageView iicons = (ImageView) row.findViewById(R.id.icon);
				label.setGravity(Gravity.CENTER_VERTICAL);

				if (icons[pos] != 0 && icons[pos] != -9999) {
					iicons.setImageResource(icons[pos]);
				} else {
					iicons.setVisibility(View.GONE);
					icon.setVisibility(View.GONE);
				}

				row.setBackgroundResource(R.drawable.white_corner_round);

				if (icons[pos] == -9999) {
					iicons.setVisibility(View.GONE);
					icon.setVisibility(View.GONE);
					tdate.setVisibility(View.GONE);
					Typeface typeface1 = Typeface.createFromAsset(
							context.getAssets(), "Roboto-Regular.ttf");

					label.setTypeface(typeface1, typeface1.BOLD_ITALIC);
					label.setGravity(Gravity.CENTER);
					new Color();
					row.setBackgroundColor(Color.parseColor("#fff6e5"));
					row.setEnabled(false);
				}

				return (row);
			} catch (Exception e) {
				System.out.print(e.toString());
				return parent;

			}
		}
	}

}
