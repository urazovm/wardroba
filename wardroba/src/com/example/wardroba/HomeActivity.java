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
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
  			txtNameSurname.setText(Constants.USER_NAME.toString());
  			txtDate.setText(Constants.USER_DATE.toString());
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
	    imgUserPhoto=(ImageView)findViewById(R.id.img_user_photo);
	    txtNameSurname=(TextView)findViewById(R.id.txt_name_surname);
	    txtDate=(TextView)findViewById(R.id.txt_date);
		
	    tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
	    txtNameSurname.setTypeface(tf);
	    txtDate.setTypeface(tf);

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
