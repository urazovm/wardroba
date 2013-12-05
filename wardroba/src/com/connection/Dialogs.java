package com.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Dialogs extends Activity 
{	
	public void setResponseFromRequest(int requestNumber) 
	{		
	}
	
	public void setResponseFromRequest(int requestNumber, Object obj) 
	{	
	}
	
	public void dialog_message(String msg) 
	{
		  final AlertDialog alertDialog = new AlertDialog.Builder(Dialogs.this).create();			  			  
			  alertDialog.setMessage(msg);

			  alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			  {

			   public void onClick(DialogInterface dialog, int which) 
			   {
				  alertDialog.dismiss();		     	    
			   }
			  });

			  
			  alertDialog.show();
		
	}
	
	public void Updatedialog_message(String msg) 
	{
		  final AlertDialog alertDialog = new AlertDialog.Builder(Dialogs.this).create();			  			  
			  alertDialog.setMessage(msg);		 
			  
			  alertDialog.show();
		
	}
	
	public void Exitdialog_message(String msg) 
	{
		  final AlertDialog alertDialog = new AlertDialog.Builder(Dialogs.this).create();

			  alertDialog.setTitle("5CONDITIONS");
			  //alertDialog.setIcon(R.drawable.close);
			  alertDialog.setMessage(msg);

			  alertDialog.setButton("Yes", new DialogInterface.OnClickListener()
			  {

			   public void onClick(DialogInterface dialog, int which) 
			   {
				   System.exit(0);		     	    
			   }
			  });

			  alertDialog.setButton2("No", new DialogInterface.OnClickListener() 
			  {

			   public void onClick(DialogInterface dialog, int which) 
			   {	   
			    
				   alertDialog.dismiss();

			   }
			  });

			  alertDialog.show();
		
	}
	
	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}

}
