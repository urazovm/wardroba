package com.example.wardroba;


import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import twitter4j.StatusUpdate;

import com.ImageLoader.ImageLoader;
import com.android.sot.twitter.TwitterApp;
import com.android.sot.twitter.TwitterApp.TwDialogListener;
import com.connection.Constants;
import com.connection.LoadingListHelper;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.example.wardroba.SmartImageView.onMyDoubleClickListener;
import com.pinterest.pinit.PinItButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class HomeActivityFragment extends Fragment 
{
	public LoadMoreListView lsvProductList;
	public ImageView imgUserPhoto;
	public TextView txtNameSurname,txtDate,txtSharLable;
	LinearLayout linOwnerHeader;
	Typeface tf;
	LinearLayout shareDialog;
	ImageView btnFacebook,btnTwitter,btnTumbler,btnGooglePlus;
	PinItButton btnPinterest;
	Button btnCancel;
	HomeProductBaseAdapter adapter=null;

	private int PAGE=1;
	String Sharing_Tag,Sharing_URL;
	String PINTEREST_CLIENT_ID = "1433818";

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
	
	///////////////////////////////////////////////
    public void setResponseFromRequest(int requestNumber) 
  	{		  		 		
  		if(Constants.all_items.size()>0)
  		{
  			adapter=new HomeProductBaseAdapter(HomeActivityFragment.this, shareDialog);
  			adapter.notifyDataSetChanged();
  			
 	    	lsvProductList.setAdapter(adapter);
 	    	lsvProductList.invalidateViews();
 	    	 
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			Toast.makeText(getActivity(), "No Record Found !", 5000).show();
  		}
  	} 	
  	public void setResponseOfLoadingMore(int requestNumber,int no_of_prod,String msg) 
  	{		  		 		
  		if(Constants.all_items.size()>0)
  		{
  			Log.i("HomeActivity", "Product  found on page:"+PAGE+" is = "+no_of_prod);
  			if(no_of_prod>0)
  			{
  				adapter=new HomeProductBaseAdapter(HomeActivityFragment.this, shareDialog);
  				adapter.notifyDataSetChanged();
  				lsvProductList.requestLayout();
  				lsvProductList.onLoadMoreComplete();
  			}
  			else
  			{
  				adapter.notifyDataSetChanged();
  				lsvProductList.requestLayout();
  				lsvProductList.onLoadMoreComplete();
  				lsvProductList.setHasMoreItems(false);
  			}
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			Toast.makeText(getActivity(), "No Record Found !", 5000).show();
  		}

  		
  		
 
  	} 	
  	public void setResponseFromRequest1(int requestNumber) 
  	{		
		if(adapter!=null)
		{
			adapter.notifyDataSetChanged();
		}
		if(Constants.my_items.size()>0)
			
		{
  			int cloth_id=Constants.all_items.get(Constants.SELECTED_ID).getPIdCloth();
			String status=Constants.all_items.get(Constants.SELECTED_ID).getPLikeStatus();
			int like_count=Constants.all_items.get(Constants.SELECTED_ID).getPLikeCount();
			int count=0;
  			if(Constants.my_items.size()>0)
  			{
  				for(WardrobaItem temp:Constants.my_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==cloth_id)
  						break;
  				
  					count++;	
  				}
  				
  			}
  			Constants.my_items.get(count).setPLikeStatus(status);
  			Constants.my_items.get(count).setPLikeCount(like_count);
		}
  	} 	
  	
  	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_activity, null);
        getActivity().findViewById(R.id.btnBackHome).setVisibility(View.GONE);
        tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        
        lsvProductList=(LoadMoreListView)root.findViewById(R.id.product_list);
 	     
 		btnFacebook=(ImageView)root.findViewById(R.id.btnFB);
 		btnTwitter=(ImageView)root.findViewById(R.id.btnTwitter);
 		btnPinterest=(PinItButton)root.findViewById(R.id.btnPinterest);
 		btnTumbler=(ImageView)root.findViewById(R.id.btnTumbler);
 		btnGooglePlus=(ImageView)root.findViewById(R.id.btnGplus);
 		btnCancel=(Button)root.findViewById(R.id.btnCancel);
 		txtSharLable=(TextView)root.findViewById(R.id.txt_share_lable);
 		mProgress = (ProgressBar)root.findViewById(R.id.progress_bar);
 		mProgress.setVisibility(View.GONE);
 	    shareDialog=(LinearLayout)root.findViewById(R.id.dialogShare);
 	    shareDialog.setVisibility(View.GONE);
 	    btnCancel.setTypeface(tf);
 	    txtSharLable.setTypeface(tf);
 	    lsvProductList.setDrawingCacheEnabled(true);
 	    lsvProductList.setOnLoadMoreListener(new OnLoadMoreListener() 
 	    {	
			@Override
			public void onLoadMore() 
			{
				LoadingListHelper webAPIHelper = new LoadingListHelper(Constants.product_list,HomeActivityFragment.this ,"Please Wait....");
				String url = Constants.HOME_PRODUCT_URL+"&id="+Constants.LOGIN_USERID+"&page="+(PAGE++);
				Log.d("Product_List= ",url.toString());
				webAPIHelper.execute(url);
			}
		});
 	    lsvProductList.setOnScrollListener(new OnScrollListener() 
		{
	        int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	        public void onScrollStateChanged(AbsListView view, int scrollState) 
	        {
	            mScrollState = scrollState;
	        }

	        @Override
	        public void onScroll(AbsListView list, int firstItem, int visibleCount, int totalCount) 
	        {
	            if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE)
	                return;

	            for (int i=0; i < visibleCount; i++) 
	            {
	                View listItem = list.getChildAt(i);
	                if (listItem == null)
	                    break;

	                linOwnerHeader = (LinearLayout) listItem.findViewById(R.id.header);

	                int topMargin = 0;
	                if (i == 0) 
	                {
	                    int top = listItem.getTop();
	                    int height = listItem.getHeight();
	                    if (top < 0) topMargin = linOwnerHeader.getHeight() < (top + height) ? -top : (height - linOwnerHeader.getHeight());
	                }

	                // set the margin.
	                if(linOwnerHeader!=null)
	                {
	                ((ViewGroup.MarginLayoutParams) linOwnerHeader.getLayoutParams()).topMargin = topMargin;
	                }
	                listItem.requestLayout();

	            }
	        }
	     });
 	  
	    
	    if(Constants.all_items.size()>0)
  		{
  			adapter=new HomeProductBaseAdapter(HomeActivityFragment.this, shareDialog);
  			adapter.notifyDataSetChanged();
  			
 	    	lsvProductList.setAdapter(adapter);
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  		}
	    

        ///////////////////////////////////// FOR TWITTER ////////////////////////////////////////////

		mHandler = new Handler();
		mTwitter = new TwitterApp(getActivity(), TWITTER_CONSUMER_KEY,TWITTER_SECRET_KEY);
		mTwitter.setListener(mTwLoginDialogListener);
	    
	    CancelSharDialog();
	    FacebookSharing();
	    TwitterSharing();
	    PinterestSharing();
	    TumblerSharing();
	    GooglePlusSharing();
	    
        return root;
    }
 
    public void onCreate(Bundle savedInstanceState) 
    {
	    super.onCreate(savedInstanceState);       
	    
	    if(isOnline()==true)
		{
			try
			{
				WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.product_list,HomeActivityFragment.this ,"Please Wait....");
				String url = Constants.HOME_PRODUCT_URL+"&id="+Constants.LOGIN_USERID+"&page="+(PAGE++);	
				Log.d("Product_List= ",url.toString());
				webAPIHelper.execute(url);    	
			}
			catch(Exception e)
			{
				
			}				
		}
		else
		{
			alert();
		}	    
	    
	    
    }
    @Override
    public void onAttach(Activity activity) 
    {
    	super.onAttach(activity);
    	try 
    	{
        	activity.findViewById(R.id.btnBackHome).setVisibility(View.GONE);	
        }
    	catch (ClassCastException e) 
        {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }
    
    public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}
        
    
   public void onPause() 
   {
	super.onPause();	
   } 
   
   public void onResume() 
   {
	super.onResume();
	//Twitter
	mHandler = new Handler();
	mTwitter = new TwitterApp(getActivity(), TWITTER_CONSUMER_KEY,TWITTER_SECRET_KEY);
	mTwitter.setListener(mTwLoginDialogListener);
	
	if(Constants.IS_PROFILE_CHANGED)
	{
		 if(isOnline()==true)
			{
				try
				{
					Constants.IS_PROFILE_CHANGED=false;
					WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.product_list,HomeActivityFragment.this ,"Please Wait....");
					String url = Constants.HOME_PRODUCT_URL+"&id="+Constants.LOGIN_USERID;	
					Log.d("Product_List= ",url.toString());
					webAPIHelper.execute(url);    	
				}
				catch(Exception e)
				{
					
				}				
			}
			else
			{
				alert();
			}
	}
	else if(adapter!=null)
	{
		adapter.notifyDataSetChanged();
		lsvProductList.refreshDrawableState();
	}
}
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Please check your internet connection...")
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
   
   public void CancelSharDialog()
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
					postMsgOnTwitter(Sharing_Tag);
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
			@SuppressWarnings("static-access")
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Pinterest", 5000).show();
		 		btnPinterest.setPartnerId(PINTEREST_CLIENT_ID); // required
		 		btnPinterest.setDebugMode(true); // optional
				btnPinterest.setImageUrl(Sharing_URL);
				btnPinterest.setUrl("http://dev.wardroba.com/"); // optional
				btnPinterest.setDescription(Sharing_Tag);
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
   
   //Twitter
   private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) 
		{
			postMsgOnTwitter(Sharing_Tag);
		}

		@Override
		public void onError(String value) {
			showToast("Twitter login failed");
			mTwitter.resetAccessToken();
		}
	};
   
	private void postMsgOnTwitter(final String msg) 
	{
		showProgressDialog("Sending tweet..");

		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try 
				{
					
					StatusUpdate status = new StatusUpdate(msg);
					
					URL url=new URL(Sharing_URL);
					InputStream is=url.openStream();
					
					status.setMedia(Uri.decode(Sharing_Tag), is);
					
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
	
	
//////////////////////////////////////////////////	


	class HomeProductBaseAdapter extends BaseAdapter 
   {
		
   	LayoutInflater mInflater;
   	
   	LinearLayout SharDialog;
   	ImageLoader imageLoader;
   	public GroupItem item ;
   	Typeface tf;
   	HomeActivityFragment mContext;
   	
   	String CommentId;
   	WardrobaItem wardrobaItem;

    HomeProductBaseAdapter(HomeActivityFragment context , LinearLayout dialog1)
   	{
   		this.mContext=context;
   		this.SharDialog=dialog1;
   		mInflater = LayoutInflater.from(mContext.getActivity());
   		imageLoader=new ImageLoader(mContext.getActivity());
   		imageLoader.clearCache();
   		
   		tf= Typeface.createFromAsset(mContext.getActivity().getAssets(),"fonts/GOTHIC.TTF");
   	    
   	}
   	public int getCount()
   	{
   		return Constants.all_items.size();
   	}

   	public Object getItem(int position)  
   	{
   		return null;
   	}
   	public long getItemId(int position) 
   	{
   		// TODO Auto-generated method stub
   		return position;
   	}
   	 class GroupItem
   	{
   		public ImageView imgUserPhoto;
   		public TextView txtNameSurname,txtDate;
   		public LinearLayout linOwnerHeader;
   	}
   	
   	public View getView(final int position, View convertView, ViewGroup parent) 
   	{
   		 	
   		 	View vi=convertView;
   			
   			
	   			vi=mInflater.inflate(R.layout.home_activity_lay,null);
	   			item =new GroupItem();
	   			item.linOwnerHeader = (LinearLayout)vi.findViewById(R.id.header);
	   			item.imgUserPhoto=(ImageView)vi.findViewById(R.id.img_user_photo);
	   		    item.txtNameSurname=(TextView)vi.findViewById(R.id.txt_name_surname);
	   		    item.txtDate=(TextView)vi.findViewById(R.id.txt_date);
	   		    
	   			vi.setTag(item);
   			
   			
   			
   	        final TextView txtLikeCount,txtCommentCount,txtShortDiscription;
 		 	 SmartImageView imgProductImage;
 		 	  //int like_count=0;
 		 	
 		 	final ProgressBar progressBar,progressBarUserPhoto;
 		 	final ImageView btnLike,btnComment,btnShare;
 		 	final ImageView imgLikeDil;
 		 	final WardrobaItem wardrobaItem=Constants.all_items.get(position);
   						
   			
   			txtLikeCount =(TextView) vi.findViewById(R.id.txt_like);
   			txtCommentCount =(TextView) vi.findViewById(R.id.txt_comment);
   			txtShortDiscription =(TextView) vi.findViewById(R.id.txt_short_description);
   			imgProductImage =(SmartImageView) vi.findViewById(R.id.img_product_photo);
   			
   			
   			imgLikeDil =(ImageView) vi.findViewById(R.id.img_like_dil);
   			progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
   			progressBarUserPhoto=(ProgressBar)vi.findViewById(R.id.progressBar2);
   			btnLike =(ImageView) vi.findViewById(R.id.img_like);
   			btnComment =(ImageView) vi.findViewById(R.id.img_comment);
   			btnShare =(ImageView) vi.findViewById(R.id.img_share);
   			
   			txtLikeCount.setTypeface(tf);
   			txtCommentCount.setTypeface(tf);
   			txtShortDiscription.setTypeface(tf);
   			item.txtNameSurname.setTypeface(tf);
   			item.txtDate.setTypeface(tf);
   			imgLikeDil.setVisibility(View.GONE);

   			item.txtNameSurname.setText(wardrobaItem.getPUserName().toString().trim());
   			item.txtDate.setText(wardrobaItem.getPUserDate().toString().trim());
   			String temp=wardrobaItem.getPImageUrl();
   			
   				//Log.d("HomeActivity", "My Product image:"+URLEncoder.encode(temp.trim()));
   			
   				progressBar.setVisibility(View.GONE);
   				imageLoader.DisplayImage(temp,imgProductImage,progressBar);
   				
   				if(item.imgUserPhoto!=null)
   				{
   					progressBarUserPhoto.setVisibility(View.GONE);
   					imageLoader.DisplayImage(wardrobaItem.getPUserImage().toString().trim(),item.imgUserPhoto,progressBarUserPhoto);
   				 //new ImageDownloaderTask(item.imgUserPhoto,progressBarUserPhoto).execute(wardrobaItem.getPUserImage());
   				}
   				   			
   			txtLikeCount.setText(String.valueOf(wardrobaItem.getPLikeCount()));
   			txtCommentCount.setText(String.valueOf(wardrobaItem.getPCommentCount()));
   			String tag1="",price="",discount="",tags="",shortDiscription = "";
   			
   			if(wardrobaItem.getPTag1()!=null)
   			{
   			tag1=wardrobaItem.getPTag1().toString().trim()+" ";
   			price=wardrobaItem.getPPrice().toString().trim()+" ";
			tags=" "+wardrobaItem.getPTag().toString().trim();
   			discount=wardrobaItem.getPDiscountedPrice().toString().trim();
   			shortDiscription=tag1+price+discount+tags;
   			StrikethroughSpan strikethroughSpan=new StrikethroughSpan();
   			int len2=tag1.length()+price.length();
   			Spannable priceSpan=new SpannableString(shortDiscription);
   			if(discount.length()>0)
   			{
					priceSpan.setSpan(strikethroughSpan, len2, len2+discount.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
   					txtShortDiscription.setText(priceSpan);
   			}
   			else
   			{
   				txtShortDiscription.setText(shortDiscription);
   			}
   			}
   			
   			String Status=wardrobaItem.getPLikeStatus().toString().trim();
   			if(Status.equals("LIKE"))
   	        {
   				btnLike.setBackgroundResource(R.drawable.like);
   	        }else
   	        {
   	        	btnLike.setBackgroundResource(R.drawable.like_h);
   	        }
   			
   			 imgProductImage.callback=new onMyDoubleClickListener() {
   					
   					@Override
   					public void setLikeProduct() {
   						// TODO Auto-generated method stub
   						
   						
   						String LikeStatus;
   						//Toast.makeText(activity, "hello like count:"+like_count++,Toast.LENGTH_SHORT).show();
   						Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
   						Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
   						Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
   						Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
   						//Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
   						
   						LikeStatus = wardrobaItem.getPLikeStatus().toString().trim();
   						//Log.d("BaseAdapter", "Like status:"+LikeStatus.toString());

   						Constants.SELECTED_ID=position;
   						int count_like = (wardrobaItem.getPLikeCount());
   						
   						String url="";
   						if(LikeStatus.equals("LIKE"))
   				        {
   							txtLikeCount.setText(String.valueOf(count_like=count_like+1));
   							btnLike.setBackgroundResource(R.drawable.like_h);
   							url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"LIKE";
   							try
   							{
   								WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,mContext);
   								//Log.d("Like URL= ",url.toString());
   								webAPIHelper.execute(url);    
   								imgLikeDil.setVisibility(View.VISIBLE);
   								Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.fade_in);
   								//anim.setFillAfter(true);
   								//anim.setRepeatMode(Animation.REVERSE);
   								//callback.setLikeProduct();
   								imgLikeDil.startAnimation(anim);
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
   										
   										imgLikeDil.setVisibility(View.GONE);
   										Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.fade_out);
   										
   										//anim.setRepeatMode(Animation.REVERSE);
   										imgLikeDil.startAnimation(anim);
   									}
   								});
   							}
   							catch(Exception e)
   							{
   								
   							}
   					    }
   						else
   						{
   							Toast.makeText(mContext.getActivity(), "already like", Toast.LENGTH_SHORT).show();
   						}
   						
   					}
   				};

   			
   			btnLike.setOnClickListener(new View.OnClickListener() 
   			{
   				public void onClick(View v) 
   				{
   					
   							// TODO Auto-generated method stub
   								String LikeStatus;
   								String url;
   								Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
   								Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
   								Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
   								Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
   								//Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
   								
   								LikeStatus = wardrobaItem.getPLikeStatus().toString().trim();
   								//Log.d("BaseAdapter", "Like status:"+LikeStatus.toString());

   								Constants.SELECTED_ID=position;
   								int count_like = (wardrobaItem.getPLikeCount());
   						
   								if(LikeStatus.equals("LIKE"))
   						        {
   									txtLikeCount.setText(String.valueOf(count_like=count_like+1));
   									btnLike.setBackgroundResource(R.drawable.like_h);
   									url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"LIKE";
   									Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
   									Constants.all_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
   						        }else
   						        {
   						        	txtLikeCount.setText(String.valueOf(count_like=count_like-1));
   						        	btnLike.setBackgroundResource(R.drawable.like);
   						        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"UNLIKE";
   						        	Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("LIKE");
   						        	Constants.all_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
   						        }
   								try
   								{
   									 
   									
   									WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,mContext);
   									Log.d("Like URL= ",url.toString());
   									webAPIHelper.execute(url);    	
   								}
   								catch(Exception e)
   								{
   									
   								}	
   				}
   			});
   		
   		
   			btnComment.setOnClickListener(new View.OnClickListener() 
   			{
   				public void onClick(View v) 
   				{
   					
   					Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
   					Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
   					Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
   					Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
   					//Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID.toString());
   					//Log.d("BaseAdapter", "Object ID:"+String.valueOf(Constants.all_items.get(position).getPObjectId()));
   					Intent intent=new Intent(mContext.getActivity(),CommentViewActivity.class);
   					mContext.startActivity(intent);
   				}
   			});
   		
   			
   			btnShare.setOnClickListener(new View.OnClickListener() 
   			{
   				public void onClick(View v) 
   				{
   					Sharing_Tag=txtShortDiscription.getText().toString();
   					Sharing_URL=String.valueOf(wardrobaItem.getPImageUrl().toString().trim());
   					
   					SharDialog.setVisibility(View.VISIBLE);
   					Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.slide_up_anim);
   					anim.setFillAfter(true);
   					SharDialog.startAnimation(anim);
   				}
   			});
   			imgProductImage.setDilImage(imgLikeDil);
   			
               item.linOwnerHeader.setOnClickListener(new View.OnClickListener() 
           	{
       			public void onClick(View v)
       			{
       				Constants.OWNERID= wardrobaItem.getPOwnerId();
       				ProfileOwnerViewFragment secondFragment=new ProfileOwnerViewFragment();
    				
    	
    				FragmentTransaction transaction = getFragmentManager().beginTransaction();
    	            transaction.replace(R.id.home_fragment_container, secondFragment);
    	            transaction.addToBackStack(null);

    	            // Commit the transaction
    	            transaction.commit();
       			}
       		});
   			

   		return vi;	
   	}	
   	
   	class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
   	    private final WeakReference<ImageView> imageViewReference;
   	    ProgressBar mProgressBar;
   	    public ImageDownloaderTask(ImageView imageView,ProgressBar progressBar) {
   	    	this.mProgressBar=progressBar;
   	        imageViewReference = new WeakReference(imageView);
   	    }
   	 
   	    @Override
   	    // Actual download method, run in the task thread
   	    protected Bitmap doInBackground(String... params) {
   	        // params comes from the execute() call: params[0] is the url.
   	        return downloadBitmap(params[0]);
   	    }
   	 
   	    @Override
   	    // Once the image is downloaded, associates it to the imageView
   	    protected void onPostExecute(Bitmap bitmap) {
   	        if (isCancelled()) {
   	            bitmap = null;
   	        }
   	 
   	        if (imageViewReference != null) {
   	            ImageView imageView = imageViewReference.get();
   	            if (imageView != null) {
   	 
   	                if (bitmap != null) {
   	                    imageView.setImageBitmap(bitmap);
   	                    mProgressBar.setVisibility(View.GONE);
   	                } else {
   	                   mProgressBar.setVisibility(View.VISIBLE);
   	                }
   	            }
   	 
   	        }
   	    }
   	 
   	}
   	
   	 Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }
 
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }


   	
       
   	
   	


   	
        
   }
}
