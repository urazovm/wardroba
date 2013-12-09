package com.example.wardroba;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;
import com.TabBar.TabHostProvider;
import com.TabBar.TabView;
import com.TabBar.TabbarView;
import com.connection.Constants;
import com.connection.WebAPIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileEditActivity extends Activity
{
	public TabHostProvider tabProvider;
    public TabView tabView;
    public static ImageView img_profile;
    public EditText Edt_name,Edt_city,Edt_email,Edt_passward,Edt_repeatpassward;
    public Button Btn_Save;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private static final int CAMERA_REQUEST = 1888; 
    public String Name,City,Email,Passward,RepeatPassward;
    public String Alertmessage , MSG , PHOTOID;
	final Context context = this;
	//keep track of cropping intent
	final int PIC_CROP = 2;
	//captured picture uri
	private Uri picUri;
    
  	public final Pattern email_pattern= Pattern.compile
			(  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
             + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
             + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
             + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
             + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
             + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
			);
	public void setResponseFromRequest(int requestNumber) 
	{
		// TODO Auto-generated method stub
		Edt_name.setText(Constants.EDITUSEREMAIL);
		Edt_city.setText(Constants.EDITUSERGENDER);
		Edt_email.setText(Constants.EDITUSEREMAIL);
	}
	
	public void setResponseFromRequest2(int requestNumber) 
	{
		if(Constants.EDITEDUSERID != 0)
		{
			EDITALERT();
		}
		else
		{
			EDITALERT1();
		}
	} 
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
        
		
		setContentView(R.layout.profile_edit_activity); 
		
		img_profile = (ImageView)findViewById(R.id.img_profile);
		
		Edt_name = (EditText)findViewById(R.id.edt_name);
		Edt_city = (EditText)findViewById(R.id.edt_city);
		Edt_email = (EditText)findViewById(R.id.edt_email);
		Edt_passward = (EditText)findViewById(R.id.edt_passward);
		Edt_repeatpassward = (EditText)findViewById(R.id.edt_repeat_passward);
		
		Btn_Save = (Button)findViewById(R.id.btn_save);
		
		
		if(isOnline()==true)
		{
			if(Constants.LOGIN_USERID != 0)
			{
		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.editprofile_list,ProfileEditActivity.this ,"Please Wait....");
				String url = Constants.PROFILE_VIEW_URL+"id_user="+Constants.LOGIN_USERID;		
				webAPIHelper.execute(url);  
			}
		}
		else
		{
			netalert();
		}

		img_profile.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				ProfilePicAlert();
			}
		});
		
		Btn_Save.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				SAVEBUTTON();
			}
		});
		
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
    public void netalert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
		builder.setMessage("Check your internet connection.")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                onBackPressed();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
    
	private void SAVEBUTTON() 
	{
		Name = Edt_name.getText().toString();
		City = Edt_city.getText().toString();
		Email = Edt_email.getText().toString();
		Passward = Edt_passward.getText().toString();
		RepeatPassward = Edt_repeatpassward.getText().toString();
		 
	    if(Name.equals("") && City.equals("") && Email.equals("") && Passward.equals("") && RepeatPassward.equals("") )
		{
	    	Alertmessage = "please enter name,city,email,passward and repeatpassward";
	    	fillalert();
		}
	    else if(Name.equals("") && Email.equals("")&& Passward.equals("") && RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,email,password and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,city,password and repeatpassward";
	    	fillalert();
	    }
	    else if(City.equals("")&& Email.equals("")&& Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city,email,password and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& Email.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,city,email and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& Email.equals("")&& Passward.equals(""))
	    {
	    	Alertmessage = "please enter name,city,email and passward";
	    	fillalert();
	    }
	    else if(Name.equals("") && Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,passward and repeatpassward";
	    	fillalert();
	    }
	    else if(City.equals("") && Email.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city,email and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && Email.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,email and repeatpassward";
	    	fillalert();
	    }
	    else if(City.equals("") && Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city,passward and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && Email.equals("")&& Passward.equals(""))
	    {
	    	Alertmessage = "please enter name,email and passward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name,city and repeatpassward";
	    	fillalert();
	    }
	    else if(Email.equals("") && Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter email,passward and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& Passward.equals(""))
	    {
	    	Alertmessage = "please enter name,city and passward";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals("")&& Email.equals(""))
	    {
	    	Alertmessage = "please enter name,city and email";
	    	fillalert();
	    }
	    
	    else if(City.equals("") && Email.equals("")&& Passward.equals(""))
	    {
	    	Alertmessage = "please enter city,email and passward";
	    	fillalert();
	    } 
	    else if(City.equals("") && Email.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city,email and repeatpassward";
	    	fillalert();
	    }
	    else if(City.equals("") && Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city,passward and repeatpassward";
	    	fillalert();
	    }
	    else if(Email.equals("") && Passward.equals("")&& RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter email,passward and repeatpassward";
	    	fillalert();
	    }     
	    
	    else if(Email.equals("") && RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter email and repeatpassward";
	    	fillalert();
	    }
	    else if(Email.equals("") && Passward.equals(""))
	    {
	    	Alertmessage = "please enter email and passward";
	    	fillalert();
	    }
	    else if(City.equals("") && RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter city and repeatpassward";
	    	fillalert();
	    }
	    else if(City.equals("") && Passward.equals(""))
	    {
	    	Alertmessage = "please enter city and passward";
	    	fillalert();
	    }
	    else if(City.equals("") && Email.equals(""))
	    {
	    	Alertmessage = "please enter city and email";
	    	fillalert();
	    }
	    else if(Name.equals("") && RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter name and repeatpassward";
	    	fillalert();
	    }
	    else if(Name.equals("") && Passward.equals(""))
	    {
	    	Alertmessage = "please enter name and passward";
	    	fillalert();
	    }
	    else if(Name.equals("") && Email.equals(""))
	    {
	    	Alertmessage = "please enter name and email";
	    	fillalert();
	    }
	    else if(Name.equals("") && City.equals(""))
	    {
	    	Alertmessage = "please enter name and city";
	    	fillalert();
	    }
	    else if(Passward.equals("") && RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter passward and repeatpassward";
	    	fillalert();
	    }
	    else if(Passward.equals(""))
	    {
	    	Alertmessage = "please enter password";
	    	fillalert();
	    }
	    else if(Email.equals(""))
	    {
	    	Alertmessage = "please enter email";
	    	fillalert();
	    }
	    else if(Name.equals(""))
	    {
	    	Alertmessage = "please enter name";
	    	fillalert();
	    }
	    else if(City.equals(""))
	    {
	    	Alertmessage = "please enter city";
	    	fillalert();
	    }
	    else if(RepeatPassward.equals(""))
	    {
	    	Alertmessage = "please enter repeatpassward";
	    	fillalert();
	    }
		else if(! checkEmail(Email))
		{
			aleartemail();
			Edt_email.setFocusable(true);
		}
		else if(!RepeatPassward.equals(Passward))
		{
	    	Alertmessage = "Password and repeat password should be same";
	    	fillalert();
		}
		else
		{
			if(isOnline()==true)
			{
//				// without profile photo
////			if(PHOTOID.equals("")) 
////			{
			    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.editprofile_list2,ProfileEditActivity.this ,"Please Wait....");
	//		    http://project.amphee.net/php/locator/xml/useredit.php?id_user=1&email=jaydip1@amphee.com&gender=Male&birthdate=10/01/2000&zipcode=123456 
			    	String url = Constants.PROFILE_SAVE_URL+"id_user="+Constants.LOGIN_USERID+"&email="+Email+"&gender=Male"+"&birthdate=10/01/2000"+"&zipcode=123456";
					webAPIHelper.execute(url); 
////			}
//			
//			// with profile photo
////			else 
////			{
////				PHOTOID = PHOTOID.substring(0, PHOTOID.length() - 5);
////				PHOTOID = PHOTOID+Constants.LOGIN_USERID+".jpeg";
//				
////		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.login_list,LoginActivity.this ,"Please Wait....");
////				String url = Constants.LOGIN_URL+"&username="+emailid1+"&password="+password1;		
////				webAPIHelper.execute(url);
////			}			
				
			}
			else
			{
				netalert();
			}
		}
	}
	public void fillalert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
 			builder.setMessage(Alertmessage)
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
    private boolean checkEmail (String email)
    {
    	return email_pattern.matcher(email).matches();
    }
    public void aleartemail()
    {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	     builder.setMessage("Please check your email address..")
	            .setCancelable(false)
	            .setNegativeButton("OK", new DialogInterface.OnClickListener() 
	            {
	                public void onClick(DialogInterface dialog, int id) 
	                {
	                     dialog.dismiss();
	                }
	            });
	     AlertDialog alert = builder.create();
	     alert.show();      
    }

    public void ProfilePicAlert()
    {	
		final Dialog dialog = new Dialog(context);
				dialog.show();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (resultCode == RESULT_OK) 
        {
            if (requestCode == SELECT_PICTURE) 
            {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
//                Log.d("PHOTOPATH",""+selectedImagePath);
//                Toast.makeText(getApplicationContext(),"PHOTO PATH="+selectedImagePath, 5000).show();
//                img_profile.setImageURI(selectedImageUri);
 
//                try 
//                {
////                    Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
////                    img_profile.setImageBitmap(bm);         
//                    
////                	Bitmap image = selectedImagePath;
////                	image = Bitmap.createScaledBitmap(image, 86, 87, true);
//                	
//                	
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, options);
//
//                 int width =87;
//				int height = 86;
//				// Calculate inSampleSize
//                 options.inSampleSize = calculateInSampleSize(options, width, height);
//
//                 options.inJustDecodeBounds = false;
//                 Bitmap myBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, options);
//                 img_profile.setImageBitmap(myBitmap);
//                    
//                } 
//                catch (FileNotFoundException e) 
//                {
//                    e.printStackTrace(); 
//                }

                
    // final code            
                try 
                {
//                    FileInputStream in = new FileInputStream(selectedImagePath);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 10;
////                    imagePath = selectedImagePath;
////                    tvPath.setText(imagePath);
//                    Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
//                    img_profile.setImageBitmap(bmp);
//                    img_profile.setScaleType(ScaleType.FIT_XY);

                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;

                    FileInputStream fis = new FileInputStream(selectedImagePath);
                    BitmapFactory.decodeStream(fis, null, o);
                    try 
                    {
						fis.close();
					} 
                    catch (IOException e) 
                    {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    int scale = 1;
                    int IMAGE_MAX_SIZE = 87;
                    
					if (o.outHeight > IMAGE_MAX_SIZE  || o.outWidth > IMAGE_MAX_SIZE) 
					{
                        scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / 
                           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
                    }

                    //Decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    fis = new FileInputStream(selectedImagePath);
                    Bitmap bmp = BitmapFactory.decodeStream(fis, null, o2);
                    img_profile.setImageBitmap(bmp);
                    img_profile.setScaleType(ScaleType.FIT_XY);
                    try 
                    {
						fis.close();
					} 
                    catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } 
                catch (FileNotFoundException e) 
                {
                    e.printStackTrace();
                }
                
//                HttpURLConnection connection = (HttpURLConnection) ((Object) selectedImageUri).openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                
                
//                Uri selectedImage = data.getData();
//                PHOTOID = getPath(selectedImage);
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//
//                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath); 
//
//                img_profile.setImageBitmap(yourSelectedImage);
                
            }
            else if (requestCode == CAMERA_REQUEST)
            {

//                Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//            	scaleDown(photo,80,false);
//                img_profile.setImageBitmap(photo);

//                Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//                img_profile.setImageBitmap(photo);
//                PHOTOID = photo.toString().trim();
//                Toast.makeText(getApplicationContext(),"PHOTOPATH="+photo, 5000).show();
    			picUri = data.getData();
    			//carry out the crop operation
    			performCrop();
    		}
    		//user is returning from cropping the image
    		else if(requestCode == PIC_CROP)
    		{
    			//get the returned data
    			Bundle extras = data.getExtras();
    			//get the cropped bitmap
    			Bitmap thePic = extras.getParcelable("data");
    			//retrieve a reference to the ImageView
    			//display the returned cropped image
    			img_profile.setImageBitmap(thePic);
    		}
        }
    }
    
