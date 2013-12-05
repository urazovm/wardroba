package com.example.wardroba;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class WardrobaDashboardActivity extends Activity
{
	public Button btnlogin , btnregister;
	Typeface typeface;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.wardroba_dashboard_activity); 
		typeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
		btnlogin = (Button)findViewById(R.id.btn_login);
		btnregister = (Button)findViewById(R.id.btn_register);
		btnlogin.setTypeface(typeface);
		btnregister.setTypeface(typeface);
		btnlogin.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent splace=new Intent(WardrobaDashboardActivity.this,LoginActivity.class);
				startActivity(splace);
			}
		});
		
		btnregister.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent splace=new Intent(WardrobaDashboardActivity.this,RegisterActivity.class);
				startActivity(splace);
			}
		});
		
	}
}
