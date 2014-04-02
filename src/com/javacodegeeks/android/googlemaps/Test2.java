package com.javacodegeeks.android.googlemaps;

import java.io.File;
import java.io.IOException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.app.ActivityGroup;

public class Test2 extends BaseActivity {

	Bundle b;
	Camera camera;
	SurfaceView surfaceView;
	boolean previewing = false;
	Context context;
	Intent cameraIntent;
	String name = "";
	Uri mImageUri;
	int CAMERA_PIC_REQUEST = 1332;
	Button merge;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test2);

		b = getIntent().getExtras();
		try {
			cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
					//android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			// request code
			File file = new File(Functions.getCachePath() + "/temp.jpg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Uri outputFileUri = Uri.fromFile(file);

			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			if (requestCode == CAMERA_PIC_REQUEST) {
				Intent intent = new Intent(Test2.this, EditPostCard.class)
						.putExtras(Test2.this.getIntent().getExtras());

				startActivity(intent);
				Test2.this.finish();
			}
		} catch (Exception e) {

		}
	}

}
