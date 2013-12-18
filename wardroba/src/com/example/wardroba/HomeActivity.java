package com.example.wardroba;


import java.util.ArrayList;

import com.connection.Constants;
import com.connection.WebAPIHelper;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class HomeActivity extends Activity
{
	public ListView lsvProductList;
	public ImageView imgUserPhoto;
	public TextView txtNameSurname,txtDate,txtSharLable;
	Typeface tf;
	LinearLayout shareDialog;
	ImageView btnFacebook,btnTwitter,btnPinterest,btnTumbler,btnGooglePlus;
	Button btnCancel;
	HomeProductBaseAdapter adapter=null;
	
	
  	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest(int requestNumber) 
  	{		  		 		
  		if(Constants.all_items.size()>0)
  		{
//  			txtNameSurname.setText(Constants.USER_NAME.toString());
//  			txtDate.setText(Constants.USER_DATE.toString());
  			adapter=new HomeProductBaseAdapter(HomeActivity.this, shareDialog);
  			adapter.notifyDataSetChanged();
  			
 	    	lsvProductList.setAdapter(adapter);
 	    	lsvProductList.invalidateViews();
 	    	
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  		}
  	} 	
  	@SuppressWarnings("unchecked")
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
    public void onCreate(Bundle savedInstanceState) 
    {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);       
	    setContentView(R.layout.home_activity);
	 	 
	    lsvProductList=(ListView)findViewById(R.id.product_list);
	    tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
	   
	    
		btnFacebook=(ImageView)findViewById(R.id.btnFB);
		btnTwitter=(ImageView)findViewById(R.id.btnTwitter);
		btnPinterest=(ImageView)findViewById(R.id.btnPinterest);
		btnTumbler=(ImageView)findViewById(R.id.btnTumbler);
		btnGooglePlus=(ImageView)findViewById(R.id.btnGplus);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		txtSharLable=(TextView)findViewById(R.id.txt_share_lable);

	    shareDialog=(LinearLayout)findViewById(R.id.dialogShare);
	    shareDialog.setVisibility(View.GONE);
	    btnCancel.setTypeface(tf);
	    txtSharLable.setTypeface(tf);
	    
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

	                LinearLayout title = (LinearLayout) listItem.findViewById(R.id.header);

	                int topMargin = 0;
	                if (i == 0) 
	                {
	                    int top = listItem.getTop();
	                    int height = listItem.getHeight();
	                    if (top < 0)
	                        topMargin = title.getHeight() < (top + height) ? -top : (height - title.getHeight());
	                }

	                // set the margin.
	                ((ViewGroup.MarginLayoutParams) title.getLayoutParams()).topMargin = topMargin;

	                listItem.requestLayout();
	            }
	        }
	     });
	    if(isOnline()==true)
		{
			try
			{
				WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.product_list,HomeActivity.this ,"Please Wait....");
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

	    CancelSharDialog();
	    FacebookSharing();
	    TwitterSharing();
	    PinterestSharing();
	    TumblerSharing();
	    GooglePlusSharing();
	    
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
   @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	//Toast.makeText(getApplicationContext(), "resume", Toast.LENGTH_SHORT).show();
	if(adapter!=null)
	{
		adapter.notifyDataSetChanged();
		lsvProductList.refreshDrawableState();
	}
}
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
				Animation anim=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.slide_down_anim);
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
					Toast.makeText(getApplicationContext(), "Facebook", 5000).show();
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
				Toast.makeText(getApplicationContext(), "Twitter", 5000).show();
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
				Toast.makeText(getApplicationContext(), "Pinterest", 5000).show();
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
				Toast.makeText(getApplicationContext(), "Tumbler", 5000).show();
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
				Toast.makeText(getApplicationContext(), "GooglePlus", 5000).show();
			}
		});
   }
}
