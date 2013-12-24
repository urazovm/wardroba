package com.example.wardroba;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraViewActivity extends Fragment implements Callback
{
	
	SurfaceView cameraSurface;
	SurfaceHolder holder;
	ImageView btnHome,btnAlbum,btnCamera,btnFlash;
	Camera camera;
	boolean isPriviewing=false;
	ViewGroup root;
	public static int IMAGE_SELECTOR=1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 root = (ViewGroup) inflater.inflate(R.layout.camera_view_fragment, null);
		btnHome=(ImageView)root.findViewById(R.id.btnHome);
		btnCamera=(ImageView)root.findViewById(R.id.btnCamera);
		btnAlbum=(ImageView)root.findViewById(R.id.btnAlbum);
		
		btnFlash=(ImageView)root.findViewById(R.id.btnFlash);
		cameraSurface=(SurfaceView)root.findViewById(R.id.cameraSurface);
		holder=cameraSurface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		 /*try {
        	 camera=Camera.open();
        	 
        	 camera.setDisplayOrientation(90);
        	 camera.setPreviewDisplay(holder);
        	 camera.startPreview();
        	 isPriviewing=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 btnCamera.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					camera.takePicture(null, null, myPictureCallback);
					
				}
			});
			setUpButtonClick();
		return root;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	public void setUpButtonClick()
	{
		btnAlbum.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View view) 
			{
				// TODO Auto-generated method stub
				
				/*CameraCropViewFragment cropFragment=new CameraCropViewFragment();
				 
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
		            transaction.replace(R.id.camera_fragment_container, cropFragment);
		            transaction.addToBackStack(null);
		            
		            // Commit the transaction
		            transaction.commit();*/
		            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
			}
		});
		btnFlash.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		//this.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK)
		{
			 Uri selectedImage = data.getData();
			 Toast.makeText(getActivity(), "selected image"+selectedImage.toString(), Toast.LENGTH_SHORT).show();
			 Bundle bundle=new Bundle();
			 bundle.putString("imageUri", selectedImage.toString());
			 CameraCropViewFragment cropFragment=new CameraCropViewFragment();
			 cropFragment.setArguments(bundle);
			 FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.replace(R.id.camera_fragment_container, cropFragment);
	            transaction.addToBackStack(null);
	            
	            // Commit the transaction
	            transaction.commit();
			 
			 
		     
		}
	}
		PictureCallback myPictureCallback=new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			 
		     try {
		    	 
		    	    
		    	 Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
		    	 Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, false);
			     saveImage(correctBmp);
			     camera.stopPreview();
			     isPriviewing=false;
			     Toast.makeText(getActivity(), "Picture saved", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
			}
		     
		}
	};
	
	 public void saveImage(Bitmap sharePhoto) 
	    {
	        File filename;
	        try 
	        {
	        	
	            String path = Environment.getExternalStorageDirectory().toString();

	            new File(path + "/wardroba").mkdirs();
	            filename = new File(path + "/wardroba/image1.jpg");

	            FileOutputStream out = new FileOutputStream(filename);
	            
	            sharePhoto.compress(Bitmap.CompressFormat.JPEG, 80, out);
	            
	            out.flush();
	            out.close();
	            
	            
	                       
	        } catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	    }    
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
		// TODO Auto-generated method stub
		Log.e("CameraView", "Surface change");
		if(isPriviewing)
		{
			try {
				camera.stopPreview();
				isPriviewing=false;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		try {
			
			int bounds=dpToPx(300);
			 camera=Camera.open();
			 Parameters parameters=camera.getParameters();
        	 parameters.setRotation(90);
        	 camera.setDisplayOrientation(90);
        	 parameters.setPreviewSize(300, 300);
        	 camera.setParameters(parameters);
        	 camera.setPreviewDisplay(holder);
        	 camera.startPreview();
        	 isPriviewing=true;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		Log.e("CameraView", "Surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
        camera.release();
        camera = null;
        isPriviewing=false;
	}
	private int dpToPx(int dp)
    {
        float density =  getActivity().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
