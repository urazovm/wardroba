package com.example.wardroba;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SmartImageView  extends ImageView 
{
	public interface onMyDoubleClickListener
	{
		public void setLikeProduct();
	}
	GestureDetector gestureDetector;
	ImageView imgDil;
	public static int LIKE_COUNT=0;
	public onMyDoubleClickListener callback;
	public SmartImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	
		
	public SmartImageView(Context context,AttributeSet set) 
	{
		super(context,set);
		 gestureDetector = new GestureDetector(context, new GestureListener());
		// TODO Auto-generated constructor stub
		 
		 initImageview();
	}
	public void setDilImage(ImageView view)
	{
		imgDil=view;
	}
	
	public void initImageview()
	{
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
	
				return gestureDetector.onTouchEvent(event);
			}
		});
	}
	
	
	
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            
            imgDil.setVisibility(View.VISIBLE);
			Animation anim=AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
			//anim.setFillAfter(true);
			//anim.setRepeatMode(Animation.REVERSE);
			callback.setLikeProduct();
			imgDil.startAnimation(anim);
			anim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					
					imgDil.setVisibility(View.GONE);
					Animation anim=AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
					
					//anim.setRepeatMode(Animation.REVERSE);
					imgDil.startAnimation(anim);
				}
			});
            return true;
        }
    }
	
}