//    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
//    {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) 
//        {
//            if (width > height) 
//            {
//                inSampleSize = Math.round((float)height / (float)reqHeight);
//            } 
//            else 
//            {
//                inSampleSize = Math.round((float)width / (float)reqWidth);
//            }
//        }
//        return inSampleSize;
//	}

	public String getPath(Uri uri) 
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public void EDITALERT()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
		builder.setMessage(Constants.EDITUSEREMSG)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	   dialog.dismiss();
		        	   Intent profile = new Intent(getApplicationContext(),ProfileActivity.class);
		        	   startActivity(profile);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
    public void EDITALERT1()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
		builder.setMessage(Constants.EDITUSEREMSG)
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
    
    private void performCrop()
    {
    	//take care of exceptions
    	try 
    	{
    		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(picUri, "image/*");
	    	//set crop properties
	    	cropIntent.putExtra("crop", "true");
	    	//indicate aspect of desired crop
	    	cropIntent.putExtra("aspectX", 1);
	    	cropIntent.putExtra("aspectY", 1);
	    	//indicate output X and Y
	    	cropIntent.putExtra("outputX", 240);
	    	cropIntent.putExtra("outputY", 240);
	    	//retrieve data on return
	    	cropIntent.putExtra("return-data", true);
	    	//start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, PIC_CROP);  
    	}
    	//respond to users whose devices do not support the crop action
    	catch(ActivityNotFoundException anfe)
    	{
    		//display an error message
    		String errorMessage = "Whoops - your device doesn't support the crop action!";
    		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }
    
}
