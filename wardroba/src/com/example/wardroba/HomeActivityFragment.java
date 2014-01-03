package com.example.wardroba;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.StatusUpdate;

import com.ImageLoader.ImageLoader;
import com.ImageLoader.Utils;
import com.android.sot.twitter.TwitterApp;
import com.android.sot.twitter.TwitterApp.TwDialogListener;
import com.connection.Constants;
import com.connection.LoadingListHelper;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.example.wardroba.SmartImageView.onMyDoubleClickListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.android.gms.plus.PlusShare;
import com.pinterest.pinit.PinIt;
import com.pinterest.pinit.PinItButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;

public class HomeActivityFragment extends Fragment 
{
	public LoadMoreListView lsvProductList;
	public ImageView imgUserPhoto;
	public TextView txtNameSurname,txtDate,txtSharLable;
	LinearLayout linOwnerHeader;
	Typeface tf;
	LinearLayout shareDialog;
	ImageView btnFacebook,btnTwitter,btnTumbler,btnGooglePlus,btnPinterest;
	
	Button btnCancel;
	HomeProductBaseAdapter adapter=null;

	private int PAGE=1;

	// facebook sharing...
	SharedPreferences preferences;
	public static final String APP_ID = "335736383227858";
	Facebook facebook;
  	AsyncFacebookRunner asyncRunner;
  	String[] permissions = { "email", "offline_access","publish_actions", "publish_stream", "user_photos", "publish_checkins","photo_upload","user_birthday","user_relationship_details"};
  	private static String ALBUM_NAME="wardroba";
  	ProgressDialog progressDialog;
  	///  **************************
  	// Tumblr sharing....
  	private static final String TAG = "TumblrDemo";
  	private static final String REQUEST_TOKEN_URL = "https://www.tumblr.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "https://www.tumblr.com/oauth/access_token";
    private static final String AUTH_URL = "https://www.tumblr.com/oauth/authorize";

    // Taken from Tumblr app registration
    public static final String CONSUMER_KEY = "sjxqXmPAI4UEDylObEXPMSlE8jIeEZlyFPaa4yTq8hoMdYomPT";
	public static final String CONSUMER_SECRET = "Ba1cyBnsdf5wzB6UEDXNrSK6rQYfVtn8L86YJYT1x65Hl98hrL";

    public static final String	OAUTH_CALLBACK_SCHEME	= "oauthflow-tumblr";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	
	public static String TUMBLR_ACCESS_TOKEN="tumblr_access_token";
	public static String TUMBLR_TOKEN_SECRET="tumblr_token_secret";
	private String TUMBLR_USER_INFO_URI="http://api.tumblr.com/v2/user/info";
	private String TUMBLR_POST_PHOTO_URI="http://api.tumblr.com/v2/blog/";
	private static String PINTEREST_APP_URL="market://details?id=com.pinterest";
	String authUrl,token,secret;
	CommonsHttpOAuthProvider provider;
	CommonsHttpOAuthConsumer consumer;
	
	Dialog tumblrDialog;

  	///
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
	private LinearLayout mProgress;
	
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
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        progressDialog=new ProgressDialog(getActivity());
        lsvProductList=(LoadMoreListView)root.findViewById(R.id.product_list);
 		btnFacebook=(ImageView)root.findViewById(R.id.btnFB);
 		btnTwitter=(ImageView)root.findViewById(R.id.btnTwitter);
 		btnPinterest=(ImageView)root.findViewById(R.id.btnPinterest);
 		btnTumbler=(ImageView)root.findViewById(R.id.btnTumbler);
 		btnGooglePlus=(ImageView)root.findViewById(R.id.btnGplus);
 		btnCancel=(Button)root.findViewById(R.id.btnCancel);
 		txtSharLable=(TextView)root.findViewById(R.id.txt_share_lable);

