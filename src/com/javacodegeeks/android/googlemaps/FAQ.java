package com.javacodegeeks.android.googlemaps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.actionbarsherlock.app.SherlockActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FAQ extends BaseActivity {
    ListView pics;
	String names[]=new String[1000];
	ArrayList<String> listItems = new ArrayList<String>();

	int count=0;
	
	public void onCreate(Bundle savedInstanceState)
	    {	   		
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.listt);
			ListView listview = (ListView) findViewById(R.id.listView1);

			JSONArray ja;
			try {
				InputStream is = getAssets().open("faq.json");
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];
				try {
				    Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				    int n;
				    while ((n = reader.read(buffer)) != -1) {
				        writer.write(buffer, 0, n);
				    }
				} finally {
				    is.close();
				}
				ja = new JSONArray(writer.toString().trim());
				String[] data = new String[ja.length()+ja.length()+1];
				int count=1;
				data[0]="FREQUENTLY ASKED QUESTIONS ";
				for(int i=0;i<ja.length();i++)
				{	
					data[count]=ja.getJSONObject(i).getString("question")+" ";
					count++;
					data[count]=ja.getJSONObject(i).getString("answer")+" ";
					count++;
				}


				listview.setAdapter(new ImageAdapter(this, R.layout.warningview,
						R.id.text1, data));
				listview.setScrollingCacheEnabled(false);
				listview.setDivider(null);
				listview.setCacheColorHint(R.color.blue);
				listview.setFadingEdgeLength(0);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

				if (pos %2 !=0 || pos==0) {
					label.setTypeface(typeface, typeface.BOLD_ITALIC);

				}
				label.setPadding(10, 0, 0, 0);
				label.setText(items[pos]);
			//	label.setText(Html.fromHtml("<a href='mailto:ask@me.it'>Send Feedback</a>"));
				label.setMovementMethod(LinkMovementMethod.getInstance());


				row.setClickable(false);
				row.setEnabled(false);

			} catch (Exception e) {
				System.out.print(e.toString());
			} 
			return (row);
		}
	}

}
