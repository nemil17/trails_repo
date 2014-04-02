package com.javacodegeeks.android.googlemaps;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Warnings extends BaseActivity {

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listt);
        
        ListView listview=(ListView) findViewById(R.id.listView1);
        String[] images=new String[11];
        
        images[0]="Know Before You Go:";
        images[1]="Every year, more than 200 people have to be rescued while hiking in parks and preserves. Make an informed decision on which trail to hike. Choose a trail that is within your ability and your hike will be more enjoyable.";
        images[2]="Be sure to always:";
        images[3]="Stay on designated trails.";
        images[4]="Tell someone where you are hiking and when you expect to return.";
        images[5]="Carry enough water for your entire hike. Remember water for your dog.";
        images[6]="Turn around and return to the trailhead when your water is 1/2 gone.";
        images[7]="Carry a cell phone.";
        images[8]="Don't hike alone.";
        images[9]="Wear appropriate footwear and clothing for hiking";
        images[10]="Use maps, know where you are going and what kind of terrain you are hiking on.";
        
        int[] icon={0,0,0,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow,R.drawable.arrow};
        
	    listview.setAdapter(new ImageAdapter(this, R.layout.warningview, R.id.text1, R.id.image1, images, icon, (new String[11]),0 ));

    }
}
