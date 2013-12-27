package com.example.wardroba;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
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
	private static boolean FLASH_LIGHT=false;
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
		 try {
			camera=getCameraInstance();
		} catch (Exception e) {
			// TODO: handle exception
		}
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
		getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
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
				if(!FLASH_LIGHT)
				{
					try {
						Parameters params=camera.getParameters();
						params.setFlashMode(Parameters.FLASH_MODE_ON);
						camera.setParameters(params);
						camera.startPreview();
						
						FLASH_LIGHT=true;
						btnFlash.setBackgroundResource(R.drawable.flash_h);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				else
				{
					try {
						Parameters params=camera.getParameters();
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(params);
						camera.startPreview();
						btnFlash.setBackgroundResource(R.drawable.flash);
						FLASH_LIGHT=false;
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		//super.onActivityResult(requestCode, resultCode, data);
		//this.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK)
		{
			 Uri selectedImage = data.getData();
			 String path=getPath(selectedImage);
			 
			 //Bitmap myProduct=(Bitmap)BitmapFactory.decodeFile(path);
			 //Bitmap scaleBitmap=Bitmap.createScaledBitmap(myProduct, myProduct.getWidth()/2, myProduct.getHeight()/2, true);
			 Bitmap myProduct=(Bitmap)decodeFile(new File(path), cameraSurface.getWidth(), cameraSurface.getHeight());
			 Toast.makeText(getActivity(), "selected image"+selectedImage.toString(), Toast.LENGTH_SHORT).show();
			 Bundle bundle=new Bundle();
			 
			
			 bundle.putParcelable("galleryImage", myProduct);
			 bundle.putString("imageTakenFrom", "gallery");
			 CameraCropViewFragment cropFragment=new CameraCropViewFragment();
			 cropFragment.setArguments(bundle);
			 FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.replace(R.id.camera_fragment_container, cropFragment);
	            transaction.addToBackStack(null);
	            transaction.commitAllowingStateLoss();
	            // Commit the transaction
	            //transaction.commit();
	
			 
		     
		}
	}
	public String getPath(Uri uri) 
	 {

	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
  }
	private Bitmap getThumbnailBitmap(final String path, final int thumbnailSize) {
	    Bitmap bitmap;
	    BitmapFactory.Options bounds = new BitmapFactory.Options();
	    bounds.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, bounds);
	    if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
	        bitmap = null;
	    }
	    int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
	            : bounds.outWidth;
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inSampleSize = originalSize / thumbnailSize;
	    bitmap = BitmapFactory.decodeFile(path, opts);
	    
	    return bitmap;
	}
	
	// decode file scale down image size



    public  Bitmap decodeFile(File f,int WIDTH,int HIGHT){
     try {
         //Decode image size
         BitmapFactory.Options o = new BitmapFactory.Options();
         o.inJustDecodeBounds = true;
         BitmapFactory.decodeStream(new FileInputStream(f),null,o);
         //The new size we want to scale to
         final int REQUIRED_WIDTH=WIDTH;
         final int REQUIRED_HIGHT=HIGHT;
         //Find the correct scale value. It should be the power of 2.
         int scale=1;
         while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
             scale*=2;
         //Decode with inSampleSize
         BitmapFactory.Options o2 = new BitmapFactory.Options();
         o2.inSampleSize=scale;
         return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
     } catch (FileNotFoundException e) {}
     return null;
     }





	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		
	}
	
		PictureCallback myPictureCallback=new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			 
		     try {
		    	 
		    	    
		    	 
			     //saveImage(correctBmp);
		    	 
			     
			     //isPriviewing=false;
		    	 //correctBmp.recycle();
		    	 byte[] resizeData=resizeImage(data);
			     Bundle bundle=new Bundle();
				 bundle.putByteArray("data", resizeData);
				 bundle.putString("imageTakenFrom", "camera");
				 CameraCropViewFragment cropFragment=new CameraCropViewFragment();
				 cropFragment.setArguments(bundle);
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
		            transaction.replace(R.id.camera_fragment_container, cropFragment);
		            transaction.addToBackStack(null);
		            transaction.commit();
		            //camera.lock();
			     //Toast.makeText(getActivity(), "Picture saved", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		     
		}
	};
	byte[] resizeImage(byte[] input) {
	    Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
	    Bitmap resized = Bitmap.createScaledBitmap(original, cameraSurface.getWidth(), cameraSurface.getHeight(), true);
	         
	    ByteArrayOutputStream blob = new ByteArrayOutputStream();
	    resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);
	 
	    return blob.toByteArray();
	}
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
				camera.release();
				isPriviewing=false;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		try {
			Log.e("CameraView", "on surface change and preview start");
			int bounds=dpToPx(300);
			
			if(camera!=null)
			{
			 camera.setPreviewDisplay(surfaceHolder);
        	 camera.startPreview();
        	 isPriviewing=true;
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		/*try {
			Log.e("CameraView", "on surface change and preview start");
			//int bounds=dpToPx(300);
			if(camera!=null)
			{
			
        	 camera.setPreviewDisplay(surfaceHolder);
        	 camera.startPreview();
        	 isPriviewing=true;
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }*/
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		// TODO Auto-generated method stub
		Log.e("CameraView", "Surface destroy");
		try {
			camera.stopPreview();
			camera.lock();
	        camera.release();
	        camera = null;
	        isPriviewing=false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	private int dpToPx(int dp)
    {
        float density =  getActivity().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
	private Camera getCameraInstance()
	{
		Camera camera=null;
		
		try {
			camera=Camera.open();
			 Parameters parameters=camera.getParameters();
        	 parameters.setRotation(90);
        	 camera.setDisplayOrientation(90);
        	 
        	 camera.setParameters(parameters);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return camera;
	}
}
