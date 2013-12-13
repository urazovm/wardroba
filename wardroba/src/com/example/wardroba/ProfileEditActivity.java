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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileEditActivity extends Fragment
{
	ImageLoader imageLoader;
	ProgressBar imgLoader1;
	ImageView imgProfilePhoto;
	Button btnSave;
	public EditText edtName,edtSurname,edttCityAddress,edtEmail,edtPassword,edtRepatPassword;
	Typeface tf;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_edit_activity, null);
		
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        imageLoader=new ImageLoader(getActivity());
        imgLoader1=(ProgressBar)root.findViewById(R.id.progLoader);
        imgProfilePhoto=(ImageView)root.findViewById(R.id.img_profile);
        btnSave=(Button)root.findViewById(R.id.btn_save);
        edtName=(EditText)root.findViewById(R.id.edt_name);
        edtSurname=(EditText)root.findViewById(R.id.edt_surname);
        edttCityAddress=(EditText)root.findViewById(R.id.edt_city);
        edtEmail=(EditText)root.findViewById(R.id.edt_email);
        edtPassword=(EditText)root.findViewById(R.id.edt_passward);
        edtRepatPassword=(EditText)root.findViewById(R.id.edt_repeat_passward);
        
        Bundle bundle=getArguments();
        edtName.setText(bundle.getString("name"));
        edtSurname.setText(bundle.getString("surname"));
        edttCityAddress.setText(bundle.getString("city"));
        edtEmail.setText(bundle.getString("Email"));
        
       
		return root;
	}
	public void onAttach(Activity activity) 
	{
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
//	@Override
//	public void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		Bundle arguments=getArguments();
//	}
}