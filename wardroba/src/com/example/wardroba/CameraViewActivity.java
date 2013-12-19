package com.example.wardroba;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraViewActivity extends Activity implements Callback
{
	
	MySurfaceView cameraSurface;
	SurfaceHolder holder;
	ImageView btnHome,btnAlbum,btnCamera,btnFlash;
	Camera camera;
	boolean isPriviewing=false;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_view_activity);
		btnHome=(ImageView)findViewById(R.id.btnHome);
		btnCamera=(ImageView)findViewById(R.id.btnCamera);
		btnAlbum=(ImageView)findViewById(R.id.btnAlbum);
		btnFlash=(ImageView)findViewById(R.id.btnFlash);
		cameraSurface=(MySurfaceView)findViewById(R.id.cameraSurface);
		holder=cameraSurface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
			 
             try {
            	 camera=Camera.open();
            	 
            	 camera.setDisplayOrientation(90);
            	 camera.setPreviewDisplay(holder);
            	 camera.startPreview();
            	 isPriviewing=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
		
		btnCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				camera.takePicture(null, null, myPictureCallback);
				
			}
		});
		
	}

	/*ShutterCallback myShutterCallback=new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
		
	};*/
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
			     Toast.makeText(getApplicationContext(), "Picture saved", Toast.LENGTH_SHORT).show();
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
			camera.stopPreview();
			isPriviewing=false;
		}
		try {
			
			 Parameters parameters=camera.getParameters();
        	 parameters.setRotation(90);
        	 parameters.setPreviewSize(width, height);
        	 camera.setDisplayOrientation(90);
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
}
