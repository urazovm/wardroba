package com.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.wardroba.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService; 
    ProgressBar myLoader;
    Context mContext;
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        mContext=context;
        executorService=Executors.newFixedThreadPool(5);
    }
    
    final int stub_id=R.drawable.progrss;
    public void DisplayImage(String url, ImageView imageView,ProgressBar loader)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        this.myLoader=loader;
        if(bitmap!=null)
        {
        		//imageView.setScaleType(ScaleType.CENTER);
        	if(bitmap.getWidth()>=300 || bitmap.getHeight()>=300)
        	{
        		imageView.setScaleType(ScaleType.FIT_XY);
        		imageView.setImageBitmap(bitmap);
        		myLoader.setVisibility(View.GONE);
        	}
        	else
        	{
        		imageView.setScaleType(ScaleType.CENTER);
        		imageView.setImageBitmap(bitmap);
        		myLoader.setVisibility(View.GONE);
        	}
        	
        }
        else
        {
            queuePhoto(url, imageView);
            myLoader.setVisibility(View.VISIBLE);
            //imageView.setImageResource(stub_id);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url) 
    {
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
        	Bitmap temp;
        	temp=BitmapFactory.decodeStream(new FileInputStream(f));
        	Log.e("Bitmap", "width:"+temp.getWidth());
        	Log.e("Bitmap", "Height:"+temp.getHeight());
        	int width = temp.getWidth();
            int height = temp.getHeight();
        	            /*BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=150;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;*/
            return temp;//BitmapFactory.decodeStream(new FileInputStream(f));//, null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    private int dpToPx(int dp)
    {
        float density =  mContext.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad)
    {
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
            {
            	
            	
            		//photoToLoad.imageView.setScaleType(ScaleType.CENTER);
            		
            	if(bitmap.getWidth()>=300 || bitmap.getHeight()>=300)
            	{
            		photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);
            		photoToLoad.imageView.setImageBitmap(bitmap);
            		myLoader.setVisibility(View.GONE);
            	}
            	else
            	{
            		photoToLoad.imageView.setScaleType(ScaleType.CENTER);
            		photoToLoad.imageView.setImageBitmap(bitmap);
            		myLoader.setVisibility(View.GONE);
            	}
            		
            	
            }//else
               // photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setDither(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); 
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
