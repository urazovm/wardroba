package com.connection;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.wardroba.ProfileEditActivity;
import com.example.wardroba.LoginActivity;
import com.example.wardroba.ProductGallery;
import com.example.wardroba.ProfileActivity;
import com.example.wardroba.RegisterActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class WebAPIHelper extends AsyncTask<String, Integer, Long>
{
	private ProgressDialog progressDlg;
	private LoginActivity login_activity;
	private RegisterActivity register_activity;
	private ProfileActivity profile_activity;
	private ProfileEditActivity edit_profile_activity;
	private ProductGallery product_activity;
	

	
	private Dialogs mainActivity;
	private Document response;
	private int requestNumber;
	private String loadingMessage;
	private WebAPIRequest webAPIRequest = new WebAPIRequest();

	public WebAPIHelper(int requestNumber, ProfileActivity activity, String msg) 
	{
		this.profile_activity = activity;
		progressDlg = new ProgressDialog(profile_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper(int requestNumber, ProductGallery activity, String msg) 
	{
		this.product_activity = activity;
		progressDlg = new ProgressDialog(product_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper(int requestNumber, ProfileEditActivity activity, String msg) 
	{
		this.edit_profile_activity = activity;
		progressDlg = new ProgressDialog(edit_profile_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	public WebAPIHelper(int requestNumber, LoginActivity activity, String msg) 
	{
		this.login_activity = activity;
		progressDlg = new ProgressDialog(login_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper(int requestNumber, RegisterActivity activity, String msg) 
	{
		this.register_activity = activity;
		progressDlg = new ProgressDialog(register_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}		

	public WebAPIHelper(int requestNumber, Dialogs activity, String msg) 
	{
		this.mainActivity = activity;
		progressDlg = new ProgressDialog(mainActivity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	protected void onPreExecute() 
	{
		progressDlg.setMessage(loadingMessage);
		progressDlg.show();
	}

	protected Long doInBackground(String... urls) 
	{
		response = webAPIRequest.performGet(urls[0]);
		return null;
	}
	protected void onPostExecute(Long i) 
	{	
		if (requestNumber == Constants.register_list)
		{			 
			if(response != null)
			{
				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
			
				Constants.REGISTER_USERID = parseIntValue(getValueFromNode(childNode,"id"));
				Constants.MSG_REGISTER = getValueFromNode(childNode,"msg");
			}
			else 
			{
				Constants.REGISTER_USERID = 0;				
				Constants.MSG_REGISTER = "";
			}	
			register_activity.setResponseFromRequest(requestNumber);
		}
		
		else if (requestNumber == Constants.profile_list)
		{			 
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);

				Constants.USERID = parseIntValue(getValueFromNode(childNode,"id_user"));
				Constants.USEREMAIL = getValueFromNode(childNode,"email");
				Constants.USERGENDER = getValueFromNode(childNode,"gender");
			}	
			profile_activity.setResponseFromRequest(requestNumber);
		}
		else if (requestNumber == Constants.editprofile_list)
		{			 
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);

				Constants.EDITUSERID = parseIntValue(getValueFromNode(childNode,"id_user"));
				Constants.EDITUSEREMAIL = getValueFromNode(childNode,"email");
				Constants.EDITUSERGENDER = getValueFromNode(childNode,"gender");
			}	
			edit_profile_activity.setResponseFromRequest(requestNumber);
		}
		else if (requestNumber == Constants.editprofile_list2)
		{			 
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				
				Constants.EDITEDUSERID = parseIntValue(getValueFromNode(childNode,"user_id"));
				Constants.EDITUSEREMSG = getValueFromNode(childNode,"msg");
			}
			else
			{
				Constants.EDITEDUSERID = 0;
			}
			edit_profile_activity.setResponseFromRequest2(requestNumber);
		}
		else if (requestNumber == Constants.fblogin_request)
		{			 
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				
				Constants.LOGIN_USERID = parseIntValue(getValueFromNode(childNode,"id"));
				Constants.MSG_LOGIN = getValueFromNode(childNode,"msg");
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}
			login_activity.setResponseFromRequest2(requestNumber);
		}
		else if (requestNumber == Constants.fblogin_request1)
		{			 
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				
				Constants.LOGIN_USERID = parseIntValue(getValueFromNode(childNode,"id"));
				Constants.MSG_LOGIN = getValueFromNode(childNode,"msg");
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}
			register_activity.setResponseFromRequest2(requestNumber);
		}
		progressDlg.dismiss();
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
			
	

