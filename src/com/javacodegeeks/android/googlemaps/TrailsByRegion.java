package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TrailsByRegion extends BaseMapActivity {
	Bundle b;
    public static double latitude=33.82;
    public static double longitude=-111.7;
    static int trail_id[];
    int count=0;
    boolean flag=true;
    int[] imagess;
    ListView listview;
    String[] data;
    String[] listItemss;
    double llat[],llon[];
    MapView mv;
    List<Overlay> overlays;

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapmain);
        b=getIntent().getExtras();
        listview=(ListView) findViewById(R.id.listView1);
        listview.setTextFilterEnabled(true);
        listview.setFastScrollEnabled(true);
	    listview.setScrollingCacheEnabled(false);
	    Spinner sp=(Spinner) findViewById(R.id.region);
	    sp.setVisibility(View.GONE);
        mv=(MapView)findViewById(R.id.MapView1);
        overlays= mv.getOverlays();
        overlays.clear();

        new ProgressTask().execute();

       // mv.invalidate();


    }

    private class ProgressTask extends AsyncTask<Void,Void,Void>{

   	ProgressDialog progress=new ProgressDialog(TrailsByRegion.this,1);
		@Override
		protected void onPreExecute() {
			progress.setMessage("Fetching Trails...");
			progress.show();
		}
		@Override
		protected void onPostExecute(Void result) {
    	    //listview.setAdapter(new ImageAdapter(TrailsByRegion.this, R.layout.clistview, R.id.text1, R.id.image1, listItemss, imagess, data,1 ));
    	    progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
	        ArrayList<String> listItems = new ArrayList<String>();

	        try {
	        		
	        		latitude=Double.parseDouble(b.getString("lat"));
	        		longitude=Double.parseDouble(b.getString("lon"));
	        		
	        		String l=Functions.TrailNearMe(latitude,longitude);
	        		
	                JSONArray ja = new JSONArray(l);
	                
	                llat=new double[ja.length()];
	                llon=new double[ja.length()];
	                trail_id=new int[ja.length()];
	                listItemss=new String[ja.length()];
	                imagess=new int[ja.length()];
	                data=new String[ja.length()];

	                for (int i = 0; i < ja.length(); i++) {
	                    JSONObject jo = (JSONObject) ja.get(i);
	                    listItems.add(jo.getString("name"));
	                    listItemss[i]=jo.getString("name");
	                    imagess[i]=R.drawable.arrow;
	                    data[i]=jo.toString();
	                    trail_id[count]=jo.getInt("id");
	                    llat[count]=jo.getDouble("lat");
	                    llon[count]=jo.getDouble("lon");
	                    count++;
	                }
	                if(listItems.isEmpty())
	                {
	                	listItems.add("No Trails near your current position");
	                	listview.setClickable(false);
	                }
	                for(int i=0;i<count;i++)
	                {
	                	overlays.add(new MyOverlay(llat[i],llon[i]));
	                }

	        } 
	        catch (Exception e) {
	        	System.out.println(e.toString());
	            // TODO Auto-generated catch block
	         } 
			// TODO Auto-generated method stub
			return null;
		}
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class MyOverlay extends com.google.android.maps.Overlay {
		double llatitude,llongitude;
		MyOverlay(double llatitude,double llongitude)
		{
			this.llongitude=llongitude;
			this.llatitude=llatitude;
		}
	    @Override
	    public void draw(Canvas canvas, MapView mapView, boolean shadow) {                              
	        super.draw(canvas, mapView, shadow);

	            Point point = new Point();
	            GeoPoint geoPoint=new GeoPoint((int)(llatitude*1E6), (int)(llongitude*1E6));
	            mapView.getProjection().toPixels(geoPoint, point);                                      

	            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_blue);  
	        
	            int x = point.x - bmp.getWidth() / 2;                                                   
	        
	            int y = point.y- bmp.getHeight();                                                   
	        
	            canvas.drawBitmap(bmp, x, y, null);
	    }

	}

}