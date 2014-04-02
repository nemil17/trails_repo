package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResults extends BaseActivity {

    static int trail_id[]=new int[1000];
    int count=0;
	String srestroom="",sparking="",scampground="",sfree="",swater="",sregions="",sname="";
	String queryURL=""; 
	EditText search;
	String listItemss[];
	int imagess[];
	String data[];
	ArrayList<String> listItems;
	ListView listview;
	Bundle b ;

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showall);
        System.out.println("Testdatda");
   		listview=(ListView) findViewById(R.id.listView1);
	   	listview.setScrollingCacheEnabled(false);
	   	listview.setFastScrollEnabled(true);
        listview.setDivider(null);
        listview.setCacheColorHint(R.color.blue);
        listview.setFadingEdgeLength(0);
        search=(EditText)findViewById(R.id.search);
        search.setHint("Filter Result");
        search.addTextChangedListener(new TextWatcher()
        {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(search.getText().toString().trim().length()==0)
				{
		    	    listview.setAdapter(new ImageAdapter(SearchResults.this, R.layout.clistview, R.id.text1, R.id.image1,listItemss ,imagess,data ,1 ));
				}
				else
				{
					int newcount=0;
					for(int i=0;i<listItemss.length;i++)
					{
						try
						{
						String temp=listItemss[i].toLowerCase();
						String tempsearch=search.getText().toString().toLowerCase();
						if(temp.contains(tempsearch))
						{
							newcount++;
						}

						}
						catch(Exception e)
						{
							System.out.println(e.toString());
						}
					}
					String[] newlist=new String[newcount];
					String[]  newdata=new String[newcount];
					newcount=0;
					for(int i=0;i<listItemss.length;i++)
					{
						try
						{
						String temp=listItemss[i].toLowerCase();
						String tempsearch=search.getText().toString().toLowerCase();
						if(temp.contains(tempsearch))
						{
							newlist[newcount]=listItemss[i];
							newdata[newcount]=data[i];
							newcount++;
						}

						}
						catch(Exception e)
						{
							System.out.println(e.toString());
						}
						
					}
		    	    listview.setAdapter(new ImageAdapter(SearchResults.this, R.layout.clistview, R.id.text1, R.id.image1,newlist ,Arrays.copyOf(imagess, newcount),newdata ,1 ));

				}
			}
        	
        });

   		listItems=new ArrayList<String>();
		b = getIntent().getExtras();
        new ProgressTask().execute();       
    }

    private class ProgressTask extends AsyncTask<Void,Void,Void>{

   	ProgressDialog progress=new ProgressDialog(SearchResults.this,1);
		@Override
		protected void onPreExecute() {
			progress.setMessage("Fetching Trails...");
			progress.show();
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void onPostExecute(Void result) {
			if(listItemss.length!=0)
			{ 
				listview.setAdapter(new ImageAdapter(SearchResults.this, R.layout.clistview, R.id.text1, R.id.image1, listItemss, imagess, data,1 ));
				search.clearFocus();
			}
			else
            {
            	listItems.add("Sorry, no trails matches the given criteria");
            	listview.setAdapter(new ArrayAdapter(SearchResults.this, android.R.layout.simple_list_item_1, listItems));
            	search.setVisibility(View.GONE);
            }
			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
		       try { 
		    	   queryURL="?";
		    	   if(b.getString("campground")!="")
		    	   queryURL=queryURL+"&camp="+b.getString("campground");
		    	   if(b.getString("water")!="")
		    	   queryURL=queryURL+"&water="+b.getString("water");
		    	   if(b.getString("restroom")!="")
		    	   queryURL=queryURL+"&bath="+b.getString("restroom");
		    	   if(b.getString("fee")!="")
		    	   queryURL=queryURL+"&fee="+b.getString("fee");
		    	   if(b.getString("parking")!="")
		    	   queryURL=queryURL+"&parking="+b.getString("parking");
		    	   if(b.getString("handicap")!="")
		    	   queryURL=queryURL+"&ada="+b.getString("handicap");
		    	   if(b.getString("skiing")!="")
		    	   queryURL=queryURL+"&skiing="+b.getString("skiing");
		    	   if(b.getString("paddle")!="")
		    	   queryURL=queryURL+"&paddle="+b.getString("paddle");
		    	   if(b.getString("horsestage")!="")
		    	   queryURL=queryURL+"&horsestage="+b.getString("horsestage");
		    	   if(b.getString("pets")!="")
		    	   queryURL=queryURL+"&pets="+b.getString("pets");
		    	   if(b.getString("surface")!="")
		    	   queryURL=queryURL+"&surface="+b.getString("surface");

		    	   if(b.getString("sort")!="")
		    	   queryURL=queryURL+"&sort="+b.getString("sort");
		    	   if(b.getString("time_to_travel_lte")!="")
		    	   queryURL=queryURL+"&time_to_travel_lte="+b.getString("time_to_travel_lte");
		    	   if(b.getString("time_to_travel_gte")!="")
		    	   queryURL=queryURL+"&time_to_travel_gte="+b.getString("time_to_travel_gte");
		    	   
		    	   if(b.getString("region")!="")
		    	   {
		    		   queryURL=queryURL+"&region="+b.getString("region");
		    	   }
		    	   if(b.getString("name")!="")
		    	   {
		    		   queryURL=queryURL+"&name="+b.getString("keyword");
		    	   }
		    	   queryURL=queryURL+"&lat="+b.getString("latitude")+"&lon="+b.getString("longitude");
		    	   
		        	queryURL=queryURL.replace(" ","%20");
		        	System.out.println(queryURL);
		        	String line=Functions.TrailsSearch(queryURL+"&distance=1000000");
		            JSONArray ja = new JSONArray(line);

			            trail_id=new int[ja.length()];
			            listItemss=new String[ja.length()];
			            imagess=new int[ja.length()];
			            data=new String[ja.length()];
	
			            for (int i = 0; i < ja.length(); i++) {
			                JSONObject jo = (JSONObject) ja.get(i);
			                listItemss[i]=jo.getString("name");
			                imagess[i]=R.drawable.arrow;
			                data[i]=jo.toString();
			                trail_id[count]=jo.getInt("id");
			                count++;
		            }
		            
		        } catch (Exception e) {
		        	System.out.println("podapo: "+e.toString());
		         } 
			return null;
		}
    }



}
