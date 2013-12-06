package com.example.wardroba;

import com.connection.Constants;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.Window;

public class SplashActivity extends Activity 
{
	 protected boolean _active = true;
	 protected int _splashTime = 2000;
	 
	 SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
   	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		loadPrefrences();
	      if(isOnline()==true)
	       {                 
	        Thread splashTread = new Thread()  
	        {
	            @Override
	            public void run() 
	            {
	                try 
	                {
	                    int waited = 0;
	                    while(_active && (waited < _splashTime)) 
	                    {
	                        sleep(100);
	                        if(_active) 
	                        {
	                            waited += 100;
	                        }
	                    }
	                } 
	                catch(InterruptedException e)
	                {
	                    e.toString();
	                } 
	                finally 
	                {
	                	if(Constants.LOGIN_USERID==0)
	                	{

						Intent splace=new Intent(SplashActivity.this,WardrobaDashboardActivity.class);
						startActivity(splace);
						finish();
	                	}
	                	else
	                	{
	                		Intent splace=new Intent(SplashActivity.this,HomeTabActivity.class);
							startActivity(splace);
							finish();
	                	}
	                	
	                }
	            }
	        };
	        splashTread.start();   
	       }
	       else
	       {
	    	   AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
				builder.setMessage("Check your internet connection.")
				       .setCancelable(false)
				       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				                SplashActivity.this.finish();
				                
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
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
       
  @Override
  protected void onPause() 
  {
	// TODO Auto-generated method stub
	super.onPause();	
  } 
  
  private void loadPrefrences()
	{
		Constants.LOGIN_USERID=preferences.getInt(Constants.KEY_LOGIN_ID, 0);
		
	}
}
