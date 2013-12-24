package com.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wardroba.LoginActivity;
import com.example.wardroba.ProfileEditActivity;
import com.example.wardroba.ProfileOwnerEditActivity;
import com.example.wardroba.RegisterActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
public class JSONfunctions  extends AsyncTask<String, Void,String> 
{
	ProgressDialog dialog;
	Context mContext;
	ProfileEditActivity profileEditActivity;
	ProfileOwnerEditActivity profileOwnerEditActivity;
	String message;
	int requestNo;
	public JSONfunctions(Context c,String msg,int req)
	{
		mContext=c;
		dialog=new ProgressDialog(mContext);
		message=msg;
		this.requestNo=req;
		
	}
	public JSONfunctions(ProfileEditActivity activity,String msg,int req)
	{
		this.profileEditActivity=activity;
		message=msg;
		dialog=new ProgressDialog(profileEditActivity.getActivity());
		this.requestNo=req;
		
	}
	public JSONfunctions(ProfileOwnerEditActivity profileOwnerEditActivity,
			String msg, int req) {
		// TODO Auto-generated constructor stub
		this.profileOwnerEditActivity=profileOwnerEditActivity;
		message=msg;
		dialog=new ProgressDialog(profileOwnerEditActivity.getActivity());
		this.requestNo=req;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		dialog.setMessage(message);
		dialog.show();
	}
	

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url=params[0];
		InputStream is = null;
		String result = null ;
		JSONObject jArray = null;
		if(Constants.login_request==requestNo)
		{
			try
			{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpget= new HttpPost(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }
		    catch(Exception e)
		    {
		            Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		    try
		    {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) 
		            {
		                sb.append(line + "\n");      
		            }
		            is.close();
		            result=sb.toString();
		    }
		    catch(Exception e)
		    {
		        Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    try
		    {
		         jArray = new JSONObject(result); 
		    }
		    catch(JSONException e)
		    {
		            Log.e("jArray................", "Error parsing data "+e.toString());
		    }
		    
		    ((LoginActivity)mContext).setResponseLogin(jArray);
		    
		}
		else if(Constants.change_password_request==requestNo)
		{
			try
			{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpget= new HttpPost(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }
		    catch(Exception e)
		    {
		            Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		    try
		    {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) 
		            {
		                sb.append(line + "\n");      
		            }
		            is.close();
		            result=sb.toString();
		    }
		    catch(Exception e)
		    {
		        Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    try
		    {
		         jArray = new JSONObject(result); 
		    }
		    catch(JSONException e)
		    {
		            Log.e("jArray................", "Error parsing data "+e.toString());
		    }
		    
		    ((RegisterActivity)mContext).setResponseChangePassword(jArray);
		    
		}
		else if(Constants.edit_profile_change_password==requestNo)
		{
			try
			{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpget= new HttpPost(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }
		    catch(Exception e)
		    {
		            Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		    try
		    {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) 
		            {
		                sb.append(line + "\n");      
		            }
		            is.close();
		            result=sb.toString();
		    }
		    catch(Exception e)
		    {
		        Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    try
		    {
		         jArray = new JSONObject(result); 
		    }
		    catch(JSONException e)
		    {
		            Log.e("jArray................", "Error parsing data "+e.toString());
		    }
		    
		    profileEditActivity.setResponseChangePassword(jArray);
		    
		}
		else if(Constants.edit_owner_profile_change_password==requestNo)
		{
			try
			{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpget= new HttpPost(url);
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }
		    catch(Exception e)
		    {
		            Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		    try
		    {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) 
		            {
		                sb.append(line + "\n");      
		            }
		            is.close();
		            result=sb.toString();
		    }
		    catch(Exception e)
		    {
		        Log.e("log_tag", "Error converting result "+e.toString());
		    }
		    try
		    {
		         jArray = new JSONObject(result); 
		    }
		    catch(JSONException e)
		    {
		            Log.e("jArray................", "Error parsing data "+e.toString());
		    }
		    
		    profileOwnerEditActivity.setResponseChangePassword(jArray);
		    
		}
		dialog.dismiss();
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
}
