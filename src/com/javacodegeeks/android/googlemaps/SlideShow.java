package com.javacodegeeks.android.googlemaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class SlideShow extends BaseActivity implements ViewFactory {
	Gallery gallery;
	ImageView iv;
	String images[];
	ImageSwitcher mSwitcher;
	ImageSwitcher iSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showslide);

		Bundle b = getIntent().getExtras();
		double lat = Double.parseDouble(b.getString("lat"));

		double lon = Double.parseDouble(b.getString("lon"));

		String Url = "http://www.panoramio.com/map/get_panoramas.php?set=public&from=0&to=20&minx="
				+ (lon - 0.05)
				+ "&miny="
				+ (lat - 0.05)
				+ "&maxx="
				+ (lon + 0.05)
				+ "&maxy="
				+ (lat + 0.05)
				+ "&size=medium&mapfilter=true%22";
		try {
			JSONObject json = new JSONObject(Functions.readURL(Url));
			JSONArray array = json.getJSONArray("photos");
			int count = array.length();
			if (count > 10) {
				count = 10;
			}
			images = new String[count];

			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				images[i] = obj.getString("photo_file_url");
			}
		} catch (JSONException e) {
			System.out.println("GOklu" + e.toString());
		}

		iSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		iSwitcher.setFactory(this);

		iSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		iSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setBackgroundColor(Color.WHITE);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				iSwitcher.setImageDrawable(Functions
						.LoadImageFromWebOperations(images[arg2]));
			}
		});
	}

	public class ImageAdapter extends BaseAdapter {

		private Context ctx;

		public ImageAdapter(Context c) {
			ctx = c;
		}

		@Override
		public int getCount() {

			return images.length;
		}

		@Override
		public Object getItem(int arg0) {

			return arg0;
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			ImageView iView = new ImageView(ctx);
			iView.setImageDrawable(Functions
					.LoadImageFromWebOperations(images[arg0]));
			iView.setScaleType(ImageView.ScaleType.FIT_XY);
			iView.setLayoutParams(new Gallery.LayoutParams(150, 150));
			return iView;
		}

	}

	@Override
	public View makeView() {
		ImageView iView = new ImageView(this);
		iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		iView.setBackgroundColor(0xFF000000);
		iView.setImageDrawable(Functions.LoadImageFromWebOperations(images[0]));

		return iView;
	}
}