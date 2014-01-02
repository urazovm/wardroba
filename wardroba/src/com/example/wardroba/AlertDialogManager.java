
package com.example.wardroba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

public class AlertDialogManager 
{
    @SuppressWarnings("deprecation")
    public void showAlertDialog(FragmentActivity homeActivityFragment, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(homeActivityFragment).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {
            	dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

	public void showAlertDialog(FragmentActivity activity, String string,
			boolean b) {
		// TODO Auto-generated method stub
		
	}


}
