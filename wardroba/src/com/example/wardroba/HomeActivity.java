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
import android.widget.AbsListView;
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
	public TextView txtNameSurname,txtDate;
	Typeface tf;
	
	private ArrayList<Constants> arr_ProductList;
	
	
  	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest(int requestNumber, Object obj) 
  	{		  		 		
  		arr_ProductList =(ArrayList<Constants>) obj;
  		if(arr_ProductList != null)
  		{  			 
//  			txtNameSurname.setText(Constants.USER_NAME.toString());
//  			txtDate.setText(Constants.USER_DATE.toString());
  			HomeProductBaseAdapter adapter=new HomeProductBaseAdapter(arr_ProductList,HomeActivity.this);
 	    	lsvProductList.setAdapter(adapter);
 	    	
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  		}
  	} 	

    public void onCreate(Bundle savedInstanceState) 
    {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);       
	    setContentView(R.layout.home_activity);
	 	 
	    lsvProductList=(ListView)findViewById(R.id.product_list);
//	    imgUserPhoto=(ImageView)findViewById(R.id.img_user_photo);
//	    txtNameSurname=(TextView)findViewById(R.id.txt_name_surname);
//	    txtDate=(TextView)findViewById(R.id.txt_date);
		
	    tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
//	    txtNameSurname.setTypeface(tf);
//	    txtDate.setTypeface(tf);
	    lsvProductList.setOnScrollListener(new OnScrollListener() 
		{
	        int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	        public void onScrollStateChanged(AbsListView view, int scrollState) 
	        {
	            // Store the state to avoid re-laying out in IDLE state.
	            mScrollState = scrollState;
	        }

	        @Override
	        public void onScroll(AbsListView list, int firstItem, int visibleCount, int totalCount) 
	        {
	            // Nothing to do in IDLE state.
	            if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE)
	                return;

	            for (int i=0; i < visibleCount; i++) 
	            {
	                View listItem = list.getChildAt(i);
	                if (listItem == null)
	                    break;

	                LinearLayout title = (LinearLayout) listItem.findViewById(R.id.header);

	                int topMargin = 0;
	                if (i == 0) {
	                    int top = listItem.getTop();
	                    int height = listItem.getHeight();

	                    // if top is negative, the list item has scrolled up.
	                    // if the title view falls within the container's visible portion,
	                    //     set the top margin to be the (inverse) scrolled amount of the container.
	                    // else
	                    //     set the top margin to be the difference between the heights.
	                    if (top < 0)
	                        topMargin = title.getHeight() < (top + height) ? -top : (height - title.getHeight());
	                }

	                // set the margin.
	                ((ViewGroup.MarginLayoutParams) title.getLayoutParams()).topMargin = topMargin;

	                // request Android to layout again.
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
}
