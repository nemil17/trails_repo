	package com.javacodegeeks.android.googlemaps;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

public class TrackTrail extends BaseActivity implements LocationListener{

    public static double latitude,latitude1;
	LocationManager locationManager;
    Location location;
    Bundle b;
    int zoom;
    String Imageurl;
    public static double longitude,longitude1;
    TouchImageView maps;
  
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracktrail);
        maps=(TouchImageView) findViewById(R.id.imageView1);
        maps.setMaxZoom(50f);
        b=getIntent().getExtras();
        maps.setScaleType(ScaleType.FIT_XY);
        
        try{
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	            //Ask the user to enable GPS
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("Location Manager");
	            builder.setMessage("Would you like to enable GPS?");
	            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    //Launch settings, allowing user to make a change
	                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                    startActivity(i);
	                }
	            });
	            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    //No location service, no Activity
	                    finish();
	                }
	            });
	            builder.create().show();
	        }
	        else{
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER,
	                1,1,
	                new MyLocationListener()
	        );
	        
	        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	        latitude=location.getLatitude();
	        longitude=location.getLongitude();
	        }

        	System.out.println("Latitude:"+location.getLatitude()+" "+location.getLongitude());
    		Imageurl=Functions.getCachePath()+ "/Trails_Downloaded/" +b.getString("trailname")+"/data.jpg";
	        maps.setImageURI(Uri.parse(Imageurl));
	        
	        String[] output=b.getString("trailname").split("\\+");
	        
	        System.out.println("gokul"+output[1]);
	        zoom=Integer.parseInt(output[1]);
	        latitude1=Double.parseDouble(output[2]);
	        
	        String outputt[]=output[3].split("\\.jpg");
	        System.out.println("gokullll"+zoom+" "+outputt[0]);
	
	        longitude1=Double.parseDouble(outputt[0]);
	        System.out.println("1");

    		Bitmap bmap = BitmapFactory.decodeFile(Imageurl).copy(Bitmap.Config.ARGB_8888, true);
	        System.out.println("1");

    		double cpy=GlobalMercator.LatToPixels((GlobalMercator.LatToMeters(location.getLatitude())),zoom);
    		double cpx=GlobalMercator.LonToPixels((GlobalMercator.LonToMeters(location.getLongitude())),zoom);

    		double py=GlobalMercator.LatToPixels((GlobalMercator.LatToMeters(latitude1)),zoom);
    		double px=GlobalMercator.LonToPixels((GlobalMercator.LonToMeters(longitude1)),zoom);
    		
	        		Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setAlpha(122);
	        System.out.println("2");

            Canvas canvas = new Canvas(bmap);
            System.out.println(px+" "+cpx+" "+py+" "+cpy);
           
            System.out.println("bitmap"+bmap.getWidth()+" "+bmap.getHeight());
            canvas.drawCircle((int)(cpx-px), bmap.getHeight()-(int)(cpy-py), 15 ,paint);

            maps.setImageBitmap(bmap);
        }
        catch(Exception e)
        {
        	System.out.println("hi"+e.toString());
        }
    }
	
	
    private class MyLocationListener implements LocationListener {
    	 
        public void onLocationChanged(Location location) {

    		Bitmap bmap = BitmapFactory.decodeFile(Imageurl).copy(Bitmap.Config.ARGB_8888, true);

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
            Toast.makeText(TrackTrail.this, message, Toast.LENGTH_LONG).show();
        }
 
        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(TrackTrail.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }
 
        public void onProviderDisabled(String s) {
        	
            Toast.makeText(TrackTrail.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }
 
        public void onProviderEnabled(String s) {
            Toast.makeText(TrackTrail.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
 
    }
 
	@Override
	public void onLocationChanged(Location loc) {
	    loc.getLatitude();
	
	    loc.getLongitude();
	
	    latitude=loc.getLatitude();
	
	    longitude=loc.getLongitude();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}


