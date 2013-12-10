package com.example.wardroba;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProfileEditActivity extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_edit_activity, null);
		return root;
	}
	 @Override
	    public void onAttach(Activity activity) {
	    	// TODO Auto-generated method stub
	    	
	    	ImageView btnBack;
			btnBack=(ImageView)activity.findViewById(R.id.btnBack);
			btnBack.setVisibility(View.VISIBLE);
			btnBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					getFragmentManager().popBackStack();
				}
			});
			super.onAttach(activity);
	    }
}