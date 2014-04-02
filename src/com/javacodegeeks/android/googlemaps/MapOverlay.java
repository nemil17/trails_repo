package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlay = new ArrayList<OverlayItem>();
	Context mContext;

	public MapOverlay(Drawable itemMarker) {
		super(boundCenterBottom(itemMarker));
		// TODO Auto-generated constructor stub
	}

	public void addItem(OverlayItem item) {
		mapOverlay.add(item);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mapOverlay.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mapOverlay.size();
	}

	public MapOverlay(Drawable itemMarker, Context context) {
		super(boundCenterBottom(itemMarker));
		mContext = context;
	}

	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		final OverlayItem item = mapOverlay.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

		dialog.setTitle(item.getTitle());
		final JSONObject ja;
		try {
			ja = new JSONObject(item.getSnippet());
			LayoutInflater alinflater = ((Activity) mContext)
					.getLayoutInflater();
			View alrow = alinflater.inflate(R.layout.mapballon, null);
			
			alrow.setBackgroundResource(R.color.dull_orange);
			
			TextView ballon = (TextView) alrow.findViewById(R.id.ballon);
			ballon.setGravity(Gravity.CENTER);
			ballon.setTextColor(mContext.getResources().getColor(R.color.green));
			ballon.setText(ja.getString("name"));
			
			ImageView icon=(ImageView)alrow.findViewById(R.id.imageView1);
			alrow.setClickable(true);
			alrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntentA1A2 = new Intent(mContext,
							AllTrailDetails.class);

					Bundle myData = new Bundle();
					try {
						myData.putString("id", String.valueOf(ja.getInt("id")));
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

						mContext.startActivity(myIntentA1A2);
						

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
			dialog.setView(alrow);
			dialog.show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}