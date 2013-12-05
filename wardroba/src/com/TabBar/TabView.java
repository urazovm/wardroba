package com.TabBar;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class TabView {

	private List<Tab> tabSet = new ArrayList<Tab>();
	private int mHeaderHeight;
	private Context context;
	private View currentView;

	private int backgroundID;
	private int selectedTabId = 0;

	public TabView(Context context) 
	{
		this.context = context;
	}

	public void addTab(Tab tab) 
	{
		tab.preferedHeight = mHeaderHeight;
		tabSet.add(tab);
	}

	public View render(int selectedTabId) 
	{
		this.selectedTabId = selectedTabId;
		return renderBOTTOM();
	}

	public View renderBOTTOM() 
	{
		int tabsize = tabSet.size();
		FrameLayout.LayoutParams pTable = new FrameLayout.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT);

		TableLayout table = new TableLayout(context);
		table.setLayoutParams(pTable);

		TableRow rowTop = new TableRow(context);

		TableLayout.LayoutParams pRowTop = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT);
		pRowTop.weight = 1;

		TableRow.LayoutParams pSpan = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT);
		pSpan.span = tabsize;
		pSpan.weight = 1;

		rowTop.addView(currentView, pSpan);

		TableRow rowBottom = new TableRow(context);
		rowBottom.setBackgroundResource(backgroundID);
		TableLayout.LayoutParams pRowBottom = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT);
		int j = 0;

		context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();

		for (int i = 0; i < tabsize; i++) 
		{
			Tab tab = tabSet.get(i);
			if (i == selectedTabId)
				tab.setSelected(true);
			View view = tab.getView();

			TableRow.LayoutParams pCol = new TableRow.LayoutParams();
			pCol.width = display.getWidth() / tabSet.size();

			rowBottom.addView(view, pCol);



		}

		table.addView(rowTop, pRowTop);
		table.addView(rowBottom, pRowBottom);

		return table;

	}
		/*
	 * @Override protected void dispatchDraw(Canvas canvas) {
	 * //canvas.drawBitmap(mHeader, 0, 0, null); super.dispatchDraw(canvas); }
	 */
	public void setCurrentView(View currentView) 
	{
		this.currentView = currentView;
	}

	public void setCurrentView(int resourceViewID) 
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceViewID, null);
		setCurrentView(view);
	}


	public void setBackgroundID(int backgroundID) 
	{
		this.backgroundID = backgroundID;
	}

	

	public Tab getTab(String tag) {
		for (int i = 0; i < tabSet.size(); i++) 
		{
			Tab t = tabSet.get(i);
			if (tag.equals(t.getTag())) 
			{
				return t;
			}
		}
		throw new IllegalArgumentException("Tab \"" + tag + "\" not found");
	}

}
