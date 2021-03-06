package com.example.wardroba;

import com.connection.Constants;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class HomeTabActivity  extends TabActivity{
	Button firstButton,secondButton,thirdButton,fourthButton;
	TabHost tabHost;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tabbar_lay);

        // tabs        
        firstButton = (Button) findViewById(R.id.firstButton);
        secondButton = (Button) findViewById(R.id.secondButton);        
        thirdButton = (Button) findViewById(R.id.thirdButton);
        fourthButton=(Button)findViewById(R.id.forthButton);
        

        Resources res = getResources(); // Resource object to get Drawables
         tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        intent = new Intent().setClass(this, HomeActivityMain.class);
        spec = tabHost.newTabSpec("first").setIndicator("First").setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, ProductGallery.class);
        spec = tabHost.newTabSpec("second").setIndicator("Second").setContent(intent);
        tabHost.addTab(spec);   

        intent = new Intent().setClass(this, CameraViewMain.class);
        spec = tabHost.newTabSpec("third").setIndicator("Third").setContent(intent);
        tabHost.addTab(spec);
        

        intent = new Intent().setClass(this, ProfileActivity.class);
        spec = tabHost.newTabSpec("forth").setIndicator("Forth").setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0);
        firstButton.setBackgroundResource(R.drawable.menu_icon_1_h);
        firstButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v)
            {
                tabHost.setCurrentTab(0);
                firstButton.setBackgroundResource(R.drawable.menu_icon_1_h);
                secondButton.setBackgroundResource(R.drawable.menu_icon_2);              
                thirdButton.setBackgroundResource(R.drawable.menu_icon_3);
                fourthButton.setBackgroundResource(R.drawable.menu_icon_4);
            }

        });


        secondButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v)
            {
                tabHost.setCurrentTab(1);
                firstButton.setBackgroundResource(R.drawable.menu_icon_1);
                secondButton.setBackgroundResource(R.drawable.menu_icon_2_h);              
                thirdButton.setBackgroundResource(R.drawable.menu_icon_3);
                fourthButton.setBackgroundResource(R.drawable.menu_icon_4);
            }

        });


        thirdButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v)
            {
                Intent intent=new Intent(HomeTabActivity.this,CameraViewMain.class);
                startActivity(intent);
                /*firstButton.setBackgroundResource(R.drawable.menu_icon_1);
                secondButton.setBackgroundResource(R.drawable.menu_icon_2);              
                thirdButton.setBackgroundResource(R.drawable.menu_icon_3_h);
                fourthButton.setBackgroundResource(R.drawable.menu_icon_4);*/
            }

        });

        fourthButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tabHost.setCurrentTab(3);
                firstButton.setBackgroundResource(R.drawable.menu_icon_1);
                secondButton.setBackgroundResource(R.drawable.menu_icon_2);              
                thirdButton.setBackgroundResource(R.drawable.menu_icon_3);
                fourthButton.setBackgroundResource(R.drawable.menu_icon_4_h);
			}
		});

        
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Toast.makeText(getApplicationContext(), "on home resume", Toast.LENGTH_SHORT).show();
		if(Constants.IS_PRODUCT_ADDED)
		{
			tabHost.setCurrentTab(0);
            firstButton.setBackgroundResource(R.drawable.menu_icon_1_h);
            secondButton.setBackgroundResource(R.drawable.menu_icon_2);              
            thirdButton.setBackgroundResource(R.drawable.menu_icon_3);
            fourthButton.setBackgroundResource(R.drawable.menu_icon_4);
		}
	}
}