 		mProgress = (LinearLayout)root.findViewById(R.id.progress_bar);
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
	    initFacebook();
	    initTumblr();
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
	else if (Constants.IS_PRODUCT_ADDED) 
	{
		if(isOnline()==true)
		{
			try
			{
				Constants.IS_PRODUCT_ADDED=false;
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
   public void initTumblr()
   {
	   
	    if(progressDialog!=null)
		    progressDialog.setMessage("Loading...");
        consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,CONSUMER_SECRET);
        provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL,ACCESS_TOKEN_URL,AUTH_URL);
		token = preferences.getString(TUMBLR_ACCESS_TOKEN, "");
		secret = preferences.getString(TUMBLR_TOKEN_SECRET, "");
       
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
   @SuppressWarnings("deprecation")
	private void initFacebook()
	{
		facebook=new Facebook(APP_ID);
		asyncRunner=new AsyncFacebookRunner(facebook);
	}
   public void FacebookSharing()
   {
		btnFacebook.setOnClickListener(new View.OnClickListener() 
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) 
			{
				btnCancel.performClick();
				String access_token = preferences.getString("access_token", null);
				mProgress.setVisibility(View.VISIBLE);
			    long expires = preferences.getLong("access_expires", 0);
			    if (access_token != null) 
			    {
			        facebook.setAccessToken(access_token);
			    }
			 
			    if (expires != 0) 
			    {
			        facebook.setAccessExpires(expires);
			    }
				if(!facebook.isSessionValid())
				{
					facebook.authorize(getActivity(), permissions, Facebook.FORCE_DIALOG_AUTH, new Facebook.DialogListener() {
					
						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "FB Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "Dailog Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onComplete(Bundle values) {
							// TODO Auto-generated method stub
							//Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
							SharedPreferences.Editor editor = preferences.edit();
	                        editor.putString("access_token",facebook.getAccessToken());
	                        editor.putLong("access_expires",facebook.getAccessExpires());
	                        editor.commit();
							createNewFacebookAlbum();
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
						}
					});
				}
				else
				{
					createNewFacebookAlbum();
				}
			}
		});
   }
   @SuppressWarnings("deprecation")
   public void createNewFacebookAlbum()
   {
	   final Bundle params = new Bundle();
	   
	   /*progressDialog.setMessage("Creating album");
	    progressDialog.show();*/
	   Toast.makeText(getActivity(), "Uploading photo", Toast.LENGTH_SHORT).show();
	   asyncRunner.request("me/albums", params, "GET", new RequestListener() {
		
		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
			//progressDialog.dismiss();
			try {
				boolean flag=false;
				JSONObject jsonObject=Util.parseJson(response);
				JSONArray jsonArray=(JSONArray)jsonObject.getJSONArray("data");
				String albumName="",albumID="";
				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject object=jsonArray.getJSONObject(i);
					Log.d("HomeActivity", "ID:"+object.getString("id"));
					Log.d("HomeActivity", "Name:"+object.getString("name"));
					albumName=object.getString("name");
					albumID=object.getString("id");
					if(albumName.equals(ALBUM_NAME))
					{
						
						flag=true;
						break;
					}
				}
				if(flag==true)
				{
					uploadPhoto(albumID);
				}
				else
				{
					createNewAlbum();
				}
				
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}, null);
	
   }
   @SuppressWarnings("deprecation")
public void createNewAlbum()
   {
	   final Bundle params = new Bundle();
	   params.putString("name", ALBUM_NAME);
	   params.putString("message", "Wardroba store product sharing album");
	  /* getActivity().runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			progressDialog.setMessage("Creating album...");
			   progressDialog.show();
		}
	});*/
	   
	   asyncRunner.request("me/albums", params,"POST",new RequestListener() {
		
		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
			//progressDialog.dismiss();
			Log.d("HomeActivity", "Create new album ---> Response:"+response);
			try {
				String albumID=Util.parseJson(response).getString("id");
				if(albumID!=null)
					uploadPhoto(albumID);
				else
					Log.d("HomeActivity", "Photo upload fail");
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	},null);
   }
   @SuppressWarnings("deprecation")
   public void uploadPhoto(String albumID)
   {
	   Bundle params = new Bundle();
	   params.putString("url", Sharing_URL);
	   params.putString("message", Sharing_Tag);
	  
	   /*getActivity().runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 progressDialog.setMessage("Uploading photo to album");
			    progressDialog.show();
		}
	});*/
	  
	   asyncRunner.request(albumID+"/photos", params, "POST", new RequestListener() {
		
		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
		
		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
			//progressDialog.dismiss();
			
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mProgress.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "Photo posted", Toast.LENGTH_SHORT).show();
				}
			});
			
