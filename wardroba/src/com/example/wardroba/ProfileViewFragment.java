package com.example.wardroba;

import com.ImageLoader.ImageLoader;
import com.TabBar.TabHostProvider;
import com.TabBar.TabView;
import com.TabBar.TabbarView;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.example.wardroba.ProductGalleryGridFragment.OnProductSelectListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileViewFragment extends Fragment
{
	public TabHostProvider tabProvider;
    public TabView tabView;
    public TextView txtName, txtCityAddress ,txtEmail,txtItems,txtFollower,txtFollowing,txtItemLabel,txtFollowerLabel,txtFollowingLabel;
    public Button Btn_edit_profile , Btn_logout;
    public ImageView Btn_back,imgProfilePhoto;
	SharedPreferences preferences;
	ImageLoader imageLoader;
	ProgressBar imgLoader;
	Typeface tf;
	
	public void setResponseFromRequest(int requestNumber,WardrobaProfile profile) 
	{
		WardrobaProfile myProfile=(WardrobaProfile)profile;
		txtName.setText(myProfile.getName()+" "+myProfile.getLastname());
		txtCityAddress.setText(myProfile.getAddress()+" "+myProfile.getCity());
		txtEmail.setText(myProfile.getEmail());
		txtItems.setText(String.valueOf(myProfile.getItems()));
		txtFollower.setText(String.valueOf(myProfile.getFollower()));
		txtFollowing.setText(String.valueOf(myProfile.getFollowing()));
		imageLoader.DisplayImage("http://images.desimartini.com/media/versions/salman_khan_6._gallery_image_100_100.jpg", imgProfilePhoto,imgLoader);
		
//		Txt_pass.setText(Constants.USERID);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_view_activity, null);
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        imageLoader=new ImageLoader(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        imgProfilePhoto=(ImageView)root.findViewById(R.id.imgProfilePhoto);
        
		txtName = (TextView)root.findViewById(R.id.txtFullname);
		txtCityAddress = (TextView)root.findViewById(R.id.txtCityAddress);
		txtEmail = (TextView)root.findViewById(R.id.txtEmail);
		txtItems=(TextView)root.findViewById(R.id.txtItems);
		txtItemLabel=(TextView)root.findViewById(R.id.txtItemLabel);
		txtFollower=(TextView)root.findViewById(R.id.txtFollowers);
		txtFollowerLabel=(TextView)root.findViewById(R.id.txtFollowersLabel);
		txtFollowing=(TextView)root.findViewById(R.id.txtFollowing);
		txtFollowingLabel=(TextView)root.findViewById(R.id.txtFollowingLabel);
		imgLoader=(ProgressBar)root.findViewById(R.id.progLoader);
		imgLoader.setVisibility(View.GONE);

		Btn_edit_profile = (Button)root.findViewById(R.id.btn_edit_profile);
		Btn_logout = (Button)root.findViewById(R.id.btn_logout);
		getActivity().findViewById(R.id.btnBack).setVisibility(View.GONE);
		txtName.setTypeface(tf);
		txtCityAddress.setTypeface(tf);
		txtEmail.setTypeface(tf);
		txtItems.setTypeface(tf);
		txtItemLabel.setTypeface(tf);
		txtFollower.setTypeface(tf);
		txtFollowerLabel.setTypeface(tf);
		txtFollowing.setTypeface(tf);
		txtFollowingLabel.setTypeface(tf);
		Btn_edit_profile.setTypeface(tf);
		Btn_logout.setTypeface(tf);
		initButtonListener(root);
		return root;
	}
	public void initButtonListener(View root)
	{
		Btn_logout.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				SharedPreferences.Editor editor = preferences.edit();
				 editor.putString("access_token",null);
                editor.putLong("access_expires",0);
                editor.putInt(Constants.KEY_LOGIN_ID, 0);
               editor.commit();
               editor.clear();
				LOGOUTALERT();
			}
		});
		
		Btn_edit_profile.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				
				ProfileEditActivity secondFragment=new ProfileEditActivity();
	            FragmentTransaction transaction = getFragmentManager().beginTransaction();
	            transaction.replace(R.id.fragment_container, secondFragment);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
			}
		});
	}
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);       

		if(isOnline()==true)
		{
			if(Constants.LOGIN_USERID != 0)
			{
		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.profile_list,ProfileViewFragment.this ,"Please Wait....");
				String url = Constants.PROFILE_VIEW_URL+"id="+Constants.LOGIN_USERID;		
				webAPIHelper.execute(url);
			}
		}
		else
		{
			netalert();
		}
		
	
    }
    
    @Override
    public void onAttach(Activity activity) 
    {
    	try 
    	{
        	activity.findViewById(R.id.btnBack).setVisibility(View.GONE);	
        }
    	catch (ClassCastException e) 
        {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
		super.onAttach(activity);
    }
    public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}
    public void netalert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Check your internet connection.")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                getFragmentManager().popBackStack();
		                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }  
    
    public void LOGOUTALERT()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Are you sure you want to logout??")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                Constants.LOGIN_USERID = 0;
		                Intent home = new Intent(getActivity(),WardrobaDashboardActivity.class);
		                startActivity(home);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
    
}
