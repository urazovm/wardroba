package com.example.wardroba;

import com.ImageLoader.ImageLoader;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileActivity extends FragmentActivity
{
	
	ImageView btnBack;
	TextView txtProfileLable;
	Typeface tf;

 
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.profile_activity);
        tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
        btnBack=(ImageView)findViewById(R.id.btnBack);
        txtProfileLable=(TextView)findViewById(R.id.txtProfileLabel);
        txtProfileLable.setTypeface(tf);
        btnBack.setVisibility(View.GONE);
		
        if(savedInstanceState!=null)
		{
			return;
		}
        // Create an instance of ExampleFragment
        ProfileViewFragment firstFragment=new ProfileViewFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
    }    
}
