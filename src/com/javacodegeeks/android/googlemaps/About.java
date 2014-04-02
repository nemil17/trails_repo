package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class About extends BaseActivity {
    ListView pics;
	String names[]=new String[1000];
	ArrayList<String> listItems = new ArrayList<String>();

	int count=0;
	
	public void onCreate(Bundle savedInstanceState)
	    {	   		
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.listt);
			ListView listview = (ListView) findViewById(R.id.listView1);

			String[] data = { "ABOUT THE AZ STATE TRAILS APP ",
					getResources().getString(R.string.about1), "DATA SOURCES",
					getResources().getString(R.string.about2),
					"ESTIMATED TIME",
					getResources().getString(R.string.about3) };

			listview.setAdapter(new ImageAdapter(this, R.layout.warningview,
					R.id.text1, data));
			listview.setScrollingCacheEnabled(false);
			listview.setDivider(null);
			listview.setCacheColorHint(R.color.blue);
			listview.setFadingEdgeLength(0);
	}
	
	private class ImageAdapter extends ArrayAdapter {
		SherlockActivity context;
		String[] items;
		int layoutId;
		int textId;

		@SuppressWarnings("unchecked")
		public ImageAdapter(SherlockActivity context, int layoutId, int textId,
				String[] items) {
			super(context, layoutId, items);
			this.context = context;
			this.items = items;
			this.layoutId = layoutId;
			this.textId = textId;
		}

		public View getView(int pos, View convertView, ViewGroup parent) {
			View row=null;
			try {
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(layoutId, null);

				ImageView icon = (ImageView) row.findViewById(R.id.image1);
				icon.setVisibility(View.GONE);
				TextView label = (TextView) row.findViewById(textId);
				Typeface typeface = Typeface.createFromAsset(
						context.getAssets(), "Roboto-Regular.ttf");
				label.setTypeface(typeface);

				if (pos == 0 || pos == 2 || pos == 4) {
					label.setTypeface(typeface, typeface.BOLD_ITALIC);
				}
				label.setPadding(10, 0, 0, 0);
				label.setText(items[pos]);
				row.setClickable(false);
				row.setEnabled(false);

			} catch (Exception e) {
				System.out.print(e.toString());
			} 
			return (row);
		}
	}

}
