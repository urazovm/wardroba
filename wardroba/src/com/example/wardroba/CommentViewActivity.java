package com.example.wardroba;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.facebook.Request;
import com.swipinglist.master.BaseSwipeListViewListener;
import com.swipinglist.master.SwipeListView;

import BaseAdapter.CommentBaseAdapter;
import BaseAdapter.HomeProductBaseAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentViewActivity extends Activity
{
	public ImageView btnBack;
	public ListView lsvComment;
	public TextView txtHeader,txtOK;
	public EditText edtAddComment;
	String MSG;
	Typeface tf;
	View selectedView;
	View prevView=null;
	boolean isOpened=false;
	int itemSelected;
	CommentBaseAdapter adapter;
	SwipeDetector swipeDetector;
	public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }
	
	private ArrayList<Comment> arr_CommentList;
	
	
  	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest(int requestNumber, Object obj) 
  	{		  		 		
  		arr_CommentList =(ArrayList<Comment>) obj;
  		if(arr_CommentList != null)
  		{  			 
  			Log.d("CommentList", "array:"+arr_CommentList.size());
  			if(arr_CommentList.size() >0)
  			{
  				adapter=new CommentBaseAdapter(arr_CommentList,CommentViewActivity.this);
  				adapter.notifyDataSetChanged();
  				lsvComment.setAdapter(adapter);
  			}else
  			{
  				adapter=new CommentBaseAdapter(arr_CommentList,CommentViewActivity.this);
  				adapter.notifyDataSetChanged();
  				lsvComment.setAdapter(adapter);
  	  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  			}
  		}
  		else
  		{
  			
  			lsvComment.setAdapter(null);
  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  		}
  	} 	
  	
	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest1(Comment c) 
  	{		  		
		//Toast.makeText(CommentViewActivity.this, "response", Toast.LENGTH_SHORT).show();
		Log.d("CommentPage", "array size:"+arr_CommentList.size());
		Log.d("CommentPage", "comment:"+c.getComment());
		
			
			arr_CommentList.add(c);
			adapter=new CommentBaseAdapter(arr_CommentList,CommentViewActivity.this);
			
			adapter.notifyDataSetChanged();
			lsvComment.setAdapter(adapter);
			int count=0;
			boolean flag=false;
			int cloth_id=Integer.parseInt(Constants.CLOTHISID);
			if(Constants.all_items.size()>0)
			{
				for(WardrobaItem temp:Constants.all_items)
					{
						int id=temp.getPIdCloth();
						if(id==cloth_id)
						{
							flag=true;
							break;
						}
					
						count++;	
					}
				if(flag)
				{
					Constants.all_items.get(count).setPCommentCount(arr_CommentList.size());
				}
			}
			count=0;
			flag=false;
			if(Constants.my_items.size()>0)
			{
				for(WardrobaItem temp:Constants.my_items)
					{
						int id=temp.getPIdCloth();
						if(id==cloth_id)
						{
							flag=true;
							break;
						}
					
						count++;	
					}
				if(flag)
				{
					Constants.my_items.get(count).setPCommentCount(arr_CommentList.size());
				}
			}
			
  	} 	
	public void setResponseForDelete()
	{
		if(arr_CommentList.size()>0)
		{
			arr_CommentList.remove(Constants.SELECTED_COMMENT);
			adapter.notifyDataSetChanged();
			int count=0;
			boolean flag=false;
			int cloth_id=Integer.parseInt(Constants.CLOTHISID);
			if(Constants.all_items.size()>0)
			{
				for(WardrobaItem temp:Constants.all_items)
					{
						int id=temp.getPIdCloth();
						if(id==cloth_id)
						{
							flag=true;
							break;
						}
					
						count++;	
					}
				if(flag)
				{
					Constants.all_items.get(count).setPCommentCount(arr_CommentList.size());
				}
			}
			count=0;
			flag=false;
			if(Constants.my_items.size()>0)
			{
				for(WardrobaItem temp:Constants.my_items)
					{
						int id=temp.getPIdCloth();
						if(id==cloth_id)
						{
							flag=true;
							break;
						}
					
						count++;	
					}
				if(flag)
				{
					Constants.my_items.get(count).setPCommentCount(arr_CommentList.size());
				}
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_view_activity);
		swipeDetector=new SwipeDetector();
		btnBack=(ImageView)findViewById(R.id.img_back);
		
		lsvComment=(ListView)findViewById(R.id.list_cooment);
		txtHeader=(TextView)findViewById(R.id.txt_header);
		txtOK=(TextView)findViewById(R.id.txt_ok);
		
		edtAddComment=(EditText)findViewById(R.id.edt_comment);
		
		tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		txtHeader.setTypeface(tf);
		txtOK.setTypeface(tf);
		edtAddComment.setTypeface(tf);
		
		if(isOnline()==true)
		{
			try
			{
				WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.comment_list,CommentViewActivity.this ,"Please Wait....");
				String url = Constants.VIEW_COMMENT_URL+"&object_id="+Constants.OBJECT_ID+"&user_id="+Constants.CLOTH_USERID+"&cloth_type="+Constants.CLOTH_TYPE;	
				Log.d("Comment View= ",url.toString());
				webAPIHelper.execute(url);    	
				
				//http://dev.wardroba.com/serviceXml/comment.php?object_id=560&user_id=66
			}
			catch(Exception e)
			{
				
			}				
		}
		else
		{
			MSG="Please check your internet connection...";
			alert();
		}
		lsvComment.setOnTouchListener(swipeDetector);
		lsvComment.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				
				if(swipeDetector.swipeDetected())
				{
					
					
					
					RelativeLayout front;
					int bounds=dpToPx(70);
					Log.d("CommentView", "swipe detected");
					if(swipeDetector.mSwipeDetected==Action.RL)
					{
						if(prevView==null)
						{
							prevView=view;
						}
						else
						{
							prevView=selectedView;
						}
						selectedView=view;
						if(isOpened)
						{
							Log.d("hello", "hello open slide");
							if(prevView!=view)
							{
								front=(RelativeLayout)prevView.findViewById(R.id.front);
								TranslateAnimation translateAnimation=new TranslateAnimation(-bounds, 0, 0, 0);
								translateAnimation.setDuration(1000);
								translateAnimation.setFillAfter(true);
								
								front.startAnimation(translateAnimation);
								front=(RelativeLayout)view.findViewById(R.id.front);
								TranslateAnimation translateAnimation1=new TranslateAnimation(0, -bounds, 0, 0);
								translateAnimation1.setDuration(1000);
								translateAnimation1.setFillAfter(true);
								front.startAnimation(translateAnimation1);
							}
							isOpened=true;
							itemSelected=position;
						}
						else
						{
							front=(RelativeLayout)view.findViewById(R.id.front);
							TranslateAnimation translateAnimation=new TranslateAnimation(0, -bounds, 0, 0);
							translateAnimation.setDuration(1000);
							translateAnimation.setFillAfter(true);
							front.startAnimation(translateAnimation);
							isOpened=true;
							itemSelected=position;
						}
					
					}
					else if (swipeDetector.mSwipeDetected==Action.LR) {
						
						if((itemSelected==position) && isOpened)
						{
							front=(RelativeLayout)view.findViewById(R.id.front);
							TranslateAnimation translateAnimation=new TranslateAnimation(-bounds, 0, 0, 0);
							translateAnimation.setDuration(1000);
							translateAnimation.setFillAfter(true);
							front.startAnimation(translateAnimation);
							isOpened=false;
						}
					}
					
					
				}
				else
				{
					
				}
			}
		});
		BackButton();
		CommentAddButton();
	}
	private int dpToPx(int dp)
    {
        float density =  getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
	
	public void BackButton()
	{
		btnBack.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				onBackPressed();
			}
		});
	}
	
	public void CommentAddButton()
	{
		txtOK.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				String CommentText=edtAddComment.getText().toString().trim();
				if(CommentText.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please add a comment...",5000).show();	
				}else
				{
					if(isOnline()==true)
					{
						try
						{
							WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.comment_add,CommentViewActivity.this);
							String url = Constants.ADD_COMMENT_URL+"&object_id="+Constants.OBJECT_ID+"&user_id="+Constants.CLOTH_USERID+"&comment="+URLEncoder.encode(CommentText)+"&cloth_type="+Constants.CLOTH_TYPE;	
							Log.d("Comment add= ",url.toString());
							webAPIHelper.execute(url);    	
							//http://dev.wardroba.com/serviceXml/product_comment.php?object_id=560&user_id=66&comment=testing

						}
						catch(Exception e)
						{
							
						}				
					}
					else
					{
						MSG="Please check your internet connection...";
						alert();
					}
				}
				
			}
		});
	}
	
	
	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}
        
   protected void onPause() 
   {
	   super.onPause();	
   } 
   
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(CommentViewActivity.this);
		builder.setMessage(MSG)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.dismiss();			                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();  
   }
   class SwipeDetector implements View.OnTouchListener {

	    

	    private static final String logTag = "SwipeDetector";
	    private static final int MIN_DISTANCE = 100;
	    private float downX, downY, upX, upY;
	    private Action mSwipeDetected = Action.None;

	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }

	    public Action getAction() {
	        return mSwipeDetected;
	    }

	    public boolean onTouch(View v, MotionEvent event) {
	        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	            downX = event.getX();
	            downY = event.getY();
	            mSwipeDetected = Action.None;
	            return false; // allow other events like Click to be processed
	        }
	        case MotionEvent.ACTION_MOVE: {
	            upX = event.getX();
	            upY = event.getY();

	            float deltaX = downX - upX;
	            float deltaY = downY - upY;

	            // horizontal swipe detection
	            if (Math.abs(deltaX) > MIN_DISTANCE) {
	                // left or right
	                if (deltaX < 0) {
	                    //Logger.show(Log.INFO,logTag, "Swipe Left to Right");
	                    mSwipeDetected = Action.LR;
	                    Log.i("CommentView", "swipe left to right");
	                    return true;
	                }
	                if (deltaX > 0) {
	                    //Logger.show(Log.INFO,logTag, "Swipe Right to Left");
	                	Log.i("CommentView", "swipe left to right");
	                	mSwipeDetected = Action.RL;
	                    return true;
	                }
	            } else 

	                // vertical swipe detection
	                if (Math.abs(deltaY) > MIN_DISTANCE) {
	                    // top or down
	                    if (deltaY < 0) {
	                        //Logger.show(Log.INFO,logTag, "Swipe Top to Bottom");
	                        mSwipeDetected = Action.TB;
	                        return false;
	                    }
	                    if (deltaY > 0) {
	                        //Logger.show(Log.INFO,logTag, "Swipe Bottom to Top");
	                        mSwipeDetected = Action.BT;
	                        return false;
	                    }
	                } 
	            return true;
	        }
	        }
	        return false;
	    }
	}
}
