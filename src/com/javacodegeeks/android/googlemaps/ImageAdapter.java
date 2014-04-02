package com.javacodegeeks.android.googlemaps;

import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class ImageAdapter extends ArrayAdapter {
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
			final int temppos = pos;
			LayoutInflater inflater = context.getLayoutInflater();
			View row = inflater.inflate(layoutId, null);

			ImageView icon = (ImageView) row.findViewById(imageId);
			if (arrows[pos] != 0) {
				icon.setImageResource(R.drawable.global_raquo);
			} else {
				icon.setVisibility(View.GONE);
			}
			TextView label = (TextView) row.findViewById(textId);
			Typeface typeface = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Regular.ttf");
			label.setTypeface(typeface);
			label.setPadding(10, 0, 0, 0);
			row.setBackgroundResource(R.drawable.white_corner_round);
			label.setText(items[pos]);
			row.setClickable(true);
			TextView magency = (TextView) row.findViewById(R.id.magency);
			magency.setPadding(10, 10, 0, 10);
			if (clickable != 0) {
				Double d = new JSONObject(data[temppos]).getDouble("distance");
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

			return (row);

		} catch (Exception e) {
			System.out.print(e.toString());
			return parent;

		}
	}
}
