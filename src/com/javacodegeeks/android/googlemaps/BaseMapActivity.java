package com.javacodegeeks.android.googlemaps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;

public class BaseMapActivity extends SherlockMapActivity {

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(0xff669900));
		getSupportActionBar().setTitle("AZ STATE TRAILS");
		getSupportActionBar().setHomeButtonEnabled(true);
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTextView = (TextView)findViewById(actionBarTitleId); 
		actionBarTextView.setTextColor(Color.WHITE);
		actionBarTextView.setTypeface(null, Typeface.BOLD);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuitem) {
		try {
			boolean connected = false;
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTED
					|| connectivityManager.getNetworkInfo(
							ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTED) {
				// we are connected to a network
				connected = true;
			} else
				connected = false;

			Intent i;
			switch (menuitem.getItemId()) {
			case android.R.id.home:
				System.out.println(this.getClass().getName());
				i = new Intent(this, GMapActivity.class);
				if (connected == false || !Functions.browserInternet()) {
					i = new Intent().setClass(this, Header.class);
				}

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.GMapActivity")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;
			case R.id.search:
				i = new Intent(this, SearchPage.class);
				if (connected == false || !Functions.browserInternet()) {
					i = new Intent().setClass(this, Header.class);
				}

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.SearchPage")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;
			case R.id.trailsnearme:
				i = new Intent(this, GMapActivity.class);
				if (connected == false || !Functions.browserInternet()) {
					i = new Intent().setClass(this, Header.class);
				}

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.GMapActivity")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;

			case R.id.mytrails:
				i = new Intent(this, LoadTrails.class);

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.LoadTrails")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;
			case R.id.about:
				i = new Intent(this, Info.class);

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.Info")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;
			case R.id.explore:
				i = new Intent(this, MapPage.class);
				if (connected == false || !Functions.browserInternet()) {
					i = new Intent().setClass(this, Header.class);
				}

				if (!(this.getClass().getName())
						.equals("com.javacodegeeks.android.googlemaps.MapPage")) {

					startActivity(i);
				} else {
					startActivity(i);

					finish();
				}
				return true;
			}
		} catch (Exception e) {

		}
		return super.onOptionsItemSelected(menuitem);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


}
