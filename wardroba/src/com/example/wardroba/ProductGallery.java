package com.example.wardroba;

import com.TabBar.TabHostProvider;
import com.TabBar.TabView;
import com.TabBar.TabbarView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ProductGallery extends Activity
{
	
	public TabHostProvider tabProvider;
    public TabView tabView;
    
	
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);       
        
        tabProvider = new TabbarView(this);
		tabView = tabProvider.getTabHost();
		tabView.setCurrentView(R.layout.product_gallery_activity);
		setContentView(tabView.render(0)); 
		
	
   }
}
