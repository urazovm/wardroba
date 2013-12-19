package com.example.wardroba;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class MySurfaceView  extends SurfaceView
{

	private int width;
	private int height;
	public MySurfaceView(Context context)
	{
		super(context);
	}
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    width = MeasureSpec.getSize(widthMeasureSpec);
	    height = width;
	    setMeasuredDimension(width, width);
	}
	public int getViewWidth() {
	    return width;
	}

	public int getViewHeight() {
	    return height;
	}
}
