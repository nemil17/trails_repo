package com.javacodegeeks.android.googlemaps;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class MapPage extends BaseMapActivity {

	MapView mv;
	boolean flag = true;
	TextView customTitleText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mappage);
		mv = (MapView) findViewById(R.id.MapView1);
		mv.setClickable(true);

		final MapController mc = mv.getController();
		double llat = 0, llon = 0;
		String output = Functions.TrailNearMe(
				Double.parseDouble(Functions.getLocations(this)[0]),
				Double.parseDouble(Functions.getLocations(this)[1]));
		JSONArray ja = null;
		MapOverlay mo;
		List<Overlay> listOfOverlays = mv.getOverlays();
		try {
			ja = new JSONArray(output);
			GeoPoint[] p = new GeoPoint[ja.length()];
			for (int i = 0; i < ja.length(); i++) {
				llat = ((JSONObject) ja.get(i)).getDouble("lat");

				llon = ((JSONObject) ja.get(i)).getDouble("lon");

				Drawable voltaIcon = this.getResources().getDrawable(
						R.drawable.mapmarker);
				MapOverlay volte = new MapOverlay(voltaIcon, this);
				GeoPoint p_Volta = new GeoPoint((int) (llat * 1E6),
						(int) (llon * 1E6));
				volte.addItem(new OverlayItem(p_Volta, "",
						((JSONObject) ja.get(i)).toString()));
				listOfOverlays.add(volte);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		mc.animateTo(new GeoPoint((int) (llat * 1E6), (int) (llon * 1E6)));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		flag = true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
