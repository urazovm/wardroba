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
		
	            // However, if we're being restored from a previous state,
	            // then we don't need to do anything and should return or else
	            // we could end up with overlapping fragments.
	           
				if(savedInstanceState!=null)
				{
					return;
				}
	            // Create an instance of ExampleFragment
	            ProductGalleryGridFragment firstFragment=new ProductGalleryGridFragment();

	            // In case this activity was started with special instructions from an Intent,
	            // pass the Intent's extras to the fragment as arguments
	            firstFragment.setArguments(getIntent().getExtras());

	            // Add the fragment to the 'fragment_container' FrameLayout
	            getSupportFragmentManager().beginTransaction()
	                    .add(R.id.fragment_container, firstFragment).commit();
	    
		// Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
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

	@Override
	public void OnProductSelected(int position) 
	{
		// TODO Auto-generated method stub
		
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
			ProductDetailFragment secondFragment=new ProductDetailFragment();
			secondFragment.setProductArray(position);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, secondFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        
	}
}
