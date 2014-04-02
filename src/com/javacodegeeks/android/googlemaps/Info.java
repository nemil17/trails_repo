package com.javacodegeeks.android.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Info extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		Button safety=(Button)findViewById(R.id.safety);
		
		safety.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i=new Intent(Info.this,Safety.class);
				startActivity(i);

			}
			
		});
		
		
		Button about=(Button)findViewById(R.id.about);
		
		about.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i=new Intent(Info.this,About.class);
				startActivity(i);
			}
			
		});


		Button faq=(Button)findViewById(R.id.faq);
		
		faq.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i=new Intent(Info.this,FAQ.class);
				startActivity(i);

			}
			
		});

		Button difficult=(Button)findViewById(R.id.difficult);
		
		difficult.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent i=new Intent(Info.this,Difficulty.class);
				startActivity(i);

			}
			
		});

		TextView feedback=(TextView)findViewById(R.id.feedback);
		feedback.setText(Html.fromHtml("<a href='mailto:pio@azstateparks.gov'>Send Feedback</a>"));
		feedback.setMovementMethod(LinkMovementMethod.getInstance());

	}

}