			Log.d("HomeActivity", "Photo upload successfully response:---->"+response);
		}
	}, null);
   }
   public void TwitterSharing()
   {
		btnTwitter.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				btnCancel.performClick();
				mProgress.setVisibility(View.VISIBLE);
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
				//Toast.makeText(getActivity(), "Pinterest", 5000).show();
				btnCancel.performClick();
				mProgress.setVisibility(View.VISIBLE);
				if(isPackageInstalled("com.pinterest", getActivity()))
				{
					new ImageSaveTask().execute();
				}
				else
				{
					Toast.makeText(getActivity(), "Pintrest is not installed on your device.Install first to start sharing.", Toast.LENGTH_SHORT).show();
		        	Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(PINTEREST_APP_URL));
		        	//i.setData(Uri.parse(PINTEREST_APP_URL));
		        	startActivity(i);
				}
			}
		});
   }
	class ImageSaveTask extends AsyncTask<String, Void, String>
	{

		
		File myFile;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			 //myFile=downloadAndSaveImage();
			
			 try {
		            Bitmap bitmap=null;
		            String folderPath=Environment.getExternalStorageDirectory().toString()+"/wardroba";
		            File f=new File(folderPath);
		            f.mkdir();
		            myFile=new File(f+"/temp.png");
		            Log.d(TAG, "File create url:"+myFile);
		            URL imageUrl = new URL(Sharing_URL.trim());
		            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
		           // conn.setConnectTimeout(30000);
		           // conn.setReadTimeout(30000);
		           // conn.setInstanceFollowRedirects(true);
		            InputStream is=conn.getInputStream();
		            FileOutputStream os = new FileOutputStream(myFile);
		            //
		           // conn.disconnect();
		            bitmap = BitmapFactory.decodeStream(is);
		            
		            bitmap =Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
		            bitmap.compress(CompressFormat.PNG, 50, os);
		            
		            /*os.flush();
		            os.close();
		            is.close();*/
		            //conn.disconnect();
		            
		        } 
			 
			 	catch(IOException io)
			 	{
			 		io.printStackTrace();
			 	}
			 	catch (Exception ex)
		        {
		        	ex.printStackTrace();
		           
		        }
			 
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgress.setVisibility(View.GONE);
			if(myFile!=null)
			{
				Intent share = new Intent(android.content.Intent.ACTION_SEND);
	            share.setType("image/*");
	             share.putExtra(Intent.EXTRA_TEXT, "test");
	             
	             share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + myFile));
	             share.setPackage("com.pinterest");
	             
	             startActivity(share);
	             
			}
		}
	}
	
	public File downloadAndSaveImage()
	{
			File file=null;
		 try {
	            Bitmap bitmap=null;
	            String folderPath=Environment.getExternalStorageDirectory().toString()+"/wardroba";
	            File f=new File(folderPath);
	            f.mkdir();
	            file=new File(folderPath+"/temp.png");
	            Log.d(TAG, "File create url:"+file);
	            URL imageUrl = new URL(Sharing_URL.trim());
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(30000);
	            conn.setReadTimeout(30000);
	            conn.setInstanceFollowRedirects(true);
	            InputStream is=conn.getInputStream();
	            FileOutputStream os = new FileOutputStream(file);
	            //conn.disconnect();
	            bitmap = BitmapFactory.decodeStream(is);
	            bitmap.compress(CompressFormat.PNG, 80, os);
	            
	            os.flush();
	            os.close();
	            is.close();
	            //conn.disconnect();
	            
	        } 
		 	catch (Exception ex)
	        {
	        	ex.printStackTrace();
	           
	        }
		 return file;
	}
	 private boolean isPackageInstalled(String packagename, Context context) {
		    PackageManager pm = context.getPackageManager();
		    try {
		        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
		        return true;
		    } catch (NameNotFoundException e) {
		        return false;
		    }
		}
   public void TumblerSharing()
   {
		btnTumbler.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				btnCancel.performClick();
				//Toast.makeText(getActivity(), "Tumbler", 5000).show();
				mProgress.setVisibility(View.VISIBLE);
				if(token.equals("") && secret.equals(""))
				{
					loginToTumblr();
				}
				else
				{
					new TumblrPostAsync().execute();
				}
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
				btnCancel.performClick();
				//Toast.makeText(getActivity(), "GooglePlus", 5000).show();
				Intent shareIntent = new PlusShare.Builder(getActivity())
					
		          .setText(Sharing_Tag)
		          .setType("image/*")
		          .setContentUrl(Uri.parse(Sharing_URL))
		          
		          .getIntent();
				
		      startActivityForResult(shareIntent, 0);
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
   

   
   public void loginToTumblr()
   {
   	consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,CONSUMER_SECRET);
       provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL,ACCESS_TOKEN_URL,AUTH_URL);
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					authUrl = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d(TAG, "Login url:"+authUrl);
						showLoginDialog(authUrl);
					}
				});
				 
				 //startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)), 1);
			}
		}).start();
   }
   @SuppressLint("SetJavaScriptEnabled")
	public void showLoginDialog(String url)
   {
   	
			
		
				// TODO Auto-generated method stub
				 tumblrDialog=new Dialog(getActivity());
				 tumblrDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 tumblrDialog.getWindow().setBackgroundDrawableResource(R.drawable.border);
				 tumblrDialog.setCancelable(false);
				 float scale = getActivity().getResources().getDisplayMetrics().density;
				 LayoutParams layoutParams=new LayoutParams((int)(scale*300+0.5f), (int)(scale*420+0.5f));
				 
				 tumblrDialog.setTitle("Tumbler");
				 //LayoutParams layoutParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				 LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 View v = inflater.inflate(R.layout.dialog, null);
				// v.setLayoutParams(layoutParams);
				tumblrDialog.setContentView(v);
			
		    	WebView webView=(WebView)tumblrDialog.findViewById(R.id.webView);
		    	webView.setWebViewClient(new TumblrWebClient());
		    	webView.getSettings().setJavaScriptEnabled(true);
		    	webView.setLayoutParams(layoutParams);
		    	webView.loadUrl(url);
		    	tumblrDialog.show();
		    	
			
   }
   
   private class TumblrWebClient extends WebViewClient
   {
   	@Override
   	public boolean shouldOverrideUrlLoading(WebView view, String url) {
   		// TODO Auto-generated method stub
   		
   		if (url.startsWith(OAUTH_CALLBACK_URL)) 
   		{
               tumblrLoginListener.onComplete(url);
               tumblrDialog.dismiss();
               return true;
           } else if (url.startsWith("authorize")) {
               return true;
           }
           return super.shouldOverrideUrlLoading(view, url);
   	}
   	@Override
   	public void onPageStarted(WebView view, String url, Bitmap favicon) {
   		// TODO Auto-generated method stub
   		super.onPageStarted(view, url, favicon);
   		if(!progressDialog.isShowing())
   			progressDialog.show();
   	}
   	@Override
   	public void onPageFinished(WebView view, String url) {
   		// TODO Auto-generated method stub
   		super.onPageFinished(view, url);
   		Log.e(TAG, "On page finish");
   		if(progressDialog.isShowing())
   			progressDialog.dismiss();
   	}
   	@Override
   	public void onReceivedError(WebView view, int errorCode,
   			String description, String failingUrl) {
   		// TODO Auto-generated method stub
   		super.onReceivedError(view, errorCode, description, failingUrl);
   		
   		Toast.makeText(getActivity(), "Error:"+description, Toast.LENGTH_SHORT).show();
   	}
   }
   TumblrLoginListener tumblrLoginListener=new TumblrLoginListener() {
		
		@Override
		public void onError(String error) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(String url) {
			// TODO Auto-generated method stub
			processToken(url);
			//new TumblrPostAsync().execute();
		}
	};
	 public void processToken(String callbackUrl) {
	        
	        final String verifier = getVerifier(callbackUrl);

	        new Thread() {
	            @Override
	            public void run() {
	                

	                try {
	                    provider.retrieveAccessToken(consumer,
	                            verifier);
	                    
	                    
	                       token=consumer.getToken();
	                       secret=consumer.getTokenSecret();
	                       Log.e(TAG, "Token:"+token);
	                       Log.e(TAG, "secret:"+secret);
	                       Editor edit=preferences.edit();
	                       edit.putString(TUMBLR_ACCESS_TOKEN, token);
	                       edit.putString(TUMBLR_TOKEN_SECRET, secret);
	                       edit.commit();
	                       getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								new TumblrPostAsync().execute();
							}
						});
	                       
	                } 
	                catch (Exception e) 
	                {
	                    e.printStackTrace();
	                }

	                
	            }
	        }.start();
	    }
	 
	 private String getVerifier(String callbackUrl) {
	        String verifier = "";

	        try {
	            callbackUrl = callbackUrl.replace(OAUTH_CALLBACK_SCHEME, "http");

	            URL url = new URL(callbackUrl);
	            String query = url.getQuery();

	            String array[] = query.split("&");

	            for (String parameter : array) {
	                String v[] = parameter.split("=");

	                if (URLDecoder.decode(v[0]).equals(
	                        oauth.signpost.OAuth.OAUTH_VERIFIER)) {
	                    verifier = URLDecoder.decode(v[1]);
	                    break;
	                }
	            }
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        }

	        return verifier;
	    }
	 
	class TumblrPostAsync extends AsyncTask<String, Void, String>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(getActivity(), "Photo posting", Toast.LENGTH_SHORT).show();
			/*progressDialog.setMessage("Photo posting to Tumblr...");
			progressDialog.show();*/
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			 
            try 
            {
           	 consumer.setTokenWithSecret(token, secret);
           	 HttpGet httpGet=new HttpGet(TUMBLR_USER_INFO_URI);
           	 consumer.sign(httpGet);
           	 DefaultHttpClient client = new DefaultHttpClient();
      		   	 HttpResponse resp = null;
      		   	 resp = client.execute(httpGet);
      		   	 String response=EntityUtils.toString(resp.getEntity());
      		   	 Log.d(TAG,"response"+response);
      		   	 JSONObject jsonObject=new JSONObject(response);
      		   	 JSONObject jsonReponse=jsonObject.getJSONObject("response");
      		   	 JSONObject userJson=jsonReponse.getJSONObject("user");
      		   	 String username=userJson.getString("name");
      		   	 Log.d(TAG,"Username:"+username);
      		   	 JSONArray blogs=userJson.getJSONArray("blogs");
      		   	 String blogName = null,blogUrl,blogTitle;
      		   	 if(blogs.length()>0)
    			 {
    				JSONObject blogObject=(JSONObject)blogs.get(0);
    				blogName=blogObject.getString("name");
    				blogUrl=blogObject.getString("url");
    				blogTitle=blogObject.getString("title");
    				Log.d(TAG,"Blog name:"+blogName);
    				Log.d(TAG,"Blog url:"+blogUrl);
    				Log.d(TAG,"Blog Title:"+blogTitle);
    			 }
    			
    			
    			HttpPost hpost = new HttpPost(TUMBLR_POST_PHOTO_URI + blogName + ".tumblr.com/post");
    			Log.d(TAG, "Post to blog:"+TUMBLR_POST_PHOTO_URI + blogName + ".tumblr.com/post");
    			
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    			nameValuePairs.add(new BasicNameValuePair(URLEncoder.encode("type", "UTF-8"),URLEncoder.encode("photo", "UTF-8")));
    			Log.e("Tumblr", "Image shareing file path" + Sharing_URL);
    			nameValuePairs.add(new BasicNameValuePair("caption", Html.toHtml(new SpannableString(Sharing_Tag))));
    			
    			nameValuePairs.add(new BasicNameValuePair("source", Sharing_URL.trim()));
    			hpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    			
      			consumer.sign(hpost);
      			
      			resp=client.execute(hpost);
      			
      			String postResponse=EntityUtils.toString(resp.getEntity());
      			Log.d(TAG, "Photo post response:"+postResponse);
    			
			 } 
            catch (OAuthMessageSignerException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 } 
            catch (OAuthExpectationFailedException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
            catch (OAuthCommunicationException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
            catch (ClientProtocolException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
            catch (IOException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 } 
            catch (JSONException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
            
            
    		  
    			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//progressDialog.dismiss();
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mProgress.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "Photo posted", Toast.LENGTH_SHORT).show();
				}
			});
			
		}
	}
   
   
   
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
					
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showToast("Posted successfully.");
							mProgress.setVisibility(View.GONE);
						}
					});
					//hideProgressDialog();
					
				} catch (Exception e) 
				{
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showToast("Oops!You have already twitted that.");
							mProgress.setVisibility(View.GONE);
						}
					});
					
					
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
   			if(wardrobaItem!=null)
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
   	
        
   }
}
