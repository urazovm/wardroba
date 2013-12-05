package com.connection;

import java.io.Serializable;

public class Constants implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
//	public static final String REGISTER_URL = "http://project.amphee.net/php/locator/xml/useradd.php?";
//	public static final String LOGIN_URL = "http://project.amphee.net/php/locator/xml/login.php?";
//	public static final String PROFILE_URL = "http://project.amphee.net/php/locator/xml/user.php?";
//	public static final String EDIT_PROFILE_URL = "http://project.amphee.net/php/locator/xml/useredit.php?";
//	public static final String LIKE_PRODUCT_URL = "http://iselectbooking.com/demo/iphone/addtofavorite.php?";
//	public static final String SEARCH_URL = "http://www.lisaci.com/iphone/searchproduct.php?";
//	public static final String PRODUCT_DETAIL_URL = "http://www.lisaci.com/iphone/product.php?";
   
	public static final String LOGIN_JSON_URL="http://dev.wardroba.com/api-login?";
	
	public static final String REGISTER_URL="http://dev.wardroba.com/serviceXml/register.php?";
	public static final String FBLOGIN_URL="http://dev.wardroba.com/serviceXml/fbuserlogin.php?";
	public static final String PROFILE_VIEW_URL="http://dev.wardroba.com/serviceXml/profile.php?";
	public static final String PROFILE_SAVE_URL="http://dev.wardroba.com/serviceXml/updateprofile.php?";
	public static final String PROFILE_PHOTO_SAVE_URL="http://dev.wardroba.com/serviceXml/userimage.php?";
	public static final String HOME_PRODUCT_URL="http://dev.wardroba.com/serviceXml/product_listing.php?";
	public static final String PRODUCT_LIKE_URL="http://dev.wardroba.com/serviceXml/product_like.php?";
	public static final String VIEW_COMMENT_URL="http://dev.wardroba.com/serviceXml/comment.php?";
	public static final String ADD_COMMENT_URL="http://dev.wardroba.com/serviceXml/product_comment.php?";
	public static final String PRODUCT_SEARCH_URL="http://dev.wardroba.com/serviceXml/product_search.php?";
	public static final String PRODUCT_DETAIL_URL="http://dev.wardroba.com/serviceXml/product.php?";
	public static final String IMAGEURL="http://dev.wardroba.com/serviceXml/productimage.php?";
	public static final String PROUCTADDURL="http://dev.wardroba.com/serviceXml/product_add.php?";
	public static final String CHANGE_PASSWARD_URL="http://dev.wardroba.com/api-update-password?";
	public static final String ITEM_COUNT_URL="http://dev.wardroba.com/serviceXml/product_count.php?";
	
	public final static int register_list = 50;
	public final static int login_list = 51;
	public final static int profile_list = 52;
	public final static int editprofile_list = 53;
	public final static int editprofile_list2 = 54;
	public final static int productlike_list = 55;
	public final static int search_list = 56;
	public final static int product_detail_list = 57;
	public final static int login_request = 58;
	public final static int change_password_request = 59;
	public final static int fblogin_request = 60;
	public final static int fblogin_request1 = 61;
 	public static String USERNAME,MSG_LOGIN;
 	public static int REGISTER_USERID;
 	public static String MSG_REGISTER;	
 	public static int LOGIN_USERID;
 	public static String LOGINFORM;
 	public static int LOGOUTID;
 	
 	public static String USEREMAIL;
 	public static String USERGENDER;
 	public static int USERID;

 	public static String EDITUSEREMAIL;
 	public static String EDITUSERGENDER;
 	public static int EDITUSERID;
 	public static String EDITUSEREMSG;
 	public static int EDITEDUSERID;

 	public static int PRODUCTID;
 	public static String PRODUCT_LIKE_MSG;


	//http://www.lisaci.com/iphone/product.php?id_product=117
	public String PDId,PDName,PDDescription,PDPrice,PDshipping,PDApproximatelyprice,PDImageUrl,PDUrl,PDBigUrl;

//	//http://www.lisaci.com/iphone/wishlistview.php?id_member=95
//	public String WishId,WishImage,WishProductId,WishProductName,WishBrand,WishPrice;

	
//	//http://www.lisaci.com/iphone/addtocart.php?addtocart=154&id_member=95&qty=1
//	public String CartDeleteId,CartImage,CartProductName,CartPostage,CartQty,CartUnit_Price,CartTotal;
//	public int CartProductId;

	//http://www.lisaci.com/iphone/searchproduct.php?search_qry=te
	public String SProductId,SPName,SPDescription,SPPrice,SPImage;

//	//http://www.lisaci.com/iphone/checkout_1.php?id_member=95
//	public String AAddressID,AName,AAddress,ACountry,ACountry_Id,ARegion,ARegion_Id,ACity,AMobileNo;

//	//http://www.lisaci.com/iphone/checkout_2.php?id_member=95&id_country=99&id_region=1485&id_address=1
//	public String APProductID,APImage,APProductName,APPostage,APQty,APUnitPrice,APTotal;
	
//	//http://www.lisaci.com/iphone/checkout_3.php?
//	public String CardAttributeValue,CardValue;
//	public String MonthAttributeValue,MonthValue;
//	public String YearAttributeValue,YearValue;
	
//	//http://www.lisaci.com/iphone/product.php?id_product=118
//	public String TotalReview,UserName,UserImage,Comment,Date,Location;
	
	//http://www.lisaci.com/iphone/changepassword.php?
	
	//http://www.lisaci.com/iphone/contactus.php?

}

