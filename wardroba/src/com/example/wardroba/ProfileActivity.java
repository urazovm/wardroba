package com.example.wardroba;

import com.TabBar.TabHostProvider;
import com.TabBar.TabView;
import com.TabBar.TabbarView;
import com.connection.Constants;
import com.connection.WebAPIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity
{
	public TabHostProvider tabProvider;
    public TabView tabView;
    public TextView Txt_name, Txt_city ,Txt_email , Txt_pass;
    public Button Btn_edit_profile , Btn_logout;
    public ImageView Btn_back;
	
	public void setResponseFromRequest(int requestNumber) 
	{
		Txt_name.setText(Constants.USEREMAIL);
		Txt_city.setText(Constants.USERGENDER);
		Txt_email.setText(Constants.USEREMAIL);
//		Txt_pass.setText(Constants.USERID);
	}
 
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
        tabProvider = new TabbarView(this);
		tabView = tabProvider.getTabHost();
		tabView.setCurrentView(R.layout.profile_view_activity);
		setContentView(tabView.render(3)); 

		Txt_name = (TextView)findViewById(R.id.txt_name);
		Txt_city = (TextView)findViewById(R.id.txt_city);
		Txt_email = (TextView)findViewById(R.id.txt_email);
//		Txt_pass = (TextView)findViewById(R.id.txt_passward);
		
		Btn_edit_profile = (Button)findViewById(R.id.btn_edit_profile);
		Btn_logout = (Button)findViewById(R.id.btn_logout);
		Btn_back = (ImageView)findViewById(R.id.btn_back);
		
		if(isOnline()==true)
		{
			if(Constants.LOGIN_USERID != 0)
			{
		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.profile_list,ProfileActivity.this ,"Please Wait....");
				String url = Constants.PROFILE_VIEW_URL+"id_user="+Constants.LOGIN_USERID;		
				webAPIHelper.execute(url);  
			}
		}
		else
		{
			netalert();
		}
		Btn_back.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				onBackPressed();
			}
		});
		
		Btn_logout.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				LOGOUTALERT();
			}
		});
		
		Btn_edit_profile.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent editprofile = new Intent(getApplicationContext(),ProfileEditActivity.class);
				startActivity(editprofile);
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
    public void netalert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
		builder.setMessage("Check your internet connection.")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                onBackPressed();
		                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }  
    
    public void LOGOUTALERT()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
		builder.setMessage("Are you sure you want to logout??")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                Constants.LOGIN_USERID = 0;
		                Intent home = new Intent(getApplicationContext(),WardrobaDashboardActivity.class);
		                startActivity(home);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
    
}
