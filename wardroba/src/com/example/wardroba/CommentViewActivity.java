package com.example.wardroba;

import com.facebook.Request;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentViewActivity extends Activity
{
	public ImageView btnBack;
	public ListView lsvComment;
	public TextView txtHeader,txtOK;
	public EditText edtAddComment;
	
	Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_view_activity);
		
		btnBack=(ImageView)findViewById(R.id.btn_back);
		
		lsvComment=(ListView)findViewById(R.id.list_cooment);
		
		txtHeader=(TextView)findViewById(R.id.txt_header);
		txtOK=(TextView)findViewById(R.id.txt_ok);
		
		edtAddComment=(EditText)findViewById(R.id.edt_comment);
		
		tf= Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		txtHeader.setTypeface(tf);
		txtOK.setTypeface(tf);
		edtAddComment.setTypeface(tf);
		
	}
}
