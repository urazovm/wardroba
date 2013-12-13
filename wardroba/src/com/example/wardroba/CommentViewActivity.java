package com.example.wardroba;

import java.util.ArrayList;

import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.facebook.Request;

import BaseAdapter.CommentBaseAdapter;
import BaseAdapter.HomeProductBaseAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentViewActivity extends Activity
{
	public ImageView btnBack;
	public ListView lsvComment;
	public TextView txtHeader,txtOK;
	public EditText edtAddComment;
	String MSG;
	Typeface tf;
	CommentBaseAdapter adapter;
	
	private ArrayList<Comment> arr_CommentList;
	
	
  	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest(int requestNumber, Object obj) 
  	{		  		 		
  		arr_CommentList =(ArrayList<Comment>) obj;
  		if(arr_CommentList != null)
  		{  			 
  			Log.d("CommentList", "array:"+arr_CommentList.size());
  			if(arr_CommentList.size() >0)
  			{
  				adapter=new CommentBaseAdapter(arr_CommentList,CommentViewActivity.this);
  				adapter.notifyDataSetChanged();
  				lsvComment.setAdapter(adapter);
  			}else
  			{
  				lsvComment.setAdapter(null);
  	  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  			}
  		}
  		else
  		{
  			
  			lsvComment.setAdapter(null);
  			Toast.makeText(getApplicationContext(), "No Record Found !", 5000).show();
  		}
  	} 	
  	
	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest1(Comment c) 
  	{		  		
		if(arr_CommentList.size()==0)
		{
			arr_CommentList=new ArrayList<Comment>();
			adapter=new CommentBaseAdapter(arr_CommentList,CommentViewActivity.this);
	    	adapter.notifyDataSetChanged();
			lsvComment.setAdapter(adapter);
		}
		arr_CommentList.add(c);
		adapter.notifyDataSetChanged();
  	} 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_view_activity);
		
		btnBack=(ImageView)findViewById(R.id.img_back);
		
		lsvComment=(ListView)findViewById(R.id.list_cooment);
		
		txtHeader=(TextView)findViewById(R.id.txt_header);
		txtOK=(TextView)findViewById(R.id.txt_ok);
		
		edtAddComment=(EditText)findViewById(R.id.edt_comment);
		
		tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		txtHeader.setTypeface(tf);
		txtOK.setTypeface(tf);
		edtAddComment.setTypeface(tf);
		
		if(isOnline()==true)
		{
			try
			{
				WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.comment_list,CommentViewActivity.this ,"Please Wait....");
				String url = Constants.VIEW_COMMENT_URL+"&object_id="+Constants.OBJECT_ID+"&user_id="+Constants.CLOTH_USERID+"&cloth_type="+Constants.CLOTH_TYPE;	
				Log.d("Comment View= ",url.toString());
				webAPIHelper.execute(url);    	
				
				//http://dev.wardroba.com/serviceXml/comment.php?object_id=560&user_id=66
			}
			catch(Exception e)
			{
				
			}				
		}
		else
		{
			MSG="Please check your internet connection...";
			alert();
		}
		
		BackButton();
		CommentAddButton();
	}
	
	public void BackButton()
	{
		btnBack.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				onBackPressed();
			}
		});
	}
	
	public void CommentAddButton()
	{
		txtOK.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				String CommentText=edtAddComment.getText().toString().trim();
				if(CommentText.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please add a comment...",5000).show();	
				}else
				{
					if(isOnline()==true)
					{
						try
						{
							WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.comment_add,CommentViewActivity.this);
							String url = Constants.ADD_COMMENT_URL+"&object_id="+Constants.OBJECT_ID+"&user_id="+Constants.CLOTH_USERID+"&comment="+CommentText+"&cloth_type="+Constants.CLOTH_TYPE;	
							Log.d("Comment View= ",url.toString());
							webAPIHelper.execute(url);    	
							//http://dev.wardroba.com/serviceXml/product_comment.php?object_id=560&user_id=66&comment=testing

						}
						catch(Exception e)
						{
							
						}				
					}
					else
					{
						MSG="Please check your internet connection...";
						alert();
					}
				}
				
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
        
   protected void onPause() 
   {
	   super.onPause();	
   } 
   
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(CommentViewActivity.this);
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
}
