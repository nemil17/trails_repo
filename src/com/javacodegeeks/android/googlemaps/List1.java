package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class List1 extends BaseActivity {

    static int trail_id[]=new int[1000];
    String[] listItemss;
    int[] imagess;
    String[] data;
    ListView listview;
    int count=0;
    Bundle b;
	String srestroom="",sparking="",scampground="",sfree="",swater="",sregions="",sname="";
	String queryURL="";

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b=getIntent().getExtras();
        listview=(ListView) findViewById(R.id.listView1);
    }
	  private class ProgressTask extends AsyncTask<Void,Void,Void>{

		   	ProgressDialog progress=new ProgressDialog(List1.this,1);
				@Override
				protected void onPreExecute() {
					progress.setMessage("Fetching Trails...");
					progress.show();
				}
				@Override
				protected void onPostExecute(Void result) {
		    	    listview.setAdapter(new ImageAdapter(List1.this, R.layout.clistview, R.id.text1, R.id.image1, listItemss, imagess, data,1 ));
					progress.dismiss();
				}

				@Override
				protected Void doInBackground(Void... params) {
			        ArrayList<String> listItems = new ArrayList<String>();

			        try {

			        		String l=Functions.TrailNearMe(Double.parseDouble(b.getString("lat")),Double.parseDouble(b.getString("lon")));
			        		
			                JSONArray ja = new JSONArray(l);
			                
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
			                    count++;
			                }
			                if(listItems.isEmpty())
			                {
			                	listItems.add("No Trails near your current position");
			                	listview.setClickable(false);
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

}
