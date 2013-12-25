package com.example.wardroba;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

public class CameraViewMain extends FragmentActivity
{
	
//	FragmentManager fm;
//	FragmentTransaction transaction;
//	@Override
//	public void onCreate(Bundle savedInstanceState) 
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.camera_view_activity);
//		
//		 CameraViewActivity firstFragment=new CameraViewActivity();
//
//	        // In case this activity was started with special instructions from an Intent,
//	        // pass the Intent's extras to the fragment as arguments
//		 	if(savedInstanceState!=null)
//			{
//				return;
//			}
//		 	firstFragment.setArguments(getIntent().getExtras());
//	        fm=getSupportFragmentManager();
//	        transaction=fm.beginTransaction();
//	        
//	        // Add the fragment to the 'fragment_container' FrameLayout
//	        transaction.add(R.id.camera_fragment_container, firstFragment);
//	        //transaction.addToBackStack(null);
//	        transaction.commit();
//	                
//	}

	FragmentManager fragmentManager;
    Fragment HomeFragment;
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
		setContentView(R.layout.camera_view_activity); 
		
		if(savedInstanceState!=null)
		{
			return;
		}
		CamaraSaveFragment camara_save_fragment=new CamaraSaveFragment();
		camara_save_fragment.setArguments(getIntent().getExtras());
        
        getSupportFragmentManager().beginTransaction().add(R.id.camera_fragment_container, camara_save_fragment).commit();
	}

	public void OnProductSelected(int position) 
	{
	
		
	}
	

	
}
