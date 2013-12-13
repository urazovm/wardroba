package com.example.wardroba;

import com.ImageLoader.ImageLoader;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileEditActivity extends Fragment
{
	ImageLoader imageLoader;
	ProgressBar imgLoader1;
	Typeface tf;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_edit_activity, null);
		imgLoader1=(ProgressBar)root.findViewById(R.id.progLoader);
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        imageLoader=new ImageLoader(getActivity());
		//imgLoader1.setVisibility(View.GONE);
		return root;
	}
	 @Override
	    public void onAttach(Activity activity) {
	    	// TODO Auto-generated method stub
	    	
	    	ImageView btnBack;
			btnBack=(ImageView)activity.findViewById(R.id.btnBack);
			btnBack.setVisibility(View.VISIBLE);
			btnBack.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					getFragmentManager().popBackStack();
				}
			});
			super.onAttach(activity);
	    }
}