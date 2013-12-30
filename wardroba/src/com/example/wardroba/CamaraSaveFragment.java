package com.example.wardroba;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.connection.Constants;
import com.connection.WebAPIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CamaraSaveFragment extends Fragment 
{

	ImageView btnUndo,btnSave;
	Typeface tf;
	TextView txtTopwear,txtBottomwear,txtShoes,txtAccessory;
	EditText editTags;
	int itemHoverColor,itemColor;
	LinearLayout layTop;
	Bundle bundle;
	Bitmap productBitmap;
	private static boolean CLOTH_TYPE_SELECTED=false;
	private int clothtype,clothsex;
	private String description;
	public void setResponseFromRequest(int requestNo,String id_cloth,String msg)
	{
		if(id_cloth!=null && msg!=null)
		{
			Toast.makeText(getActivity(), "Product saved successfully", Toast.LENGTH_SHORT).show();
			Toast.makeText(getActivity(), "Cloth ID:"+id_cloth, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getActivity(), "Product not saved", Toast.LENGTH_SHORT).show();
		}

	}
  	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.camera_view_save_activity, null);
        //getActivity().findViewById(R.id.btnBackHome).setVisibility(View.GONE);
        tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        layTop=(LinearLayout)root.findViewById(R.id.top_lay);
        btnSave=(ImageView)root.findViewById(R.id.btnSave);
        btnUndo=(ImageView)root.findViewById(R.id.btnUndo);
        txtTopwear=(TextView)root.findViewById(R.id.txt_topwear);
        txtBottomwear=(TextView)root.findViewById(R.id.txt_bottamwear);
        txtShoes=(TextView)root.findViewById(R.id.txt_shoes);
        txtAccessory=(TextView)root.findViewById(R.id.txt_accessories);
        editTags=(EditText)root.findViewById(R.id.edtTags);
        itemColor=getActivity().getResources().getColor(R.color.textcolor2);
        itemHoverColor=getActivity().getResources().getColor(R.color.textcolor);
        setupButtonClick();
	    setUpItemSelection();
	    overrideFonts(getActivity(), layTop);
	    bundle=getArguments();
	    productBitmap=(Bitmap)bundle.getParcelable("croppedImage");
	    clothsex=1;
        return root;
    }
    
  	
  	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
	    super.onCreate(savedInstanceState);
	    
    }
    @Override
    public void onAttach(Activity activity) 
    {
    	super.onAttach(activity);
    }
    public void setupButtonClick()
    {
    	btnUndo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
    	btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tags=editTags.getText().toString();
				if(!CLOTH_TYPE_SELECTED)
				{
					showAlert("Fail", "Please select cloth type");
				}
				else if(tags.equals(""))
				{
					showAlert("Fail", "Please add description of product");
				}
				else
				{
					description=tags;
					new ImageUploadTask().execute();
				}
			}
		});
    }
    public void setUpItemSelection()
    {
    	txtTopwear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtTopwear.setTextColor(itemHoverColor);
				txtTopwear.setSelected(true);
				txtBottomwear.setTextColor(itemColor);
				txtShoes.setTextColor(itemColor);
				txtAccessory.setTextColor(itemColor);
				CLOTH_TYPE_SELECTED=true;
				clothtype=1;
			}
		});
    	txtBottomwear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtTopwear.setTextColor(itemColor);
				txtBottomwear.setTextColor(itemHoverColor);
				txtBottomwear.setSelected(true);
				txtShoes.setTextColor(itemColor);
				txtAccessory.setTextColor(itemColor);
				CLOTH_TYPE_SELECTED=true;
				clothtype=2;
			}
		});
    	txtShoes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtTopwear.setTextColor(itemColor);
				txtBottomwear.setTextColor(itemColor);
				txtShoes.setTextColor(itemHoverColor);
				txtShoes.setSelected(true);
				txtAccessory.setTextColor(itemColor);
				CLOTH_TYPE_SELECTED=true;
				clothtype=3;
			}
		});
    	txtAccessory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtTopwear.setTextColor(itemColor);
				txtBottomwear.setTextColor(itemColor);
				txtShoes.setTextColor(itemColor);
				txtAccessory.setTextColor(itemHoverColor);
				txtAccessory.setSelected(true);
				CLOTH_TYPE_SELECTED=true;
				clothtype=4;
			}
		});
    }
    public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}
            
   public void onPause() 
   {
	super.onPause();	
   } 
   
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Please check your internet connection...")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.dismiss();			                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();  
   }
   private void showAlert(String title,String msg)
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(msg);
		builder.setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.dismiss();			                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
   }
   private void overrideFonts(final Context context, final View v) {
	    try {
	        if (v instanceof ViewGroup) {
	            ViewGroup vg = (ViewGroup) v;
	            for (int i = 0; i < vg.getChildCount(); i++) {
	                View child = vg.getChildAt(i);
	                overrideFonts(context, child);
	         }
	        } else if (v instanceof TextView ) {
	            ((TextView) v).setTypeface(tf);
	        }
	        else if (v instanceof EditText ) {
	            ((TextView) v).setTypeface(tf);
	        }
	    } catch (Exception e) {
	 }
	 }
   
   class ImageUploadTask extends AsyncTask<Void, Void, String> {
		 private String webAddressToPost = "http://dev.wardroba.com/serviceXml/productimage.php?login_id="+Constants.LOGIN_USERID;
		 private String imageID=null;
		 // private ProgressDialog dialog;
		 private ProgressDialog dialog = new ProgressDialog(getActivity());

		 @Override
		 protected void onPreExecute() {
		  dialog.setMessage("Please wait...");
		  dialog.show();
		 }

		 @Override
		 protected String doInBackground(Void... params) {
		  try {
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpContext localContext = new BasicHttpContext();
		   HttpPost httpPost = new HttpPost(webAddressToPost);

		   MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   
		   productBitmap.compress(CompressFormat.JPEG, 50, bos);
		   byte[] data = bos.toByteArray();
		   ByteArrayBody byteBody=new ByteArrayBody(data, "my_product.jpg");
		   entity.addPart("filename", byteBody);
		   

		   httpPost.setEntity(entity);
		   HttpResponse response = httpClient.execute(httpPost,localContext);
		   InputStream is = response.getEntity().getContent();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			
			Element node = (Element) doc.getElementsByTagName("root").item(0);
			Element image_id = (Element) node.getElementsByTagName("image_id").item(0);
			Element image_url = (Element) node.getElementsByTagName("img_url").item(0);
			 imageID=image_id.getTextContent();
			String imageUrl=image_url.getTextContent();
			Log.e("HttpResponse", "Response:Image ID"+imageID+" Image URL:"+imageUrl);
		   return imageID;
		  } catch (Exception e) {
		   // something went wrong. connection with the server error
			  e.printStackTrace();
		  }
		  return null;
		 }

		 @Override
		 protected void onPostExecute(String result) {
		  dialog.dismiss();
		  	try 
		  	{
				  WebAPIHelper apiHelper=new WebAPIHelper(Constants.product_add_request, CamaraSaveFragment.this, "Please wait...");
				  String url=Constants.PROUCTADDURL+"login_id="+Constants.LOGIN_USERID+"&clothsex="+clothsex+"&clothtype="+clothtype+"&description="+URLEncoder.encode(description)+"&image_id="+imageID;
				  Log.d("CameraSaveFragment", url);
				  apiHelper.execute(url);
			} 
		  	catch (Exception e) 
		  	{
				e.printStackTrace();
			}
		  
		  //Toast.makeText(getActivity(), "Product added successfully", Toast.LENGTH_SHORT).show();
		 }
		}
}
