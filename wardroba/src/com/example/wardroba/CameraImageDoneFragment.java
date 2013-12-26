package com.example.wardroba;

import com.edmodo.cropper.CropImageView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class CameraImageDoneFragment extends Fragment  implements OnTouchListener
{
	ViewGroup root;
	ImageView btnUndo,btnDone;
	ImageView imgDone;
	Button btnReset;
	Bundle bundle;
	Bitmap productBitmap;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	Typeface tf;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = (ViewGroup) inflater.inflate(R.layout.camera_image_done_fragment,null);
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
		imgDone=(ImageView)root.findViewById(R.id.imgDone);
		btnUndo=(ImageView)root.findViewById(R.id.btnUndo);
		btnDone=(ImageView)root.findViewById(R.id.btnDone);
		btnReset=(Button)root.findViewById(R.id.btnReset);
		btnReset.setTypeface(tf);
		bundle=getArguments();
		productBitmap=(Bitmap)bundle.getParcelable("croppedImage");
		imgDone.setImageBitmap(productBitmap);
		imgDone.setOnTouchListener(this);
		imgDone.setScaleType(ScaleType.FIT_CENTER);
		setUpButtonClick();
		return root;
	}
	public void setUpButtonClick()
	{
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CamaraSaveFragment saveFragment=new CamaraSaveFragment();
				 
				 saveFragment.setArguments(bundle);
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
		            transaction.replace(R.id.camera_fragment_container, saveFragment);
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
		btnReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
				ImageView view = (ImageView) v;
				// make the image scalable as a matrix
				view.setScaleType(ImageView.ScaleType.MATRIX);
				float scale;
		
				// Handle touch events here…
				switch (event.getAction() & MotionEvent.ACTION_MASK) 
				{
		
					case MotionEvent.ACTION_DOWN: //first finger down only
							savedMatrix.set(matrix);
							start.set(event.getX(), event.getY());
							Log.d("CameraDone", "mode=DRAG" );
							mode = DRAG;
							break;
					case MotionEvent.ACTION_UP: //first finger lifted
					
					case MotionEvent.ACTION_POINTER_UP: //second finger lifted
							mode = NONE;
							Log.d("CameraDone", "mode=NONE" );
							break;
					case MotionEvent.ACTION_POINTER_DOWN: //second finger down
							oldDist = spacing(event); // calculates the distance between two points where user touched.
							
							// minimal distance between both the fingers
							if (oldDist > 5f) 
							{
								savedMatrix.set(matrix);
								midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched.
								mode = ZOOM;
							
							}
							break;
			
					case MotionEvent.ACTION_MOVE:
							if (mode == DRAG)
							{ //movement of first finger
								matrix.set(savedMatrix);
								if (view.getLeft() >= -392)
								{
								matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
								}
							}
							else if (mode == ZOOM) 
							{ //pinch zooming
								float newDist = spacing(event);
								
								if (newDist > 5f) 
								{
								matrix.set(savedMatrix);
								scale = newDist/oldDist; //thinking I need to play around with this value to limit it**
								matrix.postScale(scale, scale, mid.x, mid.y);
								}
							}
							break;
				}
		
				// Perform the transformation
				view.setImageMatrix(matrix);
		
				return true; // indicate event was handled
		}

		private float spacing(MotionEvent event) 
		{
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		private void midPoint(PointF point, MotionEvent event) 
		{
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
		

}
