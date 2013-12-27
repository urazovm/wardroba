package com.connection;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.wardroba.Comment;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.HomeActivityFragment;
import com.example.wardroba.ProductDetailFragment;
import com.example.wardroba.ProductGalleryGridFragment;
import com.example.wardroba.ProfileEditActivity;
import com.example.wardroba.LoginActivity;
import com.example.wardroba.ProductGallery;
import com.example.wardroba.ProfileActivity;
import com.example.wardroba.ProfileOwnerEditActivity;
import com.example.wardroba.ProfileOwnerViewFragment;
import com.example.wardroba.ProfileViewFragment;
import com.example.wardroba.RegisterActivity;
import com.example.wardroba.WardrobaItem;
import com.example.wardroba.WardrobaProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;


public class LoadingListHelper extends AsyncTask<String, Integer, Long>
{
	
	private HomeActivityFragment  home_activity;
	private Dialogs mainActivity;
	private Document response;
	private int requestNumber;
	private String loadingMessage;
	Context myContext;
	
	private WebAPIRequest webAPIRequest = new WebAPIRequest();

			
	public LoadingListHelper(int requestNumber, HomeActivityFragment activity, String msg) 
	{
		this.home_activity = activity;
	
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	
	

	protected Long doInBackground(String... urls) 
	{
		response = webAPIRequest.performGet(urls[0]);
		return null;
	}
	protected void onPostExecute(Long i) 
	{	
		
		 if (requestNumber == Constants.product_list)
		{		
			//Constants.all_items.clear();
			if(response != null)
			{				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList noderesult=  node.getElementsByTagName("result");
				Element result = (Element) noderesult.item(0);
				
				NodeList cloth =  result.getElementsByTagName("cloth");
				
				for(int j=0; j<cloth.getLength(); j++)
				{
									
					WardrobaItem item=new WardrobaItem();
					Element optionchildNode = (Element) cloth.item(j);

					item.setPUserName( getValueFromNode(optionchildNode,"username"));
					item.setPUserImage( getValueFromNode(optionchildNode,"user_image"));
					item.setPUserDate( getValueFromNode(optionchildNode,"date"));
					
					item.setPIdCloth(parseIntValue(getValueFromNode(optionchildNode,"id_cloth")));
					item.setPUserId(parseIntValue(getValueFromNode(optionchildNode,"user_id")));
					item.setPOwnerId(parseIntValue(getValueFromNode(optionchildNode,"owner_id")));
					item.setPObjectId(parseIntValue(getValueFromNode(optionchildNode,"object_id")));
					item.setPLikeCount(parseIntValue(getValueFromNode(optionchildNode,"like_count")));
					item.setPCommentCount(parseIntValue(getValueFromNode(optionchildNode,"comment_count")));
					item.setPViewCount(parseIntValue(getValueFromNode(optionchildNode,"view_count")));
					item.setPTag( getValueFromNode(optionchildNode,"tags"));
					item.setPImageUrl( getValueFromNode(optionchildNode,"img_url"));
					item.setPLikeStatus(getValueFromNode(optionchildNode,"like_statue"));
					item.setPClothType(getValueFromNode(optionchildNode,"cloth_type"));
					Log.d("Like status:", "status:"+item.getPLikeStatus()+".");
					Constants.all_items.add(item);
				}
			}
			Log.d("hello", "total records:"+Constants.all_items.size());
			home_activity.setResponseOfLoadingMore(requestNumber);
		}
		
	}
		
	private float parsefloatValue(String valueFromNode) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	private String gtValueFromNode(Element node, String string) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	String getValueFromNode(Element childNode, String tagName)
	{
		String strValue = "";
		try
		{
			Element node = (Element)childNode.getElementsByTagName(tagName).item(0);
			for(int i=0;i<node.getChildNodes().getLength();i++)
			{
				strValue = strValue.concat(node.getChildNodes().item(i).getNodeValue());
			}
		}
		catch(Exception exp)
		{ 		
		}
		return strValue;
	}

	int parseIntValue(String strValue)
	{
		int value=0;
		if(strValue!=null && strValue.length()>0)
		{
			value = Integer.parseInt(strValue);
		}
		return value;
	}

	double parseDoubleValue(String strValue)
	{
		double value=0.0;
		if(strValue!=null && strValue.length()>0)
		{
			value = Double.parseDouble(strValue);
		}
		return value;
	}
}
			
	

