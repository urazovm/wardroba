package com.example.wardroba;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CameraCropViewFragment extends Fragment  
{
	ViewGroup root;
	ImageView imgCrop,btnUndo,btnCrop;
	Bundle bundle;
	String imageUrl;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = (ViewGroup) inflater.inflate(R.layout.camera_image_crop_fragment,null);
		imgCrop=(ImageView)root.findViewById(R.id.imgCrop);
		btnUndo=(ImageView)root.findViewById(R.id.btnUndo);
		btnCrop=(ImageView)root.findViewById(R.id.btnCrop);
		/*bundle=getArguments();
		imageUrl=bundle.getString("imageUri");
		imgCrop.setImageURI(Uri.parse(imageUrl));*/
		return root;
	}
}
