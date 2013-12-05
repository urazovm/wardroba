package com.TabBar;

import com.example.wardroba.CameraViewActivity;
import com.example.wardroba.HomeActivity;
import com.example.wardroba.ProductGallery;
import com.example.wardroba.ProfileActivity;
import com.example.wardroba.R;
import com.example.wardroba.AboutUsActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class TabbarView extends TabHostProvider 
{
	private Tab HomeTab;
	private Tab ProductListTab;
	private Tab ProductTab;
	private Tab ProfileTab;	
	private Tab TagTab;
	private TabView tabView;
	private Context mcontext;
	public TabbarView(Activity context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TabView getTabHost() 
	{
		tabView = new TabView(context);
		tabView.setBackgroundID(R.drawable.tab);
		
		HomeTab = new Tab(context, "Home");
		HomeTab.setIntent(new Intent(context,HomeActivity.class));
		HomeTab.setIcon(R.drawable.menu_icon_1);
		HomeTab.setIconSelected(R.drawable.menu_icon_1_h);
		
		ProductListTab = new Tab(context, "Tab2");
		ProductListTab.setIntent(new Intent(context,ProductGallery.class));
		ProductListTab.setIcon(R.drawable.menu_icon_2);
		ProductListTab.setIconSelected(R.drawable.menu_icon_2_h);
		
		ProductTab = new Tab(context, "Tab3");
		ProductTab.setIntent(new Intent(context,CameraViewActivity.class));
		ProductTab.setIcon(R.drawable.menu_icon_3);
		ProductTab.setIconSelected(R.drawable.menu_icon_3);
		
		ProfileTab = new Tab(context, "Profile");
		ProfileTab.setIntent(new Intent(context,ProfileActivity.class));
		ProfileTab.setIcon(R.drawable.menu_icon_4);
		ProfileTab.setIconSelected(R.drawable.menu_icon_4_h);
		
		TagTab = new Tab(context, "Tab5");
		TagTab.setIntent(new Intent(context,AboutUsActivity.class));
		TagTab.setIcon(R.drawable.menu_icon_1);
		TagTab.setIconSelected(R.drawable.menu_icon_1_h);
		
		
		tabView.addTab(HomeTab);
		tabView.addTab(ProductListTab);
		tabView.addTab(ProductTab);
		tabView.addTab(ProfileTab);
		tabView.addTab(TagTab);
		
		tabView.setCurrentView(R.layout.profile_view_activity);
		return tabView;
		
	}

}
