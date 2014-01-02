package com.tumblr.api;

import com.example.wardroba.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;



public class UploadImageActivity extends PostActivity {
	private static final String TAG = "UploadImageActivity";

	Uri outputFileUri;
	
	// TumblrApi api;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	
		// api = new TumblrApi(this);
		setContentView(R.layout.about_us_activity);

		Intent startIntent = getIntent();
		outputFileUri=Uri.parse(startIntent.getExtras().getString("productUri"));
		if(outputFileUri!=null)
		{
			uploadImage();
		}
	}

	
	private String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);
		} catch (Exception ex) {
			return "";
		}
	}

	private void uploadImage() {
		
		
		Toast.makeText(getApplicationContext(), "Upload image call", Toast.LENGTH_SHORT).show();
		Intent uploadIntent = new Intent(TumblrService.ACTION_POST_PHOTO);
		uploadIntent.putExtra("photo", outputFileUri.toString());
		uploadIntent.putExtra("caption", "hello");
		uploadIntent.putExtra("options", mPostOptions);
		startService(uploadIntent);

		setResult(RESULT_OK);
		finish();
	}

	
}
