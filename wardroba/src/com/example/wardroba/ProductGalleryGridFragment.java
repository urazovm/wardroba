package com.example.wardroba;

import java.util.ArrayList;
import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat.Action;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ProductGalleryGridFragment extends Fragment 
{

	List<Constants> arr_productGallery;
	ProductGalleryAdapter adapter;
	GridView gridView;
	ImageView imgSearch;
	EditText txtKeyward;
	Typeface tf;
	OnProductSelectListener callback;
	public interface OnProductSelectListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void OnProductSelected(int position,List<Constants> arrProducts);
    }
	public void setResponseFromRequest(Object obj)
	{
		if(obj!=null)
		{
			arr_productGallery=(ArrayList<Constants>)obj;
			adapter=new ProductGalleryAdapter(this.getActivity());
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			gridView.setOnItemClickListener(new OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					callback.OnProductSelected(position, arr_productGallery);
				}
				
			});
		}
		else
		{	
			Toast.makeText(getActivity(), "No products found",Toast.LENGTH_SHORT).show();
			arr_productGallery=new ArrayList<Constants>();
			adapter=new ProductGalleryAdapter(this.getActivity());
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         ViewGroup root = (ViewGroup) inflater.inflate(R.layout.product_list_activity, null);
         //init(root);
         getActivity().findViewById(R.id.btnBack).setVisibility(View.GONE);
        //Toast.makeText(getActivity(), "Hello fragment", Toast.LENGTH_SHORT).show();
         tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
         gridView=(GridView)root.findViewById(R.id.product_grid);
         
    	 imgSearch=(ImageView)root.findViewById(R.id.imgSearch);
    	 txtKeyward=(EditText)root.findViewById(R.id.txtKeyward);
    	 txtKeyward.setTypeface(tf);
    	 txtKeyward.setImeOptions(EditorInfo.IME_ACTION_GO);
    	 txtKeyward.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				 if (actionId == EditorInfo.IME_ACTION_GO) {
			            imgSearch.performClick();
			            return true;
			        }
				return false;
			}
		});
    	 imgSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String data=txtKeyward.getText().toString();
				if(!data.equals(""))
				{
					 try {
						 InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
						 inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
							WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.search_list, ProductGalleryGridFragment.this, "Please wait...");
							String url=Constants.PRODUCT_SEARCH_URL+"id="+Constants.LOGIN_USERID+"&keyword="+data;
							webAPIHelper.execute(url);
							
						} catch (Exception e) {
							// TODO: handle exception
							
						}
				}
				else
				{
					Toast.makeText(getActivity(), "Please enter keyword", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	 try {
			WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.search_list, ProductGalleryGridFragment.this, "Please wait...");
			String url=Constants.PRODUCT_SEARCH_URL+"id="+Constants.LOGIN_USERID;
			webAPIHelper.execute(url);
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
        return root;
    }
    
 
     @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
            	activity.findViewById(R.id.btnBack).setVisibility(View.GONE);
            	callback = (OnProductSelectListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
            }
        }
     void init(ViewGroup root)
     {
    	
    	  gridView=(GridView)root.findViewById(R.id.product_grid);
    	 
    	 try {
			WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.search_list, ProductGalleryGridFragment.this, "Please wait...");
			String url=Constants.PRODUCT_SEARCH_URL+"id="+Constants.LOGIN_USERID;
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
