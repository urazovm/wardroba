package com.example.wardroba;

import com.ImageLoader.ImageLoader;
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
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ProfileActivity1 extends Activity
{
	public TabHostProvider tabProvider;
    public TabView tabView;
    public TextView txtName, txtCityAddress ,txtEmail,txtItems,txtFollower,txtFollowing,txtItemLabel,txtFollowerLabel,txtFollowingLabel,txtProfileLable;
    public Button Btn_edit_profile , Btn_logout;
    public ImageView Btn_back,imgProfilePhoto;
	SharedPreferences preferences;
	ImageLoader imageLoader;
	Typeface tf;
	public void setResponseFromRequest(int requestNumber,WardrobaProfile profile) 
	{
		WardrobaProfile myProfile=(WardrobaProfile)profile;
		txtName.setText(myProfile.getName()+" "+myProfile.getLastname());
		txtCityAddress.setText(myProfile.getAddress()+" "+myProfile.getCity());
		txtEmail.setText(myProfile.getEmail());
		txtItems.setText(String.valueOf(myProfile.getItems()));
		txtFollower.setText(String.valueOf(myProfile.getFollower()));
		txtFollowing.setText(String.valueOf(myProfile.getFollowing()));
		//imageLoader.DisplayImage("http://images.desimartini.com/media/versions/salman_khan_6._gallery_image_100_100.jpg", imgProfilePhoto);
		
//		Txt_pass.setText(Constants.USERID);
	}
 
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.profile_view_activity);
        tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
        imageLoader=new ImageLoader(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        imgProfilePhoto=(ImageView)findViewById(R.id.imgProfilePhoto);
        
		txtName = (TextView)findViewById(R.id.txtFullname);
		txtCityAddress = (TextView)findViewById(R.id.txtCityAddress);
		txtEmail = (TextView)findViewById(R.id.txtEmail);
		txtItems=(TextView)findViewById(R.id.txtItems);
		txtItemLabel=(TextView)findViewById(R.id.txtItemLabel);
		txtFollower=(TextView)findViewById(R.id.txtFollowers);
		txtFollowerLabel=(TextView)findViewById(R.id.txtFollowersLabel);
		txtFollowing=(TextView)findViewById(R.id.txtFollowing);
		txtFollowingLabel=(TextView)findViewById(R.id.txtFollowingLabel);
		txtProfileLable=(TextView)findViewById(R.id.txtProfileLabel);
		Btn_edit_profile = (Button)findViewById(R.id.btn_edit_profile);
		Btn_logout = (Button)findViewById(R.id.btn_logout);
		
		txtName.setTypeface(tf);
		txtCityAddress.setTypeface(tf);
		txtEmail.setTypeface(tf);
		txtItems.setTypeface(tf);
		txtItemLabel.setTypeface(tf);
		txtFollower.setTypeface(tf);
		txtFollowerLabel.setTypeface(tf);
		txtFollowing.setTypeface(tf);
		txtFollowingLabel.setTypeface(tf);
		txtProfileLable.setTypeface(tf);
		
		Btn_edit_profile.setTypeface(tf);
		Btn_logout.setTypeface(tf);
//		Txt_pass = (TextView)findViewById(R.id.txt_passward);
		
		
		/*Btn_back = (ImageView)findViewById(R.id.btn_back);*/
		
		if(isOnline()==true)
		{
			if(Constants.LOGIN_USERID != 0)
			{
		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.profile_list,ProfileActivity1.this ,"Please Wait....");
				String url = Constants.PROFILE_VIEW_URL+"id="+Constants.LOGIN_USERID;		
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
				SharedPreferences.Editor editor = preferences.edit();
				 editor.putString("access_token",null);
                editor.putLong("access_expires",0);
                editor.putInt(Constants.KEY_LOGIN_ID, 0);
               editor.commit();
               editor.clear();
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
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity1.this);
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
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity1.this);
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
