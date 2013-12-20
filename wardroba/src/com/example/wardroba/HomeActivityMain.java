package com.example.wardroba;

import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.example.wardroba.ProductGalleryGridFragment.OnProductSelectListener;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeActivityMain extends FragmentActivity implements OnProductSelectListener
{
	
    FragmentManager fragmentManager;
    //HomeActivityFragment home_activity_fragment;
    
    Fragment HomeFragment;
    public ListView lsvProductList;
    //List<Constants> arr_productGallery;
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
		setContentView(R.layout.home_activity_main); 
		       
		if(savedInstanceState!=null)
		{
			return;
		}
        HomeActivityFragment home_activity_fragment=new HomeActivityFragment();
        home_activity_fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, home_activity_fragment).commit();
	}

	@Override
	public void OnProductSelected(int position) 
	{
	
		
	}
    
//  	@Override
//	public void OnProductSelected(int position) 
//	{
//		// TODO Auto-generated method stub
//		
//            // If the frag is not available, we're in the one-pane layout and must swap frags...
//
//            // Create fragment and give it an argument for the selected article
//			ProductDetailFragment secondFragment=new ProductDetailFragment();
//			secondFragment.setProductArray(position);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack so the user can navigate back
//            transaction.replace(R.id.fragment_container, secondFragment);
//            transaction.addToBackStack(null);
//            // Commit the transaction
//            transaction.commit();
//        
//	}
}
