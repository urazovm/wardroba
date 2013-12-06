package com.example.wardroba;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class ProductDetailFragment extends Fragment 
{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         ViewGroup root = (ViewGroup) inflater.inflate(R.layout.product_detail_activity, null);
         init(root);
        return root;
    }
    
 
     @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
              
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
            }
        }
     void init(ViewGroup root)
     {
    	 
    	 
     }

}
