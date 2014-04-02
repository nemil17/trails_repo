package com.javacodegeeks.android.googlemaps;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebView;

public class MapDirection extends BaseActivity {

	 public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mapdirection);
	        
	        WebView direction=(WebView) findViewById(R.id.direction);
	        
	        Bundle b= getIntent().getExtras();
	        
	        String url=b.getString("url");
	        
	        direction.getSettings().setJavaScriptEnabled(true);

	        direction.loadUrl(url);
	    }
	 
	 }
