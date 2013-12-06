package BaseAdapter;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.example.wardroba.HomeActivity;
import com.example.wardroba.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeProductBaseAdapter extends BaseAdapter
{
	LayoutInflater mInflater;
	Activity activity;
	List<Constants> arr_ProductList;	
	ImageLoader imageLoader;
	public GroupItem item ;
	Typeface tf;
	
	String Cloth_Id;
	String User_Id;
	String ObjectId1;
	String LikeStatus;
	String CommentId;
	
//	String Sharing_Tag;
//	String Sharing_URL;
//	String Delete_SelectionId;
//	String UsernameString;
//	String UserPhotoUrl;
//	String UserDate;
	

  	public HomeProductBaseAdapter(ArrayList<Constants> arr_category,Activity activity)
	{
		this.activity=activity;
		this.arr_ProductList=arr_category;
		mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
		 tf= Typeface.createFromAsset(activity.getAssets(),"fonts/GOTHIC.TTF");
	}

	public int getCount()
	{
		return arr_ProductList.size();
	}

	public Object getItem(int position)  
	{
		// TODO Auto-generated method stub
		return null;
	}
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}
	class GroupItem
	{
		
		public ImageView imgUserPhoto;
		public TextView txtNameSurname,txtDate;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		 final TextView txtLikeCount,txtCommentCount,txtShortDiscription;
		 final ImageView imgProductImage;
		 final ProgressBar progressBar;
		 final ImageView btnLike,btnComment,btnShare;
		
		    View vi=null;
			item =new GroupItem();
			 	
			vi=mInflater.inflate(R.layout.home_activity_lay, null);
			item.imgUserPhoto=(ImageView)vi.findViewById(R.id.img_user_photo);
		    item.txtNameSurname=(TextView)vi.findViewById(R.id.txt_name_surname);
		    item.txtDate=(TextView)vi.findViewById(R.id.txt_date);
			vi.setTag(item);			
			
			txtLikeCount =(TextView) vi.findViewById(R.id.txt_like);
			txtCommentCount =(TextView) vi.findViewById(R.id.txt_comment);
			txtShortDiscription =(TextView) vi.findViewById(R.id.txt_short_description);
			imgProductImage =(ImageView) vi.findViewById(R.id.img_product_photo);
			progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
			btnLike =(ImageView) vi.findViewById(R.id.img_like);
			btnComment =(ImageView) vi.findViewById(R.id.img_comment);
			btnShare =(ImageView) vi.findViewById(R.id.img_share);
			
			txtLikeCount.setTypeface(tf);
			txtCommentCount.setTypeface(tf);
			txtShortDiscription.setTypeface(tf);
			item.txtNameSurname.setTypeface(tf);
			item.txtDate.setTypeface(tf);
			
			item.txtNameSurname.setText(Constants.USER_NAME.toString());
			item.txtDate.setText(Constants.USER_DATE.toString());
			
			txtLikeCount.setText(String.valueOf(arr_ProductList.get(position).PLikeCount));
			txtCommentCount.setText(String.valueOf(arr_ProductList.get(position).PCommentCount));
			txtShortDiscription.setText(arr_ProductList.get(position).PShortDescription.toString().trim());
			
			
			String Status=arr_ProductList.get(position).PLikeStatus.toString().trim();
			if(Status.equals("LIKE"))
	        {
				btnLike.setBackgroundResource(R.drawable.like);
	        }else
	        {
	        	btnLike.setBackgroundResource(R.drawable.like_h);
	        }
			
			String temp=arr_ProductList.get(position).PImageUrl;
			 imageLoader.DisplayImage(temp,imgProductImage);
			 progressBar.setVisibility(View.GONE);
			
			
			
			btnLike.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					String url;
					Cloth_Id = String.valueOf(arr_ProductList.get(position).PIdCloth);
					User_Id = String.valueOf(arr_ProductList.get(position).PUserId);
					ObjectId1 = String.valueOf(arr_ProductList.get(position).PObjectId);
					LikeStatus = arr_ProductList.get(position).PLikeStatus.toString().trim();
					
					Constants.SELECTED_ID=position;
					int count_like = (arr_ProductList.get(position).PLikeCount);
			
					if(LikeStatus.equals("LIKE"))
			        {
						
						txtLikeCount.setText(String.valueOf(count_like=count_like+1));
						btnLike.setBackgroundResource(R.drawable.like_h);
						url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&status="+"LIKE";
			        }else
			        {
			        	txtLikeCount.setText(String.valueOf(count_like=count_like-1));
			        	btnLike.setBackgroundResource(R.drawable.like);
			        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&status="+"UNLIKE";
			        }
					try
					{
						WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,activity);
						Log.d("Like URL= ",url.toString());
						webAPIHelper.execute(url);    	
					}
					catch(Exception e)
					{
						
					}	
				}
			});
		
		

			btnComment.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Comment", 5000).show();
				}
			});
		

			btnShare.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Share", 5000).show();
				}
			});
		
			imgProductImage.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Photo", 5000).show();
				}
			});
		return vi;	
	}	
	
	
	private static class ViewHolder 
	{
		TextView header;
		int previousTop = 0;
	}

		
}
