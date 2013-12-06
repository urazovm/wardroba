package com.example.wardroba;

import java.util.ArrayList;
import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.example.wardroba.ProductListFragment.ProductGalleryAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductGallery extends FragmentActivity
{
	
	    FragmentManager fragmentManager;
	    ProductListFragment productList;
	    ProductDetailFragment productDetail;
	    Fragment productFragment;
	    GridView gridView;
	    List<Constants> arr_productGallery;
		
		public void setResponseFromRequest(Object obj)
		{
			if(obj!=null)
			{
				arr_productGallery=(ArrayList<Constants>)obj;
				Toast.makeText(ProductGallery.this, "Product found:"+arr_productGallery.size(), Toast.LENGTH_SHORT).show();
				
				gridView.setAdapter(new ProductGalleryAdapter(ProductGallery.this));
				gridView.setOnItemClickListener(new OnItemClickListener() 
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
		}
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
		setContentView(R.layout.product_gallery_activity); 
		 gridView=(GridView)findViewById(R.id.product_grid);
   	 
   	 try {
			WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.search_list, ProductGallery.this, "Please wait...");
			String url=Constants.PRODUCT_SEARCH_URL+"id="+Constants.LOGIN_USERID;
			Log.d("url", "url="+url);
			webAPIHelper.execute(url);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
    
    public class ProductGalleryAdapter extends BaseAdapter
    {
   	 Context mContext;
   	 ImageLoader imageLoader;
   	LayoutInflater mInflater;
   	 public ProductGalleryAdapter(Context context) 
   	 {
   		 mContext=context;
   		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		 imageLoader=new ImageLoader(mContext);
			// TODO Auto-generated constructor stub
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr_productGallery.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			if(convertView!=null)
				grid=convertView;
			else
			{
				grid=convertView=mInflater.inflate(R.layout.product_detail_activity, null);
						
			}
			ImageView img=(ImageView)grid.findViewById(R.id.imgProductIcon);
			imageLoader.DisplayImage(arr_productGallery.get(position).GImageUrl, img);
			return grid;
		}
   	 
    }
}
