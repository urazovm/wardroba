package com.connection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.wardroba.Comment;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.HomeActivity;
import com.example.wardroba.ProductDetailFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class WebAPIHelper1 extends AsyncTask<String, Integer, Long>
{
	private ProgressDialog progressDlg;

	private Dialogs mainActivity;
	private Document response;
	private int requestNumber;
	private String loadingMessage="Please wait...";
	private ProductDetailFragment mFragment;
	Context myContext;
	
	private WebAPIRequest webAPIRequest = new WebAPIRequest();
	
//	public WebAPIHelper1(int requestNumber, Context context, String msg) 
//	{
//		this.requestNumber = requestNumber;
//		this.myContext=context;
//		//progressDlg = new ProgressDialog(context);
//		//loadingMessage = msg;
//	}
	
	public WebAPIHelper1(int requestNumber, Dialogs activity, String msg) 
	{
		this.mainActivity = activity;
		progressDlg = new ProgressDialog(mainActivity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper1(int requestNumber, Context context) 
	{
		this.requestNumber = requestNumber;
		this.myContext=context;
		progressDlg = new ProgressDialog(context);
		//loadingMessage = msg;
	}
	
	public WebAPIHelper1(int requestNumber, ProductDetailFragment pFragment) 
	{
		this.requestNumber = requestNumber;
		this.mFragment=pFragment;
		progressDlg = new ProgressDialog(this.mFragment.getActivity());
		//loadingMessage = msg;
	}
	protected void onPreExecute() 
	{
		/*progressDlg.setMessage(loadingMessage);
		progressDlg.show();*/
	}

	protected Long doInBackground(String... urls) 
	{
		response = webAPIRequest.performGet(urls[0]);
		return null;
	}
	protected void onPostExecute(Long i) 
	{	
		if(requestNumber==Constants.product_like)
		{
			if(response != null)
			{
				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				String status=getValueFromNode(childNode,"status").trim();
				int like_count=parseIntValue(getValueFromNode(childNode,"like_count"));
				Log.d("WebApiHelper1", "Status:"+status);
	  			if(status.equals("LIKE"))
	  			{
	  				Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
	  			}else
	  			{
	  				Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("LIKE");
	  			}
	  			Constants.all_items.get(Constants.SELECTED_ID).setPLikeCount(like_count);
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}
			((HomeActivity)myContext).setResponseFromRequest1(requestNumber);
				//product_activity.setResponseFromRequest(productlist);
		}

		else if(requestNumber==Constants.product_detail_like)
		{
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				String status=getValueFromNode(childNode,"status").trim();
				int like_count=parseIntValue(getValueFromNode(childNode,"like_count"));
				Log.d("WebApiHelper1", "Status:"+status);
	  			if(status.equals("LIKE"))
	  			{
	  				Constants.my_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
	  			}else
	  			{
	  				Constants.my_items.get(Constants.SELECTED_ID).setPLikeStatus("LIKE");
	  			}
	  			Constants.my_items.get(Constants.SELECTED_ID).setPLikeCount(like_count);
	  			
	  			mFragment.setResponseFromRequest1(requestNumber);
				
			}
		}
		else if(requestNumber==Constants.comment_add)
		{
			Comment comment=new Comment();
			if(response != null)
			{
				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				
				comment.setComment_id(getValueFromNode(childNode,"id_comment"));
                comment.setStore_name(getValueFromNode(childNode,"store_name"));
                comment.setComment(getValueFromNode(childNode,"comment"));
                comment.setDate(getValueFromNode(childNode,"created_at"));
                Log.d("ApiHelper", "comment:"+comment.getComment());
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}

			((CommentViewActivity)myContext).setResponseFromRequest1(comment);
		}
		else if(requestNumber==Constants.comment_delete)
		{
			
			if(response != null)
			{
				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");
				Log.d("WebApiHelper1", "Delete comment:");
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}

			((CommentViewActivity)myContext).setResponseForDelete();
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
			
	

