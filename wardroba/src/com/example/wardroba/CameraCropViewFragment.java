package com.example.wardroba;

import com.edmodo.cropper.CropImageView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraCropViewFragment extends Fragment  
{
	ViewGroup root;
	ImageView btnUndo,btnCrop;
	CropImageView imgCrop;
	Bundle bundle;
	String imageUrl,imageTakenFrom;
	byte[] data;
	Bitmap productBitmap;
	Uri picUri;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = (ViewGroup) inflater.inflate(R.layout.camera_image_crop_fragment,null);
		imgCrop=(CropImageView)root.findViewById(R.id.CropImageView);
		btnUndo=(ImageView)root.findViewById(R.id.btnUndo);
		btnCrop=(ImageView)root.findViewById(R.id.btnCrop);
		bundle=getArguments();
		imageTakenFrom=bundle.getString("imageTakenFrom");
		if(imageTakenFrom.equals("gallery"))
		{
			imageUrl=bundle.getString("imageUri");
			picUri=Uri.parse(imageUrl);
			String fileUrl=getPath(picUri);
			productBitmap=BitmapFactory.decodeFile(fileUrl);
			//imgCrop.setImageURI();
			imgCrop.setImageBitmap(productBitmap);
			//performCrop();
		}
		else
		{
			data=bundle.getByteArray("data");
			Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
	    	Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, false);
	    	productBitmap=correctBmp;
	    	imgCrop.setImageBitmap(productBitmap);
		}	
		setUpButtonClick();
		return root;
	}
	public void setUpButtonClick()
	{
		btnCrop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bitmap croppedImage=imgCrop.getCroppedImage();
				Bundle bundle=new Bundle();
				bundle.putParcelable("croppedImage", croppedImage);
				 CameraImageDoneFragment doneFragment=new CameraImageDoneFragment();
				 doneFragment.setArguments(bundle);
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
		            transaction.replace(R.id.camera_fragment_container, doneFragment);
		            transaction.addToBackStack(null);
		            transaction.commit();
			}
		});
		btnUndo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
	}
	public String getPath(Uri uri) 
	 {

	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
   }
	
}
