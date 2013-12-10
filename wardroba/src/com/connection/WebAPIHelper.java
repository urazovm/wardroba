package com.connection;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.wardroba.Comment;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.HomeActivity;
import com.example.wardroba.ProductGalleryGridFragment;
import com.example.wardroba.ProfileEditActivity;
import com.example.wardroba.LoginActivity;
import com.example.wardroba.ProductGallery;
import com.example.wardroba.ProfileActivity;
import com.example.wardroba.RegisterActivity;
import com.example.wardroba.WardrobaProfile;

import android.app.ProgressDialog;
import android.content.Context;
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
	private CommentViewActivity comment_view_activity;

	private ProductGalleryGridFragment productGalleryGridFragment;

	private HomeActivity  home_activity;
	private Dialogs mainActivity;
	private Document response;
	private int requestNumber;
	private String loadingMessage;
	Context myContext;
	
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
	public WebAPIHelper(int requestNumber, HomeActivity activity, String msg) 
	{
		this.home_activity = activity;
		progressDlg = new ProgressDialog(home_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper(int requestNumber, ProductGalleryGridFragment activity, String msg) 
	{
		this.productGalleryGridFragment = activity;
		progressDlg = new ProgressDialog(productGalleryGridFragment.getActivity());
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	
	public WebAPIHelper(int requestNumber, CommentViewActivity activity, String msg) 
	{
		this.comment_view_activity = activity;
		progressDlg = new ProgressDialog(comment_view_activity);
		this.requestNumber = requestNumber;
		loadingMessage = msg;
	}
	public WebAPIHelper(int requestNumber, Context context, String msg) 
	{
		this.requestNumber = requestNumber;
		this.myContext=context;
		progressDlg = new ProgressDialog(context);
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
			WardrobaProfile myProfile=new WardrobaProfile();
			if(response != null)
			{
				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);

				myProfile.setId(parseIntValue(getValueFromNode(childNode,"id")));
				myProfile.setName(getValueFromNode(childNode,"name"));
				myProfile.setLastname(getValueFromNode(childNode,"lastname"));
				myProfile.setUsername(getValueFromNode(childNode,"username"));
				myProfile.setCity(getValueFromNode(childNode,"city"));
				myProfile.setAddress(getValueFromNode(childNode,"address"));
				myProfile.setEmail(getValueFromNode(childNode,"email"));
				myProfile.setUser_image(getValueFromNode(childNode,"user_image"));
				myProfile.setItems(parseIntValue(getValueFromNode(childNode,"items")));
				myProfile.setFollower(parseIntValue(getValueFromNode(childNode,"followers")));
				myProfile.setFollowing(parseIntValue(getValueFromNode(childNode,"following")));
			}	
			profile_activity.setResponseFromRequest(requestNumber,myProfile);
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
		else if (requestNumber == Constants.product_list)
		{		
			ArrayList<Constants> arr_ProductList = null;
			if(response != null)
			{				
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList noderesult=  node.getElementsByTagName("result");
				Element result = (Element) noderesult.item(0);
				
				Constants.USER_NAME = getValueFromNode(result,"username");
				Constants.USER_IMAGE = getValueFromNode(result,"user_image");
				Constants.USER_DATE = getValueFromNode(result,"date");
				
				
				NodeList cloth =  result.getElementsByTagName("cloth");
				
				for(int j=0; j<cloth.getLength(); j++)
				{
					if(arr_ProductList == null)
					{
						arr_ProductList = new ArrayList<Constants>();
					}					
					Constants Product_list=new Constants();
					Element optionchildNode = (Element) cloth.item(j);

					Product_list.PIdCloth = parseIntValue(getValueFromNode(optionchildNode,"id_cloth"));
					Product_list.PUserId = parseIntValue(getValueFromNode(optionchildNode,"user_id"));
					Product_list.PObjectId = parseIntValue(getValueFromNode(optionchildNode,"object_id"));
					Product_list.PLikeCount = parseIntValue(getValueFromNode(optionchildNode,"like_count"));
					Product_list.PCommentCount = parseIntValue(getValueFromNode(optionchildNode,"comment_count"));
					Product_list.PViewCount = parseIntValue(getValueFromNode(optionchildNode,"view_count"));
					Product_list.PProductRange = getValueFromNode(optionchildNode,"price_range");
					Product_list.PPrice = getValueFromNode(optionchildNode,"price");
					Product_list.PDescription = getValueFromNode(optionchildNode,"description");
					Product_list.PCategoryname = getValueFromNode(optionchildNode,"category_name");
					Product_list.PDiscountedPrice = getValueFromNode(optionchildNode,"discounted_price");
					Product_list.PDiscpontPerc = getValueFromNode(optionchildNode,"discount_perc");
					Product_list.PSeasonName = getValueFromNode(optionchildNode,"season_name");
					Product_list.PDesigner = getValueFromNode(optionchildNode,"designer");
					Product_list.PShortDescription = getValueFromNode(optionchildNode,"Shortdescription");
					Product_list.PImageUrl = getValueFromNode(optionchildNode,"img_url");
					Product_list.PLikeStatus = getValueFromNode(optionchildNode,"like_statue");
					
					arr_ProductList.add(Product_list);
				}
			}
			home_activity.setResponseFromRequest(requestNumber,arr_ProductList);
		}
		else if(requestNumber==Constants.search_list)
		{
			List<Constants> productlist=null;
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element result = (Element) nlist.item(0);

				NodeList cloth =  result.getElementsByTagName("cloth");

                for(int j=0; j<cloth.getLength(); j++)
                {
                        if(productlist == null)
                        {
                                productlist = new ArrayList<Constants>();
                        }
                        Constants Product_list=new Constants();
                        Element optionchildNode = (Element) cloth.item(j);

                        Product_list.GIdCloth =parseIntValue(getValueFromNode(optionchildNode,"id_cloth"));
                        Product_list.GUserId =parseIntValue(getValueFromNode(optionchildNode,"user_id"));
                        Product_list.GObjectId =parseIntValue(getValueFromNode(optionchildNode,"object_id"));
                        Product_list.GLikeCount =parseIntValue(getValueFromNode(optionchildNode,"like_count"));
                        Product_list.GCommentCount =parseIntValue(getValueFromNode(optionchildNode,"comment_count"));
                        Product_list.GViewCount =parseIntValue(getValueFromNode(optionchildNode,"view_count"));
                        Product_list.GProductRange = getValueFromNode(optionchildNode,"price_range");
                        Product_list.GPrice = getValueFromNode(optionchildNode,"price");
                        Product_list.GDescription =getValueFromNode(optionchildNode,"description");
                        Product_list.GShortDescription =getValueFromNode(optionchildNode,"Shortdescription");
                        Product_list.GImageUrl = getValueFromNode(optionchildNode,"img_url");
                        Product_list.GLikeStatus = getValueFromNode(optionchildNode,"like_statue");
                        Product_list.GCategoryname=getValueFromNode(optionchildNode,"category_name");
                        Product_list.GDiscountedPrice=getValueFromNode(optionchildNode,"discounted_price");
                        Product_list.GSeasonName=getValueFromNode(optionchildNode,"season_name");
                        Product_list.GDesigner=getValueFromNode(optionchildNode,"designer");
                        
                        
                        Log.d("Product", "Image:"+Product_list.GImageUrl);
                        productlist.add(Product_list);
                	}
				}

				productGalleryGridFragment.setResponseFromRequest(productlist);
			
		}
		else if(requestNumber==Constants.product_like)
		{
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				Element childNode = (Element)nlist.item(0);
				
				Constants.LIKE_STATUS = getValueFromNode(childNode,"status");
				Constants.LIKE_COUNT = parseIntValue(getValueFromNode(childNode,"like_count"));
			}
			else
			{
				Constants.LOGIN_USERID = 0;
			}
			((HomeActivity)myContext).setResponseFromRequest1(requestNumber);
				//product_activity.setResponseFromRequest(productlist);
		}
		else if(requestNumber==Constants.comment_list)
		{
			ArrayList<Comment> arr_CommentList = null;
			if(response != null)
			{
				Element node = (Element) response.getElementsByTagName("root").item(0);
				NodeList nlist =  node.getElementsByTagName("result");				
				
                for(int j=0; j<nlist.getLength(); j++)
                {
                        if(arr_CommentList == null)
                        {
                        	arr_CommentList = new ArrayList<Comment>();
                        }
                        Comment comment=new Comment();
                        Element optionchildNode = (Element) nlist.item(j);
                        String MSG=(getValueFromNode(optionchildNode,"msg"));
                        if(MSG.equals("Comment not found!"))
                        	break;
                       
	                        comment.setComment_id(getValueFromNode(optionchildNode,"id_comment"));
	                        comment.setStore_name(getValueFromNode(optionchildNode,"store_name"));
	                        comment.setComment(getValueFromNode(optionchildNode,"comment"));
	                        comment.setDate(getValueFromNode(optionchildNode,"created_at"));
	
	                        arr_CommentList.add(comment);
                        
                	}
				}

				comment_view_activity.setResponseFromRequest(requestNumber,arr_CommentList);
			
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
			
	

