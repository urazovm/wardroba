package com.example.wardroba;

import com.example.wardroba.ProductGalleryGridFragment.OnProductSelectListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivityMain extends FragmentActivity implements OnProductSelectListener
{
	
    FragmentManager fragmentManager;
    //HomeActivityFragment home_activity_fragment;
    ImageView btnBack;
    Fragment HomeFragment;
    public ListView lsvProductList;
    //List<Constants> arr_productGallery;
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
		setContentView(R.layout.home_activity_main); 
		btnBack=(ImageView)findViewById(R.id.btnBackHome);
		btnBack.setVisibility(View.GONE);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Toast.makeText(getApplicationContext(), "on home fragment activity resume", Toast.LENGTH_SHORT).show();
	}
}
