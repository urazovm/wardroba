package com.example.wardroba;

import java.io.InputStream;
import java.net.URL;

import twitter4j.StatusUpdate;

import com.ImageLoader.ImageLoader;
import com.android.sot.twitter.TwitterApp;
import com.android.sot.twitter.TwitterApp.TwDialogListener;
import com.connection.Constants;
import com.connection.WebAPIHelper1;
import android.app.Activity;
import android.app.ProgressDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailFragment extends Fragment
{
	ImageView imgProductPhoto,imgShare,imgLike,imgComment;
	ImageView btnFacebook,btnTwitter,btnPinterest,btnTumbler,btnGooglePlus;
	Button btnCancel;
	TextView txtLike,txtComment,textDescription,txtSharLable;
	ImageLoader imageLoader;
	LinearLayout shareDialog,lay_delete;
	WardrobaItem selected_item;
	ProgressBar progLoader;
	public static int SELECTED_PRODUCT=0;
	Typeface tf;

	SwipeDetector swipeDetector;
	public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

	//TWITTER

	static final String TWITTER_CONSUMER_KEY = "NJC4QxSTuMxIbPYsTj5xoA";
	static final String TWITTER_SECRET_KEY = "BsYApRSdctPi8EckamqRr3uHh5ikjDRwFRgWimI62I";
	public static String shareLink = "http://www.google.com";
	Handler mHandler;
	TwitterApp mTwitter;
	private ProgressDialog progressdialog;
	StringBuffer buffer;
	String MSG;
	private ProgressBar mProgress;
	
	String imageUrl,shortDesc;
	//////////////////////////////////
	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest1(int requestNumber) 
  	{	
			int cloth_id=Constants.my_items.get(Constants.SELECTED_ID).getPIdCloth();
			String status=Constants.my_items.get(Constants.SELECTED_ID).getPLikeStatus();
			int like_count=Constants.my_items.get(Constants.SELECTED_ID).getPLikeCount();
			int count=0;
  			if(Constants.all_items.size()>0)
  			{
  				for(WardrobaItem temp:Constants.all_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==cloth_id)
  						break;

  					count++;	
  				}
  			}
  			Constants.all_items.get(count).setPLikeStatus(status);
  			Constants.all_items.get(count).setPLikeCount(like_count);
  			updateProductDetail();
  	}
	public void setResponseFromRequest2(int requestNumber) 
  	{	

		if(Constants.PRODUCT_DELETED != 0)
		{
			if(Constants.all_items.size()>0)
  			{
				int count=0;
  				for(WardrobaItem temp:Constants.all_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==Constants.PRODUCT_DELETED)
  					{
  						Constants.all_items.remove(count);
  						Log.d("Count ID=", String.valueOf(count));
  						break;
  					}	
  					count++;
  				}
  			}
			
			if(Constants.my_items.size()>0)
  			{
  				for(WardrobaItem temp:Constants.my_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==Constants.PRODUCT_DELETED)
  					{
  						Constants.my_items.remove(Constants.PRODUCT_DELETED_SELECTED_ID);
  						Log.d("Selected ID=", String.valueOf(Constants.PRODUCT_DELETED_SELECTED_ID));
  						break;
  					}	
  				}
  			}
			
			Toast.makeText(getActivity(), "Product has been deleted successfully.", 5000).show();
			getFragmentManager().popBackStack();
			
		}else
		{
			Toast.makeText(getActivity(), "Product not deleted..", 5000).show();
		}
  	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.product_detail_lay, null);
	
		swipeDetector=new SwipeDetector();
		imgProductPhoto=(ImageView)root.findViewById(R.id.img_product_photo);
		txtLike=(TextView)root.findViewById(R.id.txt_like);
		txtComment=(TextView)root.findViewById(R.id.txt_comment);
		imgShare=(ImageView)root.findViewById(R.id.img_share);
		textDescription=(TextView)root.findViewById(R.id.txt_short_description);
		imgLike=(ImageView)root.findViewById(R.id.img_like);
		imgComment=(ImageView)root.findViewById(R.id.img_comment);
		
		mProgress = (ProgressBar)root.findViewById(R.id.progress_bar);
 		mProgress.setVisibility(View.GONE);
 		
		shareDialog=(LinearLayout)root.findViewById(R.id.dialogShare);
		lay_delete=(LinearLayout)root.findViewById(R.id.lay_delete);
		lay_delete.setVisibility(View.GONE);
		
		progLoader=(ProgressBar)root.findViewById(R.id.progLoader);
		
		btnFacebook=(ImageView)root.findViewById(R.id.btnFB);
		btnTwitter=(ImageView)root.findViewById(R.id.btnTwitter);
		btnPinterest=(ImageView)root.findViewById(R.id.btnPinterest);
		btnTumbler=(ImageView)root.findViewById(R.id.btnTumbler);
		btnGooglePlus=(ImageView)root.findViewById(R.id.btnGplus);
		btnCancel=(Button)root.findViewById(R.id.btnCancel);
		txtSharLable=(TextView)root.findViewById(R.id.txt_share_lable);
		
		imageLoader=new ImageLoader(getActivity());
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
		txtLike.setTypeface(tf);
		txtComment.setTypeface(tf);
		btnCancel.setTypeface(tf);
		textDescription.setTypeface(tf);
		txtSharLable.setTypeface(tf);
		
        ///////////////////////////////////// FOR TWITTER ////////////////////////////////////////////

		mHandler = new Handler();
		mTwitter = new TwitterApp(getActivity(), TWITTER_CONSUMER_KEY,TWITTER_SECRET_KEY);
		mTwitter.setListener(mTwLoginDialogListener);
        		
		CancelDialog();
		FacebookSharing();
		TwitterSharing();
		PinterestSharing();
		TumblerSharing();
		GooglePlusSharing();
		ProductDelete();
		return root;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
	}
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);	
	}
	@Override
	public void onStart() 
	{
		super.onStart();
		 Bundle args = getArguments();
	        if (args != null) 
	        {	            
	        }
	        updateProductDetail();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Twitter
		mHandler = new Handler();
		mTwitter = new TwitterApp(getActivity(), TWITTER_CONSUMER_KEY,TWITTER_SECRET_KEY);
		mTwitter.setListener(mTwLoginDialogListener);
		updateProductDetail();
	}
	private void updateProductDetail()
	{
		
		int likeCount,commentCount;

		selected_item=Constants.my_items.get(SELECTED_PRODUCT);
		imageUrl=selected_item.getPImageUrl();
		likeCount=selected_item.getPLikeCount();
		commentCount=selected_item.getPCommentCount();
		imageLoader.DisplayImage(imageUrl, imgProductPhoto,progLoader);
		shortDesc=selected_item.getPTag();

		txtLike.setText(String.valueOf(likeCount));
		txtComment.setText(String.valueOf(commentCount));
		textDescription.setText(shortDesc.trim());
		shareDialog.setVisibility(View.GONE);

		
		String LikeStatus = selected_item.getPLikeStatus().toString().trim();
		if(LikeStatus.equals("LIKE"))
        {
			imgLike.setBackgroundResource(R.drawable.like);
        }else
        {
        	imgLike.setBackgroundResource(R.drawable.like_h);
        }
		
		imgLike.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View arg0) 
			{
				String url;
				String LikeStatus;
				String Cloth_Id,User_Id,ObjectId1,Cloth_type;
				Cloth_Id = String.valueOf( selected_item.getPIdCloth());
				User_Id = String.valueOf(selected_item.getPUserId());
				ObjectId1 = String.valueOf(selected_item.getPObjectId());
				Cloth_type=selected_item.getPClothType().toString().trim();
				LikeStatus = selected_item.getPLikeStatus().toString().trim();
				Constants.SELECTED_ID=SELECTED_PRODUCT;
				
				int count_like = (selected_item.getPLikeCount());
		
				if(LikeStatus.equals("LIKE"))
		        {
					txtLike.setText(String.valueOf(count_like=count_like+1));
					imgLike.setBackgroundResource(R.drawable.like_h);
					url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&cloth_type="+Cloth_type+"&status="+"LIKE";
					Constants.my_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
					Constants.my_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
		        }else
		        {
		        	txtLike.setText(String.valueOf(count_like=count_like-1));
		        	imgLike.setBackgroundResource(R.drawable.like);
		        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&cloth_type="+Cloth_type+"&status="+"UNLIKE";
		        	Constants.my_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
					Constants.my_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
		        }
				try
				{
					WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_detail_like,ProductDetailFragment.this);
					Log.d("Like URL= ",url.toString());
					webAPIHelper.execute(url);    	
				}
				catch(Exception e)
				{
					
				}
				
			}
		});
		imgComment.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Constants.CLOTHISID = String.valueOf(selected_item.getPIdCloth());
				Constants.CLOTH_USERID = String.valueOf(selected_item.getPUserId());
				Constants.OBJECT_ID = String.valueOf(selected_item.getPObjectId());
				Constants.CLOTH_TYPE= selected_item.getPClothType().toString().trim();
				Intent intent=new Intent(getActivity(),CommentViewActivity.class);
	   			startActivity(intent);
			}
		});
		imgShare.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				shareDialog.setVisibility(View.VISIBLE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_anim);
				anim.setFillAfter(true);
				shareDialog.startAnimation(anim);
				
				
				
			}
		});
	
	}
	public void setProductArray(int position)
	{
		SELECTED_PRODUCT=position;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		ImageView btnBack;
		btnBack=(ImageView)activity.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		
		super.onAttach(activity);
	}
	
	
	public void ProductDelete()
	{
		imgProductPhoto.setOnTouchListener(swipeDetector);
		imgProductPhoto.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{

				if(swipeDetector.swipeDetected())
				{
					
					Log.d("ProductDetailFragment", "Swipe detected");
					if(swipeDetector.mSwipeDetected==Action.RL)
					{
						lay_delete.setVisibility(View.VISIBLE);
						TranslateAnimation translateAnimation=new TranslateAnimation(100, 0, 0, 0);
						translateAnimation.setDuration(700);
						translateAnimation.setFillBefore(true);
						lay_delete.startAnimation(translateAnimation);
						
					}
					else if(swipeDetector.mSwipeDetected==Action.LR)
					{	
						lay_delete.setVisibility(View.GONE);
						TranslateAnimation translateAnimation=new TranslateAnimation(0, 100, 0, 0);
						translateAnimation.setDuration(700);
						translateAnimation.setFillBefore(true);
						lay_delete.startAnimation(translateAnimation);
					}
				}
				
			}
		});
		lay_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ProductDeletealert();
			}
		});
	}
	
	
 
	public void ProductDeletealert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 			builder.setMessage("Are you sure you want to delete item ?")
 			       .setCancelable(false)
 			       .setPositiveButton("YES", new DialogInterface.OnClickListener() 
 			       {
 			           public void onClick(DialogInterface dialog, int id) 
 			           {
 			        	 // Toast.makeText(getActivity(), "FDF",5000).show();

 			        	   	Constants.CLOTHISID = String.valueOf(selected_item.getPIdCloth());

 			        	    WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.produce_delete,ProductDetailFragment.this,"Please wait...");
 							String url = Constants.PRODUCT_DELETE_URL+"id_cloth="+Constants.CLOTHISID;
 							Log.d("Product Delete URL= ",url.toString());
 							webAPIHelper.execute(url);
 			        	   
 			        	   //http://dev.wardroba.com/serviceXml/product_delete.php?id_cloth=50

 			           }
 			       }).setNegativeButton("NO", new DialogInterface.OnClickListener() 
 			       {
 			           public void onClick(DialogInterface dialog, int id) 
 			           {
 			        	   dialog.dismiss();
 			           }
 			       });
 			AlertDialog alert = builder.create();
 			alert.show();
    } 
	
	public void CancelDialog()
	{
		btnCancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				shareDialog.setVisibility(View.GONE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_anim);
				anim.setFillBefore(true);
				shareDialog.startAnimation(anim);
			}
		});
	}
   public void FacebookSharing()
   {
		btnFacebook.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
					Toast.makeText(getActivity(), "Facebook", 5000).show();
			}
		});
   }
   
   public void TwitterSharing()
   {
		btnTwitter.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				
				shareDialog.setVisibility(View.GONE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_anim);
				anim.setFillBefore(true);
				shareDialog.startAnimation(anim);
				
				if (mTwitter.hasAccessToken())
					postMsgOnTwitter(shortDesc);
				else
				{
					mTwitter.authorize();
				}
			}
		});
   }
   public void PinterestSharing()
   {
		btnPinterest.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Pinterest", 5000).show();
			}
		});
   }
   public void TumblerSharing()
   {
		btnTumbler.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Tumbler", 5000).show();
			}
		});
   }
   public void GooglePlusSharing()
   {
		btnGooglePlus.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "GooglePlus", 5000).show();
			}
		});
   }
   class SwipeDetector implements View.OnTouchListener 
   {
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
   
 //Twitter
   private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) 
		{
			postMsgOnTwitter(shortDesc);
		}

		@Override
		public void onError(String value) {
			showToast("Twitter login failed");
			mTwitter.resetAccessToken();
		}
	};
   
	private void postMsgOnTwitter(final String msg) 
	{
		//showProgressDialog("Sending tweet..");
		mProgress.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try 
				{
					
					StatusUpdate status = new StatusUpdate(msg);
					
					URL url=new URL(imageUrl);
					InputStream is=url.openStream();
					
					status.setMedia(Uri.decode(shortDesc), is);
					
					mTwitter.updateStatus(status);
					
					showToast("Posted successfully.");
					mProgress.setVisibility(View.GONE);
					//hideProgressDialog();
					
				} catch (Exception e) 
				{
					//showToast("Oops!You have already twitted that.");
					mProgress.setVisibility(View.GONE);
					//hideProgressDialog();
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void showToast(final String msg) 
	{
		mHandler.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * This will shows a progress dialog with loading text, this is useful to
	 * call when some other functionality is taking place.
	 **/
	public void showProgressDialog(String msg) 
	{
		mProgress.setVisibility(View.VISIBLE);
		//runOnUiThread(new RunShowLoader(msg, false));
	}


	/**
	 * Implementing Runnable for runOnUiThread(), This will show a progress
	 * dialog
	 */
	class RunShowLoader implements Runnable 
	{
		private String strMsg;
		private boolean isCancalable;

		public RunShowLoader(String strMsg, boolean isCancalable) 
		{
			this.strMsg = strMsg;
			this.isCancalable = isCancalable;
		}

		@Override
		public void run() 
		{
			try 
			{
				if (progressdialog == null|| (progressdialog != null && !progressdialog.isShowing())) 
				{
					progressdialog = ProgressDialog.show(getActivity(),"", strMsg);
					progressdialog.setCancelable(isCancalable);
				}
			} catch (Exception e) 
			{
				progressdialog = null;
				e.printStackTrace();
			}
		}
	}

	/** For hiding progress dialog **/
	public void hideProgressDialog() 
	{
		//Toast.makeText(getActivity(), MSG, 5000).show();
		mProgress.setVisibility(View.GONE);
//		runOnUiThread(new Runnable() 
//		{
//			@Override
//			public void run() {
//				try {
//					if (progressdialog != null && progressdialog.isShowing()) 
//					{
//						progressdialog.dismiss();
//					}
//					progressdialog = null;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	
}
