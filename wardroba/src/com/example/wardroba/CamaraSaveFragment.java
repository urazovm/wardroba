package com.example.wardroba;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CamaraSaveFragment extends Fragment 
{

	Typeface tf;
	
  	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.camera_view_save_activity, null);
        //getActivity().findViewById(R.id.btnBackHome).setVisibility(View.GONE);
        tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        
	    
        return root;
    }
    
  	
 
    public void onCreate(Bundle savedInstanceState) 
    {
	    super.onCreate(savedInstanceState);
	    
    }
    @Override
    public void onAttach(Activity activity) 
    {
    	super.onAttach(activity);
//    	try 
//    	{
//        	activity.findViewById(R.id.btnBackHome).setVisibility(View.GONE);	
//        }
//    	catch (ClassCastException e) 
//        {
//            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
//        }
		
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
            
   public void onPause() 
   {
	super.onPause();	
   } 
   
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Please check your internet connection...")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.dismiss();			                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();  
   }

}
