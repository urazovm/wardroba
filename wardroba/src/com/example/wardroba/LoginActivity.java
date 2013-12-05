package com.example.wardroba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.connection.Constants;
import com.connection.JSONfunctions;
import com.connection.WebAPIHelper;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.internal.Utility;
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

public class LoginActivity extends Activity
{
	public EditText Edt_email,Edt_passward;
	public Button Btn_login ;// Btn_login_facebook;
	public ImageView Btn_back;
	
	private Button fb_login;//fb_logout;
    public String emailid1,password1;
    public String Alertmessage , MSG;
    public TextView txt_facebook;
    Typeface typeface;
    private JSONObject json;
    ImageView btnBack;
  //FACEBOOK
  	private Handler mHandler;
  	public static final String APP_ID = "335736383227858";
  	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
  	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;    
  	String FirstName,LastName,Username,EmailId,Bithdate,Gende;
  	Facebook facebook;
  	AsyncFacebookRunner asyncRunner;
  	SharedPreferences preferences;
  	public static Session session;
  	ProgressDialog progressDialog;
  	//String[] permissions = { "email", "offline_access", "publish_stream", "user_photos", "publish_checkins","photo_upload"};
  	String[] permissions = { "email", "offline_access", "publish_stream", "user_photos", "publish_checkins","photo_upload","user_birthday","user_relationship_details"};     
	
  	public final Pattern email_pattern= Pattern.compile
				(  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                 + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                 + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                 + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                 + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
				);	
	
   	 public void setResponseLogin(JSONObject jArray)
   	 {
   		 json=(JSONObject)jArray;
   		 LoginAlert();
   	 }
   	 public void setResponseFromRequest2(int request)
   	 {
   		 if(Constants.LOGIN_USERID!=0)
   		 {
   			 Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
   			 startActivity(intent);
   			 
   			 
   		 }
   	 }
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		progressDialog=new ProgressDialog(this);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initFacebook();
		typeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		Edt_email = (EditText)findViewById(R.id.edt_email);
		Edt_passward = (EditText)findViewById(R.id.edt_passward);
		
		Btn_login = (Button)findViewById(R.id.btn_login);
		//btnFacebook=(LoginButton)findViewById(R.id.login_fb);
		fb_login=(Button)findViewById(R.id.login_fb);
		//fb_logout=(Button)findViewById(R.id.logout_fb);
		btnBack = (ImageView)findViewById(R.id.btnBack);
		
		Edt_email.setTypeface(typeface);
		Edt_passward.setTypeface(typeface);
		Btn_login.setTypeface(typeface);
		fb_login.setTypeface(typeface);
		
		/*txt_facebook.setTypeface(typeface);
		
		txt_facebook.setVisibility(View.VISIBLE);*/
		Btn_login.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				LOGINBUTTON();
			}
		});
		
		
		
		mHandler = new Handler();
		    
        loginToFacebook();
       // logoutToFacebook();
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
					facebook.authorize(LoginActivity.this, permissions, Facebook.FORCE_DIALOG_AUTH, new Facebook.DialogListener() {
					
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
	private void initFacebook()
	{
		facebook=new Facebook(APP_ID);
		asyncRunner=new AsyncFacebookRunner(facebook);
	}
	
	/*private void logoutToFacebook()
	{
		fb_logout.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(facebook!=null)
				{
					logoutFromFacebook();
					
				}		
					
				
			}
		});
	}*/
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
							WebAPIHelper webAPIHelper=new WebAPIHelper(Constants.fblogin_request, LoginActivity.this, "Please wait...");
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
	
	/*@SuppressWarnings("deprecation")
	private void logoutFromFacebook()
	{
		asyncRunner.logout(LoginActivity.this, new RequestListener() {
			
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, "Logout error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, "Logout error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
						facebook.setAccessExpires(0);
						facebook.setAccessToken(null);
						SharedPreferences.Editor editor = preferences.edit();
						 editor.putString("access_token",null);
	                     editor.putLong("access_expires",0);
	                    
	                    editor.commit();
	                    editor.clear();
	                    
					}
				});
				
			}
		});
	}*/
	private void LOGINBUTTON() 
	{
		emailid1=Edt_email.getText().toString().trim();
		password1=Edt_passward.getText().toString().trim();
		 
	    if(emailid1.equals("") && password1.equals(""))
		{
	    	MSG="Please ente email address and password";
	    	simpleAlert();
		}
	    else if(emailid1.equals(""))
	    {
	    	MSG="Please ente email address...";
	    	simpleAlert();
	    }
	    else if(password1.equals(""))
	    {
	    	MSG="Please ente password...";
	    	simpleAlert();
	    }
		else if(! checkEmail(emailid1))
		{
			Edt_email.setFocusable(true);
			MSG="Please enter valid email address..";
			simpleAlert();	
		}
		else
		{
			if(isOnline()==true)
			{
//		    	WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.login_list,LoginActivity.this ,"Please Wait....");
//		    	String url = Constants.LOGIN_JSON_URL+"email="+emailid1+"&password="+password1;		
//				webAPIHelper.execute(url);    
				
				String URL= Constants.LOGIN_JSON_URL+"&username="+URLEncoder.encode(emailid1)+"&password="+password1;
				Log.d("Login URL= ", URL.toString());
				JSONfunctions jsonCall=new JSONfunctions(LoginActivity.this, "Please wait...", Constants.login_request);
				jsonCall.execute(URL);
			}
			else
			{
				MSG="Please check your internet connection...";
				simpleAlert();	
			}
		}
	}
	
    public void LoginAlert()
    { 
	   	 try
	   	 {	  
	   		 if(!json.equals(""))
	   		 {
	   			String success=json.getString("success").toString();
	   			Log.d("Json", "hello:"+json.toString());
	   			if(success.equals("true"))
	   			{
		   			Constants.LOGIN_USERID=Integer.valueOf(json.getString("id").toString());
		   			
	   				Intent ii = new Intent(LoginActivity.this,HomeActivity.class);
		        	startActivity(ii); 
	   			}else
	   			{
	   				MSG="Please enter valid email or password.";
	   				simpleAlert();
	   			}
	   			
	   		 }else
	   		 {
	   			MSG="Data not availble....";
	   			simpleAlert();	 
	   		 }
	   	 }
	   	 catch(Exception e)
	   	 {
	   		MSG="Data not availble....";
	   		simpleAlert();	
	   	 }
    }

    private boolean checkEmail (String email)
    {
    	return email_pattern.matcher(email).matches();
    }

//    public void SuccsessLogin()
//    {
// 	   AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
// 	   		builder.setMessage(Constants.MSG_LOGIN)
//// 			builder.setMessage(Constants.USERNAME +" You are "+ Constants.MSG_LOGIN)
// 			       .setCancelable(false)
// 			       .setPositiveButton("OK", new DialogInterface.OnClickListener() 
// 			       {
// 			           public void onClick(DialogInterface dialog, int id) 
// 			           {
// 			        	   Edt_email.setText("");
// 			        	   Edt_passward.setText("");
// 			        	   dialog.dismiss();
// 			        	   Intent i1 = new Intent(getApplicationContext(),ProductlistActivity.class);
// 			        	   startActivity(i1);   
// 			           }
// 			       });
// 			AlertDialog alert = builder.create();
// 			alert.show();
//    }

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

        
    
    
	public void simpleAlert()
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setMessage(MSG)
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() 
			       {
			           public void onClick(DialogInterface dialog1, int id) 
			           {
			                dialog1.dismiss();			                
			           }
			       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
 	    
	}

}
