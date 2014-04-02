package com.javacodegeeks.android.googlemaps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActivityGroup;

public class Test extends BaseActivity {

	URL imageURL;
	String lat = "", lon = "";
	TouchImageView maps;
    int zoom;
	Button dloadtrail;
    public static double longitude,longitude1;
    public static double latitude,latitude1;

    Bundle b;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mapview);
		maps = (TouchImageView) findViewById(R.id.imageView1);
		maps.setMaxZoom(50f);
		b = getIntent().getExtras();
		int id = Integer.parseInt(b.getString("id"));
		final String name = b.getString("name");

		try {
			String l = Functions.GetStaticMap(id);
			final JSONObject jo = new JSONObject(l);

			String serverUrl = "http://129.219.171.240/~trails/wsgi";
			imageURL = new URL(serverUrl + jo.getString("google_static_url"));
			lon = jo.getString("ll_long");
			lat = jo.getString("ll_lat");
			Drawable drawable = LoadImageFromWebOperations(imageURL.toString());
			maps.setImageDrawable(drawable);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextView tx = (TextView) findViewById(R.id.textView1);
			tx.setText("Sorry, no image found for this trail.");
			dloadtrail.setEnabled(false);
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

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
    private class MyLocationListener implements LocationListener {
   	 
        public void onLocationChanged(Location location) {

			Drawable drawable = LoadImageFromWebOperations(imageURL.toString());
			Bitmap bmap = ((BitmapDrawable)drawable).getBitmap();

    		double cpy=GlobalMercator.LatToPixels((GlobalMercator.LatToMeters(location.getLatitude())),zoom);
    		double cpx=GlobalMercator.LonToPixels((GlobalMercator.LonToMeters(location.getLongitude())),zoom);

    		double py=GlobalMercator.LatToPixels((GlobalMercator.LatToMeters(latitude1)),zoom);
    		double px=GlobalMercator.LonToPixels((GlobalMercator.LonToMeters(longitude1)),zoom);
    		
    		
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setAlpha(122); 
        //    paint.setStrokeWidth(30);

            Canvas canvas = new Canvas(bmap);
            System.out.println(px+" "+cpx+" "+py+" "+cpy);
           
            System.out.println("bitmap"+bmap.getWidth()+" "+bmap.getHeight());
           // canvas.drawPoint((int)(cpx-px), 640-(int)(cpy-py), paint);
            canvas.drawCircle((int)(cpx-px), 640-(int)(cpy-py), 15 ,paint);
            maps.setImageBitmap(bmap);

            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(Test.this, message, Toast.LENGTH_LONG).show();
        }
 
        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(Test.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }
 
        public void onProviderDisabled(String s) {
        	
            Toast.makeText(Test.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }
 
        public void onProviderEnabled(String s) {
            Toast.makeText(Test.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
 
    }
}
