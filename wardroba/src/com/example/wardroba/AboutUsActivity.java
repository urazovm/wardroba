package com.example.wardroba;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


//public class TagActivity extends Activity implements View.OnClickListener,
//ConnectionCallbacks, OnConnectionFailedListener

public class AboutUsActivity extends Activity
{
	
    
	protected void onCreate(Bundle savedInstanceState) 
	{
	 	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.about_us_activity); 

	}
    
}
