package com.example.wardroba;

import java.util.regex.Pattern;

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
import android.widget.Toast;

public class ProfileEditActivity extends Fragment
{
	ImageLoader imageLoader;
	ProgressBar imgLoader1;
	ImageView imgProfilePhoto;
	Button btnSave;
	public EditText edtName,edtSurname,edttCityAddress,edtEmail,edtPassword,edtRepatPassword;
	Typeface tf;
	public final Pattern email_pattern= Pattern.compile
				(  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
              + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
              + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
              + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
				);
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
        edtEmail.setEnabled(false);
        edtPassword=(EditText)root.findViewById(R.id.edt_passward);
        edtRepatPassword=(EditText)root.findViewById(R.id.edt_repeat_passward);
        
        Bundle bundle=getArguments();
        edtName.setText(bundle.getString("name"));
        edtSurname.setText(bundle.getString("surname"));
        edttCityAddress.setText(bundle.getString("city"));
        edtEmail.setText(bundle.getString("Email"));
        btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!checkEmpty())
				{
					boolean valid_email=false,valid_password=false;
					String pass1,pass2;
					pass1=edtPassword.getText().toString();
					pass2=edtRepatPassword.getText().toString();
					if(isValidEmail(edtEmail.getText().toString()))
					{
						valid_email=true;
					}
					if(pass1.equals(pass2))
					{
						valid_password=true;
					}
					if(!valid_email)
					{
						Toast.makeText(getActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
					}
					else if(!valid_password)
					{
						Toast.makeText(getActivity(), "Password must be match in both field 'Password' and 'Repeat Password'", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(getActivity(), "Update profile requesting...", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
       
		return root;
	}
	public boolean checkEmpty()
	{
		String name,surname,city,email,password,repeatPassword;
		name=edtName.getText().toString();
		surname=edtSurname.getText().toString();
		city=edttCityAddress.getText().toString();
		email=edtEmail.getText().toString();
		password=edtPassword.getText().toString();
		repeatPassword=edtRepatPassword.getText().toString();
		if(name.equals("") || surname.equals("") || city.equals("") ||email.equals("") || password.equals("") || repeatPassword.equals(""))
		{
			return true;
		}
		else {
			return false;
		}
	}
	public boolean isValidEmail(String email)
	{
		if(email.matches(email_pattern.pattern()))
		{
			return true;
		}
		return false;
				
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