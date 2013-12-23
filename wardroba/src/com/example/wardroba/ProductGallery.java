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
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProductGallery extends FragmentActivity implements OnProductSelectListener
{
	
    FragmentManager fragmentManager;
    ProductGalleryGridFragment productList;
    
    Fragment productFragment;
    GridView gridView;
    List<Constants> arr_productGallery;
	
	ImageView btnBack;
	TextView txtHeader;
	Typeface tf;
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
		setContentView(R.layout.product_gallery_activity); 
		tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		btnBack=(ImageView)findViewById(R.id.btnBack);
		txtHeader=(TextView)findViewById(R.id.txtheader);
		
		btnBack.setVisibility(View.GONE);
		txtHeader.setTypeface(tf);
	           
		if(savedInstanceState!=null)
		{
			return;
		}
        ProductGalleryGridFragment firstFragment=new ProductGalleryGridFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
	}
    
  	@Override
	public void OnProductSelected(int position) 
	{

			ProductDetailFragment secondFragment=new ProductDetailFragment();
			secondFragment.setProductArray(position);
			Constants.PRODUCT_DELETED_SELECTED_ID =position;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        
	}
}
