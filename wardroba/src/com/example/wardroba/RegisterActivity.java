package com.example.wardroba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

import com.connection.Constants;
import com.connection.JSONfunctions;
import com.connection.WebAPIHelper;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	public EditText Edt_name,Edt_Surname,Edt_email,Edt_passward ;
	public Button Btn_register,Btn_register_facebook;
	private Button fb_login;//fb_logout;
	public ImageView Btn_back;
	public String name1,surname1,emailid1,password1;
    public String  MSG;    
    private JSONObject json;
    
    ImageView btnBack;
    Typeface typeface;
    //FACEBOOK
    Facebook facebook;
  	AsyncFacebookRunner asyncRunner;
  	SharedPreferences preferences;
  	public static Session session;
  	ProgressDialog progressDialog;
  	private Handler mHandler;
  	public static final String APP_ID = "501876776516754";
  	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
  	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;    
  	String FirstName,LastName,Username,EmailId,Bithdate,Gende;
  	    
   	String[] permissions = { "email", "offline_access", "publish_stream", "user_photos", "publish_checkins","photo_upload","user_birthday","user_relationship_details"};
    
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
		if(Constants.REGISTER_USERID != 0)
		{
			//http://41.wardroba.dev/store/api-update-password?id=8&password=deven&cpassword=deven
			String URL= Constants.CHANGE_PASSWARD_URL+"&id="+Constants.REGISTER_USERID+"&password="+password1+"&cpassword="+password1;
			Log.d("update Passward URL= ", URL.toString());
			JSONfunctions jsonCall=new JSONfunctions(RegisterActivity.this, "Please wait...", Constants.change_password_request);
			jsonCall.execute(URL);
		}
		else
		{
			aleartbox1();
		}
	}
	
	public void setResponseChangePassword(JSONObject jArray)
  	 {
  		 json=(JSONObject)jArray;
  		 RegisterAlert();
  	 }
	public void setResponseFromRequest2(int request)
  	 {
  		 if(Constants.LOGIN_USERID!=0)
  		 {
  			 Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
  			 startActivity(intent);
  			 
  			 
  		 }
  	 }
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		progressDialog=new ProgressDialog(this);
		initFacebook();
		typeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		
		Btn_register = (Button)findViewById(R.id.btn_register);
		btnBack = (ImageView)findViewById(R.id.btnBack);
		fb_login=(Button)findViewById(R.id.login_fb);
		Edt_name = (EditText)findViewById(R.id.edt_name);
		Edt_Surname = (EditText)findViewById(R.id.edt_surname);
		Edt_email = (EditText)findViewById(R.id.edt_email);
		Edt_passward = (EditText)findViewById(R.id.edt_passward);
		
		
		Edt_name.setTypeface(typeface);
		Edt_Surname.setTypeface(typeface);
		Edt_email.setTypeface(typeface);
		Edt_passward.setTypeface(typeface);
		Btn_register.setTypeface(typeface);
		
		fb_login.setTypeface(typeface);
		//FAcebook Login
        
		 loginToFacebook();
        RegisterButton();
        buttonBack();
	}
	private void buttonBack()
	{
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void initFacebook()
	{
		facebook=new Facebook(APP_ID);
		asyncRunner=new AsyncFacebookRunner(facebook);
	}
	private void loginToFacebook()
	{
		fb_login.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String access_token = preferences.getString("access_token", null);
			    long expires = preferences.getLong("access_expires", 0);
			    if (access_token != null) {
			        facebook.setAccessToken(access_token);
			        
			    }
			 
			    if (expires != 0) {
			        facebook.setAccessExpires(expires);
			    }
				if(!facebook.isSessionValid())
				{
					facebook.authorize(RegisterActivity.this, permissions, Facebook.FORCE_DIALOG_AUTH, new Facebook.DialogListener() {
					
						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "FB Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Dailog Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onComplete(Bundle values) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
							SharedPreferences.Editor editor = preferences.edit();
	                        editor.putString("access_token",facebook.getAccessToken());
	                        editor.putLong("access_expires",facebook.getAccessExpires());
	                        editor.commit();
							requestMeProfile();
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
						}
					});
				}
				else
				{
					requestMeProfile();
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void requestMeProfile()
	{
		progressDialog.setMessage("Please wait...");
		progressDialog.show();
		asyncRunner.request("me", new RequestListener() {
			
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				Log.d("Profile", response);
			    String json = response;
			    
			    
			    try {
					JSONObject profile=new JSONObject(json);
					final String email,firstName,lastName,fb_id;
					fb_id=profile.getString("id");
					email=profile.getString("email");
					firstName=profile.getString("first_name");
					lastName=profile.getString("last_name");
					Log.d("FacebookProfile", "Facebook user detail...");
					Log.d("FacebookProfile", "Id:"+fb_id);
					Log.d("FacebookProfile", "Email:"+email);
					Log.d("FacebookProfile", "Firstname:"+firstName);
					Log.d("FacebookProfile", "Lastname:"+lastName);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.fblogin_request1, RegisterActivity.this, "Please wait...");
							String url=Constants.FBLOGIN_URL+"email="+email+"&facebookid="+fb_id+"&firstname="+firstName+"&lastname="+lastName;
							//http://dev.wardroba.com/serviceXml/fbuserlogin.php?email=jaydip123@amphee.com&facebookid=100002988664629&firstname=jaydip&lastname=patel
							Log.d("FacebookLoginUrl", "Login url:"+url);
							webAPIHelper.execute(url);
						}
					});
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    catch (Exception e) {
					// TODO: handle exception
			    	e.printStackTrace();
				}
			    
			}
		});
		
	}
	
	public void RegisterButton()
	{
		Btn_register.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				name1 = Edt_name.getText().toString();
				surname1= Edt_Surname.getText().toString();
				emailid1=Edt_email.getText().toString();
				password1=Edt_passward.getText().toString();
						
			    if(emailid1.equals("") && password1.equals("") && name1.equals("") && surname1.equals(""))
				{
			    	MSG = "please enter name ,sername,email id and password.";
			    	fillalert();
				}
			    else if(emailid1.equals("")&& password1.equals("")&& surname1.equals(""))
			    {
			    	MSG = "please enter surname,email id and password.";
			    	fillalert();
			    }
			    else if(name1.equals("")&& password1.equals("") && surname1.equals(""))
			    {
			    	MSG = "please enter name,surname and password.";
			    	fillalert();
			    }
			    else if(name1.equals("")&& emailid1.equals("")&& surname1.equals(""))
			    {
			    	MSG = "please enter name ,suename,and email_id.";
			    	fillalert();
			    }
			    else if(password1.equals("") && surname1.equals(""))
			    {
			    	MSG = "please enter surname and password.";
			    	fillalert();
			    }
			    else if(emailid1.equals("") && surname1.equals(""))
			    {
			    	MSG = "please enter surname and email id.";
			    	fillalert();
			    }
			    else if(name1.equals("")&& surname1.equals(""))
			    {
			    	MSG = "please enter name and surname.";
			    	fillalert();
			    }else if(name1.equals(""))
			    {
			    	MSG = "please enter name.";
			    	fillalert();
			    }else if(surname1.equals(""))
			    {
			    	MSG = "please enter surname.";
			    	fillalert();
			    }else if(emailid1.equals(""))
			    {
			    	MSG = "please enter email id.";
			    	fillalert();
			    }else if(password1.equals(""))
			    {
			    	MSG = "please enter password.";
			    	fillalert();
			    }
				else if(! checkEmail(emailid1))
				{

					Edt_email.setFocusable(true);
					MSG = "please enter valid email address.";
			    	fillalert();
				}
				else
				{   
					if(isOnline()==true)
					{
					    WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.register_list,RegisterActivity.this, "Please wait...");
					    String url =Constants.REGISTER_URL+"name="+URLEncoder.encode(name1)+"&lastname="+URLEncoder.encode(surname1)+"&email="+URLEncoder.encode(emailid1)+"&password="+URLEncoder.encode(password1);		
					    Log.d("Register URL= ", url.toString());
					    webAPIHelper.execute(url);      
					   // http://114.jaydip.jd/php/wardroba/register.php?name=jaydip&lastname=jalpesh&email=jalpesh@amphee.com&password=rjaot
						
					    
					}
					else
					{
						netalert();
					}
				    
				}
			}
		});
	}
	public void RegisterAlert()
    { 
	   	 try
	   	 {	  
	   		 if(!json.equals(""))
	   		 {
	   			 Log.d("Json", "json:"+json.toString());
	   			String success=json.getString("success").toString();
	   			if(success.equals("true"))
	   			{
	   				MSG=Constants.MSG_REGISTER;
		   			Constants.LOGIN_USERID=Integer.valueOf(json.getString("id").toString());
		   			Registersuccess();
	   				
	   			}else
	   			{
	   				MSG="Please enter valid email or password.";
	     			Alert();
	   			}
	   			
	   		 }else
	   		 {
	   			MSG="Data not availble....";
     			Alert();	 
	   		 }
	   	 }
	   	 catch(Exception e)
	   	 {
	   		MSG="Data not availble....";
 			Alert();	
	   	 }
    }
	public void fillalert()
    {
 	   AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
 			builder.setMessage(MSG)
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
    
    public void Registersuccess()
    {
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		         builder.setMessage(MSG)
		                .setCancelable(false)
		                .setPositiveButton("OK", new DialogInterface.OnClickListener() 
		                {
		                    public void onClick(DialogInterface dialog, int id) 
		                    {
		                    	Intent ii = new Intent(RegisterActivity.this,HomeActivity.class);
		    		        	startActivity(ii); 
		                    }
		                });
		         AlertDialog alert = builder.create();
		         alert.show();
			}
		}) ;
    	      
    }
    
    public void aleartbox1()
    {
    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage(Constants.MSG_REGISTER)// Constants.MSG_REGISTER
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
        {
     	   AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
    }
    // FACEBOOK
    
    
	public void Alert()
	{
		
 	    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		builder.setMessage(MSG)
	       .setCancelable(false)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() 
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
