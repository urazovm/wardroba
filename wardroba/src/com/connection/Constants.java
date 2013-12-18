package com.connection;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.wardroba.WardrobaItem;

public class Constants implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
   
	public static final String LOGIN_JSON_URL="http://dev.wardroba.com/api-login?";
	
	public static final String REGISTER_URL="http://dev.wardroba.com/serviceXml/register.php?";
	public static final String FBLOGIN_URL="http://dev.wardroba.com/serviceXml/fbuserlogin.php?";
	public static final String PROFILE_VIEW_URL="http://dev.wardroba.com/serviceXml/profile.php?";
	public static final String PROFILE_SAVE_URL="http://dev.wardroba.com/serviceXml/updateprofile.php?";
	public static final String PROFILE_PHOTO_SAVE_URL="http://dev.wardroba.com/serviceXml/userimage.php?";
	public static final String HOME_PRODUCT_URL="http://dev.wardroba.com/serviceXml/all_items.php?";
	public static final String PRODUCT_LIKE_URL="http://dev.wardroba.com/serviceXml/product_like.php?";
	public static final String VIEW_COMMENT_URL="http://dev.wardroba.com/serviceXml/comment.php?";
	public static final String ADD_COMMENT_URL="http://dev.wardroba.com/serviceXml/product_comment.php?";
	public static final String PRODUCT_SEARCH_URL="http://dev.wardroba.com/serviceXml/all_my_items.php?";
	public static final String PRODUCT_DETAIL_URL="http://dev.wardroba.com/serviceXml/product.php?";
	public static final String IMAGEURL="http://dev.wardroba.com/serviceXml/productimage.php?";
	public static final String PROUCTADDURL="http://dev.wardroba.com/serviceXml/product_add.php?";
	public static final String CHANGE_PASSWARD_URL="http://dev.wardroba.com/api-update-password?";
	public static final String ITEM_COUNT_URL="http://dev.wardroba.com/serviceXml/product_count.php?";
	public static final String PRODUCT_DELETE_URL="http://dev.wardroba.com/serviceXml/product_delete.php?";
    
	public final static int register_list = 50;
	public final static int login_list = 51;
	public final static int profile_list = 52;
	public final static int editprofile_list = 53;
	public final static int editprofile_list2 = 54;
	public final static int search_list = 56;
	public final static int login_request = 58;
	public final static int change_password_request = 59;
	public final static int fblogin_request = 60;
	public final static int fblogin_request1 = 61;
	public final static int product_list = 62;
	public final static int product_like = 63;
	public final static int product_detail_like = 64;
	public final static int comment_list = 65;
	public final static int comment_add = 66;
	public final static int produce_delete = 67;

	
 	public static String USERNAME,MSG_LOGIN;
 	public static int REGISTER_USERID;
 	public static String MSG_REGISTER;	
 	public static int LOGIN_USERID;
 	public static String LOGINFORM;
 	public static int LOGOUTID;
 	public static String CLOTHISID,CLOTH_USERID,OBJECT_ID,CLOTH_TYPE;
 	
 	public static String USEREMAIL;
 	public static String USERGENDER;
 	public static int USERID;

 	public static String EDITUSEREMAIL;
 	public static String EDITUSERGENDER;
 	public static int EDITUSERID;
 	public static String EDITUSEREMSG;
 	public static int EDITEDUSERID;

 	public static String LIKE_STATUS;
 	public static int LIKE_COUNT,SELECTED_ID;
 // login session variable
 	


 	public int GIdCloth,GUserId,GObjectId,GLikeCount,GCommentCount,GViewCount;
    public String GProductRange,GPrice,GDescription,GCategoryname,GDiscountedPrice,GDiscpontPerc,GSeasonName,GDesigner,GShortDescription,GImageUrl,GLikeStatus;
     
     //public static String USER_NAME,USER_IMAGE,USER_DATE;
 	
	
 	// login session variable
 	public static String KEY_LOGIN_ID="login_id";
 	public static ArrayList<WardrobaItem> all_items=new ArrayList<WardrobaItem>();
 	public static ArrayList<WardrobaItem> my_items=new ArrayList<WardrobaItem>();

    
 	
	

}

