package com.javacodegeeks.android.googlemaps;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

public class EditPostCard extends BaseActivity {
	String name = "";
	Drawable drawable;
	ImageView imageview;
	Button save, retake;
	Bitmap thumbnail;
	int position = 0;
	Bundle b;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			File file = new File(Functions.getCachePath() + "/temp.jpg");

			if (file.exists()) {
				file.delete();
				System.gc();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpostcard);

		imageview = (ImageView) findViewById(R.id.imageView1);

		b = getIntent().getExtras();
		try {
			name = (new JSONObject(b.getString("data"))).getString("name");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setTitle(name);
		System.out.println(name);

		retake = (Button) findViewById(R.id.retake);
		retake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = new File(Functions.getCachePath() + "/temp.jpg");
				if (file.exists()) {
					file.delete();
				}
				System.gc();
				startActivity(new Intent(EditPostCard.this, Test2.class)
						.putExtras(b));
				EditPostCard.this.finish();
			}
		});
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MediaStore.Images.Media.insertImage(getContentResolver(),
						((BitmapDrawable) drawable).getBitmap(), "", "");
				File file = new File(Functions.getCachePath() + "/temp.jpg");
				if (file.exists()) {
					file.delete();
				}
				AlertDialog.Builder alertbox = new AlertDialog.Builder(
						EditPostCard.this);

				alertbox.setMessage("Image saved into gallery");

				alertbox.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								EditPostCard.this.finish();
							}
						});

				alertbox.show();

			}
		});
		File file = new File(Functions.getCachePath() + "/temp.jpg");

		if (file.exists()) {
			refresh();
		} else {
			EditPostCard.this.finish();
		}
	}

	public void refresh() {
		try {

			thumbnail = BitmapFactory.decodeFile(
					Functions.getCachePath() + "/temp.jpg").copy(
					Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(thumbnail);
			System.out.println("hi:");

			TextPaint paint = new TextPaint();
			paint.setStyle(Style.STROKE);

			paint.setTypeface(Typeface.SANS_SERIF);

			paint.setColor(Color.WHITE);
			paint.setTextSize(75);

			Bitmap stamp = BitmapFactory.decodeResource(getResources(),
					R.drawable.postcardstamp);
			Bitmap greetings = BitmapFactory.decodeResource(getResources(),
					R.drawable.greetings_from);

			canvas.drawBitmap(Bitmap.createScaledBitmap(stamp,
					thumbnail.getWidth() / 3, thumbnail.getWidth() / 3, false),
					10, 10, new Paint());
			canvas.drawBitmap(
					Bitmap.createScaledBitmap(greetings, 350, 100, false), 10,
					canvas.getHeight() - 300, new Paint());

			System.out.println("get me");

			StaticLayout layout = new StaticLayout(name, paint,
					thumbnail.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.3f,
					0, false);
			canvas.translate(0, canvas.getHeight() - 150);

			layout.draw(canvas);

			drawable = new BitmapDrawable(getResources(), thumbnail);
			drawable.draw(canvas);
			System.out.println("get me");

			Bitmap result = Bitmap.createBitmap(canvas.getWidth(),
					layout.getHeight(), Config.ARGB_8888).copy(
					Bitmap.Config.ARGB_8888, true);

			Canvas ncanvas = new Canvas(result);
			ncanvas.drawRGB(135, 105, 49);
			paint.setAlpha(80);

			canvas.drawBitmap(result, 0, 10, paint);
			// canvas.drawBitmap(greetings, 10,10, new Paint());

			drawable = new BitmapDrawable(getResources(), thumbnail);

			imageview.setImageDrawable(drawable);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

}
